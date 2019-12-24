package libgdx;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.game.GameId;

public enum GameIdEnum implements GameId {

    math(libgdx.implementations.math.MathDependencyManager.class),;

    private Class<? extends CampaignGameDependencyManager> dependencyManagerClass;

    GameIdEnum(Class<? extends CampaignGameDependencyManager> dependencyManagerClass) {
        this.dependencyManagerClass = dependencyManagerClass;
    }

    @Override
    public CampaignGameDependencyManager getDependencyManager() {
        try {
            return dependencyManagerClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
