package nth347.servlets.user;

import java.io.IOException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nth347.data.MenuDaoFactory;
import nth347.data.MenuDao;
import nth347.domain.Order;
import nth347.websockets.KitchenSessionHandler;
import nth347.websockets.KitchenSessionHandlerFactory;

@WebServlet(name = "ProceedOrderServlet", value = "/User/ProceedOrder.html")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"user"}))
public class ProceedOrderServlet extends HttpServlet {
	MenuDao menuDao = MenuDaoFactory.getMenuDao();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int maxId = menuDao.getFullMenu().size();
		// Get login name of the user
		Order order = menuDao.newOrder(request.getUserPrincipal().getName());

		for (int i = 0; i <maxId+1; i++) {
			String quantity = request.getParameter("item_" + i);
			try
			{
			    int q = Integer.parseInt(quantity);
			    if (q > 0) {
			    	menuDao.addToOrder(order.getId(), menuDao.getItem(i), q);
			    	order.addToOrder(menuDao.getItem(i), q);
			    }
			}
			catch (NumberFormatException nfe)
			{
			  	// That's fine it just means there wasn't an order for this item
			}
		}

		// Send the new order to KitchenManager
		KitchenSessionHandler handler = KitchenSessionHandlerFactory.getHandler();
		handler.addOrder(order);

		// Redirect user to the ThankYou.html page
		HttpSession session = request.getSession();
		session.setAttribute("orderId", order.getId());
		
		String redirectUrl = "/User/ThankYou.html";
		redirectUrl = response.encodeURL(redirectUrl);
		response.sendRedirect(redirectUrl);
	}
}
