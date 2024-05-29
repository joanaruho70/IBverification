<div style="padding: 0px 0px;">
    <table class="company-search-table">
        <tr>
            <td valign="middle" style="width: 60% !important;">
                <input class="form-control" type="text"
                       id="search_company_input_field"
                       placeholder="Enter all or Part of company Name "/>
            </td>
            <td style="width: 10% !important;" valign="middle"> <button class="btn btn-success" id="search_company_button">Submit</button>
            </td>

            <td style="width: 10% !important;"><img id="loader_image_gif" src="<%=request.getContextPath()%>/images/loader.gif"/></td>
        </tr>
    </table>

    <div id="display_company_list_div"></div>
</div>

<script>
    var application_rest_endpoint = "<%=request.getContextPath()%>/dcic/rest/company/";
    var display_company_list_div = $("#display_company_list_div");
    var loader_image_gif = $("#loader_image_gif");
    loader_image_gif.hide();


    $("#search_company_button").click(function (e) {
        e.preventDefault()
        var searchInput = $("#search_company_input_field").val().toLowerCase();

        if(searchInput.length<3){
           displayError("Please Input 3 or More characters to search")
            return;
        }

        var company_url= application_rest_endpoint+encodeURI(searchInput);
        searchCompanyByName(company_url);
    });

    //search for company using company name
    function searchCompanyByName(url) {
        var result_table = "<b><br></b><table id='dataTable' class='table table-sm table-striped table'>" +
            "<thead> " +
            "<tr style='font-weight: bold'><td>NSSF NUMBER</td><td>COMPANY NAME</td><td>TIN NUMBER</td><td>EMAIL</td><td>PHONE</td><td>Action</td></tr>" +
            "</thead><tbody> "

        $.ajax(
            {
                type: "GET",
                url: url,
                timeout: 300000,
                crossDomain: true,
                beforeSend: function () {
                    loader_image_gif.show();
                    display_company_list_div.html("");
                },
                success: function (response) {
                    var parse_response = JSON.parse(response)
                    var status = parse_response.code
                    var message = parse_response.message
                    var returned_data = JSON.parse(parse_response.data)

                    if (status === 0) {

                        $.each(returned_data, function (i, company) {
                            var table_row = "<tr>"
                            var view_employees_link="<%=request.getContextPath()%>"+"/dcic/web/list_of_employees/"+company['nssfNumber']+"/"+company["companyName"];
                            var view_payments="<%=request.getContextPath()%>"+"/dcic/web/view_payments_list/"+company['nssfNumber']+"/"+company["companyName"];

                            table_row += "<td class='clickable_link'><a href='"+view_employees_link+"' >" + company["nssfNumber"] + "</a></td>"
                            table_row += "<td>" + company["companyName"] + "</td>"
                            table_row += "<td>" + company["tinNumber"] + "</td>"
                            table_row += "<td>" + company["email"] + "</td>"
                            table_row += "<td>" + company["phone"] + "</td>"
                            table_row += "<td><a href='"+view_employees_link+"' ><button class='btn btn-sm btn-outline-dark'> <i class='fa fa-users'></i> Employees</button></a><a href='"+view_payments+"' > <button class='btn btn-sm btn-outline-primary'><i class='fa fa-list-alt'></i> Payments</button></a></td>"
                            table_row += "</tr>"

                            result_table += table_row;
                        })

                        result_table += "</tbody></table>"

                        //confirm that some data was returned
                        if (returned_data.length < 1) {
                            result_table += "<h3>Sorry, no information matching your query was returned</h3>"
                        }

                        //display the output
                        //add the html output to the page
                        display_company_list_div.html(result_table)

                        //hide loader
                        loader_image_gif.hide(0);

                        displayDataTable();

                    } else {
                        result_table += "</tbody></table>"
                        result_table += "<p class='alert alert-danger'>"+message+"</p>"

                        //display the output
                        //add the html output to the page
                        display_company_list_div.html(result_table)

                        //hide loader
                        loader_image_gif.hide(0)

                        displayDataTable();
                    }
                }, error: function (error) {
                    var error_text = error.statusText === "timeout" ? "Connection Timeout [ 30 seconds ]. Please check your network" : error.responseText
                    result_table += "</tbody></table>"
                    result_table += "<p class='alert alert-danger'>"+error_text+"</p>"

                    //display the output
                    //add the html output to the page
                    display_company_list_div.html(result_table)

                    //hide loader
                    loader_image_gif.hide(0)

                    displayDataTable();
                }
            });

    }


    function displayDataTable() {
        $('#dataTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                'copyHtml5',
                'excelHtml5',
                'csvHtml5',
                'pdfHtml5'
            ]
        });
    }
</script>
