/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryServer.Services;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * This class aims to be responsible for creating server side services depends on the
 * received method code as defined in this class. This class follows a singleton factory
 * pattern.
 */
public class ServiceFactory {
    // The method and response code are discussed in the report.
    public static final String ADD_METHOD = "A";
    public static final String DELETE_METHOD = "D";
    public static final String SEARCH_METHOD = "S";
    public static final String SUCCESS_ADD = "201";
    public static final String SUCCESS_DELETE = "202";
    public static final String SUCCESS_SEARCH = "203";
    public static final String FAILURE_CODE = "404";
    public static final String REQUEST_HEADER = "request_method";
    public static final String WORD_KEY = "word";
    public static final String MEANING_KEY = "meaning";
    public static final String RESPONSE_CODE_KEY = "response_code";
    public static final String RESPONSE_MESSAGE_KEY = "response_message";

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
    public Service getService(Socket socket) throws ServiceNotFoundException {
        JSONObject request = null;
        ObjectOutputStream writer = null;
        try {
            // read request from the socket
            writer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
            request = (JSONObject) reader.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // parse the request into request method and request body
        String requestMethod = (String) request.get(REQUEST_HEADER);
        request.remove(REQUEST_HEADER);
        JSONObject body = request;

        // create and return service depends on the method.
        switch (requestMethod) {
            case ADD_METHOD:
                return new AddService(socket, body, writer);
            case DELETE_METHOD:
                return new DeleteService(socket, body, writer);
            case SEARCH_METHOD:
                return new SearchService(socket, body, writer);
            default:
                throw new ServiceNotFoundException("The server does not provide " + requestMethod);
        }
    }

}
