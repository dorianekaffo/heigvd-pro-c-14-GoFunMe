package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The server's role is to handle incoming connections from multiple clients. Each
 * connection is taken in charge by a dedicated ServerThread.
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
}
