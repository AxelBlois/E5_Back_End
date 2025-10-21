package com.esiee.tp3.domain;

public class Mail extends Entity{

    String address;

    public Mail() {}

    public Mail(Long id, String address) {
        this.id = id;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
