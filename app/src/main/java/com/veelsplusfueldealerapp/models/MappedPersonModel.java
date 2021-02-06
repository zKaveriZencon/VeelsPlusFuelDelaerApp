package com.veelsplusfueldealerapp.models;

public class MappedPersonModel {
    String name, personId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public MappedPersonModel(String name, String personId) {
        this.name = name;
        this.personId = personId;
    }
}
