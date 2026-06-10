package app.gameengine.model.ai.roguelike;
import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.Timer;
import app.games.topdownobjects.EnemyArrowProjectile;

public class ShootPlayer extends Decision {
    private Timer timer;

    public ShootPlayer(Agent agent, String name, double cooldown) {
        super(agent, name);
        this.timer = new Timer(cooldown);
    }

    @Override
    public boolean decide(double dt, Level level) {
        return false;
    }

    @Override
    public void doAction(double dt, Level level) {
        this.getAgent().setVelocity(0,0);
        if(timer.tick(dt) > 0) {
            Vector2D diff = Vector2D.sub(level.getPlayer().getLocation(), this.getAgent().getLocation());
            diff.normalize();
            this.getAgent().setOrientation(diff.getX(), diff.getY());
            this.getAgent().fireProjectile(new EnemyArrowProjectile(this.getAgent().getLocation().getX(), this.getAgent().getLocation().getY()), 10, level);
        }
    }
}
