package libgdx.implementations.balloon.model;


public class PlayerPosition {

	private CoordinateInfo upValue;
	private CoordinateInfo rightValue;
	private CoordinateInfo leftValue;
	private CoordinateInfo currentPosition;

	public CoordinateInfo getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(CoordinateInfo currentPosition) {
		this.currentPosition = currentPosition;
	}

	public CoordinateInfo getUpValue() {
		return upValue;
	}

	public void setUpValue(CoordinateInfo upValue) {
		this.upValue = upValue;
	}

	public CoordinateInfo getRightValue() {
		return rightValue;
	}

	public void setRightValue(CoordinateInfo rightValue) {
		this.rightValue = rightValue;
	}

	public CoordinateInfo getLeftValue() {
		return leftValue;
	}

	public void setLeftValue(CoordinateInfo leftValue) {
		this.leftValue = leftValue;
	}

	public PlayerPosition copy() {
		PlayerPosition playerPosition = new PlayerPosition();
		playerPosition.setCurrentPosition(currentPosition);
		playerPosition.setLeftValue(leftValue);
		playerPosition.setRightValue(rightValue);
		playerPosition.setUpValue(upValue);
		return playerPosition;
	}

}
