<%-- 
    Document   : StartHall
    Created on : 2020-3-15, 15:13:23
    Author     : Jianqing Gao
    Desctription : 
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>Start A Hall</title>
        
    </head>
    <body id="userhome-body">
        <header>
            <%@include file="/WEB-INF/jspf/navbar.jspf" %>
            <br>
            <h1 class="center-text white">
                Start A StudyHall
            </h1>
            <br>
        </header>
            
            <section class="container block opacity-08">
              
                <h3>
                   Hi, ${staff.getUsername()} This tool can help you quickly start a studyhall.
                </h3>
                
                <form action="StartHall" method="POST">
                    
                <h4>
                    <img src="images/icons/smheart.svg" alt="">Choose a period:
                </h4>
                    <br>
                <select id="period-select" name="periods">
                    <!--Place a for loop inside-->
                    
                    <c:forEach var="period" items="${DatabaseMain.manageStudyHall().selectFromPeriods()}">
                        <option ${period[0].equals("1")?"selected":""} value="<c:out value="${period[0]}"></c:out>"><c:out value="${period[1]}"></c:out></option>
                    </c:forEach>
                </select>
                
                <h4>
                    <img src="images/icons/smheart.svg" alt="">2.Set a tardy time (24h system)
                </h4>
                    <br>
                    <!--SET THE TIME WHEN TO BE COUNTED AS TARDY-->
                <input type="time" name="tardyTime" required>
                
                <h4>
                    <img src="images/icons/smheart.svg" alt="">Confirm!
                </h4>
                
                <p>
                    <button type="submit" class="arrowbutton whiteBg blue inline-block"><span>Start Now</span></button> &nbsp; &nbsp; <input type="checkbox" class="checkbox inline-block" checked value="true" name="goSignin">Jump to student sign-in page!
                </p>
                
                </form>
            </section>
            
    </body>
</html>
