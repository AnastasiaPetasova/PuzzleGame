package client;

import javafx.application.Application;
import javafx.stage.Stage;
import model.PuzzleGame;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainClient extends Application {

    @Override
    public void start(Stage primaryStage) throws RemoteException, NotBoundException {
        primaryStage.setTitle("Puzzle game");

        Registry registry = LocateRegistry.getRegistry();
        PuzzleGame game = (PuzzleGame) registry.lookup("game");

        SceneSwitcher.INSTANCE.setMenuScene(primaryStage, game);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
