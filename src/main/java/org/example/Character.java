package org.example;

import java.io.Serializable;
import java.util.List;

class Character implements Serializable {
    private String name;
    private String gender;
    private String role;
    private List<String> traits;

    public Character(String name, String gender, String role, List<String> traits) {
        this.name = name;
        this.gender = gender;
        this.role = role;
        this.traits = traits;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<String> getTraits() { return traits; }
    public void setTraits(List<String> traits) { this.traits = traits; }
}