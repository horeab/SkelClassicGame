package libgdx.implementations.iqtest;

import libgdx.controls.labelimage.InventoryTableBuilderCreator;
import libgdx.controls.popup.RatingService;
import libgdx.game.MainDependencyManager;
import libgdx.implementations.SkelClassicGameIdEnum;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.implementations.skelgame.SkelGameResourceService;
import libgdx.resources.Resource;
import libgdx.resources.ResourceService;
import libgdx.screen.AbstractScreen;
import libgdx.transactions.TransactionsService;

public class IqTestMainDependencyManager extends MainDependencyManager<IqTestScreenManager, AbstractScreen, IqTestGameLabel, Resource, SkelClassicGameIdEnum> {

    @Override
    public Class<Resource> getMainResourcesClass() {
        return Resource.class;
    }

    @Override
    public Class<SkelClassicGameIdEnum> getGameIdClass() {
        return SkelClassicGameIdEnum.class;
    }

    @Override
    public ResourceService createResourceService() {
        return new SkelGameResourceService();
    }

    @Override
    public RatingService createRatingService(AbstractScreen abstractScreen) {
        return new SkelGameRatingService(abstractScreen);
    }

    @Override
    public Class<IqTestGameLabel> getGameLabelClass() {
        return IqTestGameLabel.class;
    }

    @Override
    public IqTestScreenManager createScreenManager() {
        return new IqTestScreenManager();
    }

    @Override
    public InventoryTableBuilderCreator createInventoryTableBuilderCreator() {
        throw new RuntimeException("Transactions not implemented for Game");
    }

    @Override
    public TransactionsService getTransactionsService() {
        throw new RuntimeException("Transactions not implemented for Game");
    }
}
