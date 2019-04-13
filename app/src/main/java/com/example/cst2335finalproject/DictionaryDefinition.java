package com.example.cst2335finalproject;

public class DictionaryDefinition{
    long id;
    String definitonTitle;
    String definition;
    String wordClass;

    public DictionaryDefinition(Long i, String t, String ld, String wc){
        id = i;
        definitonTitle = t;
        definition = ld;
        wordClass = wc;
    }

    public DictionaryDefinition(){
        definitonTitle = null;
        definition = null;
        wordClass = null;
    }

    public String getDefinitonTitle(){return definitonTitle;}
    public String getDefinition(){return definition;}
    public String getWordClass(){return wordClass;}
    public Long getID(){return id;}

    public void setDefinitionTitle(String T){definitonTitle = T;}
    public void setDefinition(String D){definition = D;}
    public void setWordClass(String C){wordClass = C;}
}
