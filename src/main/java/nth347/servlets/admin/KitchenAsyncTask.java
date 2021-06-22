package nth347.servlets.admin;

import nth347.data.MenuDao;
import nth347.data.MenuDaoFactory;
import nth347.domain.Order;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class KitchenAsyncTask implements Runnable {
    private AsyncContext asyncContext;

    public void setAsyncContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

    @Override
    public void run() {
        HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out = response.getWriter();
        } catch (IOException e) {
            asyncContext.complete();
            e.printStackTrace();
        }
        response.setContentType("text/html");
        long size = Long.parseLong(request.getParameter("size"));
        MenuDao menuDao = MenuDaoFactory.getMenuDao();
        // Sleep until we got a new order in the database
        while (menuDao.getAllOrders().size() < size) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                asyncContext.complete();
                e.printStackTrace();
            }
        }
        // Get the new order
        Order order = menuDao.getOrder(size);
        out.println("<p><strong>Next order: </strong>" + order.toString() + "</p>");
        out.close();

        asyncContext.complete();
    }
}
