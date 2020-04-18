cmake_minimum_required(VERSION 3.4.1)

#set(CMAKE_SYSTEM_NAME Android)
#set(CMAKE_TOOLCHAIN_FILE "toolchain file" CACHE FILEPATH "D:/android_lib/OpenCV-android-sdk/sdk/native/jni/android.toolchain.cmake")
#set(CMAKE_CXX_COMPILER "C:/Users/sariak/AppData/Local/Android/sdk/ndk/21.0.6113669/toolchains/arm-linux-androideabi-4.9/prebuilt/windows-x86_64/bin/arm-linux-androideabi-c++filt.exe")
set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

include_directories(${CMAKE_SOURCE_DIR}/src/main/jniIncludes)
#link_directories(${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})

file(GLOB PARTYLIBS "D:/ndk/opencv-3.4.4-android-sdk/OpenCV-android-sdk/sdk/native/3rdparty/libs/${ANDROID_ABI}/*.a")
file(GLOB CVLIBS "D:/ndk/opencv-3.4.4-android-sdk/OpenCV-android-sdk/sdk/native/staticlibs/${ANDROID_ABI}/libopencv_stitching.a")

add_library(
        native_lib
        SHARED
        src/main/cpp/native_lib.cpp)

add_library(
        lib_opencv
        SHARED
        IMPORTED)

add_library(
        opencv_stitching
        SHARED
        IMPORTED)

set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)
set_target_properties(opencv_stitching PROPERTIES IMPORTED_LOCATION ${CVLIBS})

find_library(log-lib
        log)

target_link_libraries(
        native_lib
        lib_opencv
        opencv_stitching
        -ljnigraphics
        ${log-lib} )