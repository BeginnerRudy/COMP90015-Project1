package DictionaryServer;

import DictionaryServer.Services.Service;
import DictionaryServer.Services.ServiceFactory;
import DictionaryServer.Services.ServiceNotFoundException;
import org.json.simple.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 * This class represents a tcp connection between client and server.
 * This class is fully responsible for all connections stuffs with clients.
 */
public class Connection implements Runnable {
    private Socket socket;

    private ServiceFactory serviceFactory;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;


    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.serviceFactory = ServiceFactory.getServiceFactory();
        this.writer = new ObjectOutputStream(this.socket.getOutputStream());
        this.reader = new ObjectInputStream(this.socket.getInputStream());
        JSONObject rep = new JSONObject();
        rep.put(ServiceFactory.RESPONSE_CODE_KEY, "Connected and Queued");
        this.writer.writeObject(rep);
    }


    @Override
    public void run() {
        try {
            socket.setSoTimeout(DictionaryServer.getServer().getInactiveTimeout());
            JSONObject rep = new JSONObject();
            rep.put(ServiceFactory.RESPONSE_CODE_KEY, "Connected and serving.");
            this.writer.writeObject(rep);
            while (true) {
                Service service = serviceFactory.getService(this.socket, writer, reader);
                service.run();
            }
        } catch (Exception e){
            handleExceptions(e);
        }

    }

    private void handleExceptions(Exception e){
        if (e instanceof SocketTimeoutException) {
//            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
//                ex.printStackTrace();
                Utility.printServerMsg("Connection", "IO exception (inactive timeout).");
            }
            Utility.printServerMsg("Connection", "Close connection due to inactive timeout.");
        } else if (e instanceof EOFException) {
            Utility.printServerMsg("Connection", "The connection is closed by client.");
        } else if (e instanceof SocketException) {
            Utility.printServerMsg("Connection", "The connection is closed by server.");
        } else if (e instanceof IOException) {
//            e.printStackTrace();
            Utility.printServerMsg("Connection", "IO exception.");
        } else if (e instanceof ClassNotFoundException) {
//            e.printStackTrace();
            Utility.printServerMsg("Casting error", "Fail to cast to JSONObject.");
        } else if (e instanceof ServiceNotFoundException) {
//            e.printStackTrace();
            Utility.printServerMsg("Service error", "Service not found.");
        }
    }

    /**
     * @return The Ip address of current socket connection.
     */
    public SocketAddress getIP() {
        return this.socket.getRemoteSocketAddress();
    }

    /**
     * @return The socket.
     */
    public Socket getSocket() {
        return socket;
    }

}
