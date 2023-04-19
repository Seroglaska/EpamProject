<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <fmt:setLocale value="${sessionScope.local}" />
            <fmt:setBundle basename="local" var="loc" />

            <fmt:message bundle="${loc}" key="local.txt.personalInfo" var="personalInfoFmt" />
            <fmt:message bundle="${loc}" key="local.label.name" var="nameFmt" />
            <fmt:message bundle="${loc}" key="local.label.phoneNumber" var="phoneNumberFmt" />
            <fmt:message bundle="${loc}" key="local.label.email" var="emailFmt" />

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Account</title>
                <link rel="stylesheet" href="css/PersonalInfo.css">
            </head>

            <body>
                <jsp:include page="/WEB-INF/jsp/Header.jsp" />

                <c:if test="${userData == null}">
                    <jsp:forward page="/restaurant">
                        <jsp:param name="command" value="print_user_registr_data" />
                    </jsp:forward>
                </c:if>

                <div class="wrapper">
                    <main class="main">
                        <a href="/restaurant?command=move_to_account">
                            <img src="../../images/goBack.png" alt="goBack" id="goBackImg">
                        </a>

                        <h2>${personalInfoFmt}</h2>

                        <c:if test="${param.invalidUpdate}">
                            <h3 style="color: red;">
                                ${param.errorMsg}
                            </h3>
                        </c:if>



                        <div class="grid-container">
                            <div>${nameFmt}</div>
                            <form action="restaurant" method="post" id="editName" style="display: none;">
                                <input type="hidden" name="command" value="edit_personal_info">
                            </form>
                            <div>
                                <c:if test="${!param.editName}">
                                    ${userData.name}
                                </c:if>
                                <c:if test="${param.editName}">
                                    <input form="editName" type="text" name="userName" value="${userData.name}"
                                        required>
                                </c:if>
                            </div>
                            <div>
                                <c:if test="${!param.editName}">
                                    <a href="/personalInfo?editName=true">
                                        <img src="../../images/edit.png" alt="edit name" class="editImg">
                                    </a>
                                </c:if>
                                <c:if test="${param.editName}">
                                    <input form="editName" type="image" src="../../images/save.png" alt="save"
                                        class="editImg" id="name">
                                </c:if>
                            </div>


                            <div>${phoneNumberFmt}</div>
                            <form action="restaurant" method="post" id="editPhone" style="display: none;">
                                <input type="hidden" name="command" value="edit_personal_info">
                            </form>
                            <div>
                                <c:if test="${!param.editPhone}">
                                    ${userData.phoneNumber}
                                </c:if>
                                <c:if test="${param.editPhone}">
                                    <input form="editPhone" type="text" name="phoneNumber"
                                        value="${userData.phoneNumber}" required>
                                </c:if>
                            </div>
                            <div>
                                <c:if test="${!param.editPhone}">
                                    <a href="/personalInfo?editPhone=true">
                                        <img src="../../images/edit.png" alt="edit phoneNumber" class="editImg">
                                    </a>
                                </c:if>
                                <c:if test="${param.editPhone}">
                                    <input form="editPhone" type="image" src="../../images/save.png" alt="save"
                                        class="editImg" id="phone">
                                </c:if>
                            </div>


                            <div>${emailFmt}</div>
                            <form action="restaurant" method="post" id="editEmail" style="display: none;">
                                <input type="hidden" name="command" value="edit_personal_info">
                            </form>
                            <div>
                                <c:if test="${!param.editEmail}">
                                    ${userData.email}
                                </c:if>
                                <c:if test="${param.editEmail}">
                                    <input form="editEmail" type="text" name="email" value="${userData.email}" required>
                                </c:if>
                            </div>
                            <div>
                                <c:if test="${!param.editEmail}">
                                    <a href="/personalInfo?editEmail=true">
                                        <img src="../../images/edit.png" alt="edit email" class="editImg">
                                    </a>
                                </c:if>
                                <c:if test="${param.editEmail}">
                                    <input form="editEmail" type="image" src="../../images/save.png" alt="save"
                                        class="editImg" id="email">
                                </c:if>
                            </div>

                        </div>
                    </main>
                </div>

                <jsp:include page="/WEB-INF/jsp/Footer.jsp" />

            </body>

            </html>