package main.java.com.haulmont.testtask.dao;

import main.java.com.haulmont.testtask.entity.Mechanic;
import main.java.com.haulmont.testtask.entity.Statistic;

import java.sql.*;
import java.util.*;

@SuppressWarnings("Duplicates")
public class MechanicDAOImpl {
    private static final String SELECT_ALL = "SELECT ID_MECHANIC , NAME, SURNAME, PATRONOMIC, hourlyPayment FROM MECHANICS";
    private static final String INSERT_INTO = "INSERT INTO MECHANICS (NAME, SURNAME, PATRONOMIC, hourlyPayment) VALUES (?, ?, ?, ?);";
    private static final String DELETE_BY_ID = "DELETE FROM MECHANICS WHERE ID_MECHANIC=?";
    private static final String UPDATE_WHERE = "UPDATE MECHANICS SET NAME = ?, SURNAME = ?, PATRONOMIC = ?, hourlyPayment = ? WHERE ID_MECHANIC = ?";
    private static final String FIND_BY_ID_MECHANIC = "SELECT * FROM MECHANICS WHERE ID_MECHANIC = ?";
    private static final String GET_STATISTIC = "select surname, count(surname) from mechanics join orders on mechanics.id_mechanic=orders.id_mechanic\n" +
            "group by surname union select surname, 0 from mechanics where id_mechanic not in(select id_mechanic from orders)";

    private static final String DATABASE_DRIVER = "org.hsqldb.jdbcDriver";
    private static final String DATABASE_URL = "jdbc:hsqldb:file:${webroot}/database/carworkshop;ifexists=true";
    private static final String USERNAME = "SA";
    private static final String PASSWORD = "";
    private Connection connection;

    private Connection createConnection(){
        try {
            Class.forName(DATABASE_DRIVER);
            try {
                connection = DriverManager.getConnection(DATABASE_URL,USERNAME, PASSWORD);
                System.out.println("Соедиение с базой данных установлено");
            } catch (SQLException e) {
                System.out.println("Не удалось установить соединение с базой данных");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Отсутствует драйвер базы данных");
        }
        return connection;
    }

    private void closeConnection(){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("SHUTDOWN");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findByIdMechanic(int idMechanic){
        PreparedStatement statement = null;
        int countRows=0;
        try {
            connection = createConnection();
            statement = connection.prepareStatement(FIND_BY_ID_MECHANIC);

            statement.setInt(1, idMechanic);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(countRows);
                countRows++;
            }

             closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countRows;
    }

    public Set<Statistic> getStatistic() {
        Set<Statistic> mechanicOrdersCounts = new HashSet<>();
        Statement statement = null;
        try {
            connection = createConnection();
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(GET_STATISTIC);
            while (resultSet.next()) {
                mechanicOrdersCounts.add(new Statistic(resultSet.getString("surname"), resultSet.getInt(2)));
            }

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mechanicOrdersCounts;
    }

    public Collection<Mechanic> getAll() {
        Set<Mechanic> mechanics = new HashSet<>();
        Statement statement = null;
        try {
            connection = createConnection();
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SELECT_ALL);
            while (resultSet.next()) {
                mechanics.add(createEntity(resultSet));
            }

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mechanics;
    }

    public void insert(Mechanic entity) {
        try  {
            Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_INTO);
            System.out.println(entity.getName());

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getPatronomic());
            statement.setDouble(4, entity.getHourlypayment());

            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMechanicById(int idMechanic) {
        try  {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID);
            statement.setInt(1, idMechanic);

            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Mechanic createEntity(ResultSet resultSet) throws SQLException {
        Mechanic mechanic = new Mechanic();

        mechanic.setId(resultSet.getInt("id_mechanic"));
        mechanic.setName(resultSet.getString("NAME"));
        mechanic.setSurname(resultSet.getString("SURNAME"));
        mechanic.setPatronomic(resultSet.getString("PATRONOMIC"));
        mechanic.setHourlypayment(resultSet.getFloat("hourlyPayment"));

        return mechanic;
    }

    public void update(Mechanic mechanic) {
        try  {
            Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_WHERE);

            statement.setString(1, mechanic.getName());
            statement.setString(2, mechanic.getSurname());
            statement.setString(3, mechanic.getPatronomic());
            statement.setDouble(4, mechanic.getHourlypayment());
            statement.setInt(5 , mechanic.getId());

            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
