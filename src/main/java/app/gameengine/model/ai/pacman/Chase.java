package app.gameengine.model.ai.pacman;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.PacmanUtils;
import app.games.pacman.Ghost;
import app.games.pacman.PacmanGame;

import java.util.ArrayList;

public class Chase extends Decision {
    private Ghost ghost;
    private PacmanGame game;
    private Vector2D location = null;

    public Chase(Ghost ghost, PacmanGame game, String name) {
        super(ghost, name);
        this.ghost = ghost;
        this.game = game;
    }

    @Override
    public boolean decide(double dt, Level level) {
        return this.ghost.getState().equals("Chase");
    }

    @Override
    public void doAction(double dt, Level level) {
        if (this.location == null) {
            this.location = this.ghost.getLocation();
        }
        Vector2D current = this.ghost.getLocation();
        if (PacmanUtils.canAct(this.ghost, dt, this.location)) {
            Vector2D target = PacmanUtils.getChaseTarget(this.game.getCurrentLevel(), this.ghost);
            ArrayList<Vector2D> validDirs = PacmanUtils.getValidDirs(this.game.getCurrentLevel(), this.ghost);
            Vector2D bestDir = PacmanUtils.getBestDirection(validDirs, current, target);
            if (bestDir != null) {
                this.ghost.setOrientation(bestDir.getX(), bestDir.getY());
                this.location = current;
            }
        }
        this.ghost.followPath(dt);
    }
}
