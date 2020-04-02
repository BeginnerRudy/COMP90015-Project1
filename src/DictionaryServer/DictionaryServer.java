package DictionaryServer;

import DictionaryServer.Services.Service;
import com.sun.source.tree.WhileLoopTree;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DictionaryServer {
    int port;
    String dictionaryFilePath;
    ServerSocket serverSocket;
    public static final int MAX_T = 1;

    public DictionaryServer(int port, String dictionaryFilePath) {
        this.port = port;
        // Initialize the dictionary by read file from disk
        this.dictionaryFilePath = dictionaryFilePath;
        Dictionary.getDictionary().init(this.dictionaryFilePath);
    }

    public void shutDown() {
        // Save the dictionary to disk before shutting down
        Dictionary.getDictionary().saveToDisk(this.dictionaryFilePath);

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
            ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
            ServiceFactory serviceFactory = ServiceFactory.getServiceFactory();

            while (true) {
                Socket socket = serverSocket.accept();
                Service service = serviceFactory.getService(socket);
                pool.execute(service);
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
