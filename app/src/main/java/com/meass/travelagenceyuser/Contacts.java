package com.meass.travelagenceyuser;

public class Contacts {

    public String name,status,image,uuid,time;

    public Contacts(String name, String status, String image, String uuid, String time) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.uuid = uuid;
        this.time = time;
    }

    public Contacts() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}