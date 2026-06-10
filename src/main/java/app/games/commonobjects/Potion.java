package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A static object that heals the player on collision.
 * 
 * @see StaticGameObject
 */
public class Potion extends StaticGameObject {
    private int heal;

    public Potion(double x, double y, int heal) {
        super(x, y);
        this.spriteSheetFilename = "MiniWorldSprites/User Interface/Icons-Essentials.png";
        this.heal = heal;
        if (heal >= 0) {
            this.defaultSpriteLocation = new SpriteLocation(0, 1);
        } else {
            this.defaultSpriteLocation = new SpriteLocation(1, 1);
        }
    }

    public int getHealAmount() { return this.heal; }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        if(otherObject.isPlayer()) {
            otherObject.setHP(otherObject.getHP() + this.heal);
            this.destroy();
        }
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
