package com.example.physicalfitnessexamination.bean;

/**
 * 图片上传实体类
 */
public class PhotoBean {
    private String pictureFullUrl;
    private String pictureId;

    public String getPictureFullUrl() {
        return pictureFullUrl;
    }

    public void setPictureFullUrl(String pictureFullUrl) {
        this.pictureFullUrl = pictureFullUrl;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }
}
