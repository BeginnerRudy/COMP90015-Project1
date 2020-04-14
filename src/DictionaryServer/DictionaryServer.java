/*
 * @Author: Renjie Meng
 * @Student ID: 877396
 * */

package DictionaryServer;

import DictionaryServer.ThreadPool.ThreadPool;

import java.io.*;
import java.net.*;

import org.apache.commons.cli.*;

/**
 * This class is responsible for all the functionalities that a multi-threading
 * dictionary sever should have.
 */
public class DictionaryServer {
    private static DictionaryServer server = new DictionaryServer();

    private int port;
    private String dictionaryFilePath;
    private ServerSocket serverSocket;

    private ThreadPool threadPool;

    private int maxPoolSize;

    private int inactiveTimeout;

    public static DictionaryServer getServer() {
        return DictionaryServer.server;
    }

    public void init(int port, String dictionaryFilePath, int maxPoolSize, int inactiveTimeout) {
        this.port = port;
        this.maxPoolSize = maxPoolSize;
        this.threadPool = new ThreadPool(this.maxPoolSize);
        this.inactiveTimeout = inactiveTimeout * 1000;


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
        this.threadPool.stopAll();

        try {
            // Close the server socket
            this.serverSocket.close();
        } catch (IOException e) {
//            e.printStackTrace();
            Utility.printServerMsg("Server", "Server socket already closed");
        }
        Utility.printServerMsg("Server", "Server closing ...");
    }

    public ThreadPool getThreadPool() {
        return threadPool;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getInactiveTimeout() {
        return inactiveTimeout;
    }


    /**
     * This method would turn on the server, that is the server start to listen
     * and response request from clients.
     */
    public void execute() {
        ServerController.getServerController().addThreadsOnGUI(this.threadPool.getThreadIds());
        try {
            serverSocket = new ServerSocket(this.port);
            while (true) {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket);
                threadPool.execute(connection);
            }
        } catch (SocketException e) {
            Utility.printServerMsg("Server", "Server socket is closed, the server is closed.");
        } catch (IOException e) {
            Utility.printServerMsg("Server", "Server socket is closed unexpectedly.");
//            e.printStackTrace();
        }
    }

    private static void addOption(Options options, String opt, String longOpt, Boolean hasArg, String description) {
        Option input = new Option(opt, longOpt, hasArg, description);
        input.setRequired(true);
        options.addOption(input);
    }

    /**
     * @param args The commandline args
     *             <p>
     *             This is the main method which is the execution flow of the dictionary server.
     */
    public static void main(String[] args) {
        Options options = new Options();
        addOption(options, "f", "file-path", true, "Dictionary filepath");
        addOption(options, "p", "port", true, "Server port");
        addOption(options, "t", "inactive", true, "inactive timeout in seconds");
        addOption(options, "s", "size", true, "max pool size");


        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);

            String filePath = cmd.getOptionValue("file-path");
            int port = Integer.parseInt(cmd.getOptionValue("port"));
            int inactiveTimeout = Integer.parseInt(cmd.getOptionValue("inactive"));
            int maxPoolSize = Integer.parseInt(cmd.getOptionValue("size"));

            // create a server
            ServerGUI serverGUI = new ServerGUI("Dictionary Server");
            serverGUI.setVisible(true);
            ServerController.getServerController().init(serverGUI);
            DictionaryServer.getServer().init(port, filePath, maxPoolSize, inactiveTimeout);

            // execute the server
            server.execute();

        } catch (ParseException e) {
            Utility.printServerMsg("Server", "Server fail to start.");
            Utility.printServerMsg("Server", e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

    }

}
