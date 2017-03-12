package com.coderbunker.hyperledger.communication;


import android.util.Log;

import com.coderbunker.hyperledger.App;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


/**
 *
 * credentials and ownership belongs to http://stackoverflow.com/questions/38162775/really-simple-tcp-client
 * */
public class TcpClient {

    public static final String SERVER_IP = App.SERVER_API;
    public static final int SERVER_PORT = App.SERVER_PORT;
    // message to send to the server
    private String serverMessage;
    // sends message received notifications
    private OnMessageReceived messageListener = null;
    // while this is true, the server will continue running
    private boolean run = false;
    // used to send messages
    private PrintWriter bufferOut;
    // used to read messages from the server
    private BufferedReader bufferIn;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        messageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        if (bufferOut != null && !bufferOut.checkError()) {
            bufferOut.println(message);
            bufferOut.flush();
        }
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        run = false;

        if (bufferOut != null) {
            bufferOut.flush();
            bufferOut.close();
        }

        messageListener = null;
        bufferIn = null;
        bufferOut = null;
        serverMessage = null;
    }

    public void run() {
        run = true;
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            Socket socket = new Socket(serverAddr, SERVER_PORT);
            try {
                bufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                bufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (run) {
                    serverMessage = bufferIn.readLine();
                    if (serverMessage != null && messageListener != null) {
                        messageListener.messageReceived(serverMessage);
                    }
                }
                Log.e(App.TAG, "Received Message: '" + serverMessage + "'");
            } catch (Exception e) {
                Log.e(App.TAG, "S: Error", e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
        } catch (Exception e) {
            Log.e(App.TAG, "C: Error", e);
        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
