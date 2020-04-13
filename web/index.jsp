<%-- 
    Document   : index
    Created on : 2020-3-6, 13:16:39
    Author     : Jianqing Gao
    Desctription : This is the home page of the user. Also the initalizing page.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>Champion's Key</title>
    </head>
    <body id='index-body'>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>

            <hgroup>
                <h1 class='center-text'>
                    Champion's Key
                </h1>
                <h3 class='center-text'>
                    Trust us: register efficiently, study efficiently, live efficiently!
                </h3>
            </hgroup>
        



        <div class='index-center-container half-opaque'>
            <div class='row'>
                <!-- Main title -->
                <div class='col-md-6'>
                    <h1>
                        Manage A Study Hall Has Never Been So Easy!
                    </h1>
                    Sign up for study hall now!
                    <form action="SignUp" method="GET">
                        <button class='btn btn-default'>
                            Sign - up!
                        </button>
                    </form>
                </div>
                <div class='col-md-6'>
                    ${loginMessage} 
                    <!--Forgot password overlay message-->
                    <%--Popup overlay class--%>
                    <div id="forgotPassword" class="overlay">
                        <div class="popup">
                            <!--Title here-->
                            <h2>Forgot Password</h2>
                            <a class="close" href="#">&times;</a>
                            <!--Content here!-->
                            <div class="popup-content">
                                Forgot your password? Don't worry! Type your email here and recover your password!
                                <form class="form-group" action="PasswordRecovery" method="POST">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
                                        <input type="email" class="form-control" name="email" placeholder="Email">
                                    </div>
                                    <input type="hidden" value="submit" name="action">
                                    <button class='btn btn-default' type='submit'>Confirm</button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <form action="Userhome" method="POST">
                        <div class="form-group">
                            <%--CHANGE HERE LATER ON--%>
                            <label for="email"><img src="images/icons/smmail.svg" class="icon-16 margin-bottom-9" alt="email">Email address:</label>
                            <input type="email" class="form-control" id="email-field" name='email' >
                            <a href="#forgotPassword">Forgot Password?</a>
                        </div>
                        <div class="form-group">
                            <!--Password field-->
                            <label for="password"><img src="images/icons/smkeyheart.svg" class="icon-16 margin-bottom-9" alt="password">Password:</label>
                            <input type="password" class="form-control" id="password-field" name='password' >
                        </div>
                        <!--<div class="checkbox">
                            <label><input type="checkbox"> Remember me</label>
                        </div>-->
                        <button type="submit" class="btn btn-default blue"><span class="glyphicon glyphicon-ok green"></span>Submit</button>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
