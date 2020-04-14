<%-- 
    Document   : SignUp
    Created on : 2020-3-10, 18:34:38
    Author     : Jianqing Gao
    Desctription : This is a page for teachers to sign up.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>Teacher Sign-ups</title>
    </head>
    <body id="userhome-body">
        <header>
            <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        
            <h1 class="center-text white">Sign Up</h1>
        </header>
        <br>
        <div class="container opacity-08">
         
            <h3><img src="images/icons/smrdflag.svg" class="icon-32">Sign-up now to start an efficiently managed studyhall!</h3>
            
 
            <!--A message that will show up to support staffs.-->
            ${signupMessage}
            <div class="row">
                <div class="col-md-6">
                    <img src="images/icons/redstar.svg"> For up to now, we only support village staffs to sign up.<br> Please use your school email to sign-up. Thank you!
                    <form action="SignUp" method="POST">
                        <div class="animated-text-input-container width-80" >
                            <input type="text" id="usernameField" required title="The perferred name for log in" name="username" autocomplete="off"/>
                            <label class="label-name"><span class="content-name">Username: </span></label>
                        </div>

                        <div class="animated-text-input-container width-80">
                            <input type="email" id="emailField" required title="Please use your school email" name="email" autocomplete="off"/>
                            <label class="label-name"><span class="content-name">Email:</span></label>
                        </div>
                        
                        <div class="animated-text-input-container width-80">
                            <input type="number" id="idField" required title="Please use your school email" name="id" autocomplete="off"/>
                            <label class="label-name"><span class="content-name">Badge ID</span></label>
                        </div>
                    

                        <div class="animated-text-input-container width-80">
                            <input type="text" id="legalNameField" required title="Your FULL name" name="legalname" autocomplete="off"/>
                            <label class="label-name"><span class="content-name">Your Full Name: </span></label>
                        </div>
                        
                        <div class="animated-text-input-container width-80">
                            <input type="password" id="passwordField" required title="password" name="password" autocomplete="off"/>
                            <label class="label-name"><span class="content-name">Password: </span></label>
                        </div>


                        <button type="submit" class="arrowbutton whiteBg blue" id="sign-up"><span>Sign-up!</span></button>
                    </form>
                </div>
                <div class="col-md-6">
                    <br><br><br>
                    <img src="images/manage-pic-3.png" width="400" alt="manage pic conf">
                </div>
            </div>
        </div>

    </body>
</html>
