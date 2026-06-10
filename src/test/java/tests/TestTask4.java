package tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import app.gameengine.model.ai.roguelike.*;
import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.model.gameobjects.*;
import app.gameengine.utils.PathfindingUtils;
import app.games.roguelikeobjects.*;
import app.games.topdownobjects.*;
import org.junit.Test;

import app.gameengine.Level;
import app.gameengine.LevelParser;
import app.gameengine.LinearGame;
import app.gameengine.model.ai.Decision;
import app.gameengine.model.physics.Vector2D;
import app.games.commonobjects.Potion;
import app.games.commonobjects.Spike;
import app.games.mario.MarioLevel;

import static org.junit.Assert.*;

public class TestTask4 {

    public static final double EPSILON = 1e-5;

    @Test
    public void testEnemiesHaveDecisionTrees() {
        Agent[] enemies = {
                new Minotaur(1, 1),
                new Demon(1, 1),
                new Archer(1, 1),
                new Sorcerer(1, 1)
        };

        for (Agent enemy : enemies) {
            assertNotNull(enemy.getDecisionTree());
            assertNotNull(enemy.getDecisionTree().getTree());
        }
    }


    @Test
    public void testShootPlayer() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Player p1 = game.getPlayer();
        Enemy e1 = new Archer(2, 2);
        e1.setDecisionTree(null);
        level.getDynamicObjects().addAll(Arrays.asList(p1, e1));
        game.loadLevel(level);
        p1.setLocation(1, 1);

        assertEquals(2, level.getDynamicObjects().size());
        e1.setVelocity(1, 1);
        Decision decision = new ShootPlayer(e1, "shoot Player", 1);

