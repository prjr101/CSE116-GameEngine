package app.games.roguelikeobjects;

import java.util.*;

import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.LevelParser;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.GameUtils;
import app.gameengine.utils.Randomizer;

/**
 * Top-down game that is randomly generated from presets each time.
 * <p>
 * Levels within this game are randomly chosen by the
 * {@link RoguelikeLevelFactory}. Within each level, {@link Marker}s denote the
 * locations of objects which are randomly chosen by either the
 * {@link EnemyFactory} or the {@link CollectibleFactory}.
 *
 * @see RoguelikeLevelFactory
 * @see EnemyFactory
 * @see CollectibleFactory
 */
public class RoguelikeGame extends Game {

    protected static final Vector2D UP_VECTOR = new Vector2D(0, -1);
    protected static final Vector2D RIGHT_VECTOR = new Vector2D(1, 0);
    protected static final Vector2D DOWN_VECTOR = new Vector2D(0, 1);
    protected static final Vector2D LEFT_VECTOR = new Vector2D(-1, 0);
    protected static final ArrayList<Vector2D> DIRECTIONS = new ArrayList<>(
            Arrays.asList(UP_VECTOR, RIGHT_VECTOR, DOWN_VECTOR, LEFT_VECTOR));

    private Vector2D mapSize = new Vector2D(4, 3);
    private int maxRooms = 10;

    private HashMap<String, RoguelikeLevel> levelMap = new HashMap<>();
    private int roomsGenerated;
    private RoguelikeLevel previousLevel;

    public HashMap<String, RoguelikeLevel> getLevelMap() {
        return this.levelMap;
    }

    public RoguelikeLevel getPreviousLevel() {
        return previousLevel;
    }

    public void setGameParameters(Vector2D mapSize, int maxRooms) {
        this.mapSize = mapSize;
        this.maxRooms = maxRooms;
    }

    @Override
    public void init() {
        super.init();
        generateMap();
    }

    @Override
    public void addLevel(Level level) {
        if (level instanceof RoguelikeLevel roguelikeLevel) {
            this.levelMap.put(level.getName(), roguelikeLevel);
        } else {
            System.err.println("** Level could not be added **");
        }
    }

    @Override
    public void changeLevel(String name) {
        Level level = null;
        if (this.levelMap.containsKey(name)) {
            level = this.levelMap.get(name);
        } else {
            level = LevelParser.parseLevel(this, "roguelike/" + name + ".csv");
            if (level != null) {
                this.addLevel(level);
            }
        }

        if (level != null) {
            // Transfer controls for smooth transition
            level.setKeyboardControls(this.currentLevel.getKeyboardControls());
            level.setMouseControls(this.currentLevel.getMouseControls());
            this.loadLevel(level);
        }
    }

    @Override
    public void loadLevel(Level level) {
        super.loadLevel(level);
        if (level instanceof RoguelikeLevel roguelikeLevel) {
            this.previousLevel = roguelikeLevel;
        }
    }

    @Override
    public void resetGame() {
        this.levelMap.clear();
        this.roomsGenerated = 0;
        this.previousLevel = null;

        super.resetGame();
        this.getPlayer().reset();
        this.generateMap();
    }

    @Override
    public void resetCurrentLevel() {
        this.resetGame();
    }

    /**
     * Randomly generate each level within the game, and load the starting level.
     */
    private void generateMap() {
        Vector2D startingPosition = getRandomStartingPosition();
        this.roomsGenerated = 0;

        ArrayList<Vector2D> explored = new ArrayList<>();
        explored.add(startingPosition);

        Stack<Vector2D> positions = new Stack<>();
        positions.push(startingPosition);
        Stack<RoguelikeLevel> levels = new Stack<>();

        RoguelikeLevel startingLevel = null;
        levels.push(null);

        while (!positions.isEmpty() && roomsGenerated < this.maxRooms) {
            Vector2D position = positions.pop();
            RoguelikeLevel level = levels.pop();
            RoguelikeLevel nextLevel = getNextLevelToGenerate();
            nextLevel.initialize(position);
            addLevel(nextLevel);
            roomsGenerated++;


            if (startingLevel == null) {
                startingLevel = nextLevel;
            }

            if (level != null) {
                nextLevel.openDoor(level);
                level.openDoor(nextLevel);
            }

            ArrayList<Vector2D> directions = Randomizer.shuffleArrayList(DIRECTIONS);
            for (Vector2D direction : directions) {
                Vector2D location = new Vector2D(position.getX() + direction.getX(), position.getY() + direction.getY());
                if (GameUtils.isInBounds(location, mapSize) && !explored.contains(location)) {
                    positions.add(location);
                    explored.add(location);
                    levels.push(nextLevel);
                }
            }
        }
        this.previousLevel = null;
        this.loadLevel(startingLevel);
    }

    private Vector2D getRandomStartingPosition() {
        return new Vector2D(Randomizer.randomInt((int) mapSize.getX()), Randomizer.randomInt((int) mapSize.getY()));
    }

    private RoguelikeLevel getNextLevelToGenerate() {
        if (roomsGenerated <= 0) {
            return RoguelikeLevelFactory.getStartingLevel(this);
        } else if (roomsGenerated < maxRooms - 1) {
            return RoguelikeLevelFactory.getRandomLevel(this);
        }
        return RoguelikeLevelFactory.getBossLevel(this);
    }
}
