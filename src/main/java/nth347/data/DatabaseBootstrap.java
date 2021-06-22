package nth347.data;

import nth347.domain.MenuCategory;
import nth347.domain.MenuItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseBootstrap {
    private List<MenuItem> getMenuItemsList() {
        List<MenuItem> menuItems= new ArrayList<>();

        menuItems.add(new MenuItem(1, "Soup of the day (v)","A delicious soup made from the chef's choice of vegetables. Served with a home baked bread roll.", MenuCategory.STARTER, 4.99));
        menuItems.add(new MenuItem(2, "Asparagus filo parcels (v)", "Fresh seasonal asparagus, wrapped in a light filo pastry, served with a chilli dipping sauce.", MenuCategory.STARTER, 6.99));
        menuItems.add(new MenuItem(3, "Chicken Terrine","Our terrine tastes of summer! We use only the finest organic chicken. Served with a mixed leaf salad. (contains nuts)", MenuCategory.STARTER, 5.99));
        menuItems.add(new MenuItem(4, "Lamb Shank","Slow cooked to perfection, our organic lamb melts in the mouth. Served with mixed vegetables and seasonal potatoes.", MenuCategory.MAIN_COURSE, 12.99));
        menuItems.add(new MenuItem(5, "Sea Bass", "We pan fry our freshly caught sea bass to seal in the flavour. Served with mixed vegetables and seasonal potatoes.", MenuCategory.MAIN_COURSE, 11.99));
        menuItems.add(new MenuItem(6, "Butternut squash risotto", "People come from far and wide for our famous risotto. Served with a mixed leaf salad. (v)", MenuCategory.MAIN_COURSE, 9.99));
        menuItems.add(new MenuItem(7, "Raspberry cheesecake", "A delightfully sweet cheesecake, served with a sour raspberry compot, to form a perfect balance to end your meal.", MenuCategory.DESERT, 6.99));
        menuItems.add(new MenuItem(8, "Lemon mousse", "Feeling full? Our mousse is delightfully light and fluffy. Served with home baked shortbread.", MenuCategory.DESERT, 6.99));
        menuItems.add(new MenuItem(9, "Fruit skewers", "Our nostalgic 80s desert is super healthy... then we add luxurious vanilla ice-cream and chocolate sauce. ", MenuCategory.DESERT, 6.99));
        menuItems.add(new MenuItem(10, "Coffee", "Espresso, Americano, Latte or Cappuccino? Tell us how you like it!", MenuCategory.DRINK, 2.99));
        menuItems.add(new MenuItem(11, "Tea", "We have a full range of classic and herbal teas.", MenuCategory.DRINK, 2.99));

        return menuItems;
    }

    public void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/restaurant","","")) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS menuitems;")) {
                preparedStatement.execute();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS orders;")) {
                preparedStatement.execute();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE menuitems (id int primary key, name varchar(30), description varchar(150), category varchar(30), price float);")) {
                preparedStatement.execute();
            }

            List<MenuItem> menuItems = getMenuItemsList();

            for (MenuItem menuItem : menuItems) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO menuitems (id, name, description, category, price) values (?,?,?,?,?);")) {
                    preparedStatement.setInt(1, menuItem.getId());
                    preparedStatement.setString(2, menuItem.getName());
                    preparedStatement.setString(3, menuItem.getDescription());
                    preparedStatement.setString(4, menuItem.getCategory().toString());
                    preparedStatement.setDouble(5, menuItem.getPrice());
                    preparedStatement.execute();
                }
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE orders (id int auto_increment primary key, customer varchar(30), contents varchar(255), status varchar(20));")) {
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
