package libgdx.implementations.kidlearn.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnDifficultyService;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterGameCreator;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterOrdLevel;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterSeqLevel;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.EnumUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnMathCaterLevelScreen<T extends Enum & KidLearnMathCaterLevel> extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    private Class<T> levelType;
    private KidLearnPreferencesManager kidLearnPreferencesManager = new KidLearnPreferencesManager();
    private KidLearnDifficultyService kidLearnDifficultyService = new KidLearnDifficultyService();

    public KidLearnMathCaterLevelScreen(Class<T> levelType) {
        this.levelType = levelType;
    }

    @Override
    public void buildStage() {
        setUpAllTable();
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        createLevelMenu(levelType, "Order");
    }

    private <L extends Enum & KidLearnMathCaterLevel> void createLevelMenu(Class<L> mathCaterLevelClass, String titleText) {
        Table table = new Table();
        Table headerTable = new Table();
        float screenWidth = ScreenDimensionsManager.getScreenWidthValue(90);
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(titleText).build());
        headerTable.add(title).pad(MainDimen.horizontal_general_margin.getDimen()).width(screenWidth / 2);
        headerTable.add(kidLearnDifficultyService.createDifficultyButtonsTable(levelType, false)).width(screenWidth / 2);
        float headerHeight = ScreenDimensionsManager.getScreenHeightValue(20);
        table.add(headerTable).height(headerHeight).width(screenWidth).row();
        List<L> allLevels = new ArrayList<>();
        for (L val : EnumUtils.getValues(mathCaterLevelClass)) {
            if (val.difficulty() == kidLearnPreferencesManager.getDifficultyLevel(levelType)) {
                allLevels.add(val);
            }
        }
        Table levelsContainer = new Table();
        levelsContainer.add(createLevelContainer(allLevels, "Forwards", true)).width(screenWidth / 2);
        levelsContainer.add(createLevelContainer(allLevels, "Backwards", false)).width(screenWidth / 2);
        table.add(levelsContainer).height(ScreenDimensionsManager.getScreenHeight() - headerHeight);
        getAllTable().add(table).grow();
    }

    private <L extends Enum & KidLearnMathCaterLevel> Table createLevelContainer(List<L> levelValues, String text, boolean ascLevels) {
        Table table = new Table();
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(text).build());
        table.add(title).pad(MainDimen.horizontal_general_margin.getDimen()).row();
        for (L level : levelValues) {
            if (ascLevels == level.asc()) {
                int levelMax;
                int levelMin;
                boolean asc;
                float interval;
                if (level instanceof KidLearnMathCaterOrdLevel) {
                    KidLearnMathCaterOrdLevel inst = (KidLearnMathCaterOrdLevel) level;
                    levelMax = inst.max;
                    levelMin = inst.min;
                    asc = inst.asc;
                    interval = inst.interval;
                } else {
                    KidLearnMathCaterSeqLevel inst = (KidLearnMathCaterSeqLevel) level;
                    levelMax = inst.max;
                    levelMin = inst.min;
                    asc = inst.asc;
                    interval = inst.interval != null ? inst.interval : inst.upTo;
                }
                Table optionBtn = createOptionBtn(level, levelMax, levelMin, asc, interval);
                table.add(optionBtn).row();
            }
        }
        return table;
    }

    private <L extends Enum & KidLearnMathCaterLevel> Table createOptionBtn(L level, int levelMax, int levelMin, final boolean asc, float interval) {
        Table table = new Table();
        MyButton btn = new ButtonBuilder(getLevelText(level, levelMax, levelMin, asc, interval))
                .setDefaultButton()
                .setFixedButtonSize(SkelClassicButtonSize.KIDLEARN_MATH_CATER_LEVEL)
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((KidLearnScreenManager) getScreenManager()).showGameScreen(new KidLearnGameContext(level));
            }
        });
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        float checkImgSideDimen = MainDimen.horizontal_general_margin.getDimen() * 3;
        Res res = kidLearnPreferencesManager.getLevelScore(level) == KidLearnMathCaterGameCreator.TOTAL_QUESTIONS ? MainResource.refresh_up : MainResource.error;
        table.add(GraphicUtils.getImage(res)).width(checkImgSideDimen).height(checkImgSideDimen);
        return table;
    }

    private <L extends Enum & KidLearnMathCaterLevel> String getLevelText(L level, int levelMax, int levelMin, final boolean asc, float interval) {
        String text = "";
        int min = asc ? levelMin : levelMax;
        int max = asc ? levelMax : levelMin;
        text = min + " to " + max + " in " + getInterval(interval);
        if (level instanceof KidLearnMathCaterSeqLevel) {
            KidLearnMathCaterSeqLevel inst = (KidLearnMathCaterSeqLevel) level;
            if (inst.evenNr) {
                text = "Even Numbers";
            }
        } else {
            KidLearnMathCaterOrdLevel inst = (KidLearnMathCaterOrdLevel) level;
        }
        return text;
    }

    private String getInterval(float interval) {
        return interval % 1 == 0 ? String.valueOf(Math.round(interval)) : String.valueOf(interval);
    }

    @Override
    public void onBackKeyPress() {
        getScreenManager().showChooseLevelScreen();
    }

}
