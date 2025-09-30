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
    private static Datamodel getInstance(){
        return instance;
    }






}
