package server.games;

import server.ServerThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class GameServer {

    private ServerThread[] playersThreads;
    ExecutorService runGame;

    /* A Lock is used to synchronize the game */
    Lock gameLock;

    /* Conditions are used to make players wait on each other */
    Condition otherPlayerConnected;
    Condition otherPlayerTurn;

    public GameServer() {}

    public GameServer(ServerThread[] threads) {

        this.otherPlayerConnected = this.gameLock.newCondition();
        this.otherPlayerTurn = this.gameLock.newCondition();

        /* Set up players */
        this.playersThreads = new ServerThread[2];

        for (int i = 0; i < playersThreads.length; ++i) {
            playersThreads[i] = threads[i];
        }

    }

}
