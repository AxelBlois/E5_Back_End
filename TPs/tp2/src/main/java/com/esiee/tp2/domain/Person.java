package com.esiee.tp2.domain;

public class Person {

    // Attributes
    private Long id;
    private String lastname;
    private String firstname;
    private String mail;
    private String mobilePhone;
    private String login;
    private String password;

    // Constructors
    public Person() {}
    public Person (Long id, String lastname, String firstname,
                   String mail, String mobilePhone, String login, String password)
    {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.mail = mail;
        this.mobilePhone = mobilePhone;
        this.login = login;
        this.password = password;
    }

    // Getters / Setters

    public Long getId() { return  id;}
    public void setId(Long id) { this.id = id; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getMobilePhone() { return mobilePhone; }
    public void setMobilePhone(String mobilePhone) { this.mobilePhone = mobilePhone; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}
