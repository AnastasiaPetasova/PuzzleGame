package client;

import model.PuzzleGame;

import java.rmi.RemoteException;

public interface Controller {

    void initializeBy(PuzzleGame game) throws RemoteException;
}
