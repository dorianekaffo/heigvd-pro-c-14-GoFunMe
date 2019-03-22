package server.games;

import javax.swing.*;

public class TicTacToeServer extends JFrame {

    private String[] board = new String[9];
    private JTextArea outputArea;
    private Player[] players;

    private class Player implements Runnable {

        @Override
        public void run() {

        }
    }
}
