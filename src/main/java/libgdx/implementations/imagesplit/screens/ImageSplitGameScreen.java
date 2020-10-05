package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.imagesplit.ImageSplitScreenManager;
import libgdx.screen.AbstractScreen;

public class ImageSplitGameScreen extends AbstractScreen<ImageSplitScreenManager> {


    private Table allTable;
    private MyButton hoverBackButton;

    public ImageSplitGameScreen() {
        init();
    }

    private void init() {
    }

    @Override
    public void buildStage() {
        createAllTable();
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
    }

    private void createAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        addActor(allTable);
        if (hoverBackButton != null) {
            hoverBackButton.toFront();
        }
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
