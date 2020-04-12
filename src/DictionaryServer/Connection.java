package DictionaryServer;

import DictionaryServer.Services.InactiveServiceException;
import DictionaryServer.Services.Service;
import DictionaryServer.Services.ServiceFactory;
import DictionaryServer.Services.ServiceNotFoundException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class represents a tcp connection between client and server.
 * This class is fully responsible for all connections stuffs with clients.
 */
public class Connection implements Runnable {
    private Socket socket;
    private ServiceFactory serviceFactory;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    private long id;


    public Connection(Socket socket, long id) throws IOException {
        this.socket = socket;
        this.serviceFactory = ServiceFactory.getServiceFactory();
        this.writer = new ObjectOutputStream(this.socket.getOutputStream());
        this.reader = new ObjectInputStream(this.socket.getInputStream());
        this.id = id;
    }


    @Override
    public void run() {
        try {
            // TODO set time out marcos
            socket.setSoTimeout(5000);
            while (true) {
                Service service = serviceFactory.getService(this.socket, writer, reader);
                service.run();
            }
        } catch (ServiceNotFoundException e) {
            e.printStackTrace();
        } catch (InactiveServiceException e) {
            try {
                writer.close();
                reader.close();
                socket.close();
                System.out.println("Socket closed due to time out");
            }catch (IOException ee){
                ee.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection class IO exception in run method");
        }

    }

    public long getId() {
        return id;
    }

}
