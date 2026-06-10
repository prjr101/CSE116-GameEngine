package app.games.topdownobjects;

import java.util.HashMap;

import app.display.common.SpriteLocation;
import app.gameengine.model.ai.DecisionTree;
import app.gameengine.model.ai.roguelike.MoveTowardPlayer;
import app.gameengine.model.ai.roguelike.ShootHomingProjectile;
import app.gameengine.model.ai.roguelike.ShootPlayer;
import app.gameengine.model.datastructures.BinaryTreeNode;

public class Sorcerer extends Enemy {

    public Sorcerer(double x, double y, int maxHP, int strength) {
        super(x, y, maxHP, strength);
        this.spriteSheetFilename = "MiniWorldSprites/Characters/Soldiers/Ranged/PurpleRanged/MagePurple.png";
        this.defaultSpriteLocation = new SpriteLocation(1, 0);
        this.animations = new HashMap<>();
        this.setDecisionTree(new DecisionTree(
                new BinaryTreeNode<>(new ShootHomingProjectile(this, "MagePurple", 1),
                        null,
                        null)));
    }

    public Sorcerer(double x, double y) {
        this(x, y, 100, 30);
    }

}
