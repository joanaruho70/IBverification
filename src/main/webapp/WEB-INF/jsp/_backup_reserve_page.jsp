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
    <script src="<%=request.getContextPath()%>/bootstrap-4.3.1-dist/js/bootstrap.min.js" type="text/javascript"></script>
    <link href="https://fonts.googleapis.com/css?family=Lato|Montserrat|Open+Sans|Raleway&display=swap" rel="stylesheet">
</head>

<style>
    body {
        padding: 20px;
        height: 100% !important;
        min-height: 100% !important;
        max-height: 100% !important;
        font-family: Calibri !important;
        overflow-y: hidden;
    }

    .company-search-table tr td {
        padding-right: 20px;
    }

    .company-search-table tr td:nth-child(2) input {
        width: 100% !important;
    }

    .display_company_list_div {
        min-height: 500px !important;
        max-height: 500px !important;
        overflow-y: scroll;
    }
</style>

<body>

<p id="notification_div"
   style="position: absolute; padding: 5px 5px 5px 30px; background:red; color: white; right: 0; max-width:50%; top: 0px; "></p>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12" style="min-height: 100% !important;">
            <div style="text-align: center">
                <img src="<%=request.getContextPath()%>/images/logo.png" style="height: 70px;"/> <br><br>
                <hr>
            </div>

            <div style="padding: 0px 0px;">
                <table class="company-search-table">
                    <tr>
                        <td valign="middle"><b>Search For Company | Employee</b></td>
                        <td valign="middle" style="width: 25%">
                            <select name="searchType" id="company_or_employee_option" class="form-control">
                                <option value="">Please Select Search Type</option>
                                <option value="company">Company</option>
                                <option value="employee">Employee</option>
                            </select>
                        </td>
                        <td valign="middle" style="width: 50%"><input class="form-control" type="text"
                                                                      id="search_company_input_field"
                                                                      placeholder="Enter  company Name | Employee Nssf Number"/>
                        </td>
                        <td valign="middle">
                            <button class="btn btn-success" id="search_company_button">Submit</button>
                        </td>
                        <td valign="middle"><img class="loader_image_gif"
                                                 src="<%=request.getContextPath()%>/images/loader.gif"/></td>
                    </tr>
                </table>
            </div>
            <hr>

            <div class="display_company_list_div" id="display_company_list_div">

            </div>

        </div>
    </div>
</div>

</body>

