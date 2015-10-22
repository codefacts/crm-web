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