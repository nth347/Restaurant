package nth347.websockets;

public class KitchenSessionHandlerFactory {
    private static KitchenSessionHandler handler;

    public static KitchenSessionHandler getHandler() {
        if (handler == null) {
            handler = new KitchenSessionHandler();
        }
        return handler;
    }
}
