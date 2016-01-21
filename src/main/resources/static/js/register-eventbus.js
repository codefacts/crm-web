var eb = new EventBus('http://localhost:8085/eventbus');

eb.onopen = function onopen() {

    // set a handler to receive a message
    eb.registerHandler('some-address', function (err, message) {
        console.log('received a message: ' + JSON.stringify(message));
    });

    eb.send('some-address', {'message': 'eventbus ready'});
}