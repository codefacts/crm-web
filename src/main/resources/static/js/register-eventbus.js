var eb = new EventBus('http://' + location.host + '/eventbus');

eb.onopen = function onopen() {

    console.info("EVENT_BUS OPENED");

    var event = new Event('EVENT_BUS_CONNECTED');

    document.dispatchEvent(event);
}

eb.onclose = function () {

    console.info("EVENT_BUS CLOSED");

    var event = new Event('EVENT_BUS_DISCONNECTED');

    document.dispatchEvent(event);
}