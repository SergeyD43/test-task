package main.java.com.haulmont.testtask.dao;

import main.java.com.haulmont.testtask.entity.Order;
import main.java.com.haulmont.testtask.entity.Status;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("Duplicates")
public class OrderDAOImpl {
    private static final String SELECT_ALL = "SELECT ID_ORDER, ID_CLIENT, ID_MECHANIC , DATE_OF_CREATION, END_DATE, COST, STATUS, DESCRIPTION FROM ORDERS JOIN STATUSES ON ORDERS.ID_STATUS = STATUSES.ID_STATUS";
    private static final String INSERT_INTO = "INSERT INTO ORDERS (ID_CLIENT, ID_MECHANIC, DATE_OF_CREATION, END_DATE, COST, ID_STATUS, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_BY_ID = "DELETE FROM ORDERS WHERE id_order=?";
    private static final String UPDATE_WHERE = "UPDATE orders SET ID_CLIENT = ?, ID_MECHANIC = ?, DATE_OF_CREATION = ?, END_DATE = ?, COST = ?, ID_STATUS = ?, DESCRIPTION = ? WHERE id_order = ?";
    private static final String FIND_BY_ID_CLIENT = "SELECT * FROM orders WHERE ID_CLIENT = ?";
    private static final String FIND_BY_ID_MECHANIC = "SELECT * FROM orders WHERE ID_MECHANIC = ?";
    private static final String FIND_ORDER_BY_FILTER = "SELECT ID_ORDER, ID_CLIENT, ID_MECHANIC , DATE_OF_CREATION, END_DATE, COST, STATUS, DESCRIPTION" +
            " FROM ORDERS JOIN STATUSES ON ORDERS.ID_STATUS = STATUSES.ID_STATUS WHERE DESCRIPTION = ? AND ID_CLIENT = ? AND ID_STATUS=? ";

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

    public Collection<Order> findOrderByFilter(String description, int idClient, Status status){
        Set<Order> orders = new HashSet<>();
        PreparedStatement statement = null;
        try {
            connection = createConnection();
            statement = connection.prepareStatement(FIND_ORDER_BY_FILTER);
            statement.setString(1, description);
            statement.setInt(2, idClient);
            int idStatus = 0;
            switch(status) {
                case ACCEPTED:
                    idStatus = 3;
                    break;
                case COMPLETED:
                    idStatus = 2;
                    break;
                case SCHEDULED:
                    idStatus = 1;
                    break;
                default:
                    break;
            }
            statement.setInt(3, idStatus);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                orders.add(createEntity(resultSet));
            }

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
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

    public int findByIdMechanic(int idMechanic){
        PreparedStatement statement = null;
        int countRows=0;
        try {
            connection = createConnection();
            statement = connection.prepareStatement(FIND_BY_ID_MECHANIC);
            statement.setInt(1, idMechanic);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                countRows++;
            }

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countRows;
    }

    public Collection<Order> getAll() {
        Set<Order> orders = new HashSet<>();
        Statement statement = null;
        try {
            connection = createConnection();
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SELECT_ALL);
            while (resultSet.next()) {
                orders.add(createEntity(resultSet));
            }

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    private Order createEntity(ResultSet resultSet) throws SQLException {

        Order order = new Order();

        order.setId(resultSet.getInt("ID_ORDER"));
        order.setIdClient(resultSet.getInt("ID_CLIENT"));
        order.setIdMechanic(resultSet.getInt("ID_MECHANIC"));
        order.setDateOfCreation(resultSet.getDate("DATE_OF_CREATION"));
        order.setEndDate(resultSet.getDate("END_DATE"));
        order.setCost(resultSet.getFloat("COST"));

        String status = resultSet.getString("STATUS");

        switch(status) {
            case "Accepted":
                order.setStatus(Status.ACCEPTED);
                break;
            case "Completed":
                order.setStatus(Status.COMPLETED);
                break;
            case "Scheduled":
                order.setStatus(Status.SCHEDULED);
                break;
            default:
                System.out.println("problems");
                break;
        }

        order.setDescription(resultSet.getString("DESCRIPTION"));

        return order;
    }

    public void insert(Order order) {
        try  {
            Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_INTO);

            java.sql.Date dateOfCreationSql = new java.sql.Date(order.getDateOfCreation().getTime());
            java.sql.Date endDateSql = new java.sql.Date(order.getEndDate().getTime());

            statement.setInt(1, order.getIdClient());
            statement.setInt(2, order.getIdMechanic());
            statement.setDate(3, dateOfCreationSql);
            statement.setDate(4,  endDateSql);
            statement.setFloat(5, order.getCost());

            Status status = order.getStatus();

            switch(status) {
                case ACCEPTED:
                    statement.setInt(6,1);
                    break;
                case COMPLETED:
                    statement.setInt(6,2);
                    break;
                case SCHEDULED:
                    statement.setInt(6,3);
                    break;
                default:
                    System.out.println("problems");
                    break;
            }

            statement.setString(7, order.getDescription());
            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrderById(int idOrder) {
        try  {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID);
            statement.setInt(1, idOrder);

            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Order order) {
        try  {
            Connection connection = createConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_WHERE);

            statement.setInt(1, order.getIdClient());
            statement.setInt(2, order.getIdMechanic());
            statement.setDate(3, (Date) order.getDateOfCreation());
            statement.setDate(4, (Date) order.getEndDate());
            statement.setFloat(5, order.getCost());

            Status status = order.getStatus();

            switch(status) {
                case ACCEPTED:
                    statement.setInt(6,1);
                    break;
                case COMPLETED:
                    statement.setInt(6,2);
                    break;
                case SCHEDULED:
                    statement.setInt(6,3);
                    break;
                default:
                    System.out.println("problems");
                    break;
            }

            statement.setString(7, order.getDescription());
            statement.setInt(8, order.getId());
            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
