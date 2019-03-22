package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket connection;
    private BufferedReader input;
    private PrintWriter output;
    private String host;
    private int port;

    /**
     * Client constructor
     *
     * @param host the server to connect to
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            this.connection = new Socket(this.host, this.port);
            this.input = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            this.output = new PrintWriter(this.connection.getOutputStream());

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void communication(String arg) {
        String line;

        this.output.println(arg);
        this.output.flush();

        try {
            if ((line = input.readLine()) != null) {
                this.output.println(line);
                this.output.flush();
                this.output.println("bye");
                this.output.flush();

                this.input.close();
                this.output.close();
                this.connection.close();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
