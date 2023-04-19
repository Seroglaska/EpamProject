<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <fmt:setLocale value="${sessionScope.local}" />
            <fmt:setBundle basename="local" var="loc" />

            <fmt:message bundle="${loc}" key="local.label.yourOrder" var="yourOrder_lbl" />
            <fmt:message bundle="${loc}" key="local.button.goToPlacingAnOrder" var="GoToPlacingAnOrder_btn" />
            <fmt:message bundle="${loc}" key="local.button.cleanCurrentOrder" var="CleanCurrentOrder_btn" />
            <fmt:message bundle="${loc}" key="local.button.orderIsEmpty" var="orderIsEmptyFmt" />

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Your order</title>
                <link rel="stylesheet" href="css/CurrentOrder.css">
            </head>

            <body>

                <jsp:include page="/WEB-INF/jsp/Header.jsp" />

                <div class="wrapper">
                    <main class="main">
                        <c:if test="${order != null}">

                            <a href="/showAccount">
                                <img src="../../images/goBack.png" alt="goBack" id="goBackImg">
                            </a>

                            <form action="restaurant" method="post" id="placeOrderForm">
                                <input type="hidden" name="command" value="move_to_place_order">

                                <table>
                                    <caption>
                                        <h1>${yourOrder_lbl}:</h1>
                                    </caption>
                                    <c:forEach items="${order.getOrderList().keySet()}" var="orderedDish">
                                        <tr>
                                            <td>
                                                <h3 class="DishName">
                                                    <li />${orderedDish.name}
                                                </h3>
                                                ${orderedDish.description}
                                            </td>
                                            <td>
                                                ${orderedDish.price}
                                            </td>
                                            <td id="tdWithForm">
                                                <input type="hidden" name="dishId" value="${orderedDish.id}">

                                                <button onclick="reduceOne(event)">-</button>
                                                <input type="text" name="quantity"
                                                    value="${order.getOrderList().get(orderedDish)}" required>
                                                <button onclick="addOne(event)">+</button> <br>
                                            </td>
                                            <td>
                                                <a
                                                    href="/restaurant?command=remove_from_order&&dishId=${orderedDish.id}">
                                                    <img src="../../images/remove.png" alt="remove" id="removeImg">
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                                <input type="submit" value="${CleanCurrentOrder_btn}" form="cleanOrderForm"
                                    id="placeOrderBtn">
                                <input type="submit" value="${GoToPlacingAnOrder_btn}" form="placeOrderForm"
                                    id="placeOrderBtn">
                            </form>

                            <form action="restaurant" method="get" id="cleanOrderForm">
                                <input type="hidden" name="command" value="clean_current_order">
                            </form>

                        </c:if>

                        <c:if test="${order == null}">
                            <a href="/showAccount">
                                <img src="../../images/goBack.png" alt="goBack" id="goBackImg">
                            </a>
                            <h1>${orderIsEmptyFmt}</h1>
                        </c:if>
                    </main>
                </div>

                <jsp:include page="/WEB-INF/jsp/Footer.jsp" />

            </body>

            <script src="../../js/CurrentOrder.js"></script>

            </html>