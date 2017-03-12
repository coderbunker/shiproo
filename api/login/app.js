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

  let ws;

   $('#wsButton').click(() => {
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
      });

    });

    // Listen for messages

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

  $('#login').click(() => {
      ws.send(JSON.stringify({
        message: 'login', 
        username: 'shuyu', 
        password: 'shuyu',
        affiliation: 'lenovo'
      }))
  });

  $('#loginReceiver').click(() => {
      ws.send(JSON.stringify({
        message: 'login', 
        username: 'ricky', 
        password: 'ricky',
        affiliation: 'google'
      }))
  });

  $('#createParcel').click(() => {
      ws.send(JSON.stringify({
        message: 'createParcel', 
      }))
  });
})();
