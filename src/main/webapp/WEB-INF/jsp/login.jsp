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
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12" style="padding: 10% 35%;">
            <div style="text-align: center;">
                <img src="<%=request.getContextPath()%>/images/logo.png" style="height: 60px;"/>
                <hr>
                <br><br>
                <form class="form-signin" action="<%= request.getContextPath()%>/dcic/web/user_login/verify"
                      method="post" enctype="application/x-www-form-urlencoded" autocomplete="off">
                    <%--<h4 class="form-signin-heading">Please sign in</h4>--%>
                    <p>
                        <label for="username" class="sr-only">Username</label>
                        <input type="text" id="username" name="username" class="form-control" placeholder="Username"
                               required autofocus>
                    </p>
                    <p>
                        <label for="password" class="sr-only">Password</label>
                        <input type="password" id="password" name="password" class="form-control" placeholder="Password"
                               required>
                    </p>
                    <input name="_csrf" type="hidden" value="c40586c1-94c7-426a-9b6a-fc037155f4dc"/>

                        <br>
                    <button class="btn btn-lg btn-dark btn-block" type="submit">Sign in</button>

                        <br>
                    <%
                        String error = (String) request.getAttribute("error");
                        if (error != null) {
                    %>
                    <p class="alert alert-danger" id="notification_div"><%= error %>
                    </p>

                    <% } %>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
