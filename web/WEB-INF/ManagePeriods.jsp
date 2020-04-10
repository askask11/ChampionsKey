<%-- 
    Document   : ManagePeriods
    Created on : 2020-3-19, 21:40:03
    Author     : Jianqing Gao
    Desctription : This is a page where user can manage the periods.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>Manage Periods</title>
    </head>
    <body id="userhome-body">
        <header>
            <%@include file="/WEB-INF/jspf/navbar.jspf" %>
            <br>
            <h1 class="white centralized center-text ">
                Manage Periods
            </h1>
            <br>
        </header>

        <section class="opacity-08 container" >

            ${managePeriodsMessage}
            <h4>
                <img src="images/icons/smheart.svg" alt="smheart"> Select Period 
            </h4>
            <br>
            <!--User select existing periods-->
            <form action="ManagePeriods" method="GET" class="form-inline">

                <select class="form-control" name="period-id">
                    <c:forEach var="period" items="${periods}">
                        <option value="<c:out value="${period[0]}"></c:out>"><c:out value="${period[1]}"></c:out></option>
                    </c:forEach>
                </select> 
                <button type='submit' class='arrowbutton whiteBg blue'><span>Submit</span></button>
            </form>
            <br>
            <div ${tableCss.getAttributeStyle()}>
                <h4>
                    <img src="images/icons/smheart.svg" alt="smheart"> Add New Student
                </h4>
                
                <!--New student to be registered in this period.-->
                <form class="form-inline" action="ManagePeriods" method="POST">
                    <select class="form-control" name="id">
                        <c:forEach var="student" items="${DatabaseMain.manageStudyHall().selectStudentFilterOutByPeriodId(periodID)}">
                            <option value="${student.getId()}">${student.getId()}, ${student.getName()}, G${student.getGrade()}</option>
                        </c:forEach>
                    </select>
                    <input type="hidden" name="action" value="insert">
                    <button class="btn btn-default" type="submit">Submit</button>
                </form>
                

            </div>




            <h4>
                <img src="images/icons/smstar.svg" alt="smstar"> Student List within this period:
            </h4>
            <br>
            <!--A table show students in this period-->
            <table class="table table-striped table-striped-nth-even-bluebg opacity-08" id="student-in-period-table" ${tableCss.getAttributeStyle()} >
                <thead>
                    <tr>
                        <td>
                            ID
                        </td>
                        <td>
                            Name
                        </td>
                        <td>
                            Grade
                        </td>
                        <td>
                            Operation
                        </td>
                    </tr>
                </thead>

                <%--Manage period for loop handle code--%>
                <tbody>
                    <c:forEach var="student" items="${dbObj}">
                        <tr>
                            <td>
                                <c:out value="${student.getId()}"></c:out>
                                </td>
                                <td>
                                <c:out value="${student.getName()}"></c:out>
                                </td>
                                <td>
                                <c:out value="${student.getGrade()}"></c:out>
                                </td>
                                <td>
                                    <form action="ManagePeriods" method="POST">
                                        <input type="hidden" value="delete" name="action">
                                        <input type="hidden" value="<c:out value="${student.getId()}"></c:out>" name="id">             
                                        <button type="submit" class="transparentButton"><img src="images/icons/deletepeople32.svg" alt="X"></button>
                                    </form>
                                </td>
                            </tr>
                    </c:forEach>
                </tbody>
            </table>

        </section>
    </body>
</html>
