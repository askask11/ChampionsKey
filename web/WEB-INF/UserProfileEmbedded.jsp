<%-- 
    Document   : UserProfileEmbedded
    Created on : 2020-4-8, 0:16:28
    Author     : Jianqing Gao
    Desctription : 
--%>

<%@ taglib prefix="lover" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>${username}'s Profile</title>
    </head>
    <body>
        <h1 class="center-text">
            Edit Your Profile
        </h1>
        ${profileMessage}
        <%--Check expired session avoid null pointer!--%>
        <lover:choose>
            <lover:when test="${empty staff}">
                You have not logged in or your session is expired. Please <a href="index" target="_parent">Log in</a> again!
            </lover:when>
            <lover:otherwise>
                <label for="idField">
                    ID: <input class="form-control " value="${staff.getId()}" name="id" id="idField" type="text" disabled title="Want to update your id? Contact support@gaogato.com please.">
                </label>
                <label for="emailField">
                    Email: 
                    <input class="form-control" id="emailField" name="email" type="email" value="${staff.getEmail()}" disabled title="please contact support@gaogato.com if you need to change your e-mail.">
                </label>   
                <!--Update username-->
                <form class="form-inline" action="UserProfileEmbedded" method="POST">
                    <input type="hidden" name="action" value="update-username">
                    <label for="usernameField">
                        Username:
                        <input id="usernameField" type="text" name="username" class="form-control" value="${username}">
                    </label>
                    <button type="submit" class="btn btn-default">Change Username</button>
                </form>
                <!--Legal Name-->
                <form class="form-inline" action="UserProfileEmbedded" method="POST">
                    <input type="hidden" name="action" value="update-legalname">
                    <label for="legalnameField">
                        Legal Name:
                        <input id="legalnameField" type="text" name="legalname" class="form-control" value="${staff.getLegalName()}">
                    </label>
                    <button type="submit" class="btn btn-default">Change Name</button>
                </form>
                <!--Password-->
                <form class="form-inline" action="UserProfileEmbedded" method="POST">
                    <input type="hidden" name="action" value="update-password">

                    Password:
                    <input name="oldPassword" class="form-control" placeholder="Enter old password" type="password">
                    <input id="passwordField" type="password" name="password1" class="form-control" placeHolder="New Password...">
                    <input class="form-control" type="password" name="password2" placeHolder="Enter Again">

                    <button type="submit" class="btn btn-default">Change Password</button>
                </form>

            </lover:otherwise>
        </lover:choose>     
    </body>
</html>
