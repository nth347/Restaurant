package nth347.servlets.user;

import nth347.data.MenuDao;
import nth347.data.MenuDaoFactory;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ThankYouServlet", value = "/User/ThankYou.html")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"user"}))
public class ThankYouServlet extends HttpServlet {
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		Long orderId = (Long) session.getAttribute("orderId");

		MenuDao menuDao = MenuDaoFactory.getMenuDao();
		Double total = menuDao.getOrderTotal(orderId);
		String status = menuDao.getOrder(orderId).getStatus();
		
		if (total == null) {
			response.sendRedirect("/Order.html");

			return;
		}
		request.setAttribute("total", total);
		request.setAttribute("status", status);
		request.setAttribute("id", orderId);

		ServletContext servletContext = getServletContext();
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/User/ThankYou.jsp");
		requestDispatcher.forward(request, response);
	}
}
