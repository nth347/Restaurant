package nth347.websockets;

import nth347.data.MenuDao;
import nth347.data.MenuDaoFactory;
import nth347.domain.Order;
import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/Admin/KitchenManager.html")
public class KitchenWebsocket {
    @OnOpen
    public void open(Session session) {
        KitchenSessionHandler handler = KitchenSessionHandlerFactory.getHandler();
        handler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        KitchenSessionHandler handler = KitchenSessionHandlerFactory.getHandler();
        handler.removeSession(session);
    }

    @OnError
    public void error(Throwable error) {
        throw new RuntimeException(error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        JSONObject json = new JSONObject(message);
        Long id = json.getLong("id");
        String status = json.getString("status");

        // Remove order with old status from the KitchenManager
        MenuDao menuDao = MenuDaoFactory.getMenuDao();
        Order order = menuDao.getOrder(id);
        KitchenSessionHandler handler = KitchenSessionHandlerFactory.getHandler();
        handler.removeOrder(order);

        // Update status to the database
        menuDao.updateOrderStatus(id, status);

        // Add new order with new status to the KitchenManager
        handler.addOrder(menuDao.getOrder(id));
    }
}
