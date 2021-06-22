package nth347.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "MenuSearchLoggingFilter", urlPatterns = {"/MenuSearch.html"})
public class MenuSearchLoggingFilter implements Filter {
    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String searchKeyWord = request.getParameter("SearchKeyWord");

        System.out.println("MenuSearchLoggingFilter: " + searchKeyWord);

        chain.doFilter(request, response);
    }
}
