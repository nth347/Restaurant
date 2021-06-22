package nth347.websockets;

import nth347.data.MenuDao;
import nth347.data.MenuDaoFactory;
import nth347.domain.Order;
import org.json.JSONObject;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KitchenSessionHandler {
    private List<Session> sessionList = new ArrayList<>();

    public void addSession(Session session) {
        sessionList.add(session);

        // Send all orders to a new session. Everytime a new client connects, it will receive all the orders
        sendAllOrders(session);
    }

    public void removeSession(Session session) {
        sessionList.remove(session);
    }

    // Send a message to all sessions
    private void sendMessage(JSONObject message) {
        for (Session session: sessionList) {
            try {
                session.getBasicRemote().sendText(message.toString());
            } catch (IOException e) {
                removeSession(session);
            }
        }
    }

    // Send a message to a single session
    private void sendMessage(JSONObject message, Session session) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException e) {
            removeSession(session);
        }
    }

    public void addOrder(Order order) {
        JSONObject json = generateJsonForOrder(order, "add");
        if (!order.getStatus().equals("Ready for collection")) {
            sendMessage(json);
        }
    }

    public void removeOrder(Order order) {
        JSONObject json = generateJsonForOrder(order, "remove");
        sendMessage(json);
    }

    private JSONObject generateJsonForOrder(Order order, String action) {
        JSONObject json = new JSONObject();
        json.append("id", order.getId().toString());
        json.append("contents", order.toString());
        json.append("status", order.getStatus());
        json.append("customer", order.getCustomer());

        json.append("time", new Date().toString());
        json.append("action", action);

        return json;
    }

    private void sendAllOrders(Session session) {
        MenuDao menuDao = MenuDaoFactory.getMenuDao();
        List<Order> orderList = menuDao.getAllOrders();

        if (orderList.size() != 0) {
            for (Order order: orderList) {
                if (!order.getStatus().equals("Ready for collection"))
                    sendMessage(generateJsonForOrder(order, "add"), session);
            }
        }
    }
}
