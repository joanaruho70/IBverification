<%@ page import="com.akanwkasa.dcic.nssf.helpers.CommonOperations" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

    <%
      String title = (String) request.getAttribute("title");
      CommonOperations operations=new CommonOperations();
      operations.checkLogin(request, response);

    %>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Directorate of Citizenship and Immigration Control</title>
    <link href="<%=request.getContextPath()%>/bootstrap-4.3.1-dist/css/bootstrap.css" type="text/css" rel="stylesheet"/>
    <link href="<%=request.getContextPath()%>/css/modv-stylesheet.css" type="text/css" rel="stylesheet"/>
    <link href="<%=request.getContextPath()%>/css/fontawesome/fontawesome.css" type="text/css" rel="stylesheet"/>
    <script src="<%=request.getContextPath()%>/js/jquery3_3_1.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/bootstrap-4.3.1-dist/js/bootstrap.min.js" type="text/javascript"></script>

    <%--data tables for export fucntionality--%>
    <script src="<%=request.getContextPath()%>/datatables/datatables.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/dcic-site.js" type="text/javascript"></script>
    <link href="<%=request.getContextPath()%>/datatables/datatables.min.css" type="text/css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css?family=Lato|Montserrat|Open+Sans|Raleway&display=swap" rel="stylesheet">
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


    .pg-backend-home, .pg-backend-home .row {
        height: 100% !important;
    }

    .pg-backend-home .row .col-sm-2 {
        height: inherit !important;
    }

    .container-fluid, .row, .col-sm-2, .col-sm-10 {
        padding: 0px !important;
        margin: 0px !important;
    }

    .side-menu{
        height: inherit !important;
        background: #4b4a4a33;
        padding:10px 15px;
    }

    .main-content-div{
        height: inherit;
        padding:15px !important;
        overflow-y: scroll !important;
    }

    .clickable_link:hover{
        background:#17a2b8; color: #fff;
    }

</style>

<body>

<div class="container-fluid pg-backend-home">
    <div class="row">
        <div class="col-sm-2">
            <div class="side-menu">
                <%--side menu display --%>
                <div style="text-align: left">
                    <img src="<%=request.getContextPath()%>/images/logo.png" style="height: 35px;"/> <br><br>
                    <hr>
                </div>

                <div>
                    <%
                        HttpSession httpSession = request.getSession();
                        String name= (String) httpSession.getAttribute("name");
                        String email= (String) httpSession.getAttribute("email");
                    %>

                    <b>Name : <%= name %></b><br>
                    <b>Email: <%= email %></b>

                    <hr>
                </div>

                <div>
                    <ul>
                        <a href="<%=request.getContextPath()%>/dcic/web/companies">    <li><i class="fa fa-external-link" aria-hidden="true"></i> Home</li></a>
                        <a href="<%=request.getContextPath()%>/dcic/web/companies"><li><i class="fa fa-building-o" aria-hidden="true"></i>    Companies</li></a>
                        <%--<a href="<%=request.getContextPath()%>/dcic/employees"><li><i class="fa fa-users" aria-hidden="true"></i>         Employees</li></a>--%>
                        <%--<a href="<%=request.getContextPath()%>/dcic/payments"> <li><i class="fa fa-money" aria-hidden="true"></i>         Payments</li></a>--%>
                        <a href="<%=request.getContextPath()%>/dcic/web/logout">   <li><i class="fa fa-power-off" aria-hidden="true"></i>     Logout</li></a>
                    </ul>
                </div>
            </div>
        </div>

        <div class="col-sm-10">
            <div class="main-content-div" style="height: 100% !important; overflow-y: scroll !important;">
                <b><p><%= title %>  </p></b> <hr>

                    <%
                        String page_to_display = (String) request.getAttribute("page");
                        Object page_data = request.getAttribute("data");
                        String error = (String) request.getAttribute("error");

                        if (error != null) {
                    %>
                     <p class="alert alert-danger" id="notification_div"><%= error %>  </p>

                    <% } %>


                <jsp:include page='<%= page_to_display %>'>
                    <jsp:param name="data" value='<%= page_data %>'/>
                </jsp:include>

            </div>
        </div>

    </div>
</div>

</body>


