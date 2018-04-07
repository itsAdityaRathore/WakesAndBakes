package com.example.mypc.wakesandbakes.Model;

import java.util.List;

/**
 * Created by MY PC on 21-Feb-18.
 */

public class Request {
    private String name;
    private String phone;
    private String address;
    private String total;
    private String status;
    private List<Order> foods; //list of food order

    public Request() {
    }

    public Request(String name, String phone, String address, String total, List<Order> foods) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.foods = foods;
        this.status = "0"; //Default 0:placed, 1:shipping, 2:shipped
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
