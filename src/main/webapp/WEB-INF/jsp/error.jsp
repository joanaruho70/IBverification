<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Directorate of Citizenship and Immigration Control</title>
    <link href="<%=request.getContextPath()%>/bootstrap-4.3.1-dist/css/bootstrap.css" type="text/css" rel="stylesheet"/>
    <link href="<%=request.getContextPath()%>/css/modv-stylesheet.css" type="text/css" rel="stylesheet"/>
    <link href="<%=request.getContextPath()%>/css/fontawesome/fontawesome.css" type="text/css" rel="stylesheet"/>
    <script src="<%=request.getContextPath()%>/js/jquery3_3_1.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/bootstrap-4.3.1-dist/js/bootstrap.min.js"
            type="text/javascript"></script>

    <%--data tables for export fucntionality--%>
    <script src="<%=request.getContextPath()%>/datatables/datatables.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/dcic-site.js" type="text/javascript"></script>
    <link href="<%=request.getContextPath()%>/datatables/datatables.min.css" type="text/css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css?family=Lato|Montserrat|Open+Sans|Raleway&display=swap"
          rel="stylesheet">
</head>

<style>
    body {
        padding: 0px !important;
        margin: 0px !important;
        height: 100% !important;
        min-height: 100% !important;
        max-height: 100% !important;
        font-family: system-ui !important;
        overflow-y: hidden;
    }

    a {
        color: inherit !important;
        text-decoration: none !important;
    }

    li {
        list-style: none !important;
    }

    ul {
        margin: 0px !important;
        padding: 0px !important;
    }


    .side-menu a li {
        padding: 13px 10px 13px 0px !important;
    }


    .pg-backend-home .row .col-sm-2 {
        height: inherit !important;
    }

    .container-fluid, .row, .col-sm-2, .col-sm-10 {
        padding: 0px !important;
        margin: 0px !important;
    }


</style>

<body>
<%
    String code = (String) request.getAttribute("code");
    String message = (String) request.getAttribute("message");

%>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12">
            <div style="text-align: center; padding:4% 10%;">
                <img src="<%=request.getContextPath()%>/images/logo.png" style="height: 60px;"/>
                <hr>
                <br>
                <h3 class="alert alert-danger"><%= code %> </h3> <br>
                <h5><%= message %> </h5>
                <br>

            </div>
        </div>

        <div class="col-sm-12">
            <div style="padding:0% 10%;">
                <p><b>Most Notable causes of errors of this kind</b></p>
                <ul>
                    <li>Editing information directly from the browser input field, please click on the given hyperlinks</li>
                    <li>Network Related issues can also result into timeouts</li>
                    <li>Connecting to the application via a Proxy</li>
                </ul>
                <br><br>
                <hr>
                <a href="<%=request.getContextPath()%>/dcic/companies">
                    <button class="btn btn-dark">Go Back To Home Page</button>
                </a>
            </div>
        </div>
    </div>
</div>


</body>
</html>
