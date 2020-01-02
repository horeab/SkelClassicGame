package libgdx.implementations.balloon.model;


import org.apache.commons.lang3.tuple.MutablePair;

public class CurrentMove {

    private PlayerPosition playerPosition = new PlayerPosition();

    private MovementFinishedInfo movementFinishedInfo = new MovementFinishedInfo();

    private Integer clickedColumn;

    private MutablePair<Integer, Integer> currentRandomTornadoNextPosition;

    private boolean tornadoAlreadyAccessed;

    private boolean movementStopped = true;

    public Integer getClickedColumn() {
        return clickedColumn;
    }

    public void setClickedColumn(Integer clickedColumn) {
        this.clickedColumn = clickedColumn;
    }

    public PlayerPosition getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(PlayerPosition playerPosition) {
        this.playerPosition = playerPosition;
    }

    public MovementFinishedInfo getMovementFinishedInfo() {
        return movementFinishedInfo;
    }

    public void setMovementFinishedInfo(MovementFinishedInfo movementFinishedInfo) {
        this.movementFinishedInfo = movementFinishedInfo;
    }

    public MutablePair<Integer, Integer> getCurrentRandomTornadoNextPosition() {
        return currentRandomTornadoNextPosition;
    }

    public void setCurrentRandomTornadoNextPosition(MutablePair<Integer, Integer> currentRandomTornadoNextPosition) {
        this.currentRandomTornadoNextPosition = currentRandomTornadoNextPosition;
    }

    public boolean isTornadoAlreadyAccessed() {
        return tornadoAlreadyAccessed;
    }

    public void setTornadoAlreadyAccessed(boolean tornadoAlreadyAccessed) {
        this.tornadoAlreadyAccessed = tornadoAlreadyAccessed;
    }

    public boolean isMovementStopped() {
        return movementStopped;
    }

    public void setMovementStopped(boolean movementStopped) {
        this.movementStopped = movementStopped;
    }
}