        decision.doAction(1.5, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(new Vector2D(-0.7071067811865475, -0.7071067811865475), e1.getOrientation());
        assertEquals(3, level.getDynamicObjects().size());
        assertTrue(level.getDynamicObjects().get(2) instanceof EnemyArrowProjectile);
        assertEquals(10, level.getDynamicObjects().get(2).getVelocity().magnitude(), EPSILON);

        p1.setLocation(2, 3);
        e1.setVelocity(5, 2);
        decision.doAction(1.5, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(new Vector2D(0, 1), e1.getOrientation());
        assertEquals(4, level.getDynamicObjects().size());
        assertTrue(level.getDynamicObjects().get(3) instanceof EnemyArrowProjectile);
        assertEquals(10, level.getDynamicObjects().get(3).getVelocity().magnitude(), EPSILON);
    }

    @Test
    public void testShootHomingProjectile() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Player p1 = game.getPlayer();
        Enemy e1 = new Sorcerer(2, 2);
        e1.setDecisionTree(null);
        level.getDynamicObjects().addAll(Arrays.asList(p1, e1));
        game.loadLevel(level);
        p1.setLocation(1, 1);

        assertEquals(2, level.getDynamicObjects().size());
        e1.setVelocity(1, 1);
        Decision decision = new ShootHomingProjectile(e1, "shoot homing", 0.9);

        decision.doAction(1, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(new Vector2D(-0.7071067811865475, -0.7071067811865475), e1.getOrientation());
        assertEquals(3, level.getDynamicObjects().size());
        assertTrue(level.getDynamicObjects().get(2) instanceof EnemyHomingProjectile);
        assertEquals(10, level.getDynamicObjects().get(2).getVelocity().magnitude(), EPSILON);

        p1.setLocation(2, 3);
        e1.setVelocity(5, 2);
        decision.doAction(1.5, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(new Vector2D(0, 1), e1.getOrientation());
        assertEquals(4, level.getDynamicObjects().size());
        assertTrue(level.getDynamicObjects().get(3) instanceof EnemyHomingProjectile);
        assertEquals(10, level.getDynamicObjects().get(3).getVelocity().magnitude(), EPSILON);
    }

    @Test
    public void testNearPlayer() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Player p1 = game.getPlayer();
        Enemy e1 = new Demon(1, 2);
        e1.setDecisionTree(null);
        level.getDynamicObjects().addAll(Arrays.asList(p1, e1));
        game.loadLevel(level);
        p1.setLocation(1, 1);

        Decision decision = new NearPlayer(e1, "near Player", 1);
        assertTrue(decision.decide(1, level));
        e1.setLocation(2, 2);
        assertFalse(decision.decide(1, level));
        e1.setLocation(1, 1);
        assertTrue(decision.decide(1, level));
        e1.setLocation(0.5, 0.5);
        assertTrue(decision.decide(1, level));
        e1.setLocation(-101, 1);
        assertFalse(decision.decide(1, level));

        p1.setLocation(5, 2);
        e1.setLocation(5, 2);
        decision = new NearPlayer(e1, "near Player", 0.5);
        assertTrue(decision.decide(1, level));
        e1.setLocation(5.5, 2);
        assertTrue(decision.decide(1, level));
        e1.setLocation(1, 1);
        assertFalse(decision.decide(1, level));
        e1.setLocation(0.5, 0.5);
        assertFalse(decision.decide(1, level));
        e1.setLocation(-101, 1);
        assertFalse(decision.decide(1, level));
    }

    @Test
    public void testLowHP() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Enemy e1 = new Minotaur(1, 2);
        e1.setDecisionTree(null);
        level.getDynamicObjects().add(e1);
        game.loadLevel(level);

        Decision decision = new LowHP(e1, "lowHP", 2);
        assertFalse(decision.decide(1, level));
        e1.setHP(1);
        assertTrue(decision.decide(1, level));
        e1.setHP(0);
        assertTrue(decision.decide(1, level));
        e1.setHP(-1);
        assertTrue(decision.decide(1, level));
        e1.setHP(2);
        assertTrue(decision.decide(1, level));
        e1.setHP(3);
        assertFalse(decision.decide(1, level));

        e1 = new Minotaur(1, 2);
        decision = new LowHP(e1, "lowHP", 8);
        assertFalse(decision.decide(1, level));
        e1.setHP(1);
        assertTrue(decision.decide(1, level));
        e1.setHP(0);
        assertTrue(decision.decide(1, level));
        e1.setHP(-1);
        assertTrue(decision.decide(1, level));
        e1.setHP(2);
        assertTrue(decision.decide(1, level));
        e1.setHP(6);
        assertTrue(decision.decide(1, level));
        e1.setHP(8);
        assertTrue(decision.decide(1, level));
        e1.setHP(9);
        assertFalse(decision.decide(1, level));
    }

