<%-- 
    Document   : ManageStudentsTable
    Created on : 2020-3-18, 2:34:08
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
        <title>Manage Student Table</title>
    </head>
    <body class='transparent-white'>

        ${stext}
        ${manageStudentsTableMessage}
        <table class="table table-striped table-striped-nth-even-bluebg">
            <thead>
                <tr>
                    <th>
                        Name
                    </th>
                    <th>
                        ID
                    </th>
                    <th>
                        Grade
                    </th>
                    <th>
                        Operation
                    </th>
                </tr>
            </thead>
            <!--Start of student table-->
            <tbody>
                <c:forEach var="student" items="${studentList}">

                    <tr>

                        <td>
                            <!--Name of each student-->
                            <form action='ManageStudentsTable' method='POST'>
                                <input class='transparent-txtinput' value='<c:out value="${student.getName()}"></c:out>' name='name' title="click to edit, hit enter to confirm." required<%--onchange='this.form.submit();'--%>>
                                <input type='hidden' value='<c:out value="${student.getId()}"></c:out>' name='id'>
                                    <input type='hidden' value='update-name' name='action'>
                                <%--<button type='submit' class='transparentButton'><img class='icon-16' src='images/icons/smgreentick.svg'></button>--%>
                            </form>
                        </td>
                        <td>
                            <!--ID of student-->
                            <form action='ManageStudentsTable' method='POST'>
                                <input type="text" class='transparent-txtinput'  value="<c:out value="${student.getId()}"></c:out>"  name='new-id'  title="click to edit, hit enter to confirm." required>
                                    <!--put a hidden old id here.-->
                                    <input type="hidden" value="<c:out value="${student.getId()}"></c:out>" name="id" >
                                    <input type='hidden' value='update-id' name='action'>
                                <%--<button type='submit' class='transparentButton'><img src='images/icons/smgreentick.svg'></button>--%>
                            </form>
                        </td>
                        <td>
                            <!--Grade of student-->
                            <form  action='ManageStudentsTable' method='POST'>
                                <input class='transparent-txtinput' value='<c:out value="${student.getGrade()}"></c:out>' name='grade'  title="click to edit, hit enter to confirm." required>
                                    <input type='hidden' value='update-grade' name="action">
                                    <input type="hidden" value="<c:out value="${student.getId()}"></c:out>" name="id">
                                <%--<button type='submit' class='transparentButton'><img src='images/icons/smgreentick.svg'></button>--%>
                            </form>
                        </td>

                        <td>
                            <form action="ManageStudentsTable" method="POST">
                                <input type="hidden" value="delete" name="action">
                                <input type="hidden" value="<c:out value="${student.getId()}"></c:out>" name="id">
                                    <button class="transparentButton" type="submit"><img src='images/icons/deletepeople32.svg' class="icon-16 margin-bottom-6" title="remove this student" alt="Remove X"></button>
                                </form>
                            </td>

                        </tr>

                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
