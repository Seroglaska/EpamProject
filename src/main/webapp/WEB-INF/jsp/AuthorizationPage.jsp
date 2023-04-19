<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <fmt:setLocale value="${sessionScope.local}" />
            <fmt:setBundle basename="local" var="loc" />

            <fmt:message bundle="${loc}" key="local.fieldSet.Authorization" var="auth_legend" />
            <fmt:message bundle="${loc}" key="local.label.EnterLogin" var="lbl_login" />
            <fmt:message bundle="${loc}" key="local.label.EnterPassword" var="lbl_pswd" />
            <fmt:message bundle="${loc}" key="local.button.signIn" var="signIn_btn" />

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Authorization Page</title>
                <link rel="stylesheet" href="css/AuthorizationPage.css">
            </head>

            <body>
                <form action="restaurant" method="post">
                    <input type="hidden" name="command" value="authorization" />

                    <fieldset>
                        <legend>${auth_legend}</legend>

                        <c:if test="${invalidSignIn == true}">
                            <label id="error" style="color: red;">Invalid login or password</label><br>
                        </c:if>

                        <p>
                            <label>${lbl_login}: </label>
                            <input type="text" name="login" value="" required /><br>
                        </p>
                        <p>
                            <label>${lbl_pswd}: </label>
                            <input type="password" name="password" value="" required />
                        </p>
                        <p>
                            <input type="submit" value="${signIn_btn}" id="signIn_button">
                        </p>
                    </fieldset>
                </form>

            </body>

            </html>