    @Test
    public void testHeal() {
        LinearGame game = new LinearGame();
        TopDownLevel level = new TopDownLevel(game, 5, 5, "level");
        Enemy e1 = new Demon(1, 2, 10, 10);
        e1.setDecisionTree(null);
        level.getDynamicObjects().add(e1);
        game.loadLevel(level);

        e1.setVelocity(1, 1);
        assertEquals(new Vector2D(1, 1), e1.getVelocity());
        Decision decision = new Heal(e1, "heal", 2, 1);
        e1.setHP(1);
        assertEquals(1, e1.getHP());
        decision.doAction(1.5, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(3, e1.getHP());
        decision.doAction(1.5, level);
        assertEquals(5, e1.getHP());
        decision.doAction(1.5, level);
        assertEquals(7, e1.getHP());

        decision = new Heal(e1, "heal", 5, 1);
        e1.setHP(1);
        assertEquals(1, e1.getHP());
        decision.doAction(1.5, level);
        assertEquals(new Vector2D(0, 0), e1.getVelocity());
        assertEquals(6, e1.getHP());
        decision.doAction(1.5, level);
        assertEquals(10, e1.getHP());
    }

    @Test
    public void testSpike() {
        Spike spike = new Spike(1, 1);
        Player player = new Player(1, 1, 10);
        assertFalse(player.isDestroyed());
        spike.collideWithDynamicObject(player);
        assertTrue(player.isDestroyed());

        Demon demon = new Demon(1, 1);
        assertFalse(demon.isDestroyed());
        spike.collideWithDynamicObject(demon);
        assertFalse(demon.isDestroyed());
    }

    @Test
    public void testPotion() {
        Potion potion = new Potion(1, 1, 5);
        Player player = new Player(1, 1, 10);
        player.setHP(5);
        assertEquals(5, player.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());
        potion.collideWithDynamicObject(player);
        assertEquals(10, player.getHP(), EPSILON);
        assertTrue(potion.isDestroyed());

        potion = new Potion(1, 1, -5);
        player.setHP(5);
        assertEquals(5, player.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());
        potion.collideWithDynamicObject(player);
        assertEquals(0, player.getHP(), EPSILON);
        assertTrue(potion.isDestroyed());

        potion = new Potion(1, 1, 5);
        Demon demon = new Demon(1, 1);
        demon.setHP(5);
        assertEquals(5, demon.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());
        potion.collideWithDynamicObject(demon);
        assertEquals(5, demon.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());

        potion = new Potion(1, 1, -5);
        demon.setHP(5);
        assertEquals(5, demon.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());
        potion.collideWithDynamicObject(demon);
        assertEquals(5, demon.getHP(), EPSILON);
        assertFalse(potion.isDestroyed());
    }

    @Test
    public void testReadStaticObject() {
        RoguelikeGame game = new RoguelikeGame();
        RoguelikeLevel level = new RoguelikeLevel(game, 100, 100, "test level");
        StaticGameObject object = LevelParser.readStaticObject(game, level,
                new ArrayList<>(Arrays.asList("StaticGameObject", "DirectionalWall", "17", "12")));
        assertNotNull(object);
        assertTrue(object instanceof DirectionalWall);
        assertEquals(new Vector2D(17, 12), object.getLocation());

        object = LevelParser.readStaticObject(game, level,
                new ArrayList<>(Arrays.asList("StaticGameObject", "Potion", "15", "2", "2")));
        assertNotNull(object);
        assertTrue(object instanceof Potion);
        assertEquals(new Vector2D(15, 2), object.getLocation());
        assertEquals(2, ((Potion) object).getHealAmount());

        object = LevelParser.readStaticObject(game, level,
                new ArrayList<>(Arrays.asList("StaticGameObject", "Spike", "8", "4")));
        assertNotNull(object);
        assertTrue(object instanceof Spike);
        assertEquals(new Vector2D(8, 4), object.getLocation());

        object = LevelParser.readStaticObject(game, level,
                new ArrayList<>(Arrays.asList("StaticGameObject", "Marker", "2", "5", "Door")));
        assertNotNull(object);
        assertTrue(object instanceof Marker);
        assertEquals(new Vector2D(2, 5), object.getLocation());
        assertEquals("Door", ((Marker) object).getMarkerID());
    }

    @Test
    public void testReadDynamicObject() {
        RoguelikeGame game = new RoguelikeGame();
        RoguelikeLevel level = new RoguelikeLevel(game, 100, 100, "test level");
        DynamicGameObject object = LevelParser.readDynamicObject(game, level,
                new ArrayList<>(Arrays.asList("DynamicGameObject", "Minotaur", "1", "2", "3", "4")));
        assertNotNull(object);
        assertTrue(object instanceof Minotaur);
        assertEquals(new Vector2D(1, 2), object.getLocation());
        assertEquals(3, object.getMaxHP());
        assertEquals(4, ((Enemy) object).getStrength());

        object = LevelParser.readDynamicObject(game, level,
                new ArrayList<>(Arrays.asList("DynamicGameObject", "Archer", "5", "6", "7", "8")));
        assertNotNull(object);
        assertTrue(object instanceof Archer);
        assertEquals(new Vector2D(5, 6), object.getLocation());
        assertEquals(7, object.getMaxHP());
        assertEquals(8, ((Enemy) object).getStrength());

        object = LevelParser.readDynamicObject(game, level,
                new ArrayList<>(Arrays.asList("DynamicGameObject", "Sorcerer", "9", "10", "11", "12")));
        assertNotNull(object);
        assertTrue(object instanceof Sorcerer);
        assertEquals(new Vector2D(9, 10), object.getLocation());
        assertEquals(11, object.getMaxHP());
        assertEquals(12, ((Enemy) object).getStrength());
    }

    @Test
    public void testParseRoguelikeLevel() {
        RoguelikeGame game = new RoguelikeGame();
        Level level = LevelParser.parseLevel(game, "testing/roguelike.csv");
        assertNotNull(level);
        assertTrue(level instanceof RoguelikeLevel);

        level = LevelParser.parseLevel(game, "testing/mario1.csv");
        assertNotNull(level);
        assertTrue(level instanceof MarioLevel);

        level = LevelParser.parseLevel(game, "testing/medium.csv");
        assertNotNull(level);
        assertTrue(level instanceof TopDownLevel);
    }

    @Test
    public void testFindPathAvoidWallsShortestPath() {
        RoguelikeGame game = new RoguelikeGame();
        Level level = LevelParser.parseLevel(game, "testing/roguelike.csv");
        Demon demon = (Demon) level.getDynamicObjects().getFirst();
        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, demon.getLocation(), level.getPlayerStartLocation());
        if (path == null) {
            assertNull(path);
        }
        double correctStartX = 1;
        double correctStartY = 1;
        double correctEndX = 2;
        double correctEndY = 2;
        double endx = 0;
        double endy = 0;
        while (path != null) {
            endx = path.getValue().getX();
            endy = path.getValue().getY();
            path = path.getNext();
        }
        assertEquals(correctStartX, demon.getLocation().getX(), .00001);
        assertEquals(correctStartY, demon.getLocation().getY(), .00001);
        assertEquals(correctEndX, endx, .00001);
        assertEquals(correctEndY, endy, .00001);
    }

