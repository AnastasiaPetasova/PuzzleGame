package client.controllers;

import client.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.GameConstants;
import model.PuzzleGame;

import java.rmi.RemoteException;

public abstract class AbstractGameController implements Controller, GameConstants {

    @FXML
    Label messageLabel;

    PuzzleGame game;

    @Override
    public void initializeBy(PuzzleGame game) throws RemoteException {
        this.game = game;

        clearMessageLabel();
    }

    void clearMessageLabel() {
        messageLabel.setText("");
    }

    void setMessage(String message) {
        messageLabel.setText(message);
    }
}
