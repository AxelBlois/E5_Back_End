package com.esiee.tp2.domain;

public class Function {

    // Attributes
    Long id;
    String code;
    String label;


    // Constructors
    public Function() {}
    public Function(Long id, String code, String label) {
        this.id = id;
        this.code = code;
        this.label = label;
    }
    
    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

}
