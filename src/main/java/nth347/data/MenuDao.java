package nth347.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nth347.domain.MenuCategory;
import nth347.domain.MenuItem;
import nth347.domain.Order;

public class MenuDao {
    public MenuDao() {
        // Uncommnet 2 lines below to re-initialize database every time you run the application
        DatabaseBootstrap databaseBootstrap = new DatabaseBootstrap();
        databaseBootstrap.initializeDatabase();
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/restaurant", "", "");
             Statement statement = connection.createStatement()
        ) {
            ResultSet results = statement.executeQuery("SELECT * FROM orders");

            while (results.next()) {
                Order order = new Order();
                order.setId(results.getLong("id"));
                order.setStatus(results.getString("status"));
                Map<MenuItem,Integer> orderMap = convertContentsToOrderMap(results.getString("contents"));
                order.setContents(orderMap);
                order.setCustomer(results.getString("customer"));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    private List<MenuItem> buildMenu(ResultSet results) throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();

        while (results.next()) {
            MenuItem menuItem = new MenuItem();
            menuItem.setId(results.getInt("id"));
            menuItem.setDescription(results.getString("description"));
            menuItem.setName(results.getString("name"));
            menuItem.setPrice(results.getDouble("price"));
            menuItem.setCategory(MenuCategory.valueOf(results.getString("category")));
            menuItems.add(menuItem);
        }

        return menuItems;
    }

    public List<MenuItem>  getFullMenu() {
        List<MenuItem> menuItems;

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/restaurant","","");
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery("SELECT * FROM menuitems")
        ) {
            menuItems = buildMenu(results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return menuItems;
    }

    public List<MenuItem> find(String searchString) {
        List<MenuItem> menuItems;

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/restaurant","","");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM menuitems WHERE name LIKE ? OR description LIKE ?")
        ) {
            preparedStatement.setString(1, "%" + searchString + "%");
            preparedStatement.setString(2, "%" + searchString + "%");
            ResultSet results = preparedStatement.executeQuery();
            menuItems = buildMenu(results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return menuItems;
    }

    public MenuItem getItem(int id) {
        List<MenuItem> menuItems;

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/restaurant","","");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM menuitems WHERE id = ?")
        ) {
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();
            menuItems = buildMenu(results);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return menuItems.get(0);
    }

    public Order newOrder(String customer) {
        Order order = new Order();
        order.setStatus("Pending");
        order.setCustomer(customer);

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/restaurant","","");
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders (status, customer) values (?, ?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, order.getStatus());
            preparedStatement.setString(2,  order.getCustomer());
            preparedStatement.execute();

            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                generatedKeys.next();
                order.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    private Map<MenuItem,Integer> convertContentsToOrderMap(String contents) {
        Map<MenuItem,Integer> orderMap = new HashMap<>();

        if (contents == null || contents.equals("")) {
            return orderMap;
        }
        String[] items = contents.split(":");
        for (String item : items) {
            String key = item.split(",")[0];
            String value = item.split(",")[1];
            MenuItem menuItem = getItem(Integer.parseInt(key));
            orderMap.put(menuItem, Integer.valueOf(value));
        }

        return orderMap;
    }

    private String convertOrderMapToContents(Map<MenuItem,Integer> orderMap) {
        String contents = "";

        if (orderMap.keySet().size() == 0) {
            return contents;
        }

        for (MenuItem menuItem : orderMap.keySet() ) {
            contents = contents + menuItem.getId() + "," + orderMap.get(menuItem) + ":";
        }

        contents = contents.substring(0, contents.length()-1);

        return contents;
    }

    public void addToOrder(Long id, MenuItem menuItem, int quantity) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/restaurant","","");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM orders WHERE ID = " + id);
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE orders SET contents = ? WHERE id = ?")
        ) {
            resultSet.next();
            String contents = resultSet.getString("contents");
            Map<MenuItem, Integer> orderMap = convertContentsToOrderMap(contents);
            orderMap.merge(menuItem, quantity, Integer::sum);
            contents = convertOrderMapToContents(orderMap);
            preparedStatement.setString(1, contents);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateOrderStatus(Long id, String status) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/restaurant","","");
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE orders SET status = ? WHERE id = ?")
        ) {
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Double getOrderTotal(Long id) {
        double d = 0d;

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/restaurant","","");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM orders WHERE id = " + id)
        ) {
            resultSet.next();
            Map<MenuItem,Integer> orderMap = convertContentsToOrderMap(resultSet.getString("contents"));
            for (MenuItem menuItem : orderMap.keySet()) {
                d += menuItem.getPrice() * orderMap.get(menuItem);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return d;
    }

    public Order getOrder(Long id) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/restaurant","","");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM orders WHERE id = " + id)
        ) {
            resultSet.next();
            Map<MenuItem, Integer> orderMap = convertContentsToOrderMap(resultSet.getString("contents"));
            Order order = new Order();
            order.setCustomer(resultSet.getString("customer"));
            order.setId(resultSet.getLong("id"));
            order.setStatus(resultSet.getString("status"));
            order.setContents(orderMap);

            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
