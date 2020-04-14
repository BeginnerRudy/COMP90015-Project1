package DictionaryClient;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientConnection {
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private ReadingThread readingThread;
    private String address;
    private int port;


    public ClientConnection(String address, int port) {
        this.address = address;
        this.port = port;
        try {

            connectToServer();
        }catch (IOException e){
            IOExceptionHandler(e);
        }
    }


    /**
     * @throws IOException The IO exception.
     *                     <p>
     *                     This method aims to connect to a server by the address and port.
     */
    private void connectToServer() throws IOException {
        this.socket = new Socket(address, port);
        this.writer = new ObjectOutputStream(this.socket.getOutputStream());
        this.reader = new ObjectInputStream(this.socket.getInputStream());
        this.readingThread = new ReadingThread(this.reader);
        this.readingThread.start();
        ClientController.getClientController().setGUIConnectivity("Connected ");
        Utility.printClientMsg("Connection", "connect to the server");
    }

    /**
     * This method would send the client request to the server, if the connection is lost, it would try to reconnect
     * the server only once.
     *
     * @param request The client request
     * @throws IOException
     */
    private void reconnectToWrite(JSONObject request) throws IOException {
        // send request to the server, reconnect if necessary
        try {
            this.writer.writeObject(request);
        } catch (IOException e) {
            Utility.printClientMsg("Connection", "reconnect to server ...");
            ClientController.getClientController().setGUIConnectivity("Reconnecting ... ");
            this.connectToServer();
            this.writer.writeObject(request);
        } catch (NullPointerException e) {
            // when the client failed to connect for the first time, then writer would be null
            Utility.printClientMsg("Connection", "reconnect to server ...");
            ClientController.getClientController().setGUIConnectivity("Reconnecting ... ");
            this.connectToServer();
            this.writer.writeObject(request);
        }
    }


    /**
     * This method aims to close all the IO stuff's safely.
     */
    public void disconnect() {
        try {
            if (this.readingThread.isConnected()) {

                this.reader.close();
                this.writer.close();
                this.socket.close();
                Utility.printClientMsg("Connection", "The connection is closed now.");
            } else {
                Utility.printClientMsg("Connection", "Nothing to close, the client and server are disconnect.");
                ClientController.getClientController().setGUIConnectivity("NOTHING TO DISCONNECT.");
            }

        } catch (IOException e) {
//            e.printStackTrace();
            Utility.printClientMsg("Connection", "NOTHING TO DISCONNECT.");
            System.out.println("Teardown error");
        } catch (NullPointerException e) {
            Utility.printClientMsg("Connection", "NOTHING TO DISCONNECT.");
            ClientController.getClientController().setGUIConnectivity("NOTHING TO DISCONNECT.");
        }
    }

    /**
     * This method would send the request to the server.
     *
     * @param request The client request.
     */
    public void sendRequest(JSONObject request) {
        try {
            // send request to the server, reconnect if necessary
            reconnectToWrite(request);
        } catch (IOException e) {
            IOExceptionHandler(e);
        }
    }

    /**
     * This method is responsible for handling the IOException.
     *
     * @param e The IOException object
     */
    private void IOExceptionHandler(IOException e) {
        if (e instanceof UnknownHostException) {
            Utility.printClientMsg("Connection", "Failed to connect to the server: " + e.getMessage() + " is unknown.");
            ClientController.getClientController().setGUIConnectivity("Server addr is unknown.");
        } else if (e instanceof ConnectException) {
            Utility.printClientMsg("Connection", "Failed to connect to the server: " + e.getMessage());
            ClientController.getClientController().setGUIConnectivity("Server is unavailable now, try later.");
        } else {
            Utility.printClientMsg("Connection", "Client side IO exception");
            ClientController.getClientController().setGUIConnectivity("Client side IO exception");
        }
    }
}
