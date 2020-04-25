package server;

import model.PuzzleGame;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainServer {

    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.codebase", " file://C:/Users/Anansy/IdeaProjects/PuzzleGame/out/production/PuzzleGame/model");

            PuzzleGame puzzleGameServer = new PuzzleGameServer();
            PuzzleGame puzzleGame = (PuzzleGame) UnicastRemoteObject.exportObject(puzzleGameServer, 0);

            Registry registry =  LocateRegistry.getRegistry();
            registry.bind("game", puzzleGame);

            System.out.println("Server is ready");

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
