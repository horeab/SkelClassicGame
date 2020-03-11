package libgdx.implementations.resourcewars.spec.model;

public class PlayerInfo {

	private static final int STARTING_MAX_HEALTH = 100;
	public static final int MAX_THREAT = 100;

	private int healthContainerMax;
	private int health;
	private int threat;

	private int reputationIncreasedForThisWonMoney;
	private int reputation;

	public PlayerInfo() {
		this.healthContainerMax = STARTING_MAX_HEALTH;
		this.health = STARTING_MAX_HEALTH;
		reputation = 0;
		threat = 0;
	}

	public int getHealthContainerMax() {
		return healthContainerMax;
	}

	public void setHealthContainerMax(int healthContainerMax) {
		this.healthContainerMax = healthContainerMax;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		if (health > healthContainerMax) {
			health = healthContainerMax;
		}
		if (health < 0) {
			health = 0;
		}
		this.health = health;
	}

	public int getThreat() {
		return threat;
	}

	public void setThreat(int threat) {
		if (threat > 100) {
			threat = 100;
		}
		if (threat < 0) {
			threat = 0;
		}
		this.threat = threat;
	}

	public int getReputationIncreasedForThisWonMoney() {
		return reputationIncreasedForThisWonMoney;
	}

	public void setReputationIncreasedForThisWonMoney(int reputationIncreasedForThisWonMoney) {
		this.reputationIncreasedForThisWonMoney = reputationIncreasedForThisWonMoney;
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		if (reputation > 100) {
			reputation = 100;
		}
		if (reputation < 0) {
			reputation = 0;
		}
		this.reputation = reputation;
	}

}
