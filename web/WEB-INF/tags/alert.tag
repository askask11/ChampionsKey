<%-- 
    Document   : alert
    Created on : Apr 21, 2020, 12:21:19 PM
    Author     : jianqing
    Description: This is the customized tag for bootstrap alerts
--%>

<%@tag description="This is the customized tag for bootstrap alerts" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="title" description="The title of the message"%>
<%@attribute name="content" fragment="true" description="Body part of the alert" %>
<%@attribute name="level" required="true" description="The level of this alert. Accepted value:
             success, info, warning, danger" %>
<%-- any content can be specified here e.g.: --%>
<div class="alert alert-${level}">
    <div style="float:right; cursor:pointer;" onclick="this.parentElement.classList.toggle('hide');
         ">X</div>
    <strong> ${title} </strong> &nbsp;${content} </div>