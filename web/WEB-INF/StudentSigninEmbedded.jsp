<%-- 
    Document   : StudentSigninEmbedded
    Created on : 2020-3-20, 16:25:08
    Author     : Jianqing Gao
    Desctription : 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ${signinEmbedBodyCss.getAttributeStyle()}>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/maincss.css">
        <title>Sign-in page</title>
    </head>
    <body class="transparent-white">
     
        <h1>
            ${titleIcon} ${titleText}
        </h1>
        <br>

        <h3 class="centralized center-text">
            ${subTitle}
            <!--Please make yourself comfortable and start working!-->
        </h3>
        <br><br>
        
        ${toolText}
        <blockquote ${successQuoteCss.getAttributeStyle()}>
            ${successQuote}
            <footer>
                ${successQuoteFooter}
            </footer>
       </blockquote>

        <h4 ${statusCss.getAttributeStyle()} >${statusText}</h4>


    </body>
</html>
