/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * @Date: 2020 April
 * */

package DictionaryServer.Services;

import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is the abstract class of the service that this dictionary server would
 * provide. The purpose of this class is to reduce the duplication of classes.
 */
public abstract class Service implements Runnable {
    Socket socket; // The socket to do I/O
    ObjectOutputStream writer;
    JSONObject body;

    public Service(Socket socket, JSONObject body, ObjectOutputStream writer) {
        this.socket = socket;
        this.writer = writer;
        this.body = body;
    }
}
