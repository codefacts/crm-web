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
    if (!date) {
        return "";
    }
    var monthNames = [
        "Jan", "Feb", "Mar",
        "Apr", "May", "Jun", "Jul",
        "Aug", "Sep", "Oct",
        "Nov", "Dec"
    ];

    var day = date.getDate();
    var monthIndex = date.getMonth();
    var year = date.getFullYear();

    return day + "-" + monthNames[monthIndex] + "-" + year;
}

function formatDateTime(date) {
    if (!date) {
        return "";
    }
    var monthNames = [
        "Jan", "Feb", "Mar",
        "Apr", "May", "Jun", "Jul",
        "Aug", "Sep", "Oct",
        "Nov", "Dec"
    ];

    var day = date.getDate();
    var monthIndex = date.getMonth();
    var year = date.getFullYear();

    return day + "-" + monthNames[monthIndex] + "-" + year + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
}

function copy(v) {
    if (v === undefined) return v;
    return JSON.parse(JSON.stringify(v))
}

function merge(object1, object2) {
    var obj = {};
    for (var x in object1) {
        obj[x] = object1[x];
    }
    for (var x in object2) {
        obj[x] = obj[x] || object2[x];
    }
    return obj;
}

function merge2(object1, object2) {
    var obj = {};
    for (var x in object1) {
        obj[x] = object1[x];
    }
    for (var x in object2) {
        obj[x] = object2[x];
    }
    return obj;
}

function exclude(obj1, arrayOfKeys) {
    var obj = copy(obj1);
    for (var x in arrayOfKeys) {
        delete obj[arrayOfKeys[x]];
    }
    return obj;
}

function include(obj1, arrayOfKeys) {
    var obj = {};
    for (var x in arrayOfKeys) {
        var key = arrayOfKeys[x];
        obj[key] = obj1[key];
    }
    return obj;
}

function removeEmptyNullWhiteSpaces(srcObj) {
    var obj = {};
    for (var x in srcObj) {
        var val = srcObj[x];
        if (val !== false && (!val || ((typeof val == "string") && !val.trim()))) {
            continue;
        }
        obj[x] = val;
    }
    return obj;
}
