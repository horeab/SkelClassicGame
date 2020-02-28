package libgdx.implementations.memory.spec;

import java.io.Serializable;
import java.util.List;

public class Level implements Serializable {

	private static final long serialVersionUID = -1860692465170832450L;

	private int rows;
	private int cols;
	private int levelNr;
	private List<Item> availableItems;

	public Level(int levelNr,  int rows, int cols, List<Item> availableItems) {
		super();
		this.setLevelNr(levelNr);
		this.rows = rows;
		this.cols = cols;
		this.availableItems = availableItems;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public List<Item> getAvailableItems() {
		return availableItems;
	}

	public void setAvailableItems(List<Item> availableItems) {
		this.availableItems = availableItems;
	}

	public int getLevelNr() {
		return levelNr;
	}

	public void setLevelNr(int levelNr) {
		this.levelNr = levelNr;
	}

}
