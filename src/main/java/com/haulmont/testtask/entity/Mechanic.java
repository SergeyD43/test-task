package main.java.com.haulmont.testtask.entity;

public class Mechanic {
    private int id;
    private String name;
    private String surname;
    private String patronomic;
    private double hourlypayment;

    public Mechanic() {
    }

    public Mechanic(int id, String name, String surname, String patronomic, double hourlypayment) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronomic = patronomic;
        this.hourlypayment = hourlypayment;
    }

    public Mechanic(String name, String surname, String patronomic, double hourlypayment) {
        this.name = name;
        this.surname = surname;
        this.patronomic = patronomic;
        this.hourlypayment = hourlypayment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronomic() {
        return patronomic;
    }

    public void setPatronomic(String patronomic) {
        this.patronomic = patronomic;
    }

    public double getHourlypayment() {
        return hourlypayment;
    }

    public void setHourlypayment(float hourlypayment) {
        this.hourlypayment = hourlypayment;
    }
}
