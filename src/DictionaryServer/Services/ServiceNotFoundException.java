/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryServer.Services;

/**
 * This class is represent that the client provides request methods other than add, delete and search.
 * In this case, the server would warn the user.
 */
public class ServiceNotFoundException extends Exception {
    public ServiceNotFoundException(String message) {
        super(message);
    }
}
