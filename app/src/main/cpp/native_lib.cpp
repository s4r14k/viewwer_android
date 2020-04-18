#include "opencv2/core.hpp"
#include <jni.h>
#include <opencv2/opencv.hpp>
#include <opencv2/core/base.hpp>
#include "opencv2/imgproc.hpp"
#include <string>
#include <iostream>

#include "opencv2/stitching.hpp"
#include "opencv2/imgcodecs.hpp"

#define BORDER_GRAY_LEVEL 0

#include <android/log.h>
#include <android/bitmap.h>
#include <sstream>

#define LOG_TAG "GESTION_STITCHING"
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG, __VA_ARGS__)
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG, __VA_ARGS__)
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL,LOG_TAG, __VA_ARGS__)

#define  cutBlackThreshold   0.001  //Threshold for cut black
//for jni call java method,Tutorial:http://www.mobileway.net/2015/03/20/android-pro-tip-call-java-methods-from-c-using-jni/
jclass javaClassRef;
jmethodID javaMethodRef;

using namespace cv;
using namespace std;


bool checkInteriorExterior(const cv::Mat &mask, const cv::Rect &croppingMask,
                           int &top, int &bottom, int &left, int &right);

bool compareX(cv::Point a, cv::Point b);
bool compareY(cv::Point a, cv::Point b);
void crop(cv::Mat &source);

template <typename T>
std::string NumberToString ( T Number )
{
    std::ostringstream ss;
    ss << Number;
    return ss.str();
}

Stitcher::Mode mode = Stitcher::PANORAMA;
std::string filepath = "/storage/sdcard0/saved_images/";
Mat finalMat;

void MatToBitmap(JNIEnv *env, Mat &mat, jobject &bitmap, jboolean needPremultiplyAlpha) {
    AndroidBitmapInfo info;
    void *pixels = 0;
    Mat &src = mat;


    try {

        LOGD("nMatToBitmap");
        CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
        LOGD("nMatToBitmap1");

        CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
                  info.format == ANDROID_BITMAP_FORMAT_RGB_565);
        LOGD("nMatToBitmap2 :%d  : %d  :%d", src.dims, src.rows, src.cols);

        CV_Assert(src.dims == 2 && info.height == (uint32_t) src.rows &&
                  info.width == (uint32_t) src.cols);
        LOGD("nMatToBitmap3");
        CV_Assert(src.type() == CV_8UC1 || src.type() == CV_8UC3 || src.type() == CV_8UC4);
        LOGD("nMatToBitmap4");
        CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
        LOGD("nMatToBitmap5");
        CV_Assert(pixels);
        LOGD("nMatToBitmap6");


        if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
            Mat tmp(info.height, info.width, CV_8UC4, pixels);
//            Mat tmp(info.height, info.width, CV_8UC3, pixels);
            if (src.type() == CV_8UC1) {
                LOGD("nMatToBitmap: CV_8UC1 -> RGBA_8888");
                cvtColor(src, tmp, COLOR_GRAY2RGBA);
            } else if (src.type() == CV_8UC3) {
                LOGD("nMatToBitmap: CV_8UC3 -> RGBA_8888");
//                cvtColor(src, tmp, COLOR_RGB2RGBA);
//                cvtColor(src, tmp, COLOR_RGB2RGBA);
                cvtColor(src, tmp, COLOR_BGR2RGBA);
//                src.copyTo(tmp);
            } else if (src.type() == CV_8UC4) {
                LOGD("nMatToBitmap: CV_8UC4 -> RGBA_8888");
                if (needPremultiplyAlpha)
                    cvtColor(src, tmp, COLOR_RGBA2mRGBA);
                else
                    src.copyTo(tmp);
            }
        } else {
            // info.format == ANDROID_BITMAP_FORMAT_RGB_565
            Mat tmp(info.height, info.width, CV_8UC2, pixels);
            if (src.type() == CV_8UC1) {
                LOGD("nMatToBitmap: CV_8UC1 -> RGB_565");
                cvtColor(src, tmp, COLOR_GRAY2BGR565);
            } else if (src.type() == CV_8UC3) {
                LOGD("nMatToBitmap: CV_8UC3 -> RGB_565");
//                src.copyTo(tmp);
                cvtColor(src, tmp, COLOR_RGB2BGR565);
            } else if (src.type() == CV_8UC4) {
                LOGD("nMatToBitmap: CV_8UC4 -> RGB_565");
                cvtColor(src, tmp, COLOR_RGBA2BGR565);
            }
        }
        AndroidBitmap_unlockPixels(env, bitmap);
        return;
    } catch (const cv::Exception &e) {
        AndroidBitmap_unlockPixels(env, bitmap);
        LOGD("nMatToBitmap catched cv::Exception: %s", e.what());
        jclass je = env->FindClass("org/opencv/core/CvException");
        if (!je) je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, e.what());
        return;
    } catch (...) {
        AndroidBitmap_unlockPixels(env, bitmap);
        LOGD("nMatToBitmap catched unknown exception (...)");
        jclass je = env->FindClass("java/lang/Exception");
        env->ThrowNew(je, "Unknown exception in JNI code {nMatToBitmap}");
        return;
    }
}

