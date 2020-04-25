package server;

import model.*;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import java.awt.Point;
import java.util.concurrent.Callable;

public class PuzzleGameServer implements Serializable, PuzzleGame, GameConstants {

    private final static Random random;

    static {
        random = new Random();
    }

    private static void checkFieldSizes(int width, int height) throws PuzzleGameException {
        String message = "";

        if (width < 1 || width > MAX_WIDTH) {
            message += String.format("width should be [%d; %d], but found %d;\n", 1, MAX_WIDTH, width);
        }

        if (height < 1 || height > MAX_HEIGHT) {
            message += String.format("height should be [%d; %d], but found %d; \n", 1, MAX_HEIGHT, height);
        }

        if ((width * height) % 2 != 0) {
            message += String.format("width x height should be even, but found %d; \n", width * height);
        }

        if (!message.isEmpty()) {
            throw new PuzzleGameException(message);
        }
    }

    private static Field generate(int width, int height, int imagesCount) throws PuzzleGameException {
        checkFieldSizes(width, height);

        int size = width * height;
        int imagePairs = size / 2;

        if (imagesCount <= imagePairs) {
            throw new PuzzleGameException(
                    String.format("There are %d image pairs, but images count is only %d", imagePairs, imagesCount)
            );
        }

        Field field = new Field(width, height);

        List<Point> tiles = new ArrayList<>();
        for (int x = 0; x < field.getWidth(); ++x) {
            for (int y = 0; y < field.getHeight(); ++y) {
                tiles.add(new Point(x, y));
            }
        }

        Collections.shuffle(tiles, random);

        List<Integer> imageIds = new ArrayList<>();
        for (int id = 1; id < imagesCount; ++id) imageIds.add(id);

        Collections.shuffle(imageIds, random);

        for (int idIndex = 0; idIndex < imagePairs; ++idIndex) {
            int imageId = imageIds.get(idIndex);

            int firstTileIndex = idIndex + idIndex;
            int secondTileIndex = firstTileIndex + 1;

            Point firstTile = tiles.get(firstTileIndex);
            field.setTile(firstTile.x, firstTile.y, imageId);

            Point secondTile = tiles.get(secondTileIndex);
            field.setTile(secondTile.x, secondTile.y, imageId);
        }

        return field;
    }

    private static final String STATE_FILE_NAME = "state.txt";

    private static void saveToFile(GameState state) {
        try (PrintWriter out = new PrintWriter(STATE_FILE_NAME)) {
            out.println(state);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static GameState loadFromFile() {
        try (BufferedReader in = new BufferedReader(new FileReader(STATE_FILE_NAME))) {
            final StringTokenizer[] tok = {new StringTokenizer("")};

            Callable<Integer> readInt = () -> {
                  if (!tok[0].hasMoreTokens()) {
                      tok[0] = new StringTokenizer(in.readLine(), "" + TEXT_SEPARATOR);
                  }

                  return Integer.parseInt(tok[0].nextToken());
            };

            int width = readInt.call();
            int height = readInt.call();

            checkFieldSizes(width, height);

            Field field = new Field(width, height);

            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    field.setTile(x, y, readInt.call());
                }
            }

            GameState state = new GameState(field);
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    state.setTileState(x, y, readInt.call());
                }
            }

            return state;
        } catch (Exception e) {
            return null;
        }
    }

    private GameState state;

    public PuzzleGameServer() {
        this.state = loadFromFile();
    }

    @Override
    public int getWidth() {
        return state.getWidth();
    }

    @Override
    public int getHeight() {
        return state.getHeight();
    }

    @Override
    public void startGame(int width, int height, int imagesCount) throws PuzzleGameException {
        Field field = generate(width, height, imagesCount);

        this.state = new GameState(field);
        saveToFile(state);
    }

    @Override
    public boolean canContinue() {
        return null != state;
    }

    @Override
    public void continueGame() throws PuzzleGameException {
        if (null == state) {
            this.state = loadFromFile();
        }

        if (!canContinue()) {
            throw new PuzzleGameException("There is no saved game");
        }
    }

    @Override
    public int getTile(int x, int y) {
        return state.getTile(x, y);
    }

    @Override
    public void openTile(int x, int y) {
        if (REMOVED == state.getTile(x, y)) return;

        if (2 == state.getLastOpenedSize()) {
            int newState = state.equalsLastOpened() ? REMOVED : CLOSED;
            state.setLastOpened(newState);
        }

        if (CLOSED != state.getTile(x, y)) return;

        state.openTile(x, y);
        checkFinish();
    }

    private void checkFinish() {
        if (isGameEnded()) {
            state.openAllTiles();
            saveToFile(state);
        }
    }

    @Override
    public boolean isGameEnded() {
        return state.isGameEnded();
    }
}
