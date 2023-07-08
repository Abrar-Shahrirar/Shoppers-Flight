package com.meass.travelagenceyuser;

public class LastMessageDate {
    String uuid,datte,time;

    public LastMessageDate() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDatte() {
        return datte;
    }

    public void setDatte(String datte) {
        this.datte = datte;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LastMessageDate(String uuid, String datte, String time) {
        this.uuid = uuid;
        this.datte = datte;
        this.time = time;
    }
}
