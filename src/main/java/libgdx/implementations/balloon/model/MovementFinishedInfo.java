package libgdx.implementations.balloon.model;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovementFinishedInfo {

    private MutablePair<Integer, Integer> finalPosition;
    private int nrOfMoves;
    private boolean destroyed;
    private int score;

    private Set<MutablePair<Integer, Integer>> cellsToUpdate = new HashSet<MutablePair<Integer, Integer>>();

    private List<Integer> soundsToPlayInOrder = new ArrayList<Integer>();

    public MutablePair<Integer, Integer> getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(MutablePair<Integer, Integer> finalPosition) {
        this.finalPosition = finalPosition;
    }

    public int getNrOfMoves() {
        return nrOfMoves;
    }

    public void setNrOfMoves(int nrOfMoves) {
        this.nrOfMoves = nrOfMoves;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public Set<MutablePair<Integer, Integer>> getCellsToUpdate() {
        return cellsToUpdate;
    }

    public void addCellToUpdate(MutablePair<Integer, Integer> cellToUpdate) {
        this.cellsToUpdate.add(new MutablePair<Integer, Integer>(cellToUpdate.getLeft(), cellToUpdate.getRight()));
    }

    public void addCellsToUpdate(Set<MutablePair<Integer, Integer>> cellsToUpdate) {
        for (MutablePair<Integer, Integer> point : cellsToUpdate) {
            addCellToUpdate(point);
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Integer> getSoundsToPlayInOrder() {
        return soundsToPlayInOrder;
    }

    public void setSoundsToPlayInOrder(List<Integer> soundsToPlayInOrder) {
        this.soundsToPlayInOrder = soundsToPlayInOrder;
    }
}
