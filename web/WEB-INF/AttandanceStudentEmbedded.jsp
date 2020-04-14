
<%-- 
    Document   : AttandanceStudentEmbedded
    Created on : 2020-3-21, 3:36:08
    Author     : Jianqing Gao
    Desctription : This is the page where allows the students attendance to show up.
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>Student Att record</title>
    </head>
    <body>

        <%--If-else statement--%>
        <c:choose>
            <c:when test="${empty staff}">
                <h1>
                    Sorry, your session is expired! Please login again!
                </h1>
            </c:when>
            <c:otherwise>

                <%--Manager--%>

                <c:catch var="ex">
                    <c:choose>
                        <c:when test="${empty DatabaseMain}">
                            <h1>Session expired!</h1>
                        </c:when>
                        <c:otherwise>

                            <%--Database access object.--%>
                            <c:set var="hallManager" value="${DatabaseMain.manageStudyHall()}"></c:set>
                            <c:set var="studentManager" value="${DatabaseMain.manageStudents()}"></c:set>

                            <%--Check for id--%>
                            <c:choose>
                                <c:when test="${empty id}">
                                    <c:set var="id" value="${param.id}" scope="page"></c:set>
                                </c:when>
                            </c:choose>

                            <%--Check for filter--%>
                            <c:choose>
                                <%--Filter by att id only--%>
                                <c:when test="${empty filter}"><!--See if the filter is empty-->
                                    <c:set var="dbObj" value="${hallManager.selectFromAttandanceHistoryByAttId(id)}"></c:set>
                                    <c:set var="userFilter" value="all students"></c:set>
                                </c:when>

                                <c:otherwise>
                                    <%--Use filter--%>
                                    <c:choose>

                                        <c:when test='${filter.equals("select-attendance-code")}'>
                                            <%--Filter by att code--%>
                                            <c:set var="userFilter" value="students by attendance code"></c:set>
                                            <c:set var="dbObj" value="${hallManager.selectFromAttandanceHistoryByAttCodeId(input,id)}"></c:set>
                                        </c:when>

                                        <c:when test='${filter.equals("select-location")}'>
                                            <%--Filter by location--%>
                                            <c:set var="userFilter" value="students by location"></c:set>
                                            <c:set var="dbObj" value='${hallManager.selectFromAttandanceHistoryByLocationAndAttId(input,id)}'></c:set>
                                        </c:when>
                                        <c:otherwise>
                                            <%--Unknown select filter, load as usual.--%>
                                            <c:set var="userFilter" value="all students"></c:set>
                                            <c:set var="dbObj" value="${hallManager.selectFromAttandanceHistoryByAttId(id)}"></c:set>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>

                            <%--Check bonded attandance record.--%>
                            <c:set var="attendance" value="${hallManager.selectAttandanceRegisterByAttID(id)}" />
                            <div class="alert alert-success">
                                <strong>Note</strong> You are currently editing <strong>${hallManager.selectDescriptionFromPeriodByID(attendance.getPeriodID())}</strong>. <span class="help-title" title="After this time, student signed in will be counted as tardy.">Tardy time:</span> <strong>${attendance.getTardyTimeAsString()}</strong>. Date:<strong>${attendance.getDateAsString()}</strong>.

                                <%-- Filter: Displaying ${filter==null?"All students.":(filter.equals("select-attendance-code")?"students by attendance status":filter.equals("select-location")?"by student's location":"All students")}--%>
                                Filter: <strong> ${userFilter} </strong>
                                <span class="right help-title" title="For select boxes, click the green tick to confirm update. For 'description', hit 'enter' key to confirm update.">How to edit?</span>
                            </div>

                            ${studentMessage}
                            <table class='table table-striped table-striped-nth-even-bluebg'>
                                <thead>
                                <th class='help-cursor' title="Student's ID">
                                    ID
                                </th>
                                <th class='help-cursor' title="Name of the students.">
                                    Name
                                </th>
                                <th class="help-cursor" title="The lastest time you update this student's information during active period.">
                                    Last Update Time
                                </th>
                                <th class='help-cursor' title='The location of this student.'>
                                    Location
                                </th>
                                <th class='help-cursor' title='The attendance code of each students.'>
                                    Attandance Code
                                </th>
                                <th class='help-cursor' title="The description of the student in this period. Students can have different descriptions for each period.">
                                    Description
                                </th>
                            </thead>
                            <!--Table body here-->
                            <tbody>
                                <c:forEach var="row" items="${dbObj}">
                                    <tr>
                                        <td>
                                            <!--ID-->
                                            <c:out value="${row.getStudentID()}"></c:out>
                                            </td>
                                            <td>
                                            ${studentManager.getStudentNameById(row.getStudentID())}
                                        </td>
                                        <td>
                                            <c:out value="${row.getLastUpdateTimeAsString()}"></c:out>
                                            </td>
                                            <td>
                                            <c:set var="location" value="${row.getLocation()}"></c:set>
                                                <!--LOCATION SELECT-->
                                                <form action="AttandanceStudentEmbedded" method="POST" class="form-inline">
                                                    <input type="hidden" name="attandanceID" value="${id}">
                                                <input type="hidden" name="studentID" value="${row.getStudentID()}">
                                                <input type="hidden" name="action" value="update-location">
                                                <select class="form-control" name="location">
                                                    <%--Select user's location--%>
                                                    <option value="1"  ${(location==1)?"selected":""} >Champion's Hall</option>
                                                    <option value="2"  ${(location==2)?"selected":""} >Learning Common</option>
                                                    <option value="3"  ${(location==3)?"selected":""} >School Store</option>
                                                    <option value="0"  ${(location==0)?"selected":""} >Unknown</option>
                                                </select>

                                                <button class="transparentButton" type="submit"><img src="images/icons/smgreentick.svg" class="margin-bottom-6 icon-16"></button>
                                            </form>
                                        </td>
                                        <td>
                                            <!--Another select here for attandance code.-->
                                            <c:set var="att" value="${row.getAttandanceCode()}"></c:set>
                                                <!--Attendance select-->
                                                <form action="AttandanceStudentEmbedded" method="POST" class="form-inline">
                                                    <input type="hidden" name="action" value="update-attandance">
                                                    <input type="hidden" name="attandanceID" value="${id}">
                                                <input type="hidden" name="studentID" value="${row.getStudentID()}">
                                                <select name="code" class="form-control">
                                                    <option value="0" ${att==0?"selected":""} >Absent</option>
                                                    <option value="1" ${att==1?"selected":""} >Present</option>
                                                    <option value="2" ${att==2?"selected":""} >Tardy</option>
                                                </select>
                                                <button class="transparentButton" type="submit"><img src="images/icons/smgreentick.svg" class="margin-bottom-6 icon-16"></button>
                                            </form>
                                        </td>
                                        <td>
                                            <!--Update description.-->
                                            <form class="form-inline" action="AttandanceStudentEmbedded" method="POST">
                                                <input type="hidden" name="action" value="update-description">
                                                <input type="hidden" name="attandanceID" value="${id}">
                                                <input type="hidden" name="studentID" value="${row.getStudentID()}">
                                                <input type="text" class="transparent-txtinput" name="description" value="${row.getDescription()}" title="Hit 'Enter' to submit.">
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <!--Add Stdent In Period Temporary-->
                            </tbody>
                        </table>
                        <form action="AttandanceStudentEmbedded" method="POST" class="form-inline">

                            <input type='hidden' name='action' value='insert'>
                            <input type="hidden" name="attandanceID" value="${id}">
                            <label for="studentID">
                                Add New Student:
                                <input id="studentID" type="number" class="form-control" name="studentID" placeHolder="Enter ID here" title="If you add a student here, the student will be registered in this class only this time. If you would like to enroll a student, please go to 'Manage Periods'.">
                            </label>
                            <button type="submit" class="btn btn-default">Confirm</button>
                        </form>

                    </c:otherwise>
                </c:choose>
            </c:catch>
            <c:if test="${ex != null}">
                Your session has expired! Please login again!
                <form method='GET' action="index">
                    <button class='arrowbutton whiteBg blue'><span>Go to index!</span></button>
                </form>
            </c:if>
        </c:otherwise>
    </c:choose>



</body>
</html>
