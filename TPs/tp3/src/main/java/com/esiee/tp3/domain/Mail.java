package com.esiee.tp3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

public class Mail extends Entity {

    private String address;
    private MailType type;

    @JsonIgnore
    private Person person;

    public Mail() {}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Mail(Long id, String address) {
        this.id = id;
        this.address = address;
    }

    public MailType getType() {return type;}

    public void setType(MailType type) {this.type = type;}

    public Person getPerson() {return person;}

    public void setPerson(Person person) {this.person = person;}
}
