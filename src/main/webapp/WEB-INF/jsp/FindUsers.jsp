<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <fmt:setLocale value="${sessionScope.local}" />
            <fmt:setBundle basename="local" var="loc" />

            <fmt:message bundle="${loc}" key="local.txt.findUser" var="findUsersFmt" />
            <fmt:message bundle="${loc}" key="local.txt.EnterLogin" var="EnterLoginFmt" />
            <fmt:message bundle="${loc}" key="local.txt.personalInfo" var="personalInfoFmt" />
            <fmt:message bundle="${loc}" key="local.label.name" var="nameFmt" />
            <fmt:message bundle="${loc}" key="local.label.phoneNumber" var="phoneNumberFmt" />
            <fmt:message bundle="${loc}" key="local.label.email" var="emailFmt" />
            <fmt:message bundle="${loc}" key="local.label.role" var="roleFmt" />

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Main page</title>
                <link rel="stylesheet" href="css/FindUsers.css">
            </head>

            <body>

                <jsp:include page="/WEB-INF/jsp/Header.jsp" />

                <div class="wrapper">
                    <main id="main-block" class="main">
                        <a href="/showAccount">
                            <img src="../../images/goBack.png" alt="goBack" id="goBackImg">
                        </a>

                        <h2>${findUsersFmt}</h2>

                        <div class="AlignItemsCenter">
                            <input id="inputUserLogin" type="text" placeholder="${EnterLoginFmt}">
                            <label for="inputUserLogin">
                                <img id="findImg" src="../../images/find.png" alt="find user">
                            </label>
                        </div>

                        <div id="foundUsers" hidden="true"></div>

                        <div class="grid-container">
                            <div>${nameFmt}</div>
                            <div id="nameDiv">
                                <span id="name"></span>
                            </div>
                            <div>
                                <img src="../../images/edit.png" alt="edit name" class="editImg">
                            </div>


                            <div>${phoneNumberFmt}</div>
                            <div id="phoneNumberDiv">
                                <span id="phoneNumber"></span>
                            </div>
                            <div>
                                <img src="../../images/edit.png" alt="edit phoneNumber" class="editImg">
                            </div>


                            <div>${emailFmt}</div>
                            <div id="emailDiv">
                                <span id="email"></span>
                            </div>
                            <div>
                                <img src="../../images/edit.png" alt="edit email" class="editImg">
                            </div>


                            <div>${roleFmt}</div>
                            <div id="roleDiv">
                                <span id="role"></span>
                            </div>
                            <div>
                                <img src="../../images/edit.png" alt="edit email" class="editImg">
                            </div>

                        </div>

                    </main>
                </div>

                <jsp:include page="/WEB-INF/jsp/Footer.jsp" />

                <script src="../../js/xhr.js"></script>
                <script src="../../js/FindUsers/User.js"></script>
                <script src="../../js/FindUsers/FindUsers.js"></script>
                <script src="../../js/FindUsers/EditBtn.js"></script>
                <script src="../../js/FindUsers/SaveBtn.js"></script>

            </body>

            </html>