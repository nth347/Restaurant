package nth347.servlets.user;

import nth347.data.MenuDao;
import nth347.data.MenuDaoFactory;
import org.json.JSONObject;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "UpdateOrderStatusAJAXServlet", value = "/User/UpdateStatusAJAX")
@ServletSecurity(@HttpConstraint(rolesAllowed = {"user"}))
public class UpdateOrderStatusAJAXServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.valueOf(request.getParameter("id"));
        MenuDao menuDao = MenuDaoFactory.getMenuDao();
        String status = menuDao.getOrder(id).getStatus();

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        JSONObject json = new JSONObject();
        json.put("status", status);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        json.put("time", simpleDateFormat.format(new Date()));

        out.write(json.toString());
        out.close();
    }
}
