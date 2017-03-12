/* global fetch, WebSocket, location */
(() => {
  const messages = document.querySelector('#messages');
  // const logout = document.querySelector('#logout');
  const login = $('#login');
  const wsButton = $('#wsButton');

  const showMessage = (message) => {
    messages.textContent += `\n${message}`;
    messages.scrollTop = messages.scrollHeight;
  };

  var parcelId = 'f45cea00-be82-47a3-a458-911280988114'
  var parcel = {
      message: 'createParcel',
      username: 'shuyu',
      shipper: 'lenovo',
      receiver: 'google',
      parcelId: parcelId,
      orderId: "order1",
      pickupAddress: "Xuhui, Shanghai, PRC",
      destinationAddress: "Mountain View, California, USA",
      size: "[61,46,46]",
      weight: 5000,
      manifest: "Lenovo X220i laptop",
      declaredValue: "500",
      currency: "USD"
  }

  let ws;

   $('#wsButton').click((e) => {
      e.preventDefault() // prevents the form from being submitted

     console.log('clicked wsButton')
    if (ws) {
      ws.onerror = ws.onopen = ws.onclose = null;
      ws.close();
    }

    ws = new WebSocket(`ws://${location.host}`);
    ws.addEventListener('open', function (event) {
      ws.addEventListener('message', function (event) {
          console.log('Message from server', event.data);
          messages.textContent += `\n${event.data}`
          const msgObj = JSON.parse(event.data)
          switch(msgObj.message) {
            case 'findRouteReply':
              $('#routes').text(JSON.stringify(event.data))
              break;

            case 'loginReply':
              $('#username').text(`logged in as ${msgObj.token}`)
              $('#parcelId').val(parcel.parcelId)
              $('#shipper').val(parcel.shipper)
              $('#pickupAddress').val(parcel.pickupAddress)
              $('#destinationAddress').val(parcel.destinationAddress)
              $('#size').val(parcel.size)
              $('#weight').val(parcel.weight)
              break;
          }
      });

    });

    ws.onerror = () => showMessage('WebSocket error');
    ws.onopen = () => {
      showMessage('WebSocket connection established');
    }
    ws.onclose = () => showMessage('WebSocket connection closed');

  });

  const handleResponse = (response) => {
    return response.ok
      ? response.json().then((data) => JSON.stringify(data, null, 2))
      : Promise.reject(new Error('Unexpected response'));
  };

  $('#login').click((e) => {
      e.preventDefault() // prevents the form from being submitted
      ws.send(JSON.stringify({
        message: 'login', 
        username: 'shuyu', 
        password: 'shuyu',
        affiliation: 'lenovo'
      }))
  });

  $('#loginReceiver').click((e) => {
      e.preventDefault() // prevents the form from being submitted
      ws.send(JSON.stringify({
        message: 'login', 
        username: 'ricky', 
        password: 'ricky',
        affiliation: 'google'
      }))
  });

  $('#createParcel').click((e) => {
    e.preventDefault() // prevents the form from being submitted

    ws.send(JSON.stringify(parcel))
  });

$('#queryRoute').click((e) => {
    e.preventDefault() // prevents the form from being submitted

    ws.send(JSON.stringify({
      message: 'findRoute',
      parcelId: parcelId,
    }))
})

$('#buyRoute').click((e) => {
    e.preventDefault() // prevents the form from being submitted
    ws.send(JSON.stringify({
      message: 'buyRoute',
      parcelId: parcelId,
      routeId: routeId
    }))
})

})();

