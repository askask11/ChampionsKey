<%-- 
    Document   : AboutMe
    Created on : 2020-4-9, 18:01:24
    Author     : Jianqing Gao
    Desctription : This is a page where "redirects" user to Johnson's blog.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class='height100'>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>About Me: Johnson's Blog</title>
    </head>
    <body class='min-height100 height100'>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <%--The main body of your content.--%>
        <iframe src='${pageContext.request.protocol.split('/')[0].equals('HTTP')?"http":"https"}://jianqinggao.com' class='full-width height100' frameborder='0'>
            Please return to pervious page because your browser does not support iframe.
        </iframe>
    </body>
</html>
