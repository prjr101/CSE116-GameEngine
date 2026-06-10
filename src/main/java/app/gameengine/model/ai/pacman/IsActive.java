package app.gameengine.model.ai.pacman;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.games.pacman.Ghost;

public class IsActive extends Decision {
    private Ghost ghost;

    public IsActive(Ghost ghost, String name) {
        super(ghost, name);
        this.ghost = ghost;
    }

    @Override
    public boolean decide(double dt, Level level) {
        return ghost.getState().equals("Chase") || ghost.getState().equals("Scatter");
    }

    @Override
    public void doAction(double dt, Level level) {}


}
