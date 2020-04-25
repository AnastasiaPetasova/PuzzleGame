package model;

public class Field implements GameConstants {

    private int width, height;
    private int[][] tiles;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        this.tiles = new int[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTile(int x, int y) {
        return tiles[y][x];
    }

    public void setTile(int x, int y, int imageId) {
        tiles[y][x] = imageId;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(height).append(TEXT_SEPARATOR).append(width).append('\n');
        for (int[] row : tiles) {
            for (int tile : row) {
                stringBuilder.append(tile);
                stringBuilder.append(TEXT_SEPARATOR);
            }

            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }
}
