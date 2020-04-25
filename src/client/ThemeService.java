package client;

import client.controllers.GameScene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.*;

public class ThemeService {

    static class Theme {

        public final String name;

        private final List<Image> images;

        public Theme(String name) {
            this.name = name;
            this.images = new ArrayList<>();
        }

        void addImage(Image image) {
            images.add(image);
        }

        Image getImage(int index) {
            return images.get(index);
        }

        @Override
        public String toString() {
            return name + " (" + (images.size() - 1) + ")";
        }
    }

    private static Theme createColorTheme() {
        String name = "Colors";

        Theme theme = new Theme(name);

        List<Color> colors = Arrays.asList(
                Color.GRAY,

                Color.MAGENTA,
                Color.OLIVE,
                Color.RED,
                Color.GREEN,
                Color.ORANGE,
                Color.YELLOW,
                Color.BLUE,
                Color.PINK,
                Color.AQUA,
                Color.FUCHSIA,
                Color.SIENNA,
                Color.BLUEVIOLET,
                Color.CHOCOLATE,
                Color.DARKORCHID,
                Color.GOLD,
                Color.PAPAYAWHIP,
                Color.PURPLE,
                Color.LIME
        );

        for (Color color : colors) {
            WritableImage image = new WritableImage(GameScene.CELL_WIDTH, GameScene.CELL_HEIGHT);
            PixelWriter pw = image.getPixelWriter();
            for (int y = 0; y < GameScene.CELL_HEIGHT; ++y) {
                for (int x = 0; x < GameScene.CELL_WIDTH; ++x) {
                    pw.setColor(x, y, color);
                }
            }

            theme.addImage(image);
        }

        return theme;
    }

    public static final ThemeService INSTANCE = new ThemeService();

    public final List<Theme> themes;
    private Theme selectedTheme;

    private ThemeService() {
        this.themes = new ArrayList<>();

        themes.add(
            createColorTheme()
        );

        selectedTheme = themes.get(0);
    }

    public Image getImage(int index) {
        return selectedTheme.getImage(index);
    }

    public int getImagesCount() {
        return selectedTheme.images.size();
    }

    void selectTheme(int index) {
        selectedTheme = themes.get(index);
    }
}
