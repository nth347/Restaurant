<%--
  Created by IntelliJ IDEA.
  User: nth347
  Date: 2/23/21
  Time: 11:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="/header.jsp"/>
    <h2>Order received</h2>
    <script>
        function updateStatus() {
            var request = new XMLHttpRequest();
            request.open("GET", "/User/UpdateStatusAJAX?id=${id}", true);
            request.send();

            request.onreadystatechange = function() {
                if (this.readyState == 4) {
                    var json = JSON.parse(this.responseText);
                    document.getElementById("status").innerHTML = json.status;
                    document.getElementById("time").innerHTML = "Last updated: " + json.time;
                }
            }
        }
        window.setInterval(
            function() { updateStatus();}, 500
        );
    </script>
    <p>Thank you! Your order has been received, you need to pay $${total}</p>
    <br/>
    <p>The current status of your order is: <span id="status">${status}</span><input type="button" value="Refresh status" onclick="updateStatus()"/></p>
    <p id="time"></p>
    </div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
