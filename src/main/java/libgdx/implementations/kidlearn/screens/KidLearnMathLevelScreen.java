package libgdx.implementations.kidlearn.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnControlsUtils;
import libgdx.implementations.kidlearn.spec.KidLearnDifficultyService;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnInAppPurchaseTable;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterGameCreator;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterOrdLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterSeqLevel;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnMathLevelScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    private KidLearnPreferencesManager kidLearnPreferencesManager = new KidLearnPreferencesManager();
    private KidLearnDifficultyService kidLearnDifficultyService = new KidLearnDifficultyService();

    @Override
    public void buildStage() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(KidLearnSpecificResource.scroll_background_math);
        setUpAllTable();
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        createLevelMenu(KidLearnGameLabel.l_math_title.getText());
        kidLearnDifficultyService.setBackgroundDiff(new KidLearnPreferencesManager().getDifficultyLevel(difficultyLevelClass()), getBackgroundStage());
    }

    private void createLevelMenu(String titleText) {
        Table table = new Table();
        Table headerTable = new Table();
        float screenWidth = ScreenDimensionsManager.getScreenWidth(90);
        headerTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWidth(ScreenDimensionsManager.getScreenWidth(38))
                .setFontConfig(KidLearnControlsUtils.getTitleFontConfig(titleText, kidLearnDifficultyService.getTitleBorderColor(difficultyLevelClass()))).setText(titleText).build()))
                .padTop(MainDimen.vertical_general_margin.getDimen() * 2).width(screenWidth / 2);
        headerTable.add(kidLearnDifficultyService.createDifficultyButtonsTable(difficultyLevelClass(), false, new KidLearnPreferencesManager().getDifficultyLevel(difficultyLevelClass()))).width(screenWidth / 2);
        float headerHeight = ScreenDimensionsManager.getScreenHeight(20);
        table.add(headerTable).height(headerHeight).width(screenWidth).row();
        Table levelsContainer = new Table();
        levelsContainer.add(createLevelContainer(kidLearnDifficultyService.getLevelsForDifficulty(KidLearnMathCaterOrdLevel.class, difficultyLevelClass()), KidLearnGameLabel.l_math_ord_title.getText())).width(screenWidth / 2);
        levelsContainer.add(createLevelContainer(kidLearnDifficultyService.getLevelsForDifficulty(KidLearnMathCaterSeqLevel.class, difficultyLevelClass()), KidLearnGameLabel.l_math_seq_title.getText())).width(screenWidth / 2);
        table.add(levelsContainer).height(ScreenDimensionsManager.getScreenHeight() - headerHeight);
        getAllTable().add(table).grow();
    }

    public static Class<KidLearnMathCaterOrdLevel> difficultyLevelClass() {
        return KidLearnMathCaterOrdLevel.class;
    }

    private <L extends Enum & KidLearnMathCaterLevel & KidLearnLevel> Table createLevelContainer(List<L> levelValues, String text) {
        Table table = new Table();
        table.add(KidLearnControlsUtils.createGameSubTitle(text)).pad(MainDimen.horizontal_general_margin.getDimen()).row();
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

    private <L extends Enum & KidLearnMathCaterLevel & KidLearnLevel> Table createOptionBtn(final L level, int levelMax, int levelMin, final boolean asc, float interval) {
        SkelClassicButtonSkin kidlearnHangmanLetter = SkelClassicButtonSkin.KIDLEARN_MATH_ORD_LEVEL;
        if (level instanceof KidLearnMathCaterSeqLevel) {
            kidlearnHangmanLetter = SkelClassicButtonSkin.KIDLEARN_MATH_SEQ_LEVEL;
        }
        Table table = new Table();
        Pair<String, String> levelText = getLevelText(level, levelMax, levelMin, asc, interval);
        float borderWidth = 3f;
        SkelClassicButtonSize levelBtnSize = getLevelBtnSize();
        MyButton btn = new ButtonBuilder()
                .setButtonSkin(kidlearnHangmanLetter)
                .setFontConfig(createBtnFontConfig(FontConfig.FONT_SIZE * 1.2f, borderWidth))
                .setWrappedText(levelText.getLeft(), levelBtnSize.getWidth())
                .setFixedButtonSize(levelBtnSize)
                .build();
        btn.getCenterRow().row();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((KidLearnScreenManager) getScreenManager()).showGameScreen(new KidLearnGameContext(level));
            }
        });
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        KidLearnInAppPurchaseTable inAppPurchaseTable = new KidLearnInAppPurchaseTable(levelBtnSize.getHeight());
        if (!Utils.isValidExtraContent() && level.isLocked()) {
            btn.setDisabled(true);
            table = inAppPurchaseTable.createForProVersion(table, true);
        }

        float checkImgSideDimen = MainDimen.horizontal_general_margin.getDimen() * 3;
        Res res = kidLearnPreferencesManager.getLevelScore(level) == KidLearnMathCaterGameCreator.TOTAL_QUESTIONS ? KidLearnSpecificResource.level_finished : KidLearnSpecificResource.level_not_finished;
        table.add(GraphicUtils.getImage(res)).width(checkImgSideDimen).height(checkImgSideDimen);
        return table;
    }

    private FontConfig createBtnFontConfig(float fontSize, float borderWidth) {
        return new FontConfig(FontColor.WHITE.getColor(), FontColor.BLACK.getColor(),
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

    @Override
    public void onBackKeyPress() {
        getScreenManager().showMainScreen();
    }

}
