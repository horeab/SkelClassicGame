package libgdx.implementations.memory;

import libgdx.controls.labelimage.InventoryTableBuilderCreator;
import libgdx.controls.popup.RatingService;
import libgdx.game.MainDependencyManager;
import libgdx.implementations.SkelClassicGameIdEnum;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.resources.Resource;
import libgdx.resources.ResourceService;
import libgdx.screen.AbstractScreen;
import libgdx.transactions.TransactionsService;

public class MemoryMainDependencyManager extends MainDependencyManager<MemoryScreenManager, AbstractScreen, SkelGameLabel, Resource, SkelClassicGameIdEnum> {

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
        return new MemoryGameResourceService();
    }

    @Override
    public Class<SkelGameLabel> getGameLabelClass() {
        return SkelGameLabel.class;
    }

    @Override
    public RatingService createRatingService(AbstractScreen abstractScreen) {
        return new SkelGameRatingService(abstractScreen);
    }

    @Override
    public MemoryScreenManager createScreenManager() {
        return new MemoryScreenManager();
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
