package main.java.com.haulmont.testtask.entity;


import java.util.Date;

public class Order {
    private int id;
    private int idClient;
    private int idMechanic;
    private Date dateOfCreation;
    private Date endDate;
    private float cost;
    private Status status;
    private String description;

    public Order() {
    }

    public Order(int idClient, int idMechanic, Date dateOfCreation, Date endDate, float cost, Status status, String description) {
        this.idClient = idClient;
        this.idMechanic = idMechanic;
        this.dateOfCreation = dateOfCreation;
        this.endDate = endDate;
        this.cost = cost;
        this.status = status;
        this.description = description;
    }

    public Order(int id, int idClient, int idMechanic, Date dateOfCreation, Date endDate, float cost, Status status, String description) {
        this.id = id;
        this.idClient = idClient;
        this.idMechanic = idMechanic;
        this.dateOfCreation = dateOfCreation;
        this.endDate = endDate;
        this.cost = cost;
        this.status = status;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", idMechanic=" + idMechanic +
                ", dateOfCreation=" + dateOfCreation +
                ", endDate=" + endDate +
                ", cost=" + cost +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdMechanic() {
        return idMechanic;
    }

    public void setIdMechanic(int idMechanic) {
        this.idMechanic = idMechanic;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
