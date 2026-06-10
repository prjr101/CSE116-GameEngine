package tests;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;

public class TestDecision extends Decision {
    private boolean traversalDirection;
    private boolean hasBeenUsed = false;

    public TestDecision(Agent agent, String string, boolean traversalDirection) {
        super(agent, string);
        this.traversalDirection = traversalDirection;
    }

    public boolean isUsed() {
        return this.hasBeenUsed;
    }

    @Override
    public boolean decide(double dt, Level level) {
        return this.traversalDirection;
    }

    @Override
    public void doAction(double dt, Level level) {
        this.hasBeenUsed = true;
    }
}
