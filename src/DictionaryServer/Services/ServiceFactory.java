package DictionaryServer.Services;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServiceFactory {
    public static final String SEPARATOR = "\\$#";
    public static final String ADD_METHOD = "A";
    public static final String DELETE_METHOD = "D";
    public static final String SEARCH_METHOD = "S";
    public static final String SUCCESS_CODE = "1";
    public static final String FAILURE_CODE = "0";

    private static ServiceFactory serviceFactory = new ServiceFactory();

    private ServiceFactory() {

    }

    public static ServiceFactory getServiceFactory() {
        return ServiceFactory.serviceFactory;
    }

    public Service getService(Socket socket) {
        String request = null;
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dis = new DataInputStream(inputStream);
            request = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String requestMethod = request.substring(0, 1);
        String body = request.substring(1);

        switch (requestMethod) {
            case ADD_METHOD:
                return new AddService(socket, body);
            case DELETE_METHOD:
                return new DeleteService(socket, body);
            case SEARCH_METHOD:
                return new SearchService(socket, body);
            default:
                // TODO Here should be a undefined method exception
                return new AddService(socket, body);
        }
    }

}
