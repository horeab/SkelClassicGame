package libgdx.implementations.resourcewars.spec.model;

public class PlayerInfo {

    private int reputation;

    public PlayerInfo() {
        reputation = 0;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        if (reputation > 100) {
            reputation = 100;
        }
        this.reputation = reputation;
    }

}
