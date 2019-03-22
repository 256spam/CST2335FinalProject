package com.example.cst2335finalproject;

public class DictionaryDefinition{
    String definitonTitle;
    String shortDefintion;
    String longDefinition;

    public DictionaryDefinition(String t, String s, String l){
        definitonTitle = t;
        shortDefintion = s;
        longDefinition = l;
    }

    public String getDefinitonTitle(){return definitonTitle;}
    public String getShortDefintion(){return shortDefintion;}
    public String getLongDefinition(){return longDefinition;}
}
