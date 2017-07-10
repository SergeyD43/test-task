package main.java.com.haulmont.testtask.entity;

import java.io.Serializable;

public class Client implements Serializable, Cloneable {
    private int id;
    private String name;
    private String surname;
    private String patronomic;
    private String phone;

    public Client() {
    }

    public Client(int id, String name, String surname, String patronomic, String phone) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronomic = patronomic;
        this.phone = phone;
    }

    public Client(String name, String surname, String patronomic, String phone) {
        this.name = name;
        this.surname = surname;
        this.patronomic = patronomic;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronomic='" + patronomic + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}
