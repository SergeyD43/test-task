package main.java.com.haulmont.testtask.entity;


public class Statistic {
    private String nameMechanic;
    private int countOrders;

    public Statistic(String nameMechanic, int countOrders) {
        this.nameMechanic = nameMechanic;
        this.countOrders = countOrders;
    }

    public String getNameMechanic() {
        return nameMechanic;
    }

    public void setNameMechanic(String nameMechanic) {
        this.nameMechanic = nameMechanic;
    }

    public int getCountOrders() {
        return countOrders;
    }

    public void setCountOrders(int countOrders) {
        this.countOrders = countOrders;
    }
}
