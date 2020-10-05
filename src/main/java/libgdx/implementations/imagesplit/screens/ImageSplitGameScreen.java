package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.imagesplit.ImageSplitScreenManager;
import libgdx.implementations.imagesplit.ImageSplitSpecificResource;
import libgdx.implementations.imagesplit.spec.ImageSplitService;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;

public class ImageSplitGameScreen extends AbstractScreen<ImageSplitScreenManager> {


    private Table allTable;
    private MyButton hoverBackButton;
    private ImageSplitService imageSplitService = new ImageSplitService();

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
        int totalCols = 2;
        int totalRows = 3;
        for (int j = 0; j < totalRows; j++) {
            for (int i = 0; i < totalCols; i++) {
                Image image = imageSplitService.crop(ImageSplitSpecificResource.i0, totalCols, totalRows, i, j);
                allTable.add(image).pad(MainDimen.horizontal_general_margin.getDimen()).width(image.getWidth()).height(image.getHeight());
            }
            allTable.row();
        }
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
