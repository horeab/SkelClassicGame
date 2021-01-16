package libgdx.implementations.fillcolor.screens;

import com.badlogic.gdx.graphics.Color;
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
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class FillColorGameScreen extends AbstractScreen<FillColorScreenManager> {

    public static final int TOTAL_MAX_SCORE = 3;
    public static final String STAR_IMG_NAME = "STAR_IMG_NAME";
    public static final String COLORBUCKETTABLE_NAME = "COLORBUCKETTABLE_NAME";
    public static final RGBColor TOOLBAR_COLOR = new RGBColor(1, 76, 199, 228);
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
        correctColors.put(Pair.of(235, FillColorService.getPixmapY(142)), RGBColor.GREEN);
        correctColors.put(Pair.of(338, FillColorService.getPixmapY(221)), RGBColor.GREEN);
        correctColors.put(Pair.of(135, FillColorService.getPixmapY(371)), RGBColor.YELLOW);
        correctColors.put(Pair.of(225, FillColorService.getPixmapY(270)), RGBColor.YELLOW);
        return correctColors;
    }


    @Override
    public void buildStage() {
        setUpAllTable();
        Table container = new Table();
        Table stackContainer = createImageStackContainer();
        container.add(stackContainer).width(stackContainer.getWidth())
                .height(stackContainer.getHeight());
        getAllTable().add(createTopBar()).growX().row();
        getAllTable().add(container).grow();
        addImage(stackContainer);
        getAllTable().row();
        int progressBarWidth = getProgressBarWidth();
        getAllTable().add(createProgressBar()).width(progressBarWidth);
        getAllTable().row();
        getAllTable().add(createColorToolbar()).width(progressBarWidth);
    }

    private Stack createTopBar() {
        Table topBar = new Table();
        Table bar1 = new Table();
        bar1.setBackground(GraphicUtils.getColorBackground(TOOLBAR_COLOR.toColor()));
        int bar1HeightPercent = 4;
        int bar2HeightPercent = 1;
        bar1.setHeight(ScreenDimensionsManager.getScreenHeightValue(bar1HeightPercent));
        Table bar2 = new Table();
        bar2.setHeight(ScreenDimensionsManager.getScreenHeightValue(bar2HeightPercent));
        bar2.setBackground(GraphicUtils.getColorBackground(RGBColor.DARK_BLUE.toColor()));
        topBar.add(bar1).growX().height(bar1.getHeight()).row();
        topBar.add(bar2).growX().height(bar2.getHeight());
        topBar.padTop(-ScreenDimensionsManager.getScreenHeightValue(bar1HeightPercent));
        Table levelIconTable = new Table();
        float marginDimen = MainDimen.horizontal_general_margin.getDimen();
        float levelInfoWidth = ScreenDimensionsManager.getScreenWidthValue(40);
        Image levelIconBackground = GraphicUtils.getImage(FillColorSpecificResource.level_nr_background);
        levelIconTable.add(createLevelIcon(levelIconBackground)).pad(marginDimen).width(levelInfoWidth)
                .height(ScreenDimensionsManager.getNewHeightForNewWidth(levelInfoWidth, levelIconBackground));
        levelIconTable.add().growX();
        Stack stack = new Stack();
        stack.add(topBar);
        stack.add(levelIconTable);
        return stack;
    }

    private Stack createLevelIcon(Image levelIconBackground) {
        float labelWidth = ScreenDimensionsManager.getScreenWidthValue(30);
        float labelHeight = ScreenDimensionsManager.getScreenHeightValue(7);
        MyWrappedLabel levelNr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(RGBColor.WHITE.toColor(),
                        Color.BLACK, Math.round(FontConfig.FONT_SIZE * 1.1f),
                        3f, 3, 3, RGBColor.BLACK.toColor(0.4f)))
                .setText(MainGameLabel.l_level.getText("0"))
                .setWrappedLineLabel(labelWidth)
                .setSingleLineLabel().build());
        levelNr.setWidth(labelWidth);
        levelNr.setHeight(labelHeight);
        Stack stack = new Stack();
        stack.add(levelIconBackground);
        stack.add(levelNr);
        return stack;
    }

    private int getProgressBarWidth() {
        return ScreenDimensionsManager.getScreenWidth();
    }

    private Table createImageStackContainer() {
        Stack image = fillColorService.getStackFromImage(imgToDisplay);
        Table stackContainer = new Table();
        stackContainer.add(image).width(image.getWidth()).height(image.getHeight());
        stackContainer.setHeight(image.getHeight());
        stackContainer.setWidth(image.getWidth());
        return stackContainer;
    }

    private void addImage(final Table stackContainer) {
        final int beforePressCorrectAnswersNr = fillColorService.getNrOfCorrectColorsPressed();
        stackContainer.setTouchable(Touchable.enabled);
        stackContainer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Stack img = fillColorService.fillWithColor(selectedColor, Math.round(x), Math.round(y));
                stackContainer.clear();
                stackContainer.add(img).width(img.getWidth()).height(img.getHeight());
                addImage(stackContainer);
                float amountToIncrease = getCorrectAnswerStepPercent();
                final int pressedCorrectAnswers = fillColorService.getNrOfCorrectColorsPressed();
                if (beforePressCorrectAnswersNr > pressedCorrectAnswers) {
                    amountToIncrease = -amountToIncrease;
                } else if (beforePressCorrectAnswersNr == pressedCorrectAnswers) {
                    amountToIncrease = 0;
                }
                if (pressedCorrectAnswers > 0) {
                    percentTable.setVisible(true);
                }
                refreshStarImages();
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

    private void refreshStarImages() {
        for (int i = 0; i < fillColorService.getWrongColorsPressed(); i++) {
            Image starImg = getRoot().findActor(STAR_IMG_NAME + i);
            if (starImg != null && starImg.isVisible()) {
                starImg.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRunnableAction(new Runnable() {
                    @Override
                    public void run() {
                        starImg.setVisible(false);
                    }
                })));
            }
        }
    }

    private Stack createProgressBar() {
        initPercentTable();
        Stack stack = new Stack();
        stack.add(createEmptyTable());
        stack.add(createProgressTable());
        stack.add(createTransparentTable());
        return stack;
    }

    private Table createEmptyTable() {
        Table emptyTable1 = new Table();
        Table emptyTable2 = new Table();
        emptyTable1.setBackground(GraphicUtils.getNinePatch(FillColorSpecificResource.progress_table_background1));
        emptyTable2.setBackground(GraphicUtils.getNinePatch(FillColorSpecificResource.progress_table_background2));
        Table emptyTable = new Table();
        emptyTable.add(emptyTable1).grow().row();
        emptyTable.add(emptyTable2).grow();
        return emptyTable;
    }

    private void initPercentTable() {
        float height = ScreenDimensionsManager.getScreenHeightValue(10);
        percentTable = new Table();
        percentTable.setVisible(false);
        percentTable.setWidth(0);
        percentTable.setHeight(height);
        percentTable.setBackground(GraphicUtils.getNinePatch(FillColorSpecificResource.progress_table_fill));
    }

    private Table createProgressTable() {
        Table progressTable = new Table();
        progressTable.add(percentTable).width(percentTable.getWidth()).height(percentTable.getHeight());
        progressTable.add().width(getProgressBarWidth());
        return progressTable;
    }

    private Table createTransparentTable() {
        float marginDimen = MainDimen.horizontal_general_margin.getDimen();
        MyWrappedLabel imgName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(RGBColor.WHITE.toColor(),
                        Color.BLACK, Math.round(FontConfig.FONT_SIZE * 1.8f),
                        3f, 3, 3, RGBColor.BLACK.toColor(0.8f)))
                .setText("Orange")
                .setSingleLineLabel().build());
        Table transparentTable = new Table();
        transparentTable.add(imgName).padLeft(marginDimen * 1);
        transparentTable.add().growX();
        Table starsTable = new Table();
        float starSideDimen = marginDimen * 5;
        for (int i = 0; i < TOTAL_MAX_SCORE; i++) {
            Image starImg = GraphicUtils.getImage(FillColorSpecificResource.star);
            starImg.setName(STAR_IMG_NAME + i);
            starsTable.add(starImg).width(starSideDimen).padRight(marginDimen).height(starSideDimen);
        }
        transparentTable.add(starsTable);
        return transparentTable;
    }

    private float getCorrectAnswerStepPercent() {
        return getProgressBarWidth() / correctColors.size();
    }

    private Table createColorToolbar() {
        Table table = new Table();
        table.setBackground(GraphicUtils.getColorBackground(TOOLBAR_COLOR.toColor()));
        List<RGBColor> colors = createRandomRGBColors(3);
        float maxSideDimen = ScreenDimensionsManager.getScreenWidthValue(20);
        float sideDimen = ScreenDimensionsManager.getScreenWidth() / colors.size();
        sideDimen = sideDimen > maxSideDimen ? maxSideDimen : sideDimen;
        Collections.shuffle(colors);
        selectedColor = colors.get(0);
        for (final RGBColor color : colors) {
            Stack stack = new Stack();
            Table colorTable = new Table();
            colorTable.setWidth(sideDimen);
            colorTable.setHeight(sideDimen);
            colorTable.setBackground(GraphicUtils.getColorBackground(color.toColor()));
            colorTable.setTouchable(Touchable.enabled);
            FillColorSpecificResource colorBucketRes = color.equals(selectedColor) ? FillColorSpecificResource.color_bucket_selected : FillColorSpecificResource.color_bucket;
            stack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedColor = color;
                    for (final RGBColor color : colors) {
                        FillColorSpecificResource colorBucketRes = color.equals(selectedColor) ? FillColorSpecificResource.color_bucket_selected : FillColorSpecificResource.color_bucket;
                        Table colorBucketTable = getRoot().findActor(COLORBUCKETTABLE_NAME + color.toHexadecimal());
                        colorBucketTable.clearChildren();
                        colorBucketTable.add(GraphicUtils.getImage(colorBucketRes));
                    }
                }
            });
            Table colorBucketTable = new Table();
            colorBucketTable.setName(COLORBUCKETTABLE_NAME + color.toHexadecimal());
            colorBucketTable.add(GraphicUtils.getImage(colorBucketRes));
            stack.add(colorTable);
            stack.add(colorBucketTable);
            table.add(stack).height(sideDimen).width(sideDimen);
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

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
