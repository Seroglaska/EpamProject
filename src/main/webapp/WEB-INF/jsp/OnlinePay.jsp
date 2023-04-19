<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <fmt:setLocale value="${sessionScope.local}" />
            <fmt:setBundle basename="local" var="loc" />

            <fmt:message bundle="${loc}" key="local.legend.order" var="orderFmt" />
            <fmt:message bundle="${loc}" key="local.button.toPay" var="toPayFmt" />
            <fmt:message bundle="${loc}" key="local.label.totalPrice" var="totalPriceFmt" />

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Your order</title>
                <link rel="stylesheet" href="css/OnlinePay.css">
            </head>

            <body>
                <jsp:include page="/WEB-INF/jsp/Header.jsp" />

                <div class="wrapper">
                    <main class="main">
                        <fieldset>
                            <legend>${orderFmt}</legend>

                            <c:forEach items="${order.getOrderList().keySet()}" var="orderedDish">
                                <label class="dishName">${orderedDish.name}
                                    x${order.getOrderList().get(orderedDish)}</label>
                                <label class="dishPrice">${orderedDish.price}</label>
                                <br>
                            </c:forEach>
                            <hr>

                            <label>${totalPriceFmt}: ${order.getTotalPrice()}</label>

                        </fieldset>

                        <form action="restaurant" method="post" id="payBtn">
                            <input type="hidden" name="command" value="online_pay">
                            <input type="submit" value="${toPayFmt}">
                        </form>
                    </main>
                </div>

                <jsp:include page="/WEB-INF/jsp/Footer.jsp" />
            </body>

            </html>