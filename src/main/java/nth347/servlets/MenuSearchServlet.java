package nth347.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nth347.data.MenuDao;
import nth347.data.MenuDaoFactory;
import nth347.domain.MenuItem;

@WebServlet(name = "MenuSearchServlet", value = "/MenuSearch.html")
public class MenuSearchServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String searchKeyWord = request.getParameter("SearchKeyWord");
		MenuDao menuDao = MenuDaoFactory.getMenuDao();
		List<MenuItem> menuItems = menuDao.find(searchKeyWord);

		request.setAttribute("menuItems", menuItems);

		ServletContext servletContext = getServletContext();
		RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/MenuSearch.jsp");
		requestDispatcher.forward(request, response);
	}
}