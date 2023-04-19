<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <fmt:setLocale value="${sessionScope.local}" />
            <fmt:setBundle basename="local" var="loc" />

            <fmt:message bundle="${loc}" key="local.txt.ordersInProcessing" var="ordersInProcessingFmt" />
            <fmt:message bundle="${loc}" key="local.txt.personalInfo" var="personalInfoFmt" />
            <fmt:message bundle="${loc}" key="local.txt.historyOfOrders" var="historyOfOrdersFmt" />
            <fmt:message bundle="${loc}" key="local.txt.currentOrder" var="currentOrderFmt" />
            <fmt:message bundle="${loc}" key="local.txt.quitFromAccount" var="quitFromAccountFmt" />
            <fmt:message bundle="${loc}" key="local.txt.confirmationOfOrders" var="confirmationOfOrdersFmt" />
            <fmt:message bundle="${loc}" key="local.txt.orders" var="ordersFmt" />
            <fmt:message bundle="${loc}" key="local.txt.findUser" var="findUsersFmt" />
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Account</title>
                <link rel="stylesheet" href="css/AccountPage.css">
            </head>

            <body>

                <jsp:include page="/WEB-INF/jsp/Header.jsp" />

                <div class="wrapper">
                    <main class="main">
                        <div class="grid-container">
                            <div>
                                <a href="/ordersInProcessing">
                                    <img src="../../images/ordersInProcessing.png" alt="${ordersInProcessingFmt}"><br>
                                    ${ordersInProcessingFmt}
                                </a>
                            </div>
                            <div>
                                <a href="/historyOfOrders">
                                    <img src="../../images/historyOfOrders.png" alt="${historyOfOrdersFmt}"><br>
                                    ${historyOfOrdersFmt}
                                </a>
                            </div>
                            <div>
                                <a href="/showCurrentOrder">
                                    <img src="../../images/order.png" alt="${currentOrderFmt}" style="width: 40%;"><br>
                                    ${currentOrderFmt}
                                </a>
                            </div>
                            <div>
                                <a href="/personalInfo">
                                    <img src="../../images/acc.png" alt="${personalInfoFmt}"><br>
                                    ${personalInfoFmt}
                                </a>
                            </div>
                            <div>
                                <a href="/restaurant?command=quit_from_account">
                                    <img src="../../images/quit.png" alt="${quitFromAccountFmt}"><br>
                                    ${quitFromAccountFmt}
                                </a>
                            </div>
                            <c:if test="${user.roleId == 1}">
                                <div>
                                    <a href="/restaurant?command=move_to_confirmation_of_orders">
                                        <img src="../../images/communicate.png" alt="${confirmationOfOrdersFmt}"><br>
                                        ${confirmationOfOrdersFmt}
                                    </a>
                                </div>
                                <div>
                                    <a href="/showFindUsers">
                                        <img src="../../images/find.png" alt="${findUsersFmt}"><br>
                                        ${findUsersFmt}
                                    </a>
                                </div>
                            </c:if>
                            <c:if test="${user.roleId == 3}">
                                <div>
                                    <a href="/restaurant?command=move_to_cook_orders">
                                        <img src="../../images/cook.png" alt="${ordersFmt}"><br>
                                        ${ordersFmt}
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </main>
                </div>

                <jsp:include page="/WEB-INF/jsp/Footer.jsp" />

            </body>

            </html>