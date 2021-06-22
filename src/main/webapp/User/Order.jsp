<%--
  Created by IntelliJ IDEA.
  User: nth347
  Date: 2/23/21
  Time: 11:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="/header.jsp"/>
    <h2>Order your food</h2>
    <form action='/User/ProceedOrder.html' method='POST' >
        <ul>
            <c:forEach items="${menuItems}" var="menuItem">
                <li>${menuItem}<input type="text" name="item_${menuItem.getId()}"/></li>
            </c:forEach>
        </ul>
        <input type="submit" value="Proceed order"/>
    </form>
    </div>
<jsp:include page="/footer.jsp"/>
</body>
</html>