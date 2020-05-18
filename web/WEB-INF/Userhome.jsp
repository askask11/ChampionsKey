<%-- 
    Document   : Userhome
    Created on : 2020-3-8, 12:35:36
    Author     : Jianqing Gao
    Desctription : The "index" page after user have signed in.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>Dashboard</title>
    </head>
    <body id="userhome-body">
        <header>
            <%--Include navbar--%>
            <%@include file="jspf/navbar.jspf" %>
            <h1 class="center-text white">Dashboard</h1>
        </header>

        <section class="container opacity-08">
            <h2>Welcome,  ${staff.getUsername()}</h2>


            <!--Visiable if user does  have a studyhall-->
            <div ${activeDivCss}>
                <h3>${currentStudyHallStatusInfo}</h3>

                <br>
                <!--Button for sign-ins-->

                <form action='StudentSigninPortol'>
                    <button class='arrowbutton centralized center-text block' title='Click this to open the page which allows students sign-in'
                            ><span>Students Sign-in Panel</span></button>

                </form>
                <br>
                <!-- Quick Tools:
                 <div class="row">
 
                     <div class="col-md-4">
                         <button class="arrowbutton bluebg">
                             <span>
                                 Use Flexi Pass
                             </span>
                         </button>
                     </div>
                     <div class="col-md-4">
                         <button class="arrowbutton bluebg">
                             <span>
                                 Go To Store
                             </span>
                         </button>
                     </div>
                     <div class="col-md-4">
                         <button class="arrowbutton bluebg">
                             <span>
                                 Sign Back In
                             </span>
                         </button>
                     </div>
                 </div>
                 <br>
 
                -->

                <form action="EndSession" method="POST">
                    <button type="submit" class="btn btn-danger">End Session</button>
                    <img class="help-cursor" title="You currently have an active session. Click to end the current studyhall." alt="help" src="images/icons/help.svg">
                </form>
                <p>Want to start another study hall? Please end current session first.</p>
                <%--Current List of students--%>
                <h4 title="This shows all students that are currently in this class." class="help-cursor"><img src="images/icons/redstar.svg" class="icon-16">Student List: <%--Attandance ID # <span class="help-title" title="An ID will be assigned to each"></span>--%></h4>

                <!--Attendance ID: ${activeAttandanceID}-->
                <!--SELECT ALL-->

                <form target='student-list-iframe' class='form-inline' action='AttandanceStudentEmbedded'>
                    <button class='arrowbutton whiteBg blue' title="Click to show all students."><span>Show All</span></button>
                    <input type='hidden' value='${attandance.getAttandanceID()}' name='id'>
                </form>

                <form target="student-list-iframe" class='form-inline ' action='AttandanceStudentEmbedded' >
                    <p> <strong> | OR | </strong>  Show by <span title="set student's attendance code as filter." class='help-title'>attendance</span>
                        <input type='hidden' value='select-attendance-code' name='filter'>
                        <input type='hidden' value='${attandance.getAttandanceID()}' name='id'>
                        <select name="input" class='form-control' title="Show all students that satisify the status you selected.">
                            <option value="1" selected>
                                Present
                            </option>
                            <option value="2">
                                Tardy
                            </option>
                            <option value="0">
                                Absent
                            </option>
                        </select>
                        <button class="btn btn-default" title="Confirm filter and refresh the list.">
                            <span> Confirm</span>
                        </button>
                    </p>
                </form>

                <form target="student-list-iframe" class='form-inline ' action='AttandanceStudentEmbedded'>
                    <p>
                        <strong> | OR | </strong> Students by <span class="help-title" title="Sey the student's current location registered in the system as filter.">location:</span>
                        <select class="form-control" name='input' title="Show all students that satisify the status you selected.">
                            <option value="1" selected>
                                Champion Hall
                            </option>
                            <option value="2">
                                Learning Commons
                            </option>
                            <option value="3">
                                School Store
                            </option>
                            <option value="0">
                                Unknown
                            </option>
                        </select>
                        <input type='hidden' value='${attandance.getAttandanceID()}' name='id'>
                        <input type='hidden' value='select-location' name='filter'>
                        <button class="btn btn-default" title="confirm and refresh your list."><span>Confirm</span></button>
                    </p>
                </form>

                <!--The student list window.-->        
                <iframe name="student-list-iframe" class="user-home-iframe"  frameBorder="0">
                    Your browser does not support iframe,
                    Visit <a href="https://windowsreport.com/browser-does-not-support-iframes/">Here</a> to fix the problem.
                </iframe>
            </div>            


            <!--Visiable if the user does has an active studyhall-->
            <div ${inactiveDivCSS}>
                <h3>
                    You currently don't have an <span class="help-cursor" title="A class you're currently operating.">active studyhall!</span>
                </h3>
                <br>
                <!--Button for starting a studyhall-->

                <form action='StartHall' method="GET">
                    <button class='centralized center-text block arrowbutton' title='click to start an active studyhall.'><span>Start A Study Hall</span></button>
                </form>
            </div>

            <h4>
                <img src="images/icons/smstar.svg" alt="smstar"><span class='help-cursor' title='Classes you hosted in the past.'>Your history attendances: </span>
            </h4>
            <form target="history-record-iframe" action="HistoryAttandanceEmbedded" method="GET">
                <button type="submit" class="arrowbutton whiteBg blue" title='click to show the record.'><span>Show</span></button>
            </form>
            <iframe name="history-record-iframe" id="history-record-iframe" class="user-home-iframe" frameBorder="0"></iframe>
        </section>
        <br><br>
        <h4 class="white center-text centralized">
            Thanks for using champions key!
        </h4>
    </body>
</html>
