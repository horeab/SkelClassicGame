package libgdx.implementations.memory.spec;

import java.io.Serializable;

public class MatrixChoice implements Serializable {

	private int x;
	private int y;
	private int item;
	private int index;

	public MatrixChoice(int x, int y, int item, int index) {
		super();
		this.setItem(item);
		this.x = x;
		this.index = index;
		this.y = y;
	}

	public int getIndex() {
		return index;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public boolean equals(MatrixChoice matrixChoice) {
		if (matrixChoice != null) {
			return this.x == matrixChoice.getX() && this.y == matrixChoice.getY();
		} else {
			return false;
		}
	}

}
