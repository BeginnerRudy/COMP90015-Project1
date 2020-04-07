package DictionaryServer;

import DictionaryServer.Services.Service;
import DictionaryServer.Services.ServiceFactory;
import DictionaryServer.ThreadPool.ThreadPool;

import java.io.*;
import java.net.*;

/**
 * This class is responsible for all the functionalities that a multi-threading
 * dictionary sever should have.
 */
public class DictionaryServer {
    int port;
    String dictionaryFilePath;
    ServerSocket serverSocket;
    ThreadPool threadPool;
    ServiceFactory serviceFactory;

    public static final int MAX_T = 10;

    public DictionaryServer(int port, String dictionaryFilePath) {
        this.port = port;
        this.threadPool = new ThreadPool(this.MAX_T);
        this.serviceFactory = ServiceFactory.getServiceFactory();

        // Initialize the dictionary by read file from disk
        this.dictionaryFilePath = dictionaryFilePath;
        Dictionary.getDictionary().init(this.dictionaryFilePath);
    }

    /**
     * This class aims to shut the sever, that is close all the I/O stuffs safely
     * and save the dictionary onto the disk.
     */
    public void shutDown() {
        // Save the dictionary to disk before shutting down
        Dictionary.getDictionary().saveToDisk(this.dictionaryFilePath);

        // Interrupt all threads
        this.threadPool.stop();

        try {
            // Close the server socket -> TODO Tell currently connected user, the server is down.
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printServerMsg("Server closing ...");
    }


    /**
     * This method would turn on the server, that is the server start to listen
     * and response request from clients.
     */
    public void execute() {
        try {
            serverSocket = new ServerSocket(this.port);
            while (true) {
                Socket socket = serverSocket.accept();
                Service service = serviceFactory.getService(socket);
                threadPool.execute(service);
            }
        } catch (SocketException e) {
            printServerMsg("Server socket is closed, the server is closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param args The commandline args
     *
     * This is the main method which is the execution flow of the dictionary server.
     */
    public static void main(String[] args) {
        // parse the command line
        if (args.length != 3) {
            printServerMsg("The command line input is incorrect, usage is shown below.");
            printServerMsg("Usage: java <class> <port> <dictionary filepath>");
            printServerMsg("Server fail to start.");
            System.exit(1);
        }

        // get the args
        int port = Integer.parseInt(args[1]);
        String dictionaryFilePath = args[2];
        System.out.println("The port is" + port);

        // create a server
        DictionaryServer server = new DictionaryServer(port, dictionaryFilePath);
        ServerController serverController = new ServerController(server);
        ServerGUI serverGUI = new ServerGUI("Dictionary Server", serverController);
        serverController.serverGUI(serverGUI);
        serverGUI.setVisible(true);

        // execute the server
        server.execute();
    }

    /**
     * @param msg The message to be print out
     * This method aims to provide a consistent format of server message, every class
     * which wants to send output should use this method.
     */
    public static void printServerMsg(String msg) {
        String prompt = ">>> ";
        System.out.println(prompt + msg);
    }
}
