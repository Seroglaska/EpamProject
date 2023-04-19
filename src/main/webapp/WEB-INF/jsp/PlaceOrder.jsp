<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <fmt:setLocale value="${sessionScope.local}" />
            <fmt:setBundle basename="local" var="loc" />

            <fmt:message bundle="${loc}" key="local.legend.order" var="orderFmt" />
            <fmt:message bundle="${loc}" key="local.h1.placingAnOrder" var="placingAnOrderFmt" />
            <fmt:message bundle="${loc}" key="local.h2.methodOfReceiving" var="methodOfReceivingFmt" />
            <fmt:message bundle="${loc}" key="local.label.takeaway" var="takeawayFmt" />
            <fmt:message bundle="${loc}" key="local.label.inPlace" var="inPlaceFmt" />
            <fmt:message bundle="${loc}" key="local.label.paymentMethod" var="paymentMethodFmt" />
            <fmt:message bundle="${loc}" key="local.label.totalPrice" var="totalPriceFmt" />
            <fmt:message bundle="${loc}" key="local.label.paymentByCardOnline" var="paymentByCardOnlineFmt" />
            <fmt:message bundle="${loc}" key="local.label.paymentByCardInPlace" var="paymentByCardInPlaceFmt" />
            <fmt:message bundle="${loc}" key="local.label.paymentByCash" var="paymentByCashFmt" />
            <fmt:message bundle="${loc}" key="local.label.placeOrder" var="placeOrderFmt" />
            <fmt:message bundle="${loc}" key="local.h2.toContinue" var="toContinueFmt" />

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Your order</title>
                <link rel="stylesheet" href="css/PlaceOrder.css">
            </head>

            <body>
                <jsp:include page="/WEB-INF/jsp/Header.jsp" />

                <div class="wrapper">
                    <main class="main">
                        <div class="grid-container">
                            <div>
                                <h1>${placingAnOrderFmt}</h1>
                                <form id="placingOrder" action="restaurant" method="get">
                                    <input type="hidden" name="command" value="place_order">

                                    <h2>${methodOfReceivingFmt}</h2>
                                    <div class="grid-container">
                                        <div>
                                            <label for="takeaway">${takeawayFmt}</label>
                                        </div>
                                        <div>
                                            <input type="radio" id="takeaway" name="receiving" value="takeaway"
                                                checked><br>
                                        </div>
                                        <div>
                                            <label for="inPlace">${inPlaceFmt}</label>
                                        </div>
                                        <div>
                                            <input type="radio" id="inPlace" name="receiving" value="in place">
                                        </div>
                                    </div>

                                    <h2>${paymentMethodFmt}</h2>

                                    <div class="grid-container">
                                        <c:forEach items="${paymentMethods}" var="paymentMethod">
                                            <div>
                                                <label
                                                    for="${paymentMethod.getMethod()}">${paymentMethod.getMethod()}</label>
                                            </div>
                                            <div>
                                                <input type="radio" id="${paymentMethod.getMethod()}" name="paymentBy"
                                                    value="${paymentMethod.getId()}" checked><br>
                                            </div>

                                        </c:forEach>
                                    </div>

                                    <br>
                                    <input id="placeOrderBtn" type="submit" value="${placeOrderFmt}" <c:if
                                        test="${user == null}">disabled</c:if>>
                                    <c:if test="${user == null}">
                                        <h3 style="color: red;">${toContinueFmt}</h3>
                                    </c:if>
                                </form>
                            </div>
                            <div>
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
                            </div>
                        </div>
                    </main>
                </div>

                <jsp:include page="/WEB-INF/jsp/Footer.jsp" />
            </body>

            </html>