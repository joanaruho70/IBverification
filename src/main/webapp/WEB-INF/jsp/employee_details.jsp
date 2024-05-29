<%@ page import="com.akanwkasa.dcic.nssf.requests_and_response.nssf.EmployeeDetails" %>
<%@ page import="com.akanwkasa.dcic.nssf.requests_and_response.nssf.EmployeePaymentDetails" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="java.util.List" %>


<%
    EmployeeDetails employeeDetails=new ObjectMapper().readValue((String)request.getAttribute("data"),EmployeeDetails.class);
    List<EmployeePaymentDetails> employeePaymentDetails=employeeDetails.getPayments();
%>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3" >
            <div style="background-color: #c9c9c92e;height: 100% !important;font-size: 12px !important; padding:10px !important; margin-right: 5px !important;">
                <table>
                    <tr><td>surname</td>       <td><%=employeeDetails.getSurname()%></td></tr>
                    <tr><td>Other Name</td>     <td><%=employeeDetails.getOtherName()%></td></tr>
                    <tr><td>Sex</td>           <td><%=employeeDetails.getSex()%></td></tr>
                    <tr><td>Date Of Birth</td>   <td><%=employeeDetails.getDateOfBirth()%></td></tr>
                    <tr><td>Nationality</td>   <td><%=employeeDetails.getNationality()%></td></tr>
                    <tr><td>job Description</td><td><%=employeeDetails.getJobDescription()%></td></tr>
                    <tr><td>Passport #</td><td><%=employeeDetails.getPassportNumber()%></td></tr>
                    <tr><td>NSSF #</td>    <td><%=employeeDetails.getNssfNumber()%></td></tr>
                </table>
            </div>
        </div>
        <div class="col-sm-9" style="max-height: 100% !important; overflow: scroll !important;">
            <table id="dataTable" class='table table-sm table-striped table-responsive'>
                <thead>
                <tr style='font-weight: bold'>
                    <td>NSSF #</td><td>Employer NSSF #</td><td>Name</td>
                    <td>contribution Type</td><td>Taxable Wages</td>
                    <td>Schedule Year</td><td>Schedule Month</td><td>Schedule Number</td><td>Receipt Date</td></tr>
                </thead>
                <tbody>
                <%

                for (EmployeePaymentDetails paymentDetails : employeePaymentDetails) {
                %>

                <tr>
                    <td><%= paymentDetails.getNssfnumber()%></td>
                    <td><%= paymentDetails.getEmployerNssfnumber()%></td>
                    <td><%= paymentDetails.getName()%></td>
                    <td><%= paymentDetails.getContributionType()%></td>
                    <td><%= String.format("%,.2f",Double.parseDouble(paymentDetails.getTaxablewages()))%></td>
                    <td><%= paymentDetails.getScheduleYear()%></td>
                    <td><%= paymentDetails.getSchedulemonth()%></td>
                    <td><%= paymentDetails.getSchedulenumber()%></td>
                    <td><%= paymentDetails.getReceiptDate()%></td>
                </tr>

                <% } %>
                </tbody>
            </table>

        </div>
    </div>
</div>

<script>
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
        alert("being called")
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
        else  return number;
    }
</script>
