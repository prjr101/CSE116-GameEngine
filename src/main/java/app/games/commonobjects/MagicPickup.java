package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.model.gameobjects.Collectible;
import app.gameengine.utils.Timer;

/**
 * A collectible which allows the player to throw damaging magic projectiles.
 * 
 * @see Collectible
 * @see PlayerMagicProjectile
 */
public class MagicPickup extends Collectible {
    private Timer timer;

    public MagicPickup(double x, double y, Game game) {
        super(x, y, game, "Magic");
        this.spriteSheetFilename = "MiniWorldSprites/Objects/BallistaBolt.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 0);
        this.timer = new Timer(.25);
    }

    public Timer getTimer() { return this.timer; }

    @Override
    public void use(Level level) {
        if(timer.check() >= 1) {
            level.getPlayer().fireProjectile(new PlayerMagicProjectile(0,0), 10, level);
        }
    }

    @Override
    public void update(double dt, Level level) {
        super.update(dt, level);
        this.timer.advance(dt);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
