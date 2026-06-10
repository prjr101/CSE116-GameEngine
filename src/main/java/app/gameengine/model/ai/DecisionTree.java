package app.gameengine.model.ai;

import app.gameengine.Level;
import app.gameengine.model.datastructures.BinaryTreeNode;

public class DecisionTree {
    private BinaryTreeNode<Decision> decision;

    public DecisionTree(BinaryTreeNode<Decision> decision) {
        this.decision = decision;
    }

    public BinaryTreeNode<Decision> getTree() { return this.decision; }

    public void setTree(BinaryTreeNode<Decision> decision) { this.decision = decision; }

    public Decision traverse(BinaryTreeNode<Decision> decisionTree, double dt, Level level) {
        Decision decision = null;
        if(decisionTree == null) {
            return null;
        }
        else if(decisionTree.getLeft() == null && decisionTree.getRight() == null) {
            decision = decisionTree.getValue();
        }
        else {
            if(decisionTree.getValue().decide(dt, level)) {
                if(decisionTree.getRight() == null) {
                    return null;
                }
                return traverse(decisionTree.getRight(), dt, level);
            }
            else {
                if(decisionTree.getLeft() == null) {
                    return null;
                }
                return traverse(decisionTree.getLeft(), dt, level);
            }
        }
        return decision;
    }

    public void traverse(double dt,  Level level) {
        Decision action = traverse(this.decision, dt, level);
        if (action != null) {
            action.doAction(dt, level);
        }
    }
}
