package server.games;


import server.ServerThread;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TicTacToeServer extends GameServer {

    private final static String[] MARKS = {"X", "O"};
    private final static int PLAYER_X = 0;
    private final static int PLAYER_O = 1;
    private String[] board = new String[9];
    private JTextArea outputArea;
    private Player[] players;
    private int currentPlayer;


    /**
     * TicTacToeServer constructor
     * @param playersThreads the players' threads
     */
    public TicTacToeServer(ServerThread[] playersThreads) {

        /* Set up the board */
        for (int i = 0; i < 9; i++) {
            board[i] = new String("");
        }

        /* Set up players */
        this.players = new Player[2];

        for (int i = 0; i < players.length; ++i) {
            players[i] = new Player(playersThreads[i], i);
            runGame.execute(players[i]);
        }

        /* First player to play is always X */
        this.currentPlayer = PLAYER_X;

    }

    public void start() {

    }

    private class Player implements Runnable {
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;
        private String mark;
        private int number;
        private boolean suspended = true;

        public Player(ServerThread thread, int number) {
            this.socket = thread.getSocket();
            this.number = number;
            this.mark = MARKS[number];

            this.input = thread.getInput();
            this.output = thread.getOutput();

        }


        @Override
        public void run() {

        }
    }
}
