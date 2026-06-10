package app.gameengine.model.ai.pacman;

import app.gameengine.Level;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.PacmanUtils;
import app.games.pacman.Ghost;
import app.games.pacman.PacmanGame;

import java.util.ArrayList;

public class Scatter extends Decision {
    private Ghost ghost;
    private PacmanGame game;
    private Vector2D location = null;

    public Scatter(Ghost ghost, PacmanGame game, String name) {
        super(ghost, name);
        this.ghost = ghost;
        this.game = game;
    }

    @Override
    public boolean decide(double dt, Level level){
        return false;
    }

    @Override
    public void doAction(double dt, Level level) {
        if (PacmanUtils.canAct(this.ghost, dt, this.location)) {
            this.location = this.ghost.getLocation();
            Vector2D target = PacmanUtils.getScatterTarget(this.game.getCurrentLevel(), this.ghost);
            ArrayList<Vector2D> validDirs = PacmanUtils.getValidDirs(this.game.getCurrentLevel(), this.ghost);
            Vector2D bestDir = PacmanUtils.getBestDirection(validDirs, this.location, target);
            if (bestDir != null) {
                this.ghost.setOrientation(bestDir.getX(), bestDir.getY());
                this.location = Vector2D.round(bestDir);
            }
        }
        this.ghost.followPath((dt));
    }
}
