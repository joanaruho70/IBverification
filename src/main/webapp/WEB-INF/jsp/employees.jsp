<div id="display_company_list_div" ></div>

<script>
    //display error function
    function displayError(message) {
        notification_div.html("")
        notification_div.html(message)
        notification_div.addClass("alert alert-danger")
        notification_div.show();
        setInterval(function (e) {
            notification_div.hide();
            notification_div.html("")
        }, 15000)
    }


    function convertNumberToWords(number) {
        if (number === 1) return "January";
        if (number === 2) return "February";
        if (number === 3) return "March";
        if (number === 4) return "April";
        if (number === 5) return "May";
        if (number === 6) return "June";
        if (number === 7) return "July";
        if (number === 8) return "August";
        if (number === 9) return "September";
        if (number === 10) return "October";
        if (number === 11) return "November";
        if (number === 12) return "December";
    }

    $(document).ready(function() {
        $('#dataTable').DataTable( {
            dom: 'Bfrtip',
            buttons: [
                'copyHtml5',
                'excelHtml5',
                'csvHtml5',
                'pdfHtml5'
            ]
        } );
    } );

</script>

<script>
    var display_company_list_div = $("#display_company_list_div");
    var data=<%=request.getAttribute("data") %>
    console.log(data)
    var loader_image_gif = $("#loader_image_gif");


    displayEmployeesUnderCompany(data);

    function displayEmployeesUnderCompany(returned_data) {

        var result_table = "<i><p>"+data.length+" Records Returned</p></i>" +
            "<table id='dataTable' class='table table-sm table-striped table'>" +
            "<thead>" +
            "<tr style='font-weight: bold'><td>NSSF #</td><td>Name</td><td>Gender</td><td>Nationality</td><td>Job Desc</td><td>Brith Date</td><td>Passport Number</td></tr>" +
            "</thead><tbody>"


        $.each(returned_data, function (i, employee) {

            var view_foreighner_details="<%=request.getContextPath()%>"+"/dcic/web/employee_details/"+employee['nssfNumber'];

            var table_row = "<tr>"
            table_row += "<td class='clickable_link'> <a href='"+view_foreighner_details+"' alt='View Details for "+employee["name"]+"'>" + employee["nssfNumber"] +   "</a></td>"
            table_row += "<td>" + employee["otherName"] +" "+ employee["surname"]+ "</td>"
            table_row += "<td>" + employee["sex"] + "</td>"
            table_row += "<td>" + employee["nationality"] + "</td>"
            table_row += "<td>" + employee["jobDescription"] + "</td>"
            table_row += "<td>" + employee["dateOfBirth"] + "</td>"
            table_row += "<td>" + employee["passportNumber"] + "</td>"
            table_row += "</tr>"

            result_table += table_row;
        })

        result_table += "</body></table>"

        if (returned_data.length < 1) {
            result_table += "<h3>Sorry, no information matching your query was returned</h3>"
        }

        //add the html output to the page
        display_company_list_div.html(result_table)

        loader_image_gif.hide(0);
    }

</script>
