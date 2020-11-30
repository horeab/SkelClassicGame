package libgdx.implementations.fillcolor.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class FillColorGameScreen extends AbstractScreen<FillColorScreenManager> {

    private Image imgToDisplay;
    private Res imgToFill;
    private FillColorService fillColorService;
    private Map<Pair<Integer, Integer>, RGBColor> correctColors;
    private Table percentTable;
    private RGBColor selectedColor;

    public FillColorGameScreen(FillColorCampaignLevelEnum campaignLevelEnum) {
        FillColorSpecificResource res = FillColorSpecificResource.img0;
        correctColors = createCorrectColors();
        imgToDisplay = FillColorService.getImageFromPixmap(FillColorService.getPixMapFromRes(res));
        imgToFill = FillColorSpecificResource.valueOf(res.name() + "_fill");
        fillColorService = new FillColorService(imgToDisplay, imgToFill, correctColors);
    }

    private Map<Pair<Integer, Integer>, RGBColor> createCorrectColors() {
        Map<Pair<Integer, Integer>, RGBColor> correctColors = new HashMap<>();
        correctColors.put(Pair.of(166, FillColorService.getPixmapY(100)), RGBColor.LIGHT_RED1);
        correctColors.put(Pair.of(176, FillColorService.getPixmapY(226)), RGBColor.GREEN);
        correctColors.put(Pair.of(137, FillColorService.getPixmapY(271)), RGBColor.GREEN);
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
        int progressBarWidth = getProgressBarWidth();
        getAllTable().add(createProgressBar(2)).width(progressBarWidth);
        getAllTable().row();
        getAllTable().add(createColorToolbar()).width(progressBarWidth);
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

    private void addImage(final Table stackContainer) {
        final int beforePressCorrectAnswersNr = fillColorService.getCorrectColorsPressed().size();
        stackContainer.setTouchable(Touchable.enabled);
        stackContainer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Stack img = fillColorService.fillWithColor(selectedColor, Math.round(x), Math.round(y));
                stackContainer.clear();
                stackContainer.add(img).width(img.getWidth()).height(img.getHeight());
                addImage(stackContainer);
                float amountToIncrease = getCorrectAnswerStepPercent();
                final int pressedCorrectAnswers = fillColorService.getCorrectColorsPressed().size();
                if (beforePressCorrectAnswersNr > pressedCorrectAnswers) {
                    amountToIncrease = -amountToIncrease;
                } else if (beforePressCorrectAnswersNr == pressedCorrectAnswers) {
                    amountToIncrease = 0;
                }
                if (pressedCorrectAnswers > 0) {
                    percentTable.setVisible(true);
                }
                percentTable.addAction(Actions.sequence(Actions.sizeBy(amountToIncrease, 0, 0.2f),
                        Utils.createRunnableAction(new Runnable() {
                            @Override
                            public void run() {
                                if (pressedCorrectAnswers == 0) {
                                    percentTable.setVisible(false);
                                }
                            }
                        })));

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
        table.setBackground(GraphicUtils.getColorBackground(RGBColor.LIGHT_BLUE.toColor()));
        float sideDimen = MainDimen.horizontal_general_margin.getDimen() * 8;
        List<RGBColor> colors = createRandomRGBColors(2);
        Collections.shuffle(colors);
        selectedColor = colors.get(0);
        for (final RGBColor color : colors) {
            Table colorTable = new Table();
            colorTable.setWidth(sideDimen);
            colorTable.setHeight(sideDimen);
            colorTable.setBackground(GraphicUtils.getColorBackground(color.toColor()));
            table.add(colorTable).height(colorTable.getHeight()).width(colorTable.getWidth());
            colorTable.setTouchable(Touchable.enabled);
            colorTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedColor = color;
                }
            });
        }
        return table;
    }

    private List<RGBColor> createRandomRGBColors(int nrOfWrongColor) {
        Set<RGBColor> colors = new HashSet<>(correctColors.values());
        for (int i = 0; i < nrOfWrongColor; i++) {
            colors.add(new RGBColor(new Random().nextInt(255) + 1,
                    new Random().nextInt(255) + 1, new Random().nextInt(255) + 1));
        }
        return new ArrayList<>(colors);
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
