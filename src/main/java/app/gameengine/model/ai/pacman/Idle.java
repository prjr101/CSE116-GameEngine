package app.gameengine.model.ai.pacman;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.games.pacman.Ghost;

public class Idle extends Decision {
    private Ghost ghost;

    public Idle(Ghost ghost, String name) {
        super(ghost, name);
        this.ghost = ghost;
    }

    @Override
    public boolean decide(double dt, Level level) {
        return false;
    }

    @Override
    public void doAction(double dt, Level level) {
        if(this.ghost.getVelocity().magnitude() <= 0) {
            this.ghost.getOrientation().negate();
            this.ghost.setOrientation(this.ghost.getOrientation().getX(), this.ghost.getOrientation().getY());
            this.ghost.followPath((dt));
        }
    }
}
