var notification_div = $("#notification_div");
$("#loader_image_gif").hide();

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
