package app.gameengine.statistics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import app.gameengine.model.datastructures.*;

/**
 * A scoreboard for sorting scores of specific games.
 */
public class Scoreboard {

    private static String STATS_DIRECTORY = "data/stats/";
    private static String STATS_POSTFIX = "_stats.csv";

    private String statsPath;
    private BinaryTreeNode<GameStat> root;
    private Comparator<GameStat> comparator;
    private boolean loaded = false;

    public Scoreboard(String gameName, Comparator<GameStat> comparator) {
        this.statsPath = STATS_DIRECTORY + gameName.toLowerCase() + STATS_POSTFIX;
        this.comparator = comparator;
    }

    public void setComparator(Comparator<GameStat> comparator) {
        this.comparator = comparator;
    }

    public Comparator<GameStat> getComparator() {
        return comparator;
    }

    public void addScore(GameStat score) {
        if (this.root == null) {
            this.root = new BinaryTreeNode<>(score, null, null);
        }
        else {
            this.addScoreHelper(this.root, score);
        }
    }

    public void addScoreHelper(BinaryTreeNode<GameStat> root, GameStat score) {
        if(this.comparator.compare(score, root.getValue())) {
            if(root.getLeft() == null) {
                root.setLeft(new BinaryTreeNode<>(score, null, null));
            }
            else {
                this.addScoreHelper(root.getLeft(), score);
            }
        }
        else {
            if(root.getRight() == null) {
                root.setRight(new BinaryTreeNode<>(score, null, null));
            }
            else {
                this.addScoreHelper(root.getRight(), score);
            }
        }
    }

    public LinkedListNode<GameStat> getScoreList() {
        if (this.root == null) {
            return null;
        }
        else {
            return inOrderTraversal(this.root);
        }
    }

    public LinkedListNode<GameStat> getScoreList(BinaryTreeNode<GameStat> root) {
        if(root == null) {
            return null;
        }
        else {
            return inOrderTraversal(root);
        }
    }

    public LinkedListNode<GameStat> inOrderTraversal(BinaryTreeNode<GameStat> root) {
        if (root == null) {
            return null;
        }
        LinkedListNode<GameStat> scoreList = new LinkedListNode<>(root.getValue(), null);
        if (root.getLeft() != null) {
            LinkedListNode<GameStat> leftList = inOrderTraversal(root.getLeft());
            LinkedListNode<GameStat> temp = leftList;
            while (temp.getNext() != null) {
                temp = temp.getNext();
            }
            temp.setNext(scoreList);
            scoreList = leftList;
        }
        if (root.getRight() != null) {
            LinkedListNode<GameStat> rightList = inOrderTraversal(root.getRight());
            LinkedListNode<GameStat> temp = scoreList;
            while (temp != null && temp.getNext() != null) {
                temp = temp.getNext();
            }
            if (temp != null) {
                temp.setNext(rightList);
            } else {
                scoreList = rightList;
            }
        }
        return scoreList;
    }

    public BinaryTreeNode<GameStat> getScoreTree() { return this.root; }

    public void setScoreTree(BinaryTreeNode<GameStat> scoreTree) { this.root = scoreTree; }

    public void loadStats() {
        if (loaded) return;
        loaded = true;
        ArrayList<String> lines = null;
        try {
            lines = new ArrayList<>(Files.readAllLines(Paths.get(statsPath)));
            for(String line : lines) {
                ArrayList<String> splits = new ArrayList<>(Arrays.asList(line.split(",")));
                GameStat stat = new GameStat(splits.get(0), Double.parseDouble(splits.get(1)), Double.parseDouble(splits.get(2)));
                addScore(stat);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveStats() {
        this.loadStats();
        if (this.root == null) {
            return;
        }
        System.out.println("** Saving statistics to " + this.statsPath + " **");
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.statsPath))) {
            for (LinkedListNode<GameStat> stats = this.getScoreList(); stats != null; stats = stats.getNext()) {
                writer.write(stats.getValue().toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("** Could not save statistics to " + this.statsPath + " **");
            e.printStackTrace();
        }
    }

}
