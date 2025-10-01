package com.esiee.tp2.model;

import  com.esiee.tp2.domain.*;
import java.util.*;

public class Datamodel {

    // Attributes
    private static Datamodel instance = new  Datamodel();

    private Map<Long, Person> persons = new HashMap<>();
    private Map<Long, Civility> civilities = new HashMap<>();
    private Map<Long, Function> functions = new HashMap<>();

    // Constructors
    private Datamodel(){}


    // Getters / Setters
    public static Datamodel getInstance(){
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



}
