function ajax(args) {
    $.ajax({
        url: args.url,
        data: args.data,
        method: args.method,
        dataType: 'json',
        cache: false,
        success: args.success,
        error: args.error || function (xhr, status, err) {
            alert(xhr.responseText);
        },
        complete: args.complete
    });
}

function formatDate(date) {
    var monthNames = [
        "Jan", "Feb", "Mar",
        "Apr", "May", "Jun", "Jul",
        "Aug", "Sep", "Oct",
        "Nov", "Dec"
    ];

    var day = date.getDate();
    var monthIndex = date.getMonth();
    var year = date.getFullYear();

    console.log(day, monthNames[monthIndex], year);
    return day + "-" + monthNames[monthIndex] + "-" + year;
}