package nth347.domain;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private Long id; //
    private Map<MenuItem, Integer> currentOrder = new HashMap<>();
    private String status; //
    private String customer; //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setContents(Map<MenuItem, Integer> contents) {
        this.currentOrder = contents;
    }

    // Add a dish to the order
    public void addToOrder(MenuItem menuItem, int quantity) {
        int currentQuantity = 0;
        if (currentOrder.get(menuItem) != null) currentQuantity = currentOrder.get(menuItem);
        currentOrder.put(menuItem, currentQuantity + quantity);
    }

    public Double getOrderTotal() {
        double total = 0d;
        for (MenuItem menuItem : currentOrder.keySet()) {
            total += currentOrder.get(menuItem) * menuItem.getPrice();
        }

        return total;
    }

    // Get contents of the order
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (MenuItem menuItem: currentOrder.keySet()) {
            sb.append(menuItem.getName()).append(": ").append(currentOrder.get(menuItem)).append("<br>");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
