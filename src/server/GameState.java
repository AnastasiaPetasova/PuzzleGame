package server;

import model.Field;
import model.GameConstants;

import java.awt.Point;
import java.util.*;

public class GameState implements GameConstants {

    private Field field;

    private int[][] tileStates;
    private int closedTiles;

    private List<Point> lastOpened;

    GameState(Field field) {
        this.field = field;
        this.tileStates = new int[field.getHeight()][field.getWidth()];
        for (int[] row : tileStates) {
            Arrays.fill(row, CLOSED);
        }

        this.closedTiles = field.getWidth() * field.getHeight();
        this.lastOpened = new ArrayList<>();
    }

    public int getWidth() { return field.getWidth(); }

    public int getHeight() { return field.getHeight(); }

    public int getRealTile(int x, int y) {
        return field.getTile(x, y);
    }

    public int getTile(int x, int y) {
        int state = tileStates[y][x];
        if (OPENED == state) {
            return getRealTile(x, y);
        } else {
            return state;
        }
    }

    public void clearLastOpened() {
        lastOpened.clear();
    }

    public int getLastOpenedSize() {
        return lastOpened.size();
    }

    public void setLastOpened(int newState) {
        for (Point point : lastOpened) {
            setTileState(point.x, point.y, newState);
        }

        clearLastOpened();
    }

    public boolean equalsLastOpened() {
        Point firstPoint = lastOpened.get(0);
        int firstTile = field.getTile(firstPoint.x, firstPoint.y);

        for (Point point : lastOpened) {
            int tile = field.getTile(point.x, point.y);

            if (firstTile != tile) return false;
        }

        return true;
    }

    public void openTile(int x, int y) {
        setTileState(x, y, OPENED);

        this.lastOpened.add(new Point(x, y));
    }

    public void setTileState(int x, int y, int newState) {
        int oldState = tileStates[y][x];
        if (oldState != newState) {
            tileStates[y][x] = newState;

            if (CLOSED == oldState) --closedTiles;
            if (CLOSED == newState) ++closedTiles;
        }
    }

    public boolean isGameEnded() {
        return 0 == closedTiles;
    }

    public void openAllTiles() {
        for (int x = 0; x < field.getWidth(); ++x) {
            for (int y = 0; y < field.getHeight(); ++y) {
                openTile(x, y);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(field).append('\n');

        for (int[] row : tileStates) {
            for (int state : row) {
                stringBuilder.append(state).append(TEXT_SEPARATOR);
            }

            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }
}
