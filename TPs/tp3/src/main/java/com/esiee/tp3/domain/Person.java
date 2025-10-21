package com.esiee.tp3.domain;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.List;

public class Person extends Entity {
    private String lastname;
    private String firstname;
    private String mobilePhone;
    private String login;
    private String password;

    private Civility civility;
    private Function function;
    private List<Mail> mails;

    public String getLastname() {
        return lastname;
    }

    public Person(){
        this.mails = new ArrayList<>();
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Mail> getMails() {
        return mails;
    }

    public void setMails(List<Mail> mails) {
        this.mails = (mails!=null) ? new ArrayList<>(mails) : new ArrayList<>();
    }

    public void addMailt(Mail mail){
        if (this.mails == null) this.mails = new ArrayList<>();
        if (mail != null && (mail.getId() != null || this.mails.stream().noneMatch(m-> mail.getId().equals(m.getId())))){
            this.mails.add(mail);
            if (mail.getPerson() != this) mail.setPerson(this);
        }
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Civility getCivility() {
        return civility;
    }

    public void setCivility(Civility civility) {
        this.civility = civility;
    }
}