//check row
bool checkRow(const cv::Mat& roi, int y)
{
    int zeroCount = 0;
    for(int x=0; x<roi.cols; x++)
    {
        if(roi.at<uchar>(y, x) == 0)
        {
            zeroCount++;
        }
    }
    if((zeroCount/(float)roi.cols)>cutBlackThreshold)
    {
        return false;
    }
    return true;
}

//check col
bool checkColumn(const cv::Mat& roi, int x)
{
    int zeroCount = 0;
    for(int y=0; y<roi.rows; y++)
    {
        if(roi.at<uchar>(y, x) == 0)
        {
            zeroCount++;
        }
    }
    if((zeroCount/(float)roi.rows)>cutBlackThreshold)
    {
        return false;
    }
    return true;
}

//find largest roi
bool cropLargestPossibleROI(const cv::Mat& gray, cv::Mat& pano, cv::Rect startROI)
{
    // evaluate start-ROI
    Mat possibleROI = gray(startROI);
    bool topOk = checkRow(possibleROI, 0);
    bool leftOk = checkColumn(possibleROI, 0);
    bool bottomOk = checkRow(possibleROI, possibleROI.rows-1);
    bool rightOk = checkColumn(possibleROI, possibleROI.cols-1);
    if(topOk && leftOk && bottomOk && rightOk)
    {
        // Found!!
        LOGE("cropLargestPossibleROI success");
        pano = pano(startROI);
        return true;
    }
    // If not, scale ROI down
    Rect newROI(startROI.x, startROI.y, startROI.width, startROI.height);
    // if x is increased, width has to be decreased to compensate
    if(!leftOk)
    {
        newROI.x++;
        newROI.width--;
    }
    // same is valid for y
    if(!topOk)
    {
        newROI.y++;
        newROI.height--;
    }
    if(!rightOk)
    {
        newROI.width--;
    }
    if(!bottomOk)
    {
        newROI.height--;
    }
    if(newROI.x + startROI.width < 0 || newROI.y + newROI.height < 0)
    {
        //sorry...
        LOGE("cropLargestPossibleROI failed");
        return false;
    }
    return cropLargestPossibleROI(gray,pano,newROI);
}