<script>
    var application_endpoint="http://192.168.4.10:8888/interface/dcic/rest/";

    var current_company_name=" "
    //companies
    var display_company_list_div = $("#display_company_list_div");
    var loader_image_gif = $(".loader_image_gif")

    //hide the notification bar
    var notification_div = $("#notification_div")
    notification_div.hide();

    $(document).ready(function () {
        loader_image_gif.hide();
        notification_div.hide();
    });


    $('#search_company_button').click(function () {
        var searchInput = $("#search_company_input_field").val().toLowerCase();

        //confirm what search type is needed
        var company_or_employee_option = $("#company_or_employee_option").val()
        if (company_or_employee_option === "") {
            displayError("Please Choose search type")
            return;
        }

        if (searchInput.length >= 3) {

            //display the loader
            loader_image_gif.show();

            if (company_or_employee_option === "company") {
                var company_url = "company/" + encodeURI(searchInput);
                var url = application_endpoint + company_url;
                searchCompanyByName(url);


            } else if (company_or_employee_option === "employee") {
                var get_employee_details_list = "foreighner/" + encodeURI(searchInput);
                var url = application_endpoint + get_employee_details_list;
                searchEmployeeByNssfNumber(url);
            }
        }//closes the length if

    });


    //this is used hen some one clicks the vie more button on compawny list
    $(document).on('click','.view_company_employees',function (e) {
        e.preventDefault();
        var nssf_number = $(this).attr("nssf_number")
        current_company_name = $(this).attr("company_name")
        $("#display_company_name_span").text(current_company_name)
        var display_employees_under_company = "list_of_foreighners/" + encodeURI(nssf_number);
        var url = application_endpoint + display_employees_under_company;
        listEmployeesUnderCompanyByNssfNumber(url);
    });


    //search for company using company name
    function searchCompanyByName(url) {
        var result_table = "<b><p>List of Companies</p></b><table class='table table-sm table-striped table'><tr style='font-weight: bold'><td>NSSF NUMBER</td><td>COMPANY NAME</td><td>TIN NUMBER</td><td>EMAIL</td><td>PHONE</td><td>Action</td></tr>"

        $.ajax(
            {
                type: "GET",
                url: url,
                timeout: 300000,
                crossDomain: true,
                beforeSend: function () {
                    loader_image_gif.show();
                    display_company_list_div.html("");
                    console.log("url = " + url)
                },
                success: function (response) {
                    console.log("Response =" + response)
                    var parse_response = JSON.parse(response)
                    var status = parse_response.code
                    var message = parse_response.message
                    var returned_data = JSON.parse(parse_response.data)

                    if (status === 0) {

                        $.each(returned_data, function (i, company) {
                            var table_row = "<tr>"
                            table_row += "<td><a href='' class='view_company_employees' nssf_number='" + company["nssfNumber"] + "' company_name='" + company["companyName"]+"' >" + company["nssfNumber"] + "</a></td>"
                            table_row += "<td>" + company["companyName"] + "</td>"
                            table_row += "<td>" + company["tinNumber"] + "</td>"
                            table_row += "<td>" + company["email"] + "</td>"
                            table_row += "<td>" + company["phone"] + "</td>"
                            table_row += "<td><button  class='view_company_employees btn btn-sm btn-outline-dark' nssf_number='" + company["nssfNumber"] + "' company_name='" + company["companyName"] + "'>View More</button ></td>"
                            table_row += "</tr>"

                            result_table += table_row;
                        })

                        result_table += "</table>"

                        //confirm that some data was returned
                        if (returned_data.length < 1) {
                            result_table += "<h3>Sorry, no information matching your query was returned</h3>"
                        }

                        //display the output
                        //add the html output to the page
                        display_company_list_div.html(result_table)

                        //hide loader
                        loader_image_gif.hide(0);

                    } else {
                        displayError(message)
                        loader_image_gif.hide(0)
                    }
                }, error: function (error) {
                    var error_text = error.statusText === "timeout" ? "Connection Timeout [ 30 seconds ]. Please check your network" : error.responseText
                    displayError(error_text);
                    loader_image_gif.hide(0)
                }
            });

    }

    function searchEmployeeByNssfNumber(url) {
        var result_table = "<b><p>Employee Details</p></b><table class='table table-sm table-striped table'><tr style='font-weight: bold'><td>SURNAME</td><td>OTHER NAME</td><td>GENDER</td><td>DATE OF BIRTH</td><td>NATIONALITY</td><td>JOB DESC</td><td>PASSPORT #</td><td>NSSF #</td></tr>"

        $.ajax(
            {
                type: "GET",
                url: url,
                crossDomain: true,
                timeout: 100000,
                beforeSend: function () {
                    loader_image_gif.show();
                    display_company_list_div.html("");
                    console.log("url = " + url)
                },
                success: function (response) {
                    console.log("Response =" + response)
                    var parse_response = JSON.parse(response)
                    var status = parse_response.code
                    var message = parse_response.message
                    var returned_data = JSON.parse(parse_response.data)

                    if (status === 0) {

                        //display some error is no data is returned
                        var table_row = "<tr>"
                        table_row += "<td>" + returned_data["surname"] + "</td>"
                        table_row += "<td>" + returned_data["otherName"] + "</td>"
                        table_row += "<td>" + returned_data["sex"] + "</td>"
                        table_row += "<td>" + returned_data["dateOfBirth"] + "</td>"
                        table_row += "<td>" + returned_data["nationality"] + "</td>"
                        table_row += "<td>" + returned_data["jobDescription"] + "</td>"
                        table_row += "<td>" + returned_data["passportNumber"] + "</td>"
                        table_row += "<td>" + returned_data["nssfNumber"] + "</td>"
                        table_row += "</tr>"
                        result_table += table_row;

                        result_table += "</table>"


                        //add the html output to the page
                        display_company_list_div.html(result_table)

                        loader_image_gif.hide(0);
                    } else {
                        displayError(message)
                        loader_image_gif.hide(0)
                    }
                }, error: function (error) {
                    var error_text = error.statusText === "timeout" ? "Connection Timeout [ 10 seconds ]. Please check your network" : error.responseText
                    displayError(error_text);
                    loader_image_gif.hide(0)
                }
            });

    }


    //get the list of employees under a certain company
    function listEmployeesUnderCompanyByNssfNumber(url) {
        var result_table = "<b><p>Employees Under  <b id='display_company_name_span'>Company</b></p></b>" +
            "<table class='table table-sm table-striped table'><tr style='font-weight: bold'><td>NSSF #</td><td>Employer NSSF #</td><td>Name</td><td>contribution Type</td><td>Taxable Wages</td><td>Schedule Year</td><td>Schedule Month</td><td>Schedule Number</td><td>Receipt Date</td></tr>"

        $.ajax(
            {
                type: "GET",
                url: url,
                timeout: 100000,
                crossDomain: true,
                beforeSend: function () {
                    loader_image_gif.show();
                    display_company_list_div.html("");
                    console.log("url = " + url)
                },
                success: function (response) {
                    console.log("Response =" + response)
                    var parse_response = JSON.parse(response)
                    var status = parse_response.code
                    var message = parse_response.message
                    var returned_data = JSON.parse(parse_response.data)

                    if (status === 0) {
                        $.each(returned_data, function (i, employee) {
                            var table_row = "<tr>"
                            table_row += "<td>" + employee["nssfnumber"] + "</td>"
                            table_row += "<td>" + employee["employerNssfnumber"] + "</td>"
                            table_row += "<td>" + employee["name"] + "</td>"
                            table_row += "<td>" + employee["contributionType"] + "</td>"
                            table_row += "<td>" + employee["taxablewages"] + "</td>"
                            table_row += "<td>" + employee["scheduleYear"] + "</td>"
                            table_row += "<td>" + employee["schedulemonth"] + "</td>"
                            table_row += "<td>" + employee["schedulenumber"] + "</td>"
                            table_row += "<td>" + employee["receiptDate"] + "</td>"
                            table_row += "</tr>"

                            result_table += table_row;
                        })

                        result_table += "</table>"

                        if (returned_data.length < 1) {
                            result_table += "<h3>Sorry, no information matching your query was returned</h3>"
                        }

                        //add the html output to the page
                        display_company_list_div.html(result_table)

                        loader_image_gif.hide(0);
                    } else {
                        displayError(message)
                        loader_image_gif.hide(0)
                    }
                }, error: function (error) {
                    var error_text = error.statusText === "timeout" ? "Connection Timeout [ 10 seconds ]. Please check your network" : error.responseText
                    displayError(error_text);
                    loader_image_gif.hide(0)
                }
            });


    }

    function displayError(message) {
        notification_div.html("")
        notification_div.html(message)
        notification_div.show();
        setInterval(function (e) {
            notification_div.hide();
            notification_div.html("")
        }, 15000)
    }

</script>


