package app.games.snake;

import java.util.ArrayList;

import app.display.common.Background;
import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.model.physics.PhysicsEngine;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.Randomizer;
import app.gameengine.utils.Timer;

/**
 * A level within a game of snake.
 * <p>
 * This level manages logic including timing of player movement, spawning new
 * food, and keeping track of the snake positions.
 * 
 * @see Level
 * @see Snake
 * @see SnakeFood
 */
public class SnakeLevel extends Level {

    private final Timer timer;
    private final ArrayList<SnakeFood> food = new ArrayList<>();
    private final ArrayList<SnakeBody> tail = new ArrayList<>();
    private final int lengthIncrease;
    private final int startingLength;
    private final int numFood;

    public SnakeLevel(Game game, int size, Timer timer, String name) {
        this(game, size, timer, name, 1, 3, 1);
    }

    public SnakeLevel(Game game, int size, Timer timer, String name, int lengthIncrease, int startingLength, int numFood) {
        super(game, new PhysicsEngine(), size, size, name);
        this.timer = timer;
        this.lengthIncrease = Math.min(lengthIncrease, size * size - startingLength);
        this.startingLength = Math.min(startingLength, size * size);
        this.numFood = Math.min(numFood, size * size - 2);
        this.keyboardControls = new SnakeControls(game);
        this.setBackground(new Background("snake/snakeColors.png", 3, 0));
    }

    /**
     * Returns the list of {@link SnakeFood} objects currently in the level.
     * 
     * @return the list of food
     */
    public ArrayList<SnakeFood> getFood() {
        return this.food;
    }

    /**
     * Returns the list of {@link SnakeBody} objects currently in the level. This
     * does not include the head of the snake.
     * 
     * @return the list of body segments
     */
    public ArrayList<SnakeBody> getTail() {
        return this.tail;
    }

    /**
     * Surrounds the level with {@link SnakeWall} objects, just out of bounds. This
     * is to prevent the snake from leaving the confines of the level.
     */
    public void wallOffBoundary() {
        for (int i = -1; i <= this.height; i++){
            for (int j = -1; j <= this.width; j++){
                if (i == -1 || i == this.height || j == -1 || j == this.width){
                    this.addStaticObject(new SnakeWall(i,j));
                }
            }
        }
    }

    /**
     * Randomly choose a new location for food, that is not already occupied. If the
     * snake head and tail fill all available space, it will advance to the next
     * level. If there is no room to spawn food, it will do nothing.
     * <p>
     * Note that food is added to this class's list of food, as well as its list of
     * static objects.
     */
    public void spawnFood() {
        if (this.tail.size()+1 == this.width*this.height){
            this.game.advanceLevel();
            return;
        }
        if (this.tail.size()+1 + this.getFood().size() == width * height){
            return;
        }
        Vector2D v = Randomizer.randomIntVector2D(new Vector2D(width, height), new ArrayList<>());
        boolean valid = true;
        for (SnakeFood snakeFood : this.food) {
            if (v.getX() == snakeFood.getLocation().getX() && v.getY() == snakeFood.getLocation().getY()) {
                valid = false;
                break;
            }
        }

        if (this.getPlayer().getLocation().getX() == v.getX() && this.getPlayer().getLocation().getY() == v.getY()){
            valid = false;
        }

        for (SnakeBody snakeBody : this.tail) {
            if (v.getX() == snakeBody.getLocation().getX() && v.getY() == snakeBody.getLocation().getY()) {
                valid = false;
                break;
            }
        }

        if (valid){
            SnakeFood food = new SnakeFood(v.getX(), v.getY(), this);
            this.addStaticObject(food);
            this.getFood().add(food);
        }

        else {
            spawnFood();
        }
    }

