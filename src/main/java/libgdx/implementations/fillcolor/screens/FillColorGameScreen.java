package libgdx.implementations.fillcolor.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.fillcolor.FillColorCampaignLevelEnum;
import libgdx.implementations.fillcolor.FillColorScreenManager;
import libgdx.implementations.fillcolor.FillColorSpecificResource;
import libgdx.implementations.fillcolor.spec.FillColorService;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class FillColorGameScreen extends AbstractScreen<FillColorScreenManager> {

    private Image imgToDisplay;
    private Res imgToFill;
    private FillColorService fillColorService = new FillColorService();
    private Map<Pair<Integer, Integer>, Color> correctColors;
    private Table percentTable;

    public FillColorGameScreen(FillColorCampaignLevelEnum campaignLevelEnum) {
        FillColorSpecificResource res = FillColorSpecificResource.img0;
        imgToDisplay = fillColorService.getImageFromPixmap(fillColorService.getPixMapFromRes(res));
        imgToFill = FillColorSpecificResource.valueOf(res.name() + "_fill");
        correctColors = createCorrectColors();
    }

    private Map<Pair<Integer, Integer>, Color> createCorrectColors() {
        Map<Pair<Integer, Integer>, Color> correctColors = new HashMap<>();
        correctColors.put(Pair.of(1, 1), Color.RED);
        correctColors.put(Pair.of(2, 1), Color.RED);
        correctColors.put(Pair.of(3, 1), Color.RED);
        return correctColors;
    }

    @Override
    public void buildStage() {
        addLevelIcon();
        setUpAllTable();
        Table container = new Table();
        Table stackContainer = createImageStackContainer();
        container.add(stackContainer).width(stackContainer.getWidth())
                .height(stackContainer.getHeight());
        getAllTable().add(container).grow();
        addImage(stackContainer);
        getAllTable().row();
        getAllTable().add(createProgressBar(2)).width(getProgressBarWidth());
        getAllTable().row();
        getAllTable().add(createColorToolbar());
    }

    protected int getProgressBarWidth() {
        return ScreenDimensionsManager.getScreenWidth();
    }

    protected Table createImageStackContainer() {
        Stack image = fillColorService.getStackFromImage(imgToDisplay);
        Table stackContainer = new Table();
        stackContainer.add(image).width(image.getWidth()).height(image.getHeight());
        stackContainer.setHeight(image.getHeight());
        stackContainer.setWidth(image.getWidth());
        return stackContainer;
    }

    private void addImage(Table stackContainer) {
        stackContainer.setTransform(true);
        stackContainer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Stack img = fillColorService.fillWithColor(imgToDisplay, imgToFill, Math.round(x), Math.round(y));
                stackContainer.clear();
                stackContainer.add(img).width(img.getWidth()).height(img.getHeight());
                addImage(stackContainer);
                percentTable.setVisible(true);
                percentTable.addAction(Actions.sizeBy(getCorrectAnswerStepPercent(), 0, 0.2f));
            }
        });
    }

    private Stack createProgressBar(int correctResp) {
        float height = ScreenDimensionsManager.getScreenHeightValue(10);
        MyWrappedLabel imgName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.5f), 8f))
                .setText("Orange")
                .setSingleLineLabel().build());
        Table emptyTable = new Table();
        Table progressTable = new Table();
        Table transparentTable = new Table();
        emptyTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        float marginDimen = MainDimen.horizontal_general_margin.getDimen();
        transparentTable.add(imgName).padLeft(marginDimen * 1);
        transparentTable.add().growX();
        Table starsTable = new Table();
        float starSideDimen = marginDimen * 5;
        for (int i = 0; i < 3; i++) {
            starsTable.add(GraphicUtils.getImage(MainResource.heart_full)).width(starSideDimen).height(starSideDimen);
        }
        percentTable = new Table();
        percentTable.setVisible(false);
        percentTable.setWidth(0);
        percentTable.setHeight(height);
        percentTable.setBackground(GraphicUtils.getNinePatch(MainResource.green_background));
        progressTable.add(percentTable).width(percentTable.getWidth()).height(percentTable.getHeight());
        progressTable.add().width(getProgressBarWidth());
        transparentTable.add(starsTable);
        Stack stack = new Stack();
        stack.add(emptyTable);
        stack.add(progressTable);
        stack.add(transparentTable);
        return stack;
    }

    private float getCorrectAnswerStepPercent() {
        return getProgressBarWidth() / correctColors.size();
    }

    private Table createColorToolbar() {
        Table table = new Table();
        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        List<Integer> colors = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        float sideDimen = MainDimen.horizontal_general_margin.getDimen() * 6;
        for (Integer color : colors) {
            table.add(GraphicUtils.getImage(MainResource.heart_full)).height(sideDimen).width(sideDimen);
        }
        return table;
    }

    private void addLevelIcon() {
        float labelWidth = ScreenDimensionsManager.getScreenWidthValue(30);
        float labelHeight = ScreenDimensionsManager.getScreenHeightValue(7);
        MyWrappedLabel levelNr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1f), 4f))
                .setText(MainGameLabel.l_level.getText("0"))
                .setWrappedLineLabel(labelWidth)
                .setSingleLineLabel().build());
        levelNr.setX(ScreenDimensionsManager.getScreenWidth() - labelWidth - MainDimen.horizontal_general_margin.getDimen());
        levelNr.setY(ScreenDimensionsManager.getScreenHeight() - MainDimen.vertical_general_margin.getDimen() * 6);
        levelNr.setWidth(labelWidth);
        levelNr.setHeight(labelHeight);
        levelNr.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        addActor(levelNr);
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
