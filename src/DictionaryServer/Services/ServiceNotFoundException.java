package DictionaryServer.Services;

public class ServiceNotFoundException extends Exception {
    public ServiceNotFoundException(String message) {
        super(message);
    }
}
