package libgdx.implementations.kidlearn.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

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
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnDifficultyService;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterGameCreator;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterOrdLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterSeqLevel;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.EnumUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnMathCaterLevelScreen<T extends Enum & KidLearnMathCaterLevel> extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    private KidLearnPreferencesManager kidLearnPreferencesManager = new KidLearnPreferencesManager();
    private KidLearnDifficultyService kidLearnDifficultyService = new KidLearnDifficultyService();

    @Override
    public void buildStage() {
        setUpAllTable();
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        createLevelMenu(KidLearnGameLabel.l_math_title.getText());
    }

    private <L extends Enum & KidLearnMathCaterLevel> void createLevelMenu(String titleText) {
        Table table = new Table();
        Table headerTable = new Table();
        float screenWidth = ScreenDimensionsManager.getScreenWidthValue(90);
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(titleText).build());
        headerTable.add(title).pad(MainDimen.horizontal_general_margin.getDimen()).width(screenWidth / 2);
        headerTable.add(kidLearnDifficultyService.createDifficultyButtonsTable(difficultyLevelClass(), false)).width(screenWidth / 2);
        float headerHeight = ScreenDimensionsManager.getScreenHeightValue(20);
        table.add(headerTable).height(headerHeight).width(screenWidth).row();
        Table levelsContainer = new Table();
        levelsContainer.add(createLevelContainer(getAllLevels(KidLearnMathCaterOrdLevel.class), KidLearnGameLabel.l_math_ord_title.getText())).width(screenWidth / 2);
        levelsContainer.add(createLevelContainer(getAllLevels(KidLearnMathCaterSeqLevel.class), KidLearnGameLabel.l_math_seq_title.getText())).width(screenWidth / 2);
        table.add(levelsContainer).height(ScreenDimensionsManager.getScreenHeight() - headerHeight);
        getAllTable().add(table).grow();
    }

    public static Class<KidLearnMathCaterOrdLevel> difficultyLevelClass() {
        return KidLearnMathCaterOrdLevel.class;
    }

    private <L extends Enum & KidLearnMathCaterLevel> List<L> getAllLevels(Class<L> mathCaterLevelClass) {
        List<L> allLevels = new ArrayList<>();
        for (L val : EnumUtils.getValues(mathCaterLevelClass)) {
            if (val.difficulty() == kidLearnPreferencesManager.getDifficultyLevel(difficultyLevelClass())) {
                allLevels.add(val);
            }
        }
        return allLevels;
    }

    private <L extends Enum & KidLearnMathCaterLevel> Table createLevelContainer(List<L> levelValues, String text) {
        Table table = new Table();
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f))
                .setWidth(getLevelBtnSize().getWidth())
                .setText(text)
                .build());
        table.add(title).pad(MainDimen.horizontal_general_margin.getDimen()).row();
        for (L level : levelValues) {
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
        return table;
    }

    private SkelClassicButtonSize getLevelBtnSize() {
        return SkelClassicButtonSize.KIDLEARN_MATH_CATER_LEVEL;
    }

    private <L extends Enum & KidLearnMathCaterLevel> Table createOptionBtn(L level, int levelMax, int levelMin, final boolean asc, float interval) {
        Table table = new Table();
        Pair<String, String> levelText = getLevelText(level, levelMax, levelMin, asc, interval);
        MyButton btn = new ButtonBuilder()
                .setFontConfig(createBtnFontConfig(FontConfig.FONT_SIZE / 1.2f, 4f))
                .setWrappedText(levelText.getLeft(), getLevelBtnSize().getWidth())
                .setDefaultButton()
                .setFixedButtonSize(getLevelBtnSize())
                .build();
        btn.getCenterRow().row();
        if (StringUtils.isNotBlank(levelText.getRight())) {
            MyWrappedLabel operationLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontConfig(createBtnFontConfig(FontConfig.FONT_SIZE / 1.7f, 2f))
                    .setWidth(getLevelBtnSize().getWidth())
                    .setText(levelText.getRight())
                    .build());
            btn.getCenterRow().padTop(MainDimen.vertical_general_margin.getDimen()).add(operationLabel);
        }
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((KidLearnScreenManager) getScreenManager()).showGameScreen(new KidLearnGameContext(level));
            }
        });
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        float checkImgSideDimen = MainDimen.horizontal_general_margin.getDimen() * 3;
        Res res = kidLearnPreferencesManager.getLevelScore(level) == KidLearnMathCaterGameCreator.TOTAL_QUESTIONS ? KidLearnSpecificResource.level_finished : KidLearnSpecificResource.level_not_finished;
        table.add(GraphicUtils.getImage(res)).width(checkImgSideDimen).height(checkImgSideDimen);
        return table;
    }

    private FontConfig createBtnFontConfig(float fontSize, float borderWidth) {
        return new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                Math.round(fontSize), borderWidth);
    }

    private <L extends Enum & KidLearnMathCaterLevel> Pair<String, String> getLevelText(L level, int levelMax, int levelMin, final boolean asc, float interval) {
        String text = "";
        int min = asc ? levelMin : levelMax;
        int max = asc ? levelMax : levelMin;
        Pair<String, String> textOperation;
        text = KidLearnGameLabel.l_math_level.getText(min, max);
        if (level instanceof KidLearnMathCaterSeqLevel) {
            KidLearnMathCaterSeqLevel inst = (KidLearnMathCaterSeqLevel) level;
            String operation = KidLearnGameLabel.l_math_seq_add.getText();
            if (!inst.asc) {
                operation = KidLearnGameLabel.l_math_seq_sub.getText();
            }
            textOperation = Pair.of(text, operation);
        } else {
            KidLearnMathCaterOrdLevel inst = (KidLearnMathCaterOrdLevel) level;
            String operation = null;
            if (interval % 10 == 0) {
                operation = KidLearnGameLabel.l_math_ten.getText();
            }
            if (KidLearnMathCaterGameCreator.getNrFromFloat(interval).contains(".")) {
                operation = KidLearnGameLabel.l_math_decimal.getText();
            }
            textOperation = Pair.of(text, operation);
        }
        return textOperation;
    }

    private String getInterval(float interval) {
        return interval % 1 == 0 ? String.valueOf(Math.round(interval)) : String.valueOf(interval);
    }

    @Override
    public void onBackKeyPress() {
        getScreenManager().showMainScreen();
    }

}
