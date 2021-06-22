package nth347.servlets;

import nth347.data.MenuDao;
import nth347.data.MenuDaoFactory;
import nth347.domain.MenuItem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewMenuServlet", value = "")
public class ViewMenuServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		MenuDao menuDao = MenuDaoFactory.getMenuDao();
		List<MenuItem> menuItems = menuDao.getFullMenu();

		request.setAttribute("menuItems", menuItems);

		ServletContext servletContext = getServletContext();
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/index.jsp");
		requestDispatcher.forward(request, response);
	}
}
