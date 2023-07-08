package com.meass.travelagenceyuser;

public class OrderDetails {
    String ordertakingname, Cusname,cus_number,ordertaking_number,time,date,status,name,address,contact,number,parcelitem,weight,
    cus_email,takingemail;

    public OrderDetails() {
    }

    public String getOrdertakingname() {
        return ordertakingname;
    }

    public void setOrdertakingname(String ordertakingname) {
        this.ordertakingname = ordertakingname;
    }

    public String getCusname() {
        return Cusname;
    }

    public void setCusname(String cusname) {
        Cusname = cusname;
    }

    public String getCus_number() {
        return cus_number;
    }

    public void setCus_number(String cus_number) {
        this.cus_number = cus_number;
    }

    public String getOrdertaking_number() {
        return ordertaking_number;
    }

    public void setOrdertaking_number(String ordertaking_number) {
        this.ordertaking_number = ordertaking_number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getParcelitem() {
        return parcelitem;
    }

    public void setParcelitem(String parcelitem) {
        this.parcelitem = parcelitem;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCus_email() {
        return cus_email;
    }

    public void setCus_email(String cus_email) {
        this.cus_email = cus_email;
    }

    public String getTakingemail() {
        return takingemail;
    }

    public void setTakingemail(String takingemail) {
        this.takingemail = takingemail;
    }

    public OrderDetails(String ordertakingname, String cusname, String cus_number, String ordertaking_number, String time, String date, String status, String name,
                        String address, String contact, String number, String parcelitem, String weight, String cus_email, String takingemail) {
        this.ordertakingname = ordertakingname;
        Cusname = cusname;
        this.cus_number = cus_number;
        this.ordertaking_number = ordertaking_number;
        this.time = time;
        this.date = date;
        this.status = status;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.number = number;
        this.parcelitem = parcelitem;
        this.weight = weight;
        this.cus_email = cus_email;
        this.takingemail = takingemail;
    }
}
