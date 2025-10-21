package com.esiee.tp2.models;

import  com.esiee.tp2.domain.*;
import java.util.*;

public class Datamodel {

    // Attributes
    private static Datamodel instance = null;

    private Map<Long, Person> persons = new HashMap<>();
    private Map<Long, Civility> civilities = new HashMap<>();
    private Map<Long, Function> functions = new HashMap<>();

    // Constructors
    private Datamodel(){}


    // Getters / Setters
    public static Datamodel getInstance(){
        if (instance == null){
            instance = new Datamodel();
            instance.initData();
        }
        return instance;
    }

    public List<Person> getPersons(){
        return new ArrayList<>(persons.values());
    }

    public Person getPerson(Long id){
        return persons.get(id);
    }

    public List<Civility> getCivilities(){
        return new ArrayList<>(civilities.values());
    }

    public Civility getCivility(Long id){
        return civilities.get(id);
    }

    public List<Function> getFunctions(){
        return new ArrayList<>(functions.values());
    }

    public Function getFunction(Long id){
        return functions.get(id);
    }

    public void initData(){

        civilities.put(1L, new Civility(1L,"mr","Monsieur"));
        civilities.put(2L, new Civility(2L,"mme","Madame"));

        functions.put(1L, new Function(1L, "dev", "Développeur"));
        functions.put(2L, new Function(2L, "pm", "Product Manaer"));

        persons.put(1L, new Person(1L, "Rozan","Baptiste", "br@gmail.com", "0600000001", "rozanb", "1234"));
        persons.put(2L, new Person(2L, "Chang", "Charlotte", "cc@mgail.com","0600000002", "changc", "0000"));

    }

    public static void main(String[] args){
        Datamodel datamodel = getInstance();

        for (Person p : datamodel.getPersons()){
            System.out.println(p.getLogin() + " " + p.getPassword());
        }
    }

}
