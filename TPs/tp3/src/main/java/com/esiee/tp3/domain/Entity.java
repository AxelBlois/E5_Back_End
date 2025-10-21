package com.esiee.tp3.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Entity implements Serializable {

    protected Long id;

    public Long getId() {return id;}

    public void setId(Long id ) {this.id = id;}
}
