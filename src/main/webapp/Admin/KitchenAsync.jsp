<%--
  Created by IntelliJ IDEA.
  User: nth347
  Date: 3/29/21
  Time: 3:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="/header.jsp" />
    <h2>All orders will be displayed here.</h2>
    <script>
    var connectionOpen = false;
    var nextOrder = 1;
    function getOrders(){
        if(!connectionOpen){
            connectionOpen = true;
            var xmlHttpRequest = new XMLHttpRequest();
            xmlHttpRequest.onreadystatechange = function() {
                if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
                    connectionOpen = false;
                    nextOrder++;
                    var ordersDiv = document.getElementById("orders");
                    ordersDiv.innerHTML = xmlHttpRequest.responseText + ordersDiv.innerHTML;
                }
            }
            xmlHttpRequest.open("GET", "/Admin/KitchenAsync.html?size=" + nextOrder, true);
            xmlHttpRequest.send();
        }
    }
    // 500 milliseconds
    setInterval(getOrders, 500);
    </script>
    <div id="orders"></div>
    </div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
