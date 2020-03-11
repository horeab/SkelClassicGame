package libgdx.implementations.resourcewars.spec.logic;

import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.Inventory;
import libgdx.implementations.resourcewars.spec.model.PlayerInfo;
import libgdx.implementations.resourcewars.spec.model.enums.WeaponType;

public class WeaponTransactionsManager {

	public void buyWeapon(WeaponType weaponType, CurrentGame currentGame) {
		int daysPassed = currentGame.getDaysPassed();
		Inventory inventory = currentGame.getMyInventory();
		PlayerInfo playerInfo = currentGame.getPlayerInfo();

		inventory.setBudget(inventory.getBudget() - weaponType.getCurrentPrice(daysPassed));
		playerInfo.setThreat(playerInfo.getThreat() - weaponType.getThreatReduce());
	}
}
