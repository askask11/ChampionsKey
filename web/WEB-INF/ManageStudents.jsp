<%-- 
    Document   : ManageStudents
    Created on : 2020-3-17, 23:28:01
    Author     : Jianqing Gao
    Desctription : This is a class where staffs can manage students.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <script src="js/main.js"></script>
        <title>Manage Students</title>
    </head>
    <body id="userhome-body" onload="listenMouseTitle(); helpcount(5, 'This page can help you manage students in the database.');">
        <header>
            <%@include file="/WEB-INF/jspf/navbar.jspf" %>
            <br>
            <h1 class="white center-text centralized">
                Manage Students
            </h1>
            <br>
        </header>
        <!--Overlay classes-->
        <div id="batchHelp" class="overlay">
            <div class="popup">
                <!--Title here-->
                <h2>Adding Students In Batch</h2>
                <a class="close" href="#">&times;</a>
                <!--Content here!-->
                <div class="popup-content">
                    You may add multiple students at a time.<br>
                    <ol>
                        <li>
                            Create a .txt file
                        </li>
                        <li>
                            Enter your students information in the .txt file in this format:<br>
                            id, name, grade<br>
                            id, name, grade<br>
                            id, name, grade<br>
                            .... and so on
                        </li>
                        <li>
                            Save, click <button>Browse...</button>, choose the file you just created.
                        </li>
                        <li>
                            When you finished, click &nbsp; <button class="btn btn-default">Submit</button> &nbsp; at right side of it.
                        </li>
                        <li>Student's list should be updated in a moment.</li>
                    </ol>
                </div>
            </div>
        </div>



        <section class="container opacity-08">
            <br>
            <h4>
                <img src="images/icons/smheart.svg" alt="heart"> Add New Student:
            </h4>
            <form class="form-inline" action='ManageStudentsTable' method='POST' target="manageStudents">
                <!--User add new student-->
                <input type="text" name="name" class="form-control" required placeholder="Student's Name" value="">

                <select class="form-control" name="grade">
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9" selected>9</option>
                    <option value="10">10</option>
                    <option value="11">11</option>
                    <option value="12">12</option>
                </select>
                <input type="number" name="id" placeholder="Student's ID" required class="form-control" value="">
                <input type='hidden' name="action" value="insert">
                <button class='btn btn-default'><img src="images/icons/smgreentick.svg" alt="pass" class="margin-bottom-6">Confirm</button>
                <button class="transparentButton" type="reset"><img src="images/icons/x16.svg" alt="x"></button>
            </form>
            <!--Batch adding-->
            <h4>
                Batch adding <a href='#batchHelp'><img src='images/icons/help.svg' class='icon-16'></a>
            </h4>
            <%--Allow user to add students from file.--%>
            <form class="form-inline" action='UploadStudentsList' method='POST' target="manageStudents" enctype="multipart/form-data" >
                <input type='file' class='form-control' required name='uploadFile' title="Choose a file to upload. click the ? icon if you have any questions.">

                <button class='btn btn-default' type='submit' title="Confirm batch adding">Submit</button>
            </form>
            <br>
            <h4>
                <img src="images/icons/smrdflag.svg" alt="flag"> Search Tool:
            </h4>


            <div class="inline-block" >
                <form action="ManageStudentsTable" method="GET" target='manageStudents'>
                    <input type="hidden" value="select-all" name="action">
                    <button class="arrowbutton whiteBg blue" type='submit' title="Show all students in the database."><span>Show All</span></button>

                </form>
            </div>
            <div class="inline-block">
                <form class="form-inline" action="ManageStudentsTable" method="GET" target='manageStudents'>
                    <div class="form-group">
                        <label class="" for="name"> | Or Filter By Name</label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="Student's name" required title="enter student's name and click 'confirm' to search.">
                        <input type="hidden" value="select-by-name" name="action">
                    </div>
                    <button type="submit" class="btn btn-default" title='Continue and search students by name.'>Confirm</button>
                </form>
            </div>

            <br>
            <h4>
                <img src="images/icons/smstar.svg" alt="star" title='Students in the database'> Student List:
            </h4>
            <iframe class="manage-students-iframe" name="manageStudents"></iframe>
        </section>

    </body>
</html>