extern "C" {

JNIEXPORT void JNICALL Java_com_priscilla_viewwer_utils_NativePanorama_crop
        (JNIEnv * env, jclass clazz, jlong imageAddressArray){
    Mat & image = * (Mat*) imageAddressArray;
    crop(image);
}

JNIEXPORT void JNICALL Java_com_priscilla_viewwer_utils_NativePanorama_processPanorama
        (JNIEnv * env, jclass clazz, jlongArray imageAddressArray, jlong outputAddress) {
    // Get the length of the long array
    jsize a_len = env->GetArrayLength(imageAddressArray);
    // Convert the jlongArray to an array of jlong
    jlong *imgAddressArr = env->GetLongArrayElements(imageAddressArray,0);
    // Create a vector to store all the image
    vector< Mat > imgVec;

    for(int k=0;k<a_len;k++)
    {
        // Get the image
        Mat & curimage=*(Mat*)imgAddressArr[k];
        //Mat newimage;
        // Convert to a 3 channel Mat to use with Stitcher module
        //cvtColor(curimage, newimage, CV_BGRA2RGB);
        // Reduce the resolution for fast computation

        //float scale = 1000.0f / curimage.rows;
        //resize(curimage, curimage, Size(scale * curimage.rows, scale * curimage.cols));

        // Save as PNG and load

        /*std::string filepath = "/storage/sdcard0/saved_images_panorama/";
        std::string fileName = "outputStitchingv";
        fileName += NumberToString(k);
        fileName += ".png";
        filepath += fileName;
        imwrite("test.png", curimage, compression_params);*/

        imgVec.push_back(curimage);
        //vector<int> params;
        //params.push_back(CV_IMWRITE_JPEG_QUALITY);
        //params.push_back(60);
        //string filepath = "/storage/sdcard0/azela_captures/file.jpg";
        //int img = imwrite(filepath, curimage, params);
    }
    Mat & result  = *(Mat*) outputAddress;

    Stitcher stitcher = Stitcher::createDefault(true);

    //detail::BestOf2NearestMatcher *matcher = new detail::BestOf2NearestMatcher(false, 0.5f);
    //stitcher.setFeaturesMatcher(matcher);
    //stitcher.setBundleAdjuster(new detail::BundleAdjusterRay());
    //stitcher.setSeamFinder(new detail::NoSeamFinder);
    //stitcher.setExposureCompensator(new detail::NoExposureCompensator());//exposure compensation

    //stitcher.setBlender(new detail::MultiBandBlender());

    //stitcher.setRegistrationResol(-1); /// 0.6
    //stitcher.setSeamEstimationResol(-1);   /// 0.1
    //stitcher.setCompositingResol(-1);   //1
    //stitcher.setPanoConfidenceThresh(-1);   //1
    //stitcher.setWaveCorrection(true);
    //stitcher.setWaveCorrectKind(detail::WAVE_CORRECT_HORIZ);


    LOGD("Begin stitching ...");
    Stitcher::Status status = stitcher.stitch(imgVec, result);
    if (status != Stitcher::OK)
    {
        LOGD("Can't stitch images, error code");
        cout << "Can't stitch images, error code = " << int(status) << endl;
    } else {
        LOGD("Stitching OK");
    }
    //crop(result);
    // Release the jlong array
    env->ReleaseLongArrayElements(imageAddressArray, imgAddressArr ,0);
}

JNIEXPORT jintArray JNICALL
Java_com_priscilla_viewwer_utils_NativePanorama_beginStitching(JNIEnv *env, jclass clazz,
                                                               jobjectArray listaddr, jint numero) {
    std::string fileName = "outputStitchingv";
    fileName += NumberToString(numero);
    fileName += ".jpg";

    filepath += fileName;

    jstring jstr;
    jsize len = env->GetArrayLength(listaddr);
    vector<Mat> maths;
    for (int i = 0; i < len; ++i) {
        jstr = (jstring) env->GetObjectArrayElement(listaddr, i);
        const char *path = (char *) env->GetStringUTFChars(jstr, 0);

        Mat mat = imread(path);

        maths.push_back(mat);
    }

    Stitcher stitcher = Stitcher::createDefault(false);
    // stitcher.setWaveCorrection(false);

    detail::BestOf2NearestMatcher *matcher = new detail::BestOf2NearestMatcher(false, 0.5f);
    stitcher.setFeaturesMatcher(matcher);
    stitcher.setBundleAdjuster(new detail::BundleAdjusterRay());
    stitcher.setSeamFinder(new detail::NoSeamFinder);
    stitcher.setExposureCompensator(new detail::NoExposureCompensator());//exposure compensation
    stitcher.setBlender(new detail::FeatherBlender());

    Stitcher::Status status = stitcher.stitch(maths, finalMat);

    LOGD("Begin stitching ...");

    jintArray jint_arr = env->NewIntArray(3);
    jint *elems = env->GetIntArrayElements(jint_arr, NULL);
    elems[0] = status;//status code
    elems[1] = finalMat.cols;//wide
    elems[2] = finalMat.rows;//high

    if (status != Stitcher::OK)
    {
        LOGD("Can't stitch images, error code");
        cout << "Can't stitch images, error code = " << int(status) << endl;
    } else {
        LOGD("Stitching ...");
    }
    //![stitching]

    env->ReleaseIntArrayElements(jint_arr, elems, 0);

    vector<int> params;
    params.push_back(CV_IMWRITE_JPEG_QUALITY);
    params.push_back(60);
    int img = imwrite(filepath, finalMat, params);

    if (img && status == Stitcher::OK) {
        LOGD("Stitching completed successfully");

        if (numero == 1) {
            Mat img = imread("outputStitchingv1.png", CV_LOAD_IMAGE_GRAYSCALE);

            Mat threshold_output;
            /// Detect edges using Threshold
            threshold( img, threshold_output, 20, 255, THRESH_BINARY );

            imshow( "threshold_output", threshold_output );

            vector<vector<Point> > contours;
            vector<Vec4i> hierarchy;
            /// Find contours
            findContours( threshold_output, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0) );

            /// Approximate contours to polygons + get bounding rects
            vector<vector<Point> > contours_poly( contours.size() );
            Rect boundRect;

            double maxArea = 0.0;
            for( int i = 0; i < contours.size(); i++ )
            {
                double area = contourArea(contours[i]);
                if(area > maxArea) {
                    maxArea = area;
                    approxPolyDP( Mat(contours[i]), contours_poly[i], 3, true );
                    boundRect = boundingRect( Mat(contours_poly[i]) );
                }
            }

            Mat cropImage = img(boundRect);
            imshow("cropImage", cropImage);
        }
    }

    filepath = "/storage/sdcard0/saved_images/";

    cout << "stitching completed successfully\n" << filepath << " saved!";
    return jint_arr;
}
JNIEXPORT void JNICALL
Java_com_priscilla_viewwer_utils_NativePanorama_getMat(JNIEnv *env, jclass type, jlong mat) {

    Mat *res = (Mat *) mat;
    res->create(finalMat.rows, finalMat.cols, finalMat.type());
    memcpy(res->data, finalMat.data, finalMat.rows * finalMat.step);

}
JNIEXPORT int JNICALL
Java_com_priscilla_viewwer_utils_NativePanorama_getBitmap(JNIEnv *env, jclass type, jobject bitmap) {
    if (finalMat.dims != 2){
        return -1;
    }

    MatToBitmap(env,finalMat,bitmap,false);

    return 0;
}

