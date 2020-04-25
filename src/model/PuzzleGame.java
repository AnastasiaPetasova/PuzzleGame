package model;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PuzzleGame extends Remote {

    int getWidth() throws RemoteException;
    int getHeight() throws RemoteException;

    /**
     * Creates new game with height x width tiles
     *
     * @param width - number of columns
     * @param height - number of rows
     * @param maxImageId - max possible used image id for generating
     * @throws PuzzleGameException - if game can't be started or generated (incorrect height/width/maxImageId)
     */
    void startGame(int width, int height, int maxImageId) throws PuzzleGameException, RemoteException ;

    boolean canContinue() throws RemoteException;

    /**
     * Continues saved earlier game
     * @throws PuzzleGameException - if game can't be continued
     */
    void continueGame() throws PuzzleGameException, RemoteException;

    int getTile(int x, int y) throws RemoteException;

    void openTile(int x, int y) throws RemoteException;
    boolean isGameEnded() throws RemoteException;
}
