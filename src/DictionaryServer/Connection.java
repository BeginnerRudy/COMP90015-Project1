package DictionaryServer;

import DictionaryServer.Services.InactiveServiceException;
import DictionaryServer.Services.Service;
import DictionaryServer.Services.ServiceFactory;
import DictionaryServer.Services.ServiceNotFoundException;
import org.json.simple.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

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
            // TODO set time out marcos
            socket.setSoTimeout(DictionaryServer.getServer().getInactiveTimeout());
            JSONObject rep = new JSONObject();
            rep.put(ServiceFactory.RESPONSE_CODE_KEY, "Connected and serving.");
            this.writer.writeObject(rep);
            while (true) {
                Service service = serviceFactory.getService(this.socket, writer, reader);
                service.run();
            }
        } catch (SocketTimeoutException e) {
//            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
//                ex.printStackTrace();
                Utility.printServerMsg("Connection", "IO exception (inactive timeout).");
            }
            Utility.printServerMsg("Connection", "Close connection due to inactive timeout.");
        } catch (EOFException e) {
            Utility.printServerMsg("Connection", "The connection is closed by client.");
        } catch (SocketException e) {
            Utility.printServerMsg("Connection", "The connection is closed by server.");
        } catch (IOException e) {
//            e.printStackTrace();
            Utility.printServerMsg("Connection", "IO exception.");
        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
            Utility.printServerMsg("Casting error", "Fail to cast to JSONObject.");
        } catch (ServiceNotFoundException e) {
//            e.printStackTrace();
            Utility.printServerMsg("Service error", "Service not found.");
        }

    }

    public InetAddress getIP() {
        return this.socket.getInetAddress();
    }

    public Socket getSocket() {
        return socket;
    }

}