JNIEXPORT jint JNICALL
Java_com_priscilla_viewwer_utils_NativePanorama_jniStitching(JNIEnv *env, jclass clazz, jobjectArray source, jstring result, jdouble scale) {
    clock_t beginTime, endTime;
    double timeSpent;
    beginTime = clock();
    //init jni call java method

    int i = 0;
    bool try_use_gpu = true;
    vector<Mat> imgs;
    Mat img;
    Mat img_scaled;
    Mat pano;
    Mat pano_tocut;
    Mat gray;

    const char* result_name = env->GetStringUTFChars(result, JNI_FALSE);  //convert result
    LOGE("result_name=%s",result_name);
    LOGE("scale=%f",scale);
    //jsize result_length = env->GetStringLength(result);
    //LOGE("result_length=%d\n",result_length);

    int imgCount = env->GetArrayLength(source); //img count
    LOGE("source imgCount=%d",imgCount);
    for(i=0;i<imgCount;i++)
    {
        jstring jsource = (jstring)(env->GetObjectArrayElement(source, i));
        const char* source_name = env->GetStringUTFChars(jsource, JNI_FALSE);  //convert jsource
        LOGE("Add index %d source_name=:%s", i, source_name);
        img=imread(source_name);
        Size dsize = Size((int)(img.cols*scale),(int)(img.rows*scale));
        img_scaled = Mat(dsize,CV_32S);
        resize(img,img_scaled,dsize);
        imgs.push_back(img_scaled);
        env->ReleaseStringUTFChars(jsource, source_name);  //release convert jsource
    }
    img.release();

    Stitcher stitcher = Stitcher::createDefault(try_use_gpu);

//        detail::BestOf2NearestMatcher *matcher = new detail::BestOf2NearestMatcher(false, 0.5f);
//        stitcher.setFeaturesMatcher(matcher);
//        stitcher.setBundleAdjuster(new detail::BundleAdjusterRay());
//        stitcher.setSeamFinder(new detail::NoSeamFinder);
//        stitcher.setExposureCompensator(new detail::NoExposureCompensator());//exposure compensation
//        stitcher.setBlender(new detail::FeatherBlender());

    Stitcher::Status status = stitcher.stitch(imgs, pano);
    if (status != Stitcher::OK)
    {
        LOGE("stitching error");
        return (int)status;
    }
    //release imgs
    for(i=0;i<imgs.size();i++)
    {
        imgs[i].release();
    }

    //cut black edges
    //LOGE("stitching success,cutting black....");
    pano_tocut = pano;
    cvtColor(pano_tocut, gray, CV_BGR2GRAY);
    Rect startROI(0,0,gray.cols,gray.rows); // start as the source image - ROI is the complete SRC-Image
    cropLargestPossibleROI(gray,pano_tocut,startROI);
    gray.release();

    imwrite(result_name, pano_tocut);
    pano.release();
    pano_tocut.release();
    env->ReleaseStringUTFChars(result, result_name);  //release convert result
    endTime = clock();
    timeSpent = (double)(endTime - beginTime) / CLOCKS_PER_SEC;
    LOGE("success,total cost time %f seconds",timeSpent);
    // env->CallVoidMethod(clazz, javaMethodRef, timeSpent);
    return 0;
}
}

