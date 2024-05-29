<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>

<%
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, -1); // to get previous year add -1
    Date previousYear = cal.getTime();
    new SimpleDateFormat("yyyy-MM-dd").format(previousYear);
%>

<p class="alert alert-danger" id="notification_div"></p>

<div style="padding: 0px 0px;">
    <table class="company-search-table">
        <tr>
            <td valign="middle" style="width: 30% !important;">
                <input type="date" class="form-control"
                       id="start_date"  value="<%= new SimpleDateFormat("yyyy-MM-dd").format(previousYear)%>"
                       placeholder="start Date " required/>

            </td>

            <td valign="middle" style="width: 30% !important;">
                <input type="date" class="form-control" required
                       id="end_date" value="<%= new SimpleDateFormat("yyyy-MM-dd").format(new Date())%>"
                       placeholder="End Date "/>
            </td>

            <td style="width: 10% !important;" valign="middle">
                <button class="btn btn-success" id="search_payments_button">Submit</button>
            </td>

            <td style="width: 10% !important;"><img id="loader_image_gif"
                                                    src="<%=request.getContextPath()%>/images/loader.gif"/></td>
        </tr>
    </table>

    <div id="display_company_list_div"></div>
</div>

<script>
    var application_rest_endpoint = "<%=request.getContextPath()%>/dcic/rest/search_payments_by_date_range/<%=request.getAttribute("nssf_number")%>";
    var display_company_list_div = $("#display_company_list_div");
    var loader_image_gif = $("#loader_image_gif");
    var notification_div = $("#notification_div");

    loader_image_gif.hide()
    notification_div.hide()


    $("#search_payments_button").click(function (e) {

        e.preventDefault()
        var start_date = $("#start_date").val().toLowerCase();
        var end_date = $("#end_date").val().toLowerCase();

        if (start_date === null || end_date === null) {
            displayError("Start Date and End Date Must Be Supplied")
            return;
        }

        var post_data = {start_date: start_date, end_date: end_date}

        var result_table = "<b><br></b>" +
            "<table id='dataTable' class='table table-sm table-striped table'><thead>" +
            "<tr style='font-weight: bold'>" +
            "<td>NSSF #</td><td>Employer NSSF #</td><td>Name</td><td>contribution Type</td><td>Taxable Wages</td>" +
            "<td>Schedule Year</td><td>Schedule Month</td><td>Schedule Number</td><td>Receipt Date</td>" +
            "</tr></thead><tbody> "

        $.ajax(
            {
                type: "POST",
                url: application_rest_endpoint,
                data: post_data,
                timeout: 300000,
                crossDomain: true,
                beforeSend: function () {
                    loader_image_gif.show();
                    display_company_list_div.html("");
                    console.log(application_rest_endpoint)
                },
                success: function (response) {
                    console.log(response)
                    var parse_response = JSON.parse(JSON.stringify(response))
                    var status = parse_response.code
                    var message = parse_response.message
                    var returned_data = JSON.parse(parse_response.data)

                    if (status === 0) {

                        $.each(returned_data, function (i, employee) {

                            var view_foreighner_details="<%=request.getContextPath()%>"+"/dcic/web/employee_details/"+employee['nssfnumber'];
                            var table_row = "<tr>"
                            table_row += "<td class='clickable_link'> <a href='"+view_foreighner_details+"' alt='View Details for "+employee["name"]+"'>" + employee["nssfnumber"] +   "</a></td>"
                            table_row += "<td >" + employee["employerNssfnumber"] + "</td>"
                            table_row += "<td>" + employee["name"] + "</td>"
                            table_row += "<td>" + employee["contributionType"] + "</td>"
                            table_row += "<td>" + new Intl.NumberFormat('en-US').format(employee["taxablewages"]) + "</td>"
                            table_row += "<td>" + employee["scheduleYear"] + "</td>"
                            table_row += "<td>" + convertNumberToWords(employee["schedulemonth"]) + "</td>"
                            table_row += "<td>" + employee["schedulenumber"] + "</td>"
                            table_row += "<td>" + employee["receiptDate"] + "</td>"
                            table_row += "</tr>"

                            result_table += table_row;
                        })

                        result_table += "</tbody></table>"

                        if (returned_data.length < 1) {
                            result_table += "<h3>Sorry, no information matching your query was returned</h3>"
                        }

                        //display the output
                        //add the html output to the page
                        display_company_list_div.html(result_table)

                        //display the download buttons
                        displayDownLoadButtons();

                        //hide loader
                        loader_image_gif.hide(0);

                    } else {
                        result_table += "</tbody></table>"
                        result_table += "<p class='alert alert-danger'>"+message+"</p>"

                        //display the output
                        //add the html output to the page
                        display_company_list_div.html(result_table)

                        displayDownLoadButtons();

                        loader_image_gif.hide(0)
                    }
                }, error: function (error) {
                    var error_text = error.statusText === "timeout" ? "Connection Timeout [ 30 seconds ]. Please check your network" : error.responseText
                    displayError(error_text);
                    loader_image_gif.hide(0)
                }
            });

    })

</script>

<script>

   function displayDownLoadButtons(){
        $('#dataTable').DataTable( {
            dom: 'Bfrtip',
            buttons: [
                'copyHtml5',
                'excelHtml5',
                'csvHtml5',
                'pdfHtml5'
            ]
        } )
   }
</script>
