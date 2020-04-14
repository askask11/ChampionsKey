<%-- 
    Document   : ModifyStudyHall
    Created on : 2020-3-10, 11:19:33
    Author     : Jianqing Gao
    Desctription : This is a main page, a table will be embedded into this page to allow stafs
to modify the study hall.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>Modify Hall</title>
    </head>
    <!--The body part of the user home.-->
    <body id="userhome-body">
        <header>
            <%@include file="jspf/navbar.jspf" %>
            <br>
            <h1 class="center-text white">
                Modify A Hall
            </h1>
        </header>
        <br>

        <div class="container opacity-08">

            <h4>
                <img src="images/icons/redstar.svg" class="icon-16"> Attandance Studyhall Info ID#${attandanceID}
            </h4>

            <!--Attendance filter-->
            <form target="student-list-by-attandance">
                <p>Show by attendance
                    <select name="attandanceFilter">
                        <option value="all">
                            All
                        </option>
                        <option value="ontime">
                            On-time
                        </option>
                        <option value="tardy">
                            Tardy
                        </option>
                        <option value="absent">
                            Absent
                        </option>

                    </select>
                    <button class="arrowbutton">
                        <span> Confirm</span>
                    </button>
                </p>
            </form>
            <iframe name="student-list-by-attandance" class="user-home-iframe"  frameBorder="0">
                Sorry bad.
            </iframe>

            <br>
            <!--Location filter-->
            <form action="" target="student-list-location-iframe">
                <p>
                    Students by location:
                    <select class="">
                        <option value="all">
                            All
                        </option>
                        <option value="hall">
                            Main Champion Hall
                        </option>
                        <option value="store">
                            School Store
                        </option>
                        <option value="learningCommon">
                            Learning Commons
                        </option>
                        <option value="na">
                            unknown/others
                        </option>
                    </select>
                    <button class="arrowbutton"><span>Confirm</span></button>
                </p>
            </form>

            <!--This is where the embedded table will go-->
            <iframe name="student-list-location-iframe" class="user-home-iframe" frameBorder="0">
                Student Location IFRAME
            </iframe>
        </div>
    </body>
</html>
