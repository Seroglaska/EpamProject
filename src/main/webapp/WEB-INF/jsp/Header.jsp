<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

        <fmt:setLocale value="${sessionScope.local}" />
        <fmt:setBundle basename="local" var="loc" />

        <fmt:message bundle="${loc}" key="local.button.signUp" var="signUp_btn" />
        <fmt:message bundle="${loc}" key="local.button.signIn" var="signIn_btn" />
        <fmt:message bundle="${loc}" key="local.button.ru" var="ru_button" />
        <fmt:message bundle="${loc}" key="local.button.en" var="en_button" />
        <fmt:message bundle="${loc}" key="local.link.Menu" var="menu_link" />

        <link rel="stylesheet" href="css/Header.css">

        <header>
            <div>
                <form action="restaurant" method="get">
                    <input type="hidden" name="command" value="change_locale">
                    <input type="hidden" name="local" value="en">
                    <input type="submit" value="${en_button}" class="localeBtn">
                </form>
                <form action="restaurant" method="get">
                    <input type="hidden" name="command" value="change_locale">
                    <input type="hidden" name="local" value="ru">
                    <input type="submit" value="${ru_button}" class="localeBtn">
                </form>
                <a id="homeLink" href="/home" class="headLink">${menu_link}</a></li>
            </div>

            <div class="parentBoxHeader">
                <div style="padding: 1em;">
                    <c:if test="${user == null}">
                        <a href="/authorizationPage">
                            <button id="signIn_button">${signIn_btn}</button>
                        </a>
                        <a href="/registrationPage">
                            <button id="signUp_button">${signUp_btn}</button>
                        </a>
                    </c:if>
                </div>

                <c:if test="${user != null}">
                    <div class="boxHeader">
                        <a href="/restaurant?command=move_to_account" id="moveToAcc">
                            <img id="accImg" src="../../images/accHeader.png" alt="acc">
                        </a>
                        ${user.getName()}
                    </div>
                </c:if>
                <a href="/showCurrentOrder" class="headLink">
                    <img id="orderImg" src="../../images/orderHeader.png" alt="Your order">
                    <span style="margin-left: -15px;" id="quantityOfDishes">${quantityOfDishes}</span>
                </a>
            </div>

        </header>