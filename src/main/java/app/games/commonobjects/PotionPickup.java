package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.model.gameobjects.Collectible;

/**
 * A single-use collectible which allows the player to heal some amount of
 * damage.
 * 
 * @see Collectible
 */
public class PotionPickup extends Collectible {
    private int heal;

    public PotionPickup(double x, double y, int heal, Game game) {
        super(x, y, game, "Health Potion");
        this.spriteSheetFilename = "MiniWorldSprites/User Interface/Icons-Essentials.png";
        this.defaultSpriteLocation = new SpriteLocation(3, 1);
        this.heal = heal;
    }

    public int getHealAmount() {
        return this.heal;
    }

    @Override
    public void use(Level level) {
        level.getPlayer().setHP(level.getPlayer().getHP()+this.heal);
        level.getPlayer().removeActiveItem();
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
