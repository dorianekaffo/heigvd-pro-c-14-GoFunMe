package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The server's role is to handle incoming connections from multiple clients. Each
 * connection is taken in charge by a dedicated thread.
 */
public class Server {

    final static Logger LOG = Logger.getLogger(Server.class.getName());
    static final int PORT = 1337;
    private ServerSocket serverSocket;
    private Boolean listening;

    /**
     * Server constructor
     */
    public Server() {
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Starts the listening for incoming connections
     */
    public void start() {
        LOG.info("Listening for new connections.");
        this.listening = true;

        /* The server keeps listening for new connections */
        while (listening) {
            LOG.log(Level.INFO, "Waiting (blocking) for a new client on port {0}", PORT);

            /* Once a connection arrives, the server starts a thread to handle it */
            try {
                Socket clientSocket = serverSocket.accept();
                LOG.info("A new client has arrived. Starting a new thread.");
                new Thread(new ServerThread(clientSocket)).start();
            } catch (IOException e) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    /**
     * This inner class implements the behavior of the server's threads, whose
     * responsibility is to take care of clients once they have connected.
     */
    private class ServerThread implements Runnable {

        Socket socket = null;
        BufferedReader input;
        PrintWriter output;

        /**
         * ServerThread constructor
         * @param socket the client socket
         */
        public ServerThread(Socket socket) {
            this.socket = socket;

            /* Obtain streams from Socket */
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());
            } catch (IOException ioException) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ioException);
            }

        }

        public void run() {
            String line;
            boolean shouldRun = true;

            output.println("Welcome to the Multi-Threaded Server.\nSend me text lines and conclude with the BYE command.");
            output.flush();
            try {
                LOG.info("Reading until client sends BYE or closes the connection...");
                while ((shouldRun) && (line = input.readLine()) != null) {
                    LOG.info(line);
                    if (line.equalsIgnoreCase("bye")) {
                        shouldRun = false;
                        output.println("Close request...");
                        output.flush();
                        continue;
                    }
                    output.println("> " + line.toUpperCase());
                    output.flush();
                }

                LOG.info("Cleaning up resources...");
                socket.close();
                input.close();
                output.close();

            } catch (IOException ex) {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                if (output != null) {
                    output.close();
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }
}
