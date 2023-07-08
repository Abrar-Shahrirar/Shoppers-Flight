package com.meass.travelagenceyuser;

public class FollerModelAndList {
     String name, email, photo, mobile,username,userid,time;

    public FollerModelAndList(String name, String email, String photo, String mobile, String username, String userid, String time) {
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.mobile = mobile;
        this.username = username;
        this.userid = userid;
        this.time = time;
    }

    public FollerModelAndList() {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
