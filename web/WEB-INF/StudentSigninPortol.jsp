<%-- 
    Document   : StudentSigninPortol
    Created on : 2020-3-8, 14:18:38
    Author     : Jianqing Gao
    Desctription : This is a interface for user to sign in
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>Students Sign-In</title>
    </head>
    <body id="userhome-body" >
        <header>

            <%@include file="/WEB-INF/jspf/navbar.jspf" %>

            <h1 class="center-text white">Sign-in</h1>
        </header>

        <div class="container opacity-08 padding2">
            <h1 class="center-text" id="sign-in-welcome-title"><%----%>
                 <!--Sample--> Welcome to ${DatabaseMain.manageStudyHall().selectDescriptionFromPeriodByID(attandance.getPeriodID())} study hall!
            </h1>


            <h2 class="center-text">
                Please scan your ID to sign in!
            </h2>

            <br><br><br>



            <form action="StudentSigninEmbedded" target="status-previous-person" class="centralized block center-text" method="POST">

                <div class="animated-text-input-container inline-block" id="mainInputID">
                    <input type="number" required title="Enter/scan users ID to continue" autofocus name="id"/>
                    <label class="label-name"><span class="content-name">Enter ID/Name: </span></label>
                </div>

                <!--<span id="sign-in-bytext"> &nbsp; Sign-in by </span>
                <%--Select--%>
                <select name="by" class="inline-block" >
                    <option value="id">
                        ID
                    </option>
                    <!--Bad idea. Name is not unique. Do not impl this.-->
                    <!--<option value="name">
                        Name
                    </option>
                </select>-->
                <br>
                <button class="arrowbutton whiteBg blue block centralized"><span><img src="images/icons/smgreentick.svg" alt="V" class="icon-16 margin-bottom-9">Sign-in</span></button>
            </form>




                <br><br>
            <iframe name="status-previous-person" class="signin-iframe" frameBorder="0">
                UNSUPPORTED!
            </iframe>


        </div>



    </body>
</html>
