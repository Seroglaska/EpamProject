<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <fmt:setLocale value="${sessionScope.local}" />
            <fmt:setBundle basename="local" var="loc" />

            <fmt:message bundle="${loc}" key="local.txt.ordersInProcessing" var="ordersInProcessingFmt" />
            <fmt:message bundle="${loc}" key="local.h2.methodOfReceiving" var="methodOfReceivingFmt" />
            <fmt:message bundle="${loc}" key="local.label.totalPrice" var="totalFmt" />
            <fmt:message bundle="${loc}" key="local.label.statusOfOrder" var="statusFmt" />
            <fmt:message bundle="${loc}" key="local.txt.date" var="dateFmt" />
            <fmt:message bundle="${loc}" key="local.txt.noOrdersInProcessing" var="noOrdersInProcessingFmt" />

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Order in processing</title>
                <link rel="stylesheet" href="css/OrdersInProcessing.css">
            </head>

            <body>

                <jsp:include page="/WEB-INF/jsp/Header.jsp" />

                <div class="wrapper">
                    <main class="main">

                        <a href="/restaurant?command=move_to_account">
                            <img src="../../images/goBack.png" alt="goBack" id="goBackImg">
                        </a>

                        <c:if test="${ordersInProcessing == null}">
                            <jsp:forward page="/restaurant">
                                <jsp:param name="command" value="get_orders_in_processing" />
                            </jsp:forward>
                        </c:if>

                        <c:if test="${empty ordersInProcessing}">
                            <h2>
                                ${noOrdersInProcessingFmt}
                            </h2>
                        </c:if>
                        <c:if test="${not empty ordersInProcessing}">
                            <table>
                                <caption>
                                    <h1>${ordersInProcessingFmt}</h1>
                                </caption>
                                <th>№</th>
                                <th>${totalFmt}</th>
                                <th>${dateFmt}</th>
                                <th>${methodOfReceivingFmt}</th>
                                <th>${statusFmt}</th>

                                <c:forEach items="${ordersInProcessing}" var="order">
                                    <tr>

                                        <td>
                                            ${order.id}
                                        </td>
                                        <td>
                                            ${order.totalPrice}
                                        </td>
                                        <td>
                                            ${order.dateTime}
                                        </td>
                                        <td>
                                            ${order.methodOfReceiving}
                                        </td>
                                        <td>
                                            ${order.status}
                                        </td>
                                    </tr>

                                </c:forEach>
                            </table>
                        </c:if>

                    </main>
                </div>

                <jsp:include page="/WEB-INF/jsp/Footer.jsp" />

            </body>

            </html>