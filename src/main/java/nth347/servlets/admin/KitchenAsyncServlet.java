package nth347.servlets.admin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "KitchenAsyncServlet", value = "/Admin/KitchenAsync.html", asyncSupported = true)
public class KitchenAsyncServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AsyncContext context = request.startAsync(request, response);

        KitchenAsyncTask task = new KitchenAsyncTask();
        task.setAsyncContext(context);

        context.start(task);
    }
}
