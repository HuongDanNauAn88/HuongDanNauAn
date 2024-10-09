package com.example.huongdannauan.model;

public class MonAn {
    private int id;
    private String title;
    private String image;
    private String tendaydu;

    public String getTendaydu() {
        return tendaydu;
    }

    public void setTendaydu(String tendaydu) {
        this.tendaydu = tendaydu;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
