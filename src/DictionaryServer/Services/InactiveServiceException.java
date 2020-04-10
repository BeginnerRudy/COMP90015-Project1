package DictionaryServer.Services;

import org.json.simple.JSONObject;

import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class indicates that the client has not been active for a certain period.
 */
public class InactiveServiceException extends Exception {
    public InactiveServiceException(String message) {
        super(message);
    }
}
