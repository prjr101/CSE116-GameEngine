package app.gameengine.utils;

import app.gameengine.Level;
import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.model.gameobjects.GameObject;
import app.gameengine.model.physics.Vector2D;

import java.util.*;

public class PathfindingUtils {

    public static LinkedListNode<Vector2D> findPath(Vector2D start, Vector2D end) {
        Vector2D begin = Vector2D.floor(start);
        Vector2D finish = Vector2D.floor(end);
        LinkedListNode<Vector2D> path = new LinkedListNode<>(new Vector2D(begin.getX(), begin.getY()), null);
        LinkedListNode<Vector2D> current = path;
        // loop that subtracts x's and iterates by one
        if(begin.getX() >= finish.getX()) {
            while(begin.getX() != finish.getX()) {
                begin.setX(begin.getX() - 1);
                LinkedListNode<Vector2D> node = new LinkedListNode<>(new Vector2D(begin.getX(), begin.getY()), null);
                current.setNext(node);
                current = node;
            }
        }
        else if(begin.getX() <= finish.getX()) {
            while(begin.getX() != finish.getX()) {
                begin.setX(begin.getX() + 1);
                LinkedListNode<Vector2D> node = new LinkedListNode<>(new Vector2D(begin.getX(), begin.getY()), null);
                current.setNext(node);
                current = node;
            }
        }
        // loop that subtracts y's and iterates by one
        if(begin.getY() >= finish.getY()) {
            while(begin.getY() != finish.getY()) {
                begin.setY(begin.getY() - 1);
                LinkedListNode<Vector2D> node = new LinkedListNode<>(new Vector2D(begin.getX(), begin.getY()), null);
                current.setNext(node);
                current = node;
            }
        }
        else if(begin.getY() <= finish.getY()) {
            while(begin.getY() != finish.getY()) {
                begin.setY(begin.getY() + 1);
                LinkedListNode<Vector2D> node = new LinkedListNode<>(new Vector2D(begin.getX(), begin.getY()), null);
                current.setNext(node);
                current = node;
            }
        }
        return path;
    }

    public static LinkedListNode<Vector2D> findPathAvoidWalls(Level level, Vector2D start, Vector2D end) {
        Vector2D begin = Vector2D.floor(start);
        Vector2D finish = Vector2D.floor(end);
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
        if(!GameUtils.isInBounds(level, begin) || !GameUtils.isInBounds(level, finish) || blockedLocations.contains(begin) || blockedLocations.contains(finish)) {
            return null;
        }
        else {
            //queue
            Queue<Vector2D> queue = new LinkedList<>();
            //visited
            HashSet<Vector2D> visited = new HashSet<>();
            //parent map
            HashMap<Vector2D, Vector2D> parents = new HashMap<>();
            //bfs
            queue.add(begin);
            visited.add(begin);
            boolean endReached = false;
            while(!queue.isEmpty()) {
                Vector2D current = queue.poll();
                if(current.equals(finish)) {
                    endReached = true;
                    break;
                }
                else {
                    Vector2D above = new Vector2D(current.getX(), current.getY()-1);
                    if(!visited.contains(above) && GameUtils.isInBounds(level, above) && !blockedLocations.contains(above)) {
                        queue.add(above);
                        visited.add(above);
                        parents.put(above, current);
                    }
                    Vector2D below = new Vector2D(current.getX(), current.getY()+1);
                    if(!visited.contains(below) && GameUtils.isInBounds(level, below) && !blockedLocations.contains(below)) {
                        queue.add(below);
                        visited.add(below);
                        parents.put(below, current);
                    }
                    Vector2D left = new Vector2D(current.getX()-1, current.getY());
                    if(!visited.contains(left) && GameUtils.isInBounds(level, left) && !blockedLocations.contains(left)) {
                        queue.add(left);
                        visited.add(left);
                        parents.put(left, current);
                    }
                    Vector2D right = new Vector2D(current.getX()+1, current.getY());
                    if(!visited.contains(right) && GameUtils.isInBounds(level, right) && !blockedLocations.contains(right)) {
                        queue.add(right);
                        visited.add(right);
                        parents.put(right, current);
                    }
                }
            }
            //pathConstruction if end is found
            if(!endReached) {
                return null;
            }
            else {
                Vector2D current = finish;
                LinkedListNode<Vector2D> path = new LinkedListNode<>(current, null);
                while(!current.equals(begin)) {
                    current = parents.get(current);
                    path = new LinkedListNode<>(current, path);
                }
                return path;
            }
        }
    }
}
