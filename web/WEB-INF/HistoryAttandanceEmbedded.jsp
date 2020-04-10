<%-- 
    Document   : HistoryAttandanceEmbedded
    Created on : 2020-3-21, 2:13:26
    Author     : Jianqing Gao
    Desctription :  This is a page where user can see their classes hosted and manage them.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>History Attendance</title>
    </head>
    <body class="opacity-08">

        ${periodMessage}
        <table class="table table-striped table-striped-nth-even-bluebg">
            <thead>
                <tr> 
                    <td>
                        Date
                    </td>
                    <td>
                        Period Name
                    </td>
                    <td>
                        Tardy Time
                    </td>
                    <td>
                        Operation
                    </td>
                </tr>
            </thead>

            <%--Declar DB accessor--%>
            <c:set var="manager" value="${DatabaseMain.manageStudyHall()}"></c:set>
                <tbody>
                <c:forEach var="attendance" items="${manager.selectFromAttandancesByTeacherID(staff.getId())}">
                    <tr> 
                        <td>
                            <c:out value="${attendance.getDateAsString()}"></c:out>
                            </td>

                            <td>
                            ${manager.selectDescriptionFromPeriodByID(attendance.getPeriodID())}
                        </td>

                        <td>
                            <c:out value="${attendance.getTardyTimeAsString()}"></c:out>
                            </td>

                            <td>
                                <form action="AttandanceStudentEmbedded" method="GET" class="inline-block">
                                    <input value="<c:out value="${attendance.getAttandanceID()}"></c:out>" name='id' type="hidden">
                                    <button type="submit" class="transparentButton"><img src="images/icons/edit32.svg" alt="edit"></button>
                                </form>
                                <form action="RestartSession" method="POST" class="inline-block ${attandance!=null?"hide":""} " target="_parent">
                                <input type="hidden" value="${attendance.getAttandanceID()}" name="id" >
                                <button class="transparentButton" type="submit" title="resume this class. After you click on this button, this class will be active again."><span class="icon-32 blue">&#9654;</span></button>
                            </form>

                            <c:choose>
                                <c:when test="${empty attandance}">
                                    <c:set var="currentId" scope="page" value="-1"></c:set>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="currentId" scope="page" value="${attandance.getAttandanceID()}"></c:set>
                                </c:otherwise>
                            </c:choose>

                            <form action="HistoryAttandanceEmbedded" method="GET" class="inline-block ${attendance.getAttandanceID()==currentId?"hide":""}">
                                <input value="<c:out value="${attendance.getAttandanceID()}"></c:out>" name='id' type="hidden">
                                    <input type='hidden' value='delete' name='action'>
                                    <button type="submit" class="transparentButton" title="Delete this record and student's attendance record associated with it."><img src="images/icons/x16.svg" alt="delete" class="icon-32" ></button>
                                </form>  
                            </td>
                        </tr>

                </c:forEach>


            </tbody>
        </table>
    </body>
</html>
