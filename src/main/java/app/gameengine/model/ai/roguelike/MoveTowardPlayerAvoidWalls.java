package app.gameengine.model.ai.roguelike;
import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.utils.PathfindingUtils;

public class MoveTowardPlayerAvoidWalls extends Decision {
    public MoveTowardPlayerAvoidWalls(Agent agent, String name) {
        super(agent, name);
    }

    @Override
    public boolean decide(double dt, Level level) {
        return false;
    }

    @Override
    public void doAction(double dt, Level level) {
        if(this.getAgent().getPath() == null) {
            this.getAgent().setPath(PathfindingUtils.findPathAvoidWalls(level, this.getAgent().getLocation(), level.getPlayer().getLocation()));
        }
        else {
            this.getAgent().followPath(dt);
        }
    }
}