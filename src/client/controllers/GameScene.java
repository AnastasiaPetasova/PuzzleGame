package client.controllers;

import client.SceneSwitcher;
import client.ThemeService;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import model.PuzzleGame;

import java.rmi.RemoteException;

public class GameScene extends AbstractGameController {

    public final static int CELL_WIDTH = 150;
    public final static int CELL_HEIGHT = 150;

    @FXML
    private GridPane fieldGridPane;

    @FXML
    private Button menuButton;

    Canvas[][] tiles;

    int width, height;

    @Override
    public void initializeBy(PuzzleGame game) throws RemoteException {
        super.initializeBy(game);

        this.width = game.getWidth();
        this.height = game.getHeight();

        initFieldGridPane();
        initTileImageViews();
        initMenuButton();

        drawTiles();
    }

    private void initFieldGridPane() {
        initTableConstraints();
    }

    private void initTableConstraints() {
        fieldGridPane.setPrefHeight(CELL_HEIGHT * height);
        fieldGridPane.setPrefWidth(CELL_WIDTH * width);

        for (int j = 0; j < width; j++) {
            ColumnConstraints cc = new ColumnConstraints();
            fieldGridPane.getColumnConstraints().add(cc);
        }

        for (int i = 0; i < height; i++) {
            RowConstraints rc = new RowConstraints();
            fieldGridPane.getRowConstraints().add(rc);
        }
    }

    void tileClickAction(int x, int y) throws RemoteException {
        if (x < 0 || width <= x) return;
        if (y < 0 || height <= y) return;
        game.openTile(x, y);
    }

    private void initTileImageViews() {
        this.tiles = new Canvas[height][width];

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                tiles[y][x] = new Canvas(CELL_WIDTH, CELL_HEIGHT);
                fieldGridPane.add(tiles[y][x], x, y);

                int finalX = x;
                int finalY = y;
                tiles[y][x].setOnMouseClicked(event -> {
                    clearMessageLabel();

                    try {
                        tileClickAction(finalX, finalY);
                        drawTiles();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private void drawTiles() throws RemoteException {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int tile = game.getTile(x, y);

                Canvas tileCanvas = tiles[y][x];
                GraphicsContext gc = tileCanvas.getGraphicsContext2D();

                if (REMOVED == tile) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(0, 0, tileCanvas.getWidth(), tileCanvas.getHeight());
                } else {
                    Image image = ThemeService.INSTANCE.getImage(tile);
                    gc.drawImage(image, 0, 0);
                }

                gc.setStroke(Color.WHITE);
                gc.strokeRect(0, 0, tileCanvas.getWidth(), tileCanvas.getHeight());
            }
        }

        if (game.isGameEnded()) {
            setMessage("Победа!");
        }
    }

    private void initMenuButton() {
        menuButton.setOnAction(actionEvent -> SceneSwitcher.INSTANCE.setMenuScene(actionEvent, game));
    }
}
