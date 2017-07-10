package main.java.com.haulmont.testtask.dao;

import main.java.com.haulmont.testtask.entity.Client;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("Duplicates")
public class ClientDAOImpl {
    private static final String SELECT_ALL = "SELECT ID_CLIENT , NAME, SURNAME, PATRONOMIC, PHONE FROM CLIENTS";
    private static final String INSERT_INTO = "INSERT INTO CLIENTS (NAME, SURNAME, PATRONOMIC, PHONE) VALUES (?, ?, ?, ?);";
    private static final String DELETE_BY_ID = "DELETE FROM CLIENTS WHERE ID_CLIENT=?";
    private static final String UPDATE_WHERE = "UPDATE CLIENTS SET NAME = ?, SURNAME = ?, PATRONOMIC = ?, PHONE = ? WHERE ID_CLIENT = ?";
    private static final String FIND_BY_ID_CLIENT = "SELECT * FROM CLIENTS WHERE ID_CLIENT = ?";

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

    public Collection<Client> getAll() {
        Set<Client> clients = new HashSet<>();
        Statement statement = null;
        try {
            connection = createConnection();
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SELECT_ALL);
            while (resultSet.next()) {
                clients.add(createEntity(resultSet));
            }

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public void insert(Client entity) {
        try  {
            Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_INTO);

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getPatronomic());
            statement.setString(4, entity.getPhone());

            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClientById(int idClient) {
        try  {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID);
            statement.setInt(1, idClient);

            statement.executeUpdate();
            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Client client) {
        try  {
            Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_WHERE);

            statement.setString(1, client.getName());
            statement.setString(2, client.getSurname());
            statement.setString(3, client.getPatronomic());
            statement.setString(4, client.getPhone());
            statement.setInt(5 , client.getId());

            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findByIdClient(int idClient){
        PreparedStatement statement = null;
        int countRows=0;
        try {
            connection = createConnection();
            statement = connection.prepareStatement(FIND_BY_ID_CLIENT);
            statement.setInt(1, idClient);

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

    private Client createEntity(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setId(resultSet.getInt("ID_CLIENT"));
        client.setName(resultSet.getString("NAME"));
        client.setSurname(resultSet.getString("SURNAME"));
        client.setPatronomic(resultSet.getString("PATRONOMIC"));
        client.setPhone(resultSet.getString("PHONE"));
        return client;
    }


}
