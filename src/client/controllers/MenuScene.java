package client.controllers;

import client.SceneSwitcher;
import client.ThemeService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import model.PuzzleGame;
import model.PuzzleGameException;

import java.rmi.RemoteException;

public class MenuScene extends AbstractGameController {

    @FXML
    private Spinner<Integer> widthSpinner;

    @FXML
    private Spinner<Integer> heightSpinner;

    @FXML
    private Button startGameButton;

    @FXML
    private Button continueGameButton;

    @FXML
    private Button exitButton;

    @Override
    public void initializeBy(PuzzleGame game) throws RemoteException {
        super.initializeBy(game);

        initSpinners();
        initButtons();
    }

    private void initSpinners() {
        this.widthSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, MAX_WIDTH, MAX_WIDTH)
        );

        this.heightSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, MAX_HEIGHT, MAX_HEIGHT)
        );
    }

    private void initButtons() {
        this.startGameButton.setOnAction(actionEvent -> {
                int width = widthSpinner.getValue();
                int height = heightSpinner.getValue();

                int imagesCount = ThemeService.INSTANCE.getImagesCount();

                try {
                    game.startGame(width, height, imagesCount);
                    SceneSwitcher.INSTANCE.setGameScene(actionEvent, game);
                } catch (PuzzleGameException e) {
                    setMessage(e.getMessage());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        );

        try {
            this.continueGameButton.setDisable(!game.canContinue());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        this.continueGameButton.setOnAction(actionEvent -> {
            try {
                game.continueGame();
                SceneSwitcher.INSTANCE.setGameScene(actionEvent, game);
            } catch (PuzzleGameException e) {
                setMessage(e.getMessage());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        this.exitButton.setOnAction(actionEvent -> SceneSwitcher.INSTANCE.findStage(actionEvent).close());
    }
}
