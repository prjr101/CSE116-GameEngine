package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A {@code StaticGameObject} that prevents collision by moving any
 * {@code DynamicGameObject}s that collide with it.
 */
public class Wall extends StaticGameObject {

    public Wall(double x, double y) {
        super(x, y);
        this.spriteSheetFilename = "MiniWorldSprites/Ground/Cliff.png";
        this.defaultSpriteLocation = new SpriteLocation(3, 0);
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        double left1 = this.getHitbox().getLocation().getX();
        double right1 = this.getHitbox().getDimensions().getX() + this.getHitbox().getLocation().getX();
        double top1 = this.getHitbox().getLocation().getY();
        double bottom1 = this.getHitbox().getDimensions().getY() + this.getHitbox().getLocation().getY();

        double left2 = otherObject.getHitbox().getLocation().getX();
        double right2 = otherObject.getHitbox().getDimensions().getX() + otherObject.getHitbox().getLocation().getX();
        double top2 = otherObject.getHitbox().getLocation().getY();
        double bottom2 = otherObject.getHitbox().getDimensions().getY() + otherObject.getHitbox().getLocation().getY();

        double overlapLeft = right2 - left1;
        double overlapRight = right1 - left2;
        double xoverlap = Math.min(overlapLeft, overlapRight);

        double overlapTop = bottom2 - top1;
        double overlapBottom = bottom1 - top2;
        double yoverlap = Math.min(overlapTop, overlapBottom);

        if (xoverlap < yoverlap) {

            double centerWallX = (left1 + right1) / 2.0;
            double centerOtherX = (left2 + right2) / 2.0;

            if (centerWallX < centerOtherX) {
                otherObject.setLocation(otherObject.getLocation().getX() + xoverlap,
                        otherObject.getLocation().getY());
            }
            else if (centerWallX > centerOtherX) {
                otherObject.setLocation(otherObject.getLocation().getX() - xoverlap,
                        otherObject.getLocation().getY());
            }
            else {
                // Horizontal complete overlap → deterministic: push right
                otherObject.setLocation(otherObject.getLocation().getX() + xoverlap,
                        otherObject.getLocation().getY());
            }

            otherObject.setVelocity(0, otherObject.getVelocity().getY());

        }
        else {

            double centerWallY = (top1 + bottom1) / 2.0;
            double centerOtherY = (top2 + bottom2) / 2.0;

            if (centerWallY < centerOtherY) {
                otherObject.setLocation(otherObject.getLocation().getX(),
                        otherObject.getLocation().getY() + yoverlap);
            }
            else if (centerWallY > centerOtherY) {
                otherObject.setLocation(otherObject.getLocation().getX(),
                        otherObject.getLocation().getY() - yoverlap);
            }
            else {
                // Vertical complete overlap → deterministic: push down
                otherObject.setLocation(otherObject.getLocation().getX(),
                        otherObject.getLocation().getY() + yoverlap);
            }

            otherObject.setVelocity(otherObject.getVelocity().getX(), 0);
        }
    }
}
