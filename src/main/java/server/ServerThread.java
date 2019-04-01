package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the behavior of the server's threads, whose
 * responsibility is to take care of clients once they have connected.
 */
public class ServerThread implements Runnable {

    public Socket socket = null;
    private BufferedReader input;
    private PrintWriter output;
    final static Logger LOG = Logger.getLogger(Server.class.getName());

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

    /**
     * Get current socket
     * @return the thread's socket
     */
    public Socket getSocket() {
        return this.socket;
    }

    /**
     * Get the input stream opened through the socket
     * @return the input stream
     */
    public BufferedReader getInput() {
        return this.input;
    }

    /**
     * Get the output stream opened through the socket
     * @return the output stream
     */
    public PrintWriter getOutput() {
        return this.output;
    }

    /**
     * Start the thread
     */
    public void run() {
        String line;
        boolean shouldRun = true;

        output.println("Welcome on GoFunMe.\nSend me text lines and conclude with the BYE command.");
        output.flush();

        /* test function */
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
