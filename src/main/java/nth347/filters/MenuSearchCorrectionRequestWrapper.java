package nth347.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MenuSearchCorrectionRequestWrapper extends HttpServletRequestWrapper {
    private String newSearchKeyWord;

    public MenuSearchCorrectionRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public void setNewSearchKeyWord(String newSearchKeyWord) {
        this.newSearchKeyWord = newSearchKeyWord;
    }

    @Override
    public String getParameter(String name) {
        if (name.equals("SearchKeyWord")) {
            return newSearchKeyWord;
        }
        else {
            return super.getParameter(name);
        }
    }
}
