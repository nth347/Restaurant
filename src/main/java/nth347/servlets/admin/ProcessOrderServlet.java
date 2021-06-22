package nth347.servlets.admin;

import nth347.data.MenuDao;
import nth347.data.MenuDaoFactory;
import nth347.domain.Order;
import nth347.websockets.KitchenSessionHandler;
import nth347.websockets.KitchenSessionHandlerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProcessOrderServlet", value = "/Admin/ProcessOrder.html")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"admin"}))
public class ProcessOrderServlet extends HttpServlet{
	MenuDao menuDao = MenuDaoFactory.getMenuDao();

	// Get list of all orders
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		List<Order> orders;
		orders = menuDao.getAllOrders();
		request.setAttribute("orders", orders);

		List<String> statuses = new ArrayList<>();
		statuses.add("Pending");
		statuses.add("Order accepted");
		statuses.add("Payment received");
		statuses.add("Being prepared");
		statuses.add("Ready for collection");

		request.setAttribute("statuses", statuses);

		ServletContext context = getServletContext();
		RequestDispatcher dispatch = context.getRequestDispatcher("/Admin/ProcessOrder.jsp");
		dispatch.forward(request, response);
	}

	// Update status of an order
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		MenuDao menuDao = MenuDaoFactory.getMenuDao();
		Long id = Long.valueOf(request.getParameter("id"));
		String status =  request.getParameter("status");

		// Remove the old order from the KitchenManager
		Order order = menuDao.getOrder(id);
		KitchenSessionHandler handler = KitchenSessionHandlerFactory.getHandler();
		handler.removeOrder(order);

		// Update status to the database
		menuDao.updateOrderStatus(id, status);

		// Update the new order to the KitchenManager
		handler.addOrder(menuDao.getOrder(id));

		doGet(request, response);
	}
}
