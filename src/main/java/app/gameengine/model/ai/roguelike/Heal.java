package app.gameengine.model.ai.roguelike;
import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.utils.Timer;

public class Heal extends Decision {
    private int healAmount;
    private Timer timer;
    public Heal(Agent agent, String name, int healAmount, double cooldown) {
        super(agent, name);
        this.healAmount = healAmount;
        this.timer = new Timer(cooldown);
    }

    @Override
    public boolean decide(double dt, Level level) { return false; }

    @Override
    public void doAction(double dt, Level level) {
        this.getAgent().setVelocity(0,0);
        if(timer.tick(dt) > 0) {
            this.getAgent().setHP(this.getAgent().getHP() + this.healAmount);
        }
    }

}
