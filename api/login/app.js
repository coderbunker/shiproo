/* global fetch, WebSocket, location */
(() => {
  const messages = document.querySelector('#messages');
  const wsButton = document.querySelector('#wsButton');
  const logout = document.querySelector('#logout');
  const login = document.querySelector('#login');

  const showMessage = (message) => {
    messages.textContent += `\n${message}`;
    messages.scrollTop = messages.scrollHeight;
  };

  let ws;

  wsButton.onclick = () => {
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

  };

  const handleResponse = (response) => {
    return response.ok
      ? response.json().then((data) => JSON.stringify(data, null, 2))
      : Promise.reject(new Error('Unexpected response'));
  };

  login.onclick = () => {
      ws.send(JSON.stringify({
        message: 'login', 
        username: 'shuyu', 
        password: 'shuyu',
        affiliation: 'lenovo'
      }))
  };

  logout.onclick = () => {
    fetch('/logout', { method: 'DELETE', credentials: 'same-origin' })
      .then(handleResponse)
      .then(showMessage)
      .catch((err) => showMessage(err.message));
  };
})();
