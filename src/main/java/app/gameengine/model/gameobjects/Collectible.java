package app.gameengine.model.gameobjects;

import app.gameengine.Game;
import app.gameengine.Level;

/**
 * A static object which is added to the player's inventory on collision.
 * 
 * @see StaticGameObject
 * @see Player
 */
public abstract class Collectible extends StaticGameObject {
    private Game game;
    private String itemID;

    public Collectible(double x, double y, Game game, String itemID) {
        super(x, y);
        this.game = game;
        this.itemID = itemID;
    }

    public abstract void use(Level level);

    @Override
    public void collideWithDynamicObject(DynamicGameObject object) {
        if(object.isPlayer()) {
            this.game.getPlayer().addInventoryItem(this);
            this.destroy();
        }
    }

    public String getItemID() {
        return itemID;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
