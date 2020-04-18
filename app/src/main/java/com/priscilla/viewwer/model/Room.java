package com.priscilla.viewwer.model;

import static com.priscilla.viewwer.utils.Extensions.ToThumbnailPath;

public class Room {

    public int Id;
    public String SceneName;
    public String TaskId;
    public String RoomName;
    public String PanoramaPath;
    public byte[] PanoRamaFile;
    public String PanoRamaFilePath;
    public byte[] PanoRamaThumbFile;
    public String PanoRamaThumbPath;
    public String SceneTypeId;
    public String SceneTypeName;

    public Room() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSceneName() {
        return SceneName;
    }

    public void setSceneName(String sceneName) {
        SceneName = sceneName;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public String getPanoramaPath() {
        return PanoramaPath;
    }

    public void setPanoramaPath(String panoramaPath) {
        PanoramaPath = panoramaPath;
    }

    public byte[] getPanoRamaFile() {
        return PanoRamaFile;
    }

    public void setPanoRamaFile(byte[] panoRamaFile) {
        PanoRamaFile = panoRamaFile;
    }

    public String getPanoRamaFilePath() {
        return PanoRamaFilePath;
    }

    public void setPanoRamaFilePath(String panoRamaFilePath) {
        PanoRamaFilePath = panoRamaFilePath;
    }

    public byte[] getPanoRamaThumbFile() {
        return PanoRamaThumbFile;
    }

    public void setPanoRamaThumbFile(byte[] panoRamaThumbFile) {
        PanoRamaThumbFile = panoRamaThumbFile;
    }

    public String getPanoRamaThumbPath() {
        return ToThumbnailPath(PanoRamaFilePath);
    }

    public void setPanoRamaThumbPath(String panoRamaThumbPath) {
        PanoRamaThumbPath = ToThumbnailPath(PanoRamaFilePath);
    }

    public String getSceneTypeId() {
        return SceneTypeId;
    }

    public void setSceneTypeId(String sceneTypeId) {
        SceneTypeId = sceneTypeId;
    }

    public String getSceneTypeName() {
        return SceneTypeName;
    }

    public void setSceneTypeName(String sceneTypeName) {
        SceneTypeName = sceneTypeName;
    }

    public String getSceneTypeImageUrl() {
        return SceneTypeImageUrl;
    }

    public void setSceneTypeImageUrl(String sceneTypeImageUrl) {
        SceneTypeImageUrl = sceneTypeImageUrl;
    }

    public String SceneTypeImageUrl;
}
