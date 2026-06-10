package app.gameengine.model.ai.roguelike;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.model.physics.Vector2D;

public class NearPlayer extends Decision {
    private double distanceThreshold;

    public NearPlayer(Agent agent, String name, double distanceThreshold) {
        super(agent, name);
        this.distanceThreshold = distanceThreshold;
    }

    @Override
    public void doAction(double dt, Level level) {}

    @Override
    public boolean decide(double dt, Level level) {
        return Vector2D.euclideanDistance(this.getAgent().getLocation(), level.getPlayer().getLocation()) <= this.distanceThreshold;
    }
}
