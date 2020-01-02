package libgdx.implementations;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.game.GameId;
import libgdx.implementations.balloon.BalloonDependencyManager;
import libgdx.implementations.math.MathDependencyManager;

public enum GameIdEnum implements GameId {

    balloon(BalloonDependencyManager.class),
    math(MathDependencyManager.class),;

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
