<%-- 
    Document   : PasswordRecovery
    Created on : 2020-4-5, 4:52:13
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
        <title>Password Recovery</title>
    </head>
    <body id="password-recovery-body">
        <header>
            <%@include file="./jspf/navbar.jspf" %>
            <br>
            <h1 class="center-text">Password Recovery</h1>
        </header>
        <br>
        <br>

        <%--read the "bash" value from parameter.--%>
        <c:set var="bash" scope="page" value="${param.bash}" />
        <c:set var="dbMain" scope="page" value="${DatabaseMain}"></c:set>
        <c:set var="staffManager" scope="page" value="${dbMain.manageStaff()}"></c:set>
        <jsp:useBean id="alert" class="util.BSAlerts"/>


        <div class="container opacity-08">
            <h3 class="center-text">${bash==null?"Sumbit A Request":"Your password recovery request"}</h3>

            <c:set var="action" value="${param.action}" scope="page"></c:set>
            <c:choose>
                <c:when test="${empty bash}">
                    <%--Either a new ruqest or a new request be submitted.
                    Need to be determined by "action"
                    --%>
                    <%--Action?--%>

                    <c:choose>
                        <c:when test="${empty action}">
                            <%--A plain new request never be submitted
                            User need to fill out the new request.
                            --%>
                            <form action="PasswordRecovery" method="POST">
                                <!--Invisible action to determine next step-->
                                <input type="hidden" value="submit" name="action">
                                <!--User's email-->
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
                                    <input type="email" class="form-control" name="email" placeholder="Email" required>
                                </div>
                                <!--Confirm entry-->
                                <button class='btn btn-default' type='submit'>Confirm</button>
                            </form>
                        </c:when>
                        <c:when test="${action.equals('submit')}">
                            <%--Connect to database & request a new bash--%>
                            <c:set var="staff" value="${staffManager.selectStaffByEmail(param.email)}"></c:set>
                            <c:choose>
                                <c:when test="${empty staff}">
                                    <h3 class='red'>The email you submitted is not registered!</h3>
                                    You may <a href='PasswordRecovery'>re-submit</a> or <a href="SignUp">Sign up</a>(Staff only).
                                    Question? Please contact <a href="mailto:support@gaogato.com">support@gaogato.com</a>
                                </c:when>
                                <c:otherwise>
                                    <%--Get class--%>
                                    <jsp:useBean id="randomizer" scope="page" class="util.Randomizer"></jsp:useBean>

                                    <c:set var="bashGenerated" value="${randomizer.generateBash()}"></c:set>

                                    <c:set var="rowsAffected" value="${staffManager.requestRecoveryBash(staff.getId(),bashGenerated)}" scope="page"></c:set>

                                    <c:choose>
                                        <%--Either inserted or updated successfully.--%>
                                        <c:when test="${rowsAffected==1}">
                                            <%--Send an email--%>
                                            <jsp:useBean id="mailer" class="connector.email.Mailer"></jsp:useBean>
                                            <c:catch var="mailEx">
                                                ${mailer.sendPasswordRecoveryMail(param.email, staff.getUsername(),bashGenerated)}
                                            </c:catch>
                                            <c:if test="${mailEx!=null}">
                                                Problem with sending mail! Please report this message to <a href="mailto:report@gaogato.com">report@gaogato.com</a>! Thank you!
                                                <%--Print stack trace--%>
                                                <c:forEach var="line" items="${mailEx.stackTrace}">
                                                    -><c:out value="${line}"></c:out>
                                                </c:forEach>
                                            </c:if>
                                            <c:if test="${empty mailEx}">
                                                <h1 class="green">Verification Email sent successfully!</h1>
                                            </c:if>       
                                        </c:when>
                                        <c:otherwise>
                                            RowsAffected = ${rowsAffected}; Sad. Error. Please report to report@gaogato.com
                                        </c:otherwise>
                                    </c:choose>

                                </c:otherwise>
                            </c:choose>

                        </c:when>




                        <c:otherwise>
                            <%--Ending cluse of action.--%>
                            Error: Unknown Action type. Action = ${action}
                        </c:otherwise>
                    </c:choose>


                </c:when>
                <c:otherwise>
                    <%--Code for submitting a bash--%>
                    <%--try to delete the user from bash table.--%>
                    <c:catch var="sqle">

                        <%--See if the bash is a valid bash--%>
                        <c:choose>
                            <c:when test="${empty param.action}">
                                <c:set var="staffId" scope="page" value="${staffManager.selectIdFromRecoveryBash(bash)}"></c:set>
                                <c:if test="${staffId>0}">

                                    <%--Determine if the deletion is successful--%>
                                    <!--Load password fill-in form-->
                                    Please enter your new password here:
                                    <form action="PasswordRecovery" class="form-inline" method="POST">  
                                        <input class="form-control" type="password" placeHolder="Type Your New Password" name="password" required size="35">
                                        <span class="hide">Notice: Please don't touch hidden fields! We don't want to blacklist your ip!</span> 
                                        <input type="hidden" value="${bash}" name="bash">
                                        <input type="hidden" value="confirm-update" name="action">
                                        <button type="submit" class="btn btn-default">Submit!</button>
                                    </form>

                                </c:if>
                                <%--In case there's no staff associated with it.--%>
                                <c:if test="${staffId<=0}">
                                    <h1><strong class="red">This bash is invalid, it might have been terminated or expired.</strong></h1>
                                </c:if>
                            </c:when>
                            <c:when test="${param.action.equals('confirm-update')}">
                                <%--determine Validity of the bash, and select the user's id associated with it--%>
                                <c:set var="id" value="${staffManager.selectIdFromRecoveryBash(bash)}"></c:set>
                                <c:if test="${id>0}">
                                    <c:set var="nothing" value="${staffManager.deleteFromRecoverBash(bash)}"></c:set>
                                    <c:if test="${param.password != null}">
                                        <c:set var="rowsAffected" value="${staffManager.updatePasswordById(id,param.password)}"/>
                                        <%--Valid Update--%>
                                        <c:choose>
                                            <c:when test="${rowsAffected==1}">
                                                <h1 class='green'>Update Successfully! You may try to login now!</h1>
                                                <form action='index' method='GET'>
                                                    <button class='whiteBg arrowbutton blue'><span>Log In</span></button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <h1>
                                                    Maybe successful! 
                                                </h1>
                                                <h4>
                                                    You may try to use your new password to <a href="index">login</a>. If you have any problem,
                                                    please contact support@gaogato.com! Thank you!
                                                </h4>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    <%--User entered empty password--%>
                                    <c:if test="${empty param.password}">
                                        <strong>You did not enter any password!</strong>
                                        <form action="PasswordRecovery" class="form-inline" method="POST">  
                                            <input type="password" placeHolder="Type Your New Password" name="password" required="">
                                            <span class="hide">Notice: Please don't touch hidden fields! We don't want to blacklist your ip!</span> 
                                            <input type="hidden" value="${bash}" name="bash">
                                            <input type="hidden" value="confirm-update" name="action">
                                            <button type="submit" class="btn btn-default">Submit!</button>
                                        </form>
                                    </c:if>

                                </c:if>
                                <%--Else--%>
                                <c:if test="${id<=0}">
                                    <strong class='red'>The bash does not exists, it might has been terminated </strong>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <strong class='red'> Error: Unknown Action. Please contact support@gaogato.com if this continues.</strong>
                            </c:otherwise>
                        </c:choose>
                    </c:catch>
                    <c:if test="${sqle!=null}">
                        <h1 class='red'>Exception happened!</h1>
                        
                        <h4>Please report this page with full stack trace to report@gaogato.com</h4>
                        Full stack trace:
                        ${sqle.message}
                        <c:forEach var="line" items="${sqle.stackTrace}">
                            -><c:out value="${line}"></c:out>
                        </c:forEach>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>
