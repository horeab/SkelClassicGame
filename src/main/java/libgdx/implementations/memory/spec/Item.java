package libgdx.implementations.memory.spec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

	private int itemIndex;
	private String itemName;
	private List<String> itemValues = new ArrayList<String>();


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List<String> getItemValues() {
		return itemValues;
	}

	public void setItemValues(List<String> itemValues) {
		this.itemValues = itemValues;
	}

	public int getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}

	public String getItemName() {
		return itemName;
	}
}
