<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <fmt:setLocale value="${sessionScope.local}" />
            <fmt:setBundle basename="local" var="loc" />

            <fmt:message bundle="${loc}" key="local.h2.done" var="doneFmt" />

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Your order</title>
                <link rel="stylesheet" href="css/FinishingTheOrder.css">
            </head>

            <body>
                <jsp:include page="/WEB-INF/jsp/Header.jsp" />


                <div class="wrapper">
                    <main class="main">
                        <img id="tick" src="../../images/tick.png" alt="Complete">
                        <h2>${doneFmt}</h2>
                    </main>
                </div>

                <jsp:include page="/WEB-INF/jsp/Footer.jsp" />
            </body>

            </html>