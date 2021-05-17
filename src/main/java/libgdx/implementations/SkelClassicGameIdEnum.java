package libgdx.implementations;

import libgdx.campaign.CampaignGameDependencyManager;
import libgdx.game.GameId;
import libgdx.implementations.applepie.ApplePieDependencyManager;
import libgdx.implementations.balloon.BalloonDependencyManager;
import libgdx.implementations.buylow.BuyLowDependencyManager;
import libgdx.implementations.fillcolor.FillColorDependencyManager;
import libgdx.implementations.imagesplit.ImageSplitDependencyManager;
import libgdx.implementations.iqtest.IqTestDependencyManager;
import libgdx.implementations.kidlearn.KidLearnDependencyManager;
import libgdx.implementations.math.MathDependencyManager;
import libgdx.implementations.memory.MemoryDependencyManager;
import libgdx.implementations.resourcewars.ResourceWarsDependencyManager;

public enum SkelClassicGameIdEnum implements GameId {

    iqtest(IqTestDependencyManager.class),
    balloon(BalloonDependencyManager.class),
    math(MathDependencyManager.class),
    buylow(BuyLowDependencyManager.class),
    imagesplit(ImageSplitDependencyManager.class),
    kidlearn(KidLearnDependencyManager.class),
    fillcolor(FillColorDependencyManager.class),
    applepie(ApplePieDependencyManager.class),
    memory(MemoryDependencyManager.class),
    resourcewars(ResourceWarsDependencyManager.class),;

    private Class<? extends CampaignGameDependencyManager> dependencyManagerClass;

    SkelClassicGameIdEnum(Class<? extends CampaignGameDependencyManager> dependencyManagerClass) {
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
