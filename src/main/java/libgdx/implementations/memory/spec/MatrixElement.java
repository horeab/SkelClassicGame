package libgdx.implementations.memory.spec;

import java.io.Serializable;

public class MatrixElement implements Serializable {

	private static final long serialVersionUID = 704519348680349488L;

	private int item;
	private boolean showed;
	private boolean found;

	public MatrixElement(int item, boolean showed, boolean found) {
		super();
		this.setItem(item);
		this.showed = showed;
		this.setFound(found);
	}

	public boolean isShowed() {
		return showed;
	}

	public void setShowed(boolean showed) {
		this.showed = showed;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}
}
