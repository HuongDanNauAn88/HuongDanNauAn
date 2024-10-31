package com.example.huongdannauan.model;

public class User {
    private String avatar;
    private String name;
    private String email;
    private String monAnDaLuu;
    private String monAnDaXem;
    private String tinTucDaLuu;
    private String Gender;
    private String Age;

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    // Constructor
    public User() {
        // Trống cho Firestore
    }

    public User(String avata, String email, String monandaluu, String monandaxem, String name, String tintucdaluu, String Gender, String Age) {
        this.avatar = avata;
        this.email = email;
        this.monAnDaLuu = monandaluu;
        this.monAnDaXem = monandaxem;
        this.name = name;
        this.tinTucDaLuu = tintucdaluu;
        this.Gender = Gender;
        this.Age = Age;
    }

    // Getter và Setter
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMonAnDaLuu() {
        return monAnDaLuu;
    }

    public void setMonAnDaLuu(String monAnDaLuu) {
        this.monAnDaLuu = monAnDaLuu;
    }

    public String getMonAnDaXem() {
        return monAnDaXem;
    }

    public void setMonAnDaXem(String monAnDaXem) {
        this.monAnDaXem = monAnDaXem;
    }

    public String getTinTucDaLuu() {
        return tinTucDaLuu;
    }

    public void setTinTucDaLuu(String tinTucDaLuu) {
        this.tinTucDaLuu = tinTucDaLuu;
    }
}
