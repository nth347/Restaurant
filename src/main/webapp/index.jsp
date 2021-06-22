<%--
  Created by IntelliJ IDEA.
  User: nth347
  Date: 2/14/21
  Time: 5:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="/header.jsp"/>
  <h2>Menu item</h2>
  <ul>
    <c:forEach items="${menuItems}" var="menuItem">
      <c:if test="${menuItem.getPrice() > 1}">
        <li>${menuItem} - ${menuItem.getDescription()}</li>
      </c:if>
    </c:forEach>
  </ul>
  </div>
<jsp:include page="/footer.jsp"/>
</body>
</html>