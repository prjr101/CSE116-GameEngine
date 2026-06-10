package app.gameengine.model.ai.roguelike;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;

public class LowHP extends Decision {
    private int HPthreshold;

    public LowHP(Agent agent, String name, int HPthreshold) {
        super(agent, name);
        this.HPthreshold = HPthreshold;
    }

    @Override
    public void doAction(double dt, Level level) {}

    @Override
    public boolean decide(double dt, Level level) {
        return this.getAgent().getHP() <= this.HPthreshold;
    }
}