    @Test
    public void testFindPathAvoidWallsStartEndSame() {
        RoguelikeGame game = new RoguelikeGame();
        Level level = LevelParser.parseLevel(game, "testing/roguelike.csv");

        Vector2D start = new Vector2D(1,1);
        LinkedListNode<Vector2D> path =
                PathfindingUtils.findPathAvoidWalls(level, start, start);
        if (path != null) {
            assertEquals(start.getX(), path.getValue().getX(), .00001);
            assertEquals(start.getY(), path.getValue().getY(), .00001);
            assertNull(path.getNext());
        }
    }

    @Test
    public void testFindPathAvoidWallsNullPath() {
        RoguelikeGame game = new RoguelikeGame();
        Level level = LevelParser.parseLevel(game, "testing/roguelike.csv");
        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, new Vector2D(30,40), level.getPlayerStartLocation());
        assertNull(path);
    }

    @Test
    public void testFindPathAvoidWallsLongPath() {
        RoguelikeGame game = new RoguelikeGame();
        Level level = LevelParser.parseLevel(game, "testing/roguelike.csv");
        Demon demon = (Demon) level.getDynamicObjects().getFirst();
        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, demon.getLocation(), new Vector2D(10,1));
        double correctStartX = 1;
        double correctStartY = 1;
        double correctEndX = 10;
        double correctEndY = 1;
        double endx = 0;
        double endy = 0;
        while (path != null) {
            endx = path.getValue().getX();
            endy = path.getValue().getY();
            path = path.getNext();
        }
        assertEquals(correctStartX, demon.getLocation().getX(), .00001);
        assertEquals(correctStartY, demon.getLocation().getY(), .00001);
        assertEquals(correctEndX, endx, .00001);
        assertEquals(correctEndY, endy, .00001);
    }

    @Test
    public void testFindPathAvoidWallsOneTile() {
        RoguelikeGame game = new RoguelikeGame();
        Level level = LevelParser.parseLevel(game, "testing/roguelike.csv");
        Vector2D start = new Vector2D(2,2);
        Vector2D[] targets = {
                new Vector2D(3,2),
                new Vector2D(1,2),
                new Vector2D(2,3),
                new Vector2D(2,1)
        };

        for (Vector2D end : targets) {
            LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, start, end);
            if (path != null && path.getNext() != null) {
                Vector2D first = path.getValue();
                Vector2D second = path.getNext().getValue();
                assertEquals(start.getX(), first.getX(), .00001);
                assertEquals(start.getY(), first.getY(), .00001);
                assertEquals(end.getX(), second.getX(), .00001);
                assertEquals(end.getY(), second.getY(), .00001);
            }
        }
    }

    @Test
    public void testFindPathAvoidWallsValidPath() {
        RoguelikeGame game = new RoguelikeGame();
        Level level = LevelParser.parseLevel(game, "testing/roguelike.csv");
        Demon demon = (Demon) level.getDynamicObjects().getFirst();
        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, demon.getLocation(), level.getPlayerStartLocation());
        if (path == null) {
            assertTrue(path==null);
        }
        else {
            Vector2D vector = path.getValue();
            while(path.getNext() != null) {
                path = path.getNext();
                assertTrue((Math.abs(vector.getX()-path.getValue().getX())==1 && Math.abs(vector.getY()-path.getValue().getY())==0) || (Math.abs(vector.getX()-path.getValue().getX())==0 && Math.abs(vector.getY()-path.getValue().getY())==1));
                vector = path.getValue();
            }
        }
    }

    @Test
    public void testFindPathAvoidWallsAvoidsWalls() {
        RoguelikeGame game = new RoguelikeGame();
        Level level = LevelParser.parseLevel(game, "testing/roguelike.csv");

        Demon demon = (Demon) level.getDynamicObjects().getFirst();

        LinkedListNode<Vector2D> path =
                PathfindingUtils.findPathAvoidWalls(level,
                        demon.getLocation(),
                        new Vector2D(10,1));
        HashSet<Vector2D> blockedLocations = new HashSet<>();
        for(GameObject object : level.getDynamicObjects()) {
            if(object.isSolid()) {
                blockedLocations.add(Vector2D.floor(object.getLocation()));
            }
        }
        for(GameObject object : level.getStaticObjects()) {
            if(object.isSolid()) {
                blockedLocations.add(Vector2D.floor(object.getLocation()));
            }
        }
        while (path != null) {
            Vector2D pos = path.getValue();
            assertFalse(blockedLocations.contains(pos));
            path = path.getNext();
        }
    }

    @Test
    public void testFindPathAvoidWalls1() {
        RoguelikeGame game = new RoguelikeGame();
        DynamicGameObject obj = new Tower(1, 2);
        DynamicGameObject obj2 = new Tower(2, 1);
        Level level = new RoguelikeLevel(game, 10, 10, "test");
        level.getDynamicObjects().add(obj);
        level.getDynamicObjects().add(obj2);
        Vector2D start = new Vector2D(1,2);
        Vector2D end = new Vector2D(2,1);
        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, start, end);
        assertNull(path);
    }

    @Test
    public void testFindPathAvoidWalls2() {
        RoguelikeGame game = new RoguelikeGame();
        DynamicGameObject obj = new Tower(1, 0);
        DynamicGameObject obj2 = new Tower(0, 1);
        Level level = new RoguelikeLevel(game, 10, 10, "test");
        level.getDynamicObjects().add(obj);
        level.getDynamicObjects().add(obj2);
        Vector2D start = new Vector2D(5,5);
        Vector2D end = new Vector2D(5,6);
        LinkedListNode<Vector2D> path = PathfindingUtils.findPathAvoidWalls(level, start, end);
        assertNotNull(path);
    }
}