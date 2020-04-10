<%-- 
    Document   : Verify
    Created on : 2020-3-12, 14:20:05
    Author     : Jianqing Gao
    Desctription : 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>Verify</title>
    </head>
    <body id="userhome-body">

        <header>
            <%@include file="/WEB-INF/jspf/navbar.jspf" %>
            <br>
            <h1 class="center-text">
                New User Verification
            </h1>
            <br>
        </header>

        <div class="container opacity-08">
            <h3>
                ${verifyMessageTitle}
            </h3>

            ${verifyMessageBody}

            <form action="index" method="GET">
                <button class="whiteBg blue arrowbutton" type="submit">
                    <span>Home</span>
                </button>
            </form>
        </div>

    </body>
</html>
