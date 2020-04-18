package com.priscilla.viewwer.utils;

import android.os.Environment;
import android.util.Log;

import com.priscilla.viewwer.model.Room;

import com.priscilla.viewwer.model.SceneModel;
import com.priscilla.viewwer.model.TourBuildRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Extensions {


    public static List<SceneModel> ToScenes(List<Room> rooms)
    {
        List<SceneModel> scenes = new ArrayList<SceneModel>();
        for(Room item : rooms){
            SceneModel scene = new SceneModel();
            scene.ImageName = GetName(item.PanoRamaFilePath);
            scene.SceneId = Integer.toString(item.getId());
            scene.IsMain = item.getSceneTypeName().equals("salon") ? true:false;
            scenes.add(scene);
        }


        return scenes;
    }

    public static List<SceneModel> ToThumbs(List<Room> rooms)
    {
        List<SceneModel> scenes = new ArrayList<>();
        for (Room item : rooms)
        {
            SceneModel scene = new SceneModel();
            scene.ImageName = GetThumbName(item.getPanoRamaThumbPath());
            scene.IsMain = false;
            scene.SceneId = Integer.toString(item.getId());

            scenes.add(scene);
        }
        return scenes;
    }


    public static String ToThumbnailPath(String path)
    {
        String fileName = (new File(path)).getName();
        String thumbName = String.format("thumb_%1$s", fileName);
        String thumbPath = path.replace(fileName, thumbName);
        return thumbPath;
    }

    public static String GetName(String path)
    {
        try
        {
           // return (new File(path)).getName().replace("_", "");
            return (new File(path)).getName();
        }
        catch (RuntimeException e)
        {
            return null;
        }
    }

    public static String GetThumbName(String path)
    {
        try
        {
            return (new File(path)).getName();
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static void deleteDirectory(){
        File image= new File(Environment.getExternalStorageDirectory()+File.separator+"viewwer/");
        if(image.exists()){
            File[] files=image.listFiles();
            for(int i=0;i<files.length;i++){
                Log.d("test","debut de la suppresion"+i);
                files[i].delete();
            }
            image.getAbsoluteFile().delete();
        }


    }
}
