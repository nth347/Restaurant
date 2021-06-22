package nth347.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "MenuSearchCorrectionFilter", urlPatterns = {"/MenuSearch.html"})
public class MenuSearchCorrectionFilter implements Filter {
    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String searchKeyWord = request.getParameter("SearchKeyWord");

        if (searchKeyWord.equalsIgnoreCase("chook")) {
            MenuSearchCorrectionRequestWrapper menuSearchCorrectionRequestWrapper = new MenuSearchCorrectionRequestWrapper((HttpServletRequest) request);
            menuSearchCorrectionRequestWrapper.setNewSearchKeyWord("chicken");

            System.out.println("MenuSearchCorrectionFilter: chook to chicken");

            chain.doFilter(menuSearchCorrectionRequestWrapper, response);
        }
        else {
            chain.doFilter(request, response);
        }
    }
}
