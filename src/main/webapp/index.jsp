<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Main page</title>
        <link rel="stylesheet" href="css/Index.css">
    </head>

    <body>

        <jsp:include page="/WEB-INF/jsp/Header.jsp" />

        <div class="wrapper">
            <main id="main-block" class="main">
                <jsp:include page="/WEB-INF/jsp/Menu.jsp" />
            </main>
        </div>

        <jsp:include page="/WEB-INF/jsp/Footer.jsp" />

    </body>

    </html>