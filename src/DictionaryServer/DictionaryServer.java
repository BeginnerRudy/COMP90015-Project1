package DictionaryServer;

import DictionaryServer.Services.Service;
import DictionaryServer.Services.ServiceFactory;
import DictionaryServer.ThreadPool.ThreadPool;

import java.io.*;
import java.net.*;

public class DictionaryServer {
    int port;
    String dictionaryFilePath;
    ServerSocket serverSocket;
    ThreadPool threadPool;
    ServiceFactory serviceFactory;

    public static final int MAX_T = 1;

    public DictionaryServer(int port, String dictionaryFilePath) {
        this.port = port;
        this.threadPool = new ThreadPool(this.MAX_T);
        this.serviceFactory = ServiceFactory.getServiceFactory();

        // Initialize the dictionary by read file from disk
        this.dictionaryFilePath = dictionaryFilePath;
        Dictionary.getDictionary().init(this.dictionaryFilePath);
    }

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


    public static void main(String[] args) {
        // parse the command line
        if (args.length != 2) {
            printServerMsg("The command line input is incorrect, usage is shown below.");
            printServerMsg("Usage: java <class> <port> <dictionary filepath>");
            printServerMsg("Server fail to start.");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String dictionaryFilePath = args[1];

        // create a server
        DictionaryServer server = new DictionaryServer(port, dictionaryFilePath);
        ServerController serverController = new ServerController(server);
        ServerGUI serverGUI = new ServerGUI("Dictionary Server", serverController);
        serverController.serverGUI(serverGUI);
        serverGUI.setVisible(true);
        // execute the server
        server.execute();
    }

    public static void printServerMsg(String msg) {
        String prompt = ">>> ";
        System.out.println(prompt + msg);
    }
}
