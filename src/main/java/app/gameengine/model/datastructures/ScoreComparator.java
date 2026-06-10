package app.gameengine.model.datastructures;

import app.gameengine.statistics.GameStat;

public class ScoreComparator implements Comparator<GameStat> {
    @Override
    public boolean compare(GameStat a, GameStat b) {
        return a.getScore() > b.getScore();
    }
}
