package DictionaryServer.Services;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * This class aims to be responsible for creating server side services depends on the
 * received method code as defined in this class. This class follows a singleton factory
 * pattern.
 */
public class ServiceFactory {
    // separator is used to separate word and meaning.
    public static final String SEPARATOR = "\\$#";

    // The method and response code are discussed in the report.
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


    /**
     * @param socket The messages comes from this socket.
     * @return The correct service, the client ask for.
     */
    public Service getService(Socket socket) {
        String request = null;
        try {
            // read request from the socket
            InputStream inputStream = socket.getInputStream();
            DataInputStream dis = new DataInputStream(inputStream);
            request = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // parse the request into request method and request body
        String requestMethod = request.substring(0, 1);
        String body = request.substring(1);

        // create and return service depends on the method.
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