    /**
     * Increase the length of the snake by creating new body segments. The amount
     * which the length increases by is determined by the lengthIncrease passed into
     * the constructor.
     * <p>
     * Note that body segments are added to this class's list of body segments, as
     * well as its list of static objects.
     */
    public void lengthenSnake() {
    //(1, 0) is right, (0, 1) is down, (-1, 0) is left, and (0, -1) is up
        if (this.tail.isEmpty()){
            for (int i = 0; i < this.lengthIncrease; i++){
                if (this.getPlayer().getOrientation().getX() == 1) {
                    SnakeBody body = new SnakeBody(this.getPlayer().getLocation().getX()-1, this.getPlayer().getLocation().getY());
                    this.tail.addFirst(body);
                    this.addStaticObject(body);
                }
                else if (this.getPlayer().getOrientation().getX() == -1) {
                    SnakeBody body = new SnakeBody(this.getPlayer().getLocation().getX()+1, this.getPlayer().getLocation().getY());
                    this.tail.addFirst(body);
                    this.addStaticObject(body);
                }
                else if (this.getPlayer().getOrientation().getY() == 1) {
                    SnakeBody body = new SnakeBody(this.getPlayer().getLocation().getX(), this.getPlayer().getLocation().getY()-1);
                    this.tail.addFirst(body);
                    this.addStaticObject(body);
                }
                else if (this.getPlayer().getOrientation().getY() == -1) {
                    SnakeBody body = new SnakeBody(this.getPlayer().getLocation().getX(), this.getPlayer().getLocation().getY()+1);
                    this.tail.addFirst(body);
                    this.addStaticObject(body);
                }
            }
        }

        else {
            for (int i = 0; i < this.lengthIncrease; i++){
                SnakeBody body = new SnakeBody(this.tail.getFirst().getLocation().getX(), this.tail.getFirst().getLocation().getY());
                this.tail.addFirst(body);
                this.addStaticObject(body);
            }
        }
    }

    /**
     * Create the snake at the center of the level. All body segments start in a
     * stack behind the head of the snake.
     */
    public void spawnSnake() {
        for (int i = 0; i < this.startingLength-1; i++) {
            Vector2D orientation = new Vector2D(this.getPlayer().getOrientation().getX(), this.getPlayer().getOrientation().getY());
            if (this.getPlayer().getOrientation().getX() == 1) {
                SnakeBody body = new SnakeBody(this.getPlayer().getLocation().getX()-1, this.getPlayer().getLocation().getY());
                this.tail.addFirst(body);
                this.addStaticObject(body);
            }
            else if (this.getPlayer().getOrientation().getX() == -1) {
                SnakeBody body = new SnakeBody(this.getPlayer().getLocation().getX()+1, this.getPlayer().getLocation().getY());
                this.tail.addFirst(body);
                this.addStaticObject(body);
            }
            else if (this.getPlayer().getOrientation().getY() == 1) {
                SnakeBody body = new SnakeBody(this.getPlayer().getLocation().getX(), this.getPlayer().getLocation().getY()-1);
                this.tail.addFirst(body);
                this.addStaticObject(body);
            }
            else if (this.getPlayer().getOrientation().getY() == -1) {
                SnakeBody body = new SnakeBody(this.getPlayer().getLocation().getX(), this.getPlayer().getLocation().getY()+1);
                this.tail.addFirst(body);
                this.addStaticObject(body);
            }
        }
    }

    /**
     * Move each segment of the snake forward in its direction of travel by one
     * tile, including both body segments and the head of the snake.
     */
    private void moveSnake() {
        //(1, 0) is right, (0, 1) is down, (-1, 0) is left, and (0, -1) is up
        Vector2D location = new Vector2D(this.getPlayer().getLocation().getX(), this.getPlayer().getLocation().getY());
        double x = location.getX();
        double y = location.getY();
        double newX;
        double newY;
        for (int i = tail.size()-1; i >= 0; i--) {
            newX = tail.get(i).getLocation().getX();
            newY = tail.get(i).getLocation().getY();
            this.tail.get(i).setLocation(x, y);
            x = newX;
            y = newY;
        }
        Vector2D orientation = new Vector2D(this.getPlayer().getOrientation().getX(), this.getPlayer().getOrientation().getY());
        this.getPlayer().setLocation(location.getX()+orientation.getX(),location.getY()+orientation.getY());
    }

    @Override
    public void update(double dt) {
        this.food.removeIf(SnakeFood::isDestroyed);
        this.tail.removeIf(SnakeBody::isDestroyed);
        // Move body
        if (this.timer.tick(dt) > 0) {
            this.keyboardControls.processInput(0);
            this.moveSnake();
        }
        super.update(dt);
    }

    @Override
    public void load() {
        super.load();
        this.food.forEach(SnakeFood::destroy);
        this.tail.forEach(SnakeBody::destroy);
        this.food.clear();
        this.tail.clear();

        this.spawnSnake();
        for (int i = 0; i < this.numFood; i++) {
            this.spawnFood();
        }
        this.wallOffBoundary();
    }

    @Override
    public void reset() {
        this.load();
    }

    @Override
    public String getUIString() {
        return String.format("Score: %.0f", this.score);
    }

}
