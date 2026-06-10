package app.gameengine;

import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.model.gameobjects.Player;

/**
 * A {@code Game} subclass that organizes levels linearly, such that each level
 * has a single "next" level. This is implemented using the
 * {@link LinkedListNode} class.
 * <p>
 * This class implements the functionality to advance to the next level, as well
 * as additional functionality to replace or remove levels by name.
 * 
 * @see LinkedListNode
 * @see Game
 * @see Level
 */
public class LinearGame extends Game {
    private LinkedListNode<Level> levels;

    public LinearGame() {
        super();
    }

    public LinearGame(Player player) {
        super(player);
    }

    public LinkedListNode<Level> getLevelList() {
        return this.levels;
    }

    public void setLevelList(LinkedListNode<Level> levels) {
        this.levels = levels;
    }

    public void addLevel(Level level) {
        LinkedListNode<Level> newNode = new LinkedListNode<>(level, null);
        if(this.levels == null) {
            this.levels = newNode;
        }
        else {
            LinkedListNode<Level> current = this.levels;
            while(current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
    }

    public void advanceLevel() {
        LinkedListNode<Level> current = this.levels;
        while(current != null) {
            if(current.getValue().getName().equals(getCurrentLevel().getName())) {
                if(current.getNext() != null) {
                    this.loadLevel(current.getNext().getValue());
                }
                return;
            }
            current = current.getNext();
        }
    }

    public void removeLevelByName(String levelName) {
        if(this.levels == null) {
            return;
        }
        if(this.levels.getValue().getName().equals(levelName)) {
            this.levels = this.levels.getNext();
            return;
        }
        LinkedListNode<Level> current = this.levels;
        while(current.getNext() != null) {
            if(current.getNext().getValue().getName().equals(levelName)) {
                current.setNext(current.getNext().getNext());
                return;
            }
            current = current.getNext();
        }
    }

    public void reset() {
        if (this.levels != null) {
            LinkedListNode<Level> current = this.levels.getNext();
            while (current != null) {
                current.getValue().reset();
                current = current.getNext();
            }
            this.levels.getValue().reset();
            this.currentLevel = levels.getValue();
        }
    }
}
