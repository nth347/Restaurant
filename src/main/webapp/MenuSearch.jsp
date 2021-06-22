<%--
  Created by IntelliJ IDEA.
  User: nth347
  Date: 2/19/21
  Time: 11:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="/header.jsp"/>
    <h2>Your search results</h2>
    <ul>
    <c:forEach items="${menuItems}" var="menuItem">
        <li>${menuItem} - ${menuItem.description}</li>
    </c:forEach>
    </ul>
    </div>
<jsp:include page="/footer.jsp"/>
</body>
</html>