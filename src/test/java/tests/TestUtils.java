package tests;

import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.model.physics.Vector2D;

import static org.junit.Assert.assertTrue;

public class TestUtils {
    public static void validatePath(LinkedListNode<Vector2D> path) {
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
}