bool checkInteriorExterior(const cv::Mat &mask, const cv::Rect &croppingMask,
                           int &top, int &bottom, int &left, int &right)
{
    // Return true if the rectangle is fine as it is
    bool result = true;

    cv::Mat sub = mask(croppingMask);
    int x = 0;
    int y = 0;

    // Count how many exterior pixels are, and choose that side for
    // reduction where mose exterior pixels occurred (that's the heuristic)

    int top_row = 0;
    int bottom_row = 0;
    int left_column = 0;
    int right_column = 0;

    for (y = 0, x = 0; x < sub.cols; ++x)
    {
        // If there is an exterior part in the interior we have
        // to move the top side of the rect a bit to the bottom
        if (sub.at<char>(y, x) == 0)
        {
            result = false;
            ++top_row;
        }
    }

    for (y = (sub.rows - 1), x = 0; x < sub.cols; ++x)
    {
        // If there is an exterior part in the interior we have
        // to move the bottom side of the rect a bit to the top
        if (sub.at<char>(y, x) == 0)
        {
            result = false;
            ++bottom_row;
        }
    }

    for (y = 0, x = 0; y < sub.rows; ++y)
    {
        // If there is an exterior part in the interior
        if (sub.at<char>(y, x) == 0)
        {
            result = false;
            ++left_column;
        }
    }

    for (x = (sub.cols - 1), y = 0; y < sub.rows; ++y)
    {
        // If there is an exterior part in the interior
        if (sub.at<char>(y, x) == 0)
        {
            result = false;
            ++right_column;
        }
    }

    // The idea is to set `top = 1` if it's better to reduce
    // the rect at the top than anywhere else.
    if (top_row > bottom_row)
    {
        if (top_row > left_column)
        {
            if (top_row > right_column)
            {
                top = 1;
            }
        }
    }
    else if (bottom_row > left_column)
    {
        if (bottom_row > right_column)
        {
            bottom = 1;
        }
    }

    if (left_column >= right_column)
    {
        if (left_column >= bottom_row)
        {
            if (left_column >= top_row)
            {
                left = 1;
            }
        }
    }
    else if (right_column >= top_row)
    {
        if (right_column >= bottom_row)
        {
            right = 1;
        }
    }

    return result;
}

bool compareX(cv::Point a, cv::Point b)
{
    return a.x < b.x;
}

bool compareY(cv::Point a, cv::Point b)
{
    return a.y < b.y;
}

void crop(cv::Mat &source)
{
    cv::Mat gray;
    source.convertTo(source, CV_8U);
    cvtColor(source, gray, cv::COLOR_RGB2GRAY);

    // Extract all the black background (and some interior parts maybe)

    cv::Mat mask = gray > 0;

    // now extract the outer contour
    std::vector<std::vector<cv::Point> > contours;
    std::vector<cv::Vec4i> hierarchy;

    cv::findContours(mask, contours, hierarchy, cv::RETR_EXTERNAL, cv::CHAIN_APPROX_NONE, cv::Point(0, 0));
    cv::Mat contourImage = cv::Mat::zeros(source.size(), CV_8UC3);;

    // Find contour with max elements

    int maxSize = 0;
    int id = 0;

    for (int i = 0; i < contours.size(); ++i)
    {
        if (contours.at((unsigned long)i).size() > maxSize)
        {
            maxSize = (int)contours.at((unsigned long)i).size();
            id = i;
        }
    }

    // Draw filled contour to obtain a mask with interior parts

    cv::Mat contourMask = cv::Mat::zeros(source.size(), CV_8UC1);
    drawContours(contourMask, contours, id, cv::Scalar(255), -1, 8, hierarchy, 0, cv::Point());

    // Sort contour in x/y directions to easily find min/max and next

    std::vector<cv::Point> cSortedX = contours.at((unsigned long)id);
    std::sort(cSortedX.begin(), cSortedX.end(), compareX);
    std::vector<cv::Point> cSortedY = contours.at((unsigned long)id);
    std::sort(cSortedY.begin(), cSortedY.end(), compareY);

    int minXId = 0;
    int maxXId = (int)(cSortedX.size() - 1);
    int minYId = 0;
    int maxYId = (int)(cSortedY.size() - 1);

    cv::Rect croppingMask;

    while ((minXId < maxXId) && (minYId < maxYId))
    {
        cv::Point min(cSortedX[minXId].x, cSortedY[minYId].y);
        cv::Point max(cSortedX[maxXId].x, cSortedY[maxYId].y);
        croppingMask = cv::Rect(min.x, min.y, max.x - min.x, max.y - min.y);

        // Out-codes: if one of them is set, the rectangle size has to be reduced at that border

        int ocTop = 0;
        int ocBottom = 0;
        int ocLeft = 0;
        int ocRight = 0;

        bool finished = checkInteriorExterior(contourMask, croppingMask, ocTop, ocBottom, ocLeft, ocRight);

        if (finished == true)
        {
            break;
        }

        // Reduce rectangle at border if necessary

        if (ocLeft)
        { ++minXId; }
        if (ocRight)
        { --maxXId; }
        if (ocTop)
        { ++minYId; }
        if (ocBottom)
        { --maxYId; }
    }
    source = source(croppingMask);
}
