package client;

import client.controllers.AbstractGameController;
import client.controllers.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.PuzzleGame;

import javax.tools.Tool;
import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.EventObject;
import java.util.function.Consumer;

public class SceneSwitcher {

    private final static String MENU_SCENE_NAME = "menu";
    private final static int MENU_SCENE_WIDTH = 450, MENU_SCENE_HEIGHT = 450;

    private final static String GAME_SCENE_NAME = "game";
    private final static int GAME_SCENE_WIDTH = 100, GAME_SCENE_HEIGHT = 100;

    public final static SceneSwitcher INSTANCE = new SceneSwitcher();

    private SceneSwitcher() {

    }

    private static class ResultInfo {

        FXMLLoader loader;
        Stage stage;
        Scene scene;

        ResultInfo(FXMLLoader loader) {
            this.loader = loader;
        }

        <T extends AbstractGameController> T getController() {
            return loader.getController();
        }

        Parent getParent() {
            try {
                return loader.load();
            } catch (IOException ignored) {
                ignored.printStackTrace();
                return null;
            }
        }
    }

    private ResultInfo findParentBy(String sceneName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                String.format("/client/scenes/%s.fxml", sceneName)
        ));
        return new ResultInfo(loader);
    }

    private ResultInfo createScene(String sceneName, double width, double height) {
        ResultInfo resultInfo = findParentBy(sceneName);

        resultInfo.scene = new Scene(
                resultInfo.getParent(),
                width, height
        );

        return resultInfo;
    }

    public Stage findStage(EventObject event) {
        return (Stage) ((Node)event.getSource()).getScene().getWindow();
    }

    private ResultInfo setScene(Stage stage, String sceneName, double width, double height,
                                Consumer<ResultInfo> preShowAction) {
        ResultInfo info = createScene(sceneName, width, height);

        info.stage = stage;

        preShowAction.accept(info);
        setScene(stage, info.scene);
        // info.stage.getScene().getStylesheets().add("styles.css");
        return info;
    }

    private void setScene(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        stage.setX(screenSize.width / 2.0 - scene.getWidth() / 2);
        stage.setY(screenSize.height / 2.0 - scene.getHeight() / 2);
    }

    public void setScene(EventObject event, String sceneName, double width, double height, PuzzleGame game) {
        Stage stage = findStage(event);
        setScene(stage, sceneName, width, height, game);
    }

    public void setScene(Stage stage, String sceneName, double width, double height, PuzzleGame game) {
        setScene(stage, sceneName, width, height, (ResultInfo ri) ->
                {
                    try {
                        ri.getController().initializeBy(game);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public void setGameScene(EventObject event, PuzzleGame game) {
        try {
            double width = GAME_SCENE_WIDTH + game.getWidth() * GameScene.CELL_WIDTH;
            double height = GAME_SCENE_HEIGHT + game.getHeight() * GameScene.CELL_HEIGHT;

            setScene(event, GAME_SCENE_NAME, width, height, game);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setMenuScene(EventObject event, PuzzleGame game) {
        setScene(event, MENU_SCENE_NAME, MENU_SCENE_WIDTH, MENU_SCENE_HEIGHT, game);
    }

    public void setMenuScene(Stage stage, PuzzleGame game) {
        setScene(stage, MENU_SCENE_NAME, MENU_SCENE_WIDTH, MENU_SCENE_HEIGHT, game);
    }
}
