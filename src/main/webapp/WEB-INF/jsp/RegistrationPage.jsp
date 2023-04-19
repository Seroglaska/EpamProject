<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <fmt:setLocale value="${sessionScope.local}" />
            <fmt:setBundle basename="local" var="loc" />

            <fmt:message bundle="${loc}" key="local.fieldSet.Registration" var="registr_legend" />
            <fmt:message bundle="${loc}" key="local.label.login" var="login_lbl" />
            <fmt:message bundle="${loc}" key="local.label.password" var="password_lbl" />
            <fmt:message bundle="${loc}" key="local.label.name" var="name_lbl" />
            <fmt:message bundle="${loc}" key="local.label.phoneNumber" var="phoneNumber_lbl" />
            <fmt:message bundle="${loc}" key="local.label.email" var="email_lbl" />
            <fmt:message bundle="${loc}" key="local.button.signUp" var="signUp_btn" />

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Registration page</title>
                <link rel="stylesheet" href="css/RegistrationPage.css">
            </head>

            <body>
                <form action="restaurant" method="post">
                    <input type="hidden" name="command" value="registration" />
                    <input type="hidden" name="roleId" value="" /><br>

                    <fieldset>
                        <legend>${registr_legend}</legend>

                        <c:if test="${param.invalidSignUp}">
                            <label id="error" style="color: red;">${param.validationMessage}</label><br>
                        </c:if>

                        <p>
                            <label>${login_lbl}: </label>
                            <input type="text" name="login" value="" required /><br>
                        </p>
                        <p>
                            <label>${password_lbl}: </label>
                            <input type="password" name="password" value="" required /><br>
                        </p>
                        <p>
                            <label>${name_lbl}: </label>
                            <input type="text" name="name" value="" required /><br>
                        </p>
                        <p>
                            <label>${phoneNumber_lbl}: </label>
                            <input type="text" placeholder="+375" name="phoneNumber" value="" required /><br>
                        </p>
                        <p>
                            <label>${email_lbl}: </label>
                            <input type="email" name="email" value="" /><br>
                        </p>
                        <p>
                            <input type="submit" value="${signUp_btn}" id="signUp_button">
                        </p>
                    </fieldset>
                </form>
            </body>

            </html>