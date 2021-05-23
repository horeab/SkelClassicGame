package libgdx.implementations.kidlearn.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.List;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnControlsUtils;
import libgdx.implementations.kidlearn.spec.KidLearnDifficultyService;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnInAppPurchaseTable;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class KidLearnEngLevelScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    private KidLearnPreferencesManager kidLearnPreferencesManager = new KidLearnPreferencesManager();
    private KidLearnDifficultyService kidLearnDifficultyService = new KidLearnDifficultyService();

    @Override
    public void buildStage() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(KidLearnSpecificResource.scroll_background_eng);
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        setUpAllTable();
        createChooseLevelMenu();
        kidLearnDifficultyService.setBackgroundDiff(new KidLearnPreferencesManager().getDifficultyLevel(difficultyLevelClass()), getBackgroundStage());
    }

    private void createChooseLevelMenu() {
        Table table = new Table();
        Table btnTable = new Table();
        float btnTableHeight = ScreenDimensionsManager.getScreenHeightValue(70);
        List<KidLearnEngWordsLevel> wordsLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnEngWordsLevel.class, difficultyLevelClass());
        List<KidLearnEngHangmanLevel> hangmanLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnEngHangmanLevel.class, difficultyLevelClass());
        List<KidLearnEngVerbLevel> verbLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnEngVerbLevel.class, difficultyLevelClass());
        int i = 0;
        for (KidLearnEngWordsLevel level : wordsLevels) {
            addLevelToTable(btnTable, i, level, KidLearnEngWordsGameCreator.TOTAL_QUESTIONS);
            i++;
        }
        for (KidLearnEngHangmanLevel level : hangmanLevels) {
            addLevelToTable(btnTable, i, level, KidLearnEngHangmanGameCreator.TOTAL_QUESTIONS);
            i++;
        }
        for (KidLearnEngVerbLevel level : verbLevels) {
            addLevelToTable(btnTable, i, level, KidLearnEngVerbGameCreator.TOTAL_QUESTIONS);
            i++;
        }
        float extraHeight = ScreenDimensionsManager.getScreenHeight() - btnTableHeight;
        table.add(KidLearnControlsUtils.createGameTitleAllWidth(KidLearnGameLabel.l_eng_title.getText(), kidLearnDifficultyService.getTitleBorderColor(difficultyLevelClass())))
                .height(extraHeight / 2).row();
        table.add().height(extraHeight / 2).row();
        Table contentContainer = new Table();
        contentContainer.add(btnTable).top().height(btnTableHeight);
        Table difficultyButtonsTable = kidLearnDifficultyService.createDifficultyButtonsTable(difficultyLevelClass(), true, new KidLearnPreferencesManager().getDifficultyLevel(difficultyLevelClass()));
        difficultyButtonsTable.toBack();
        contentContainer.add(difficultyButtonsTable).width(ScreenDimensionsManager.getScreenWidthValue(20)).top().height(btnTableHeight);
        table.add(contentContainer).top().height(btnTableHeight);
        new ActorAnimation(getAbstractScreen()).animateFastFadeIn(table);
        getAllTable().add(table).grow();
    }

    private <T extends Enum & KidLearnLevel & KidLearnEngLevel> void addLevelToTable(Table btnTable, int i, T level, int totalQuestions) {
        int colspan = 1;
        if (i == 2) {
            btnTable.row();
            colspan = 2;
        }
        Table chooseLevelBtnTable = new Table();
        MyButton chooseLevelBtn = createChooseLevelBtn(level, totalQuestions);
        float padSide = MainDimen.horizontal_general_margin.getDimen() * 5;
        final ButtonSize buttonSize = getChooseLevelBtnSize();
        chooseLevelBtnTable.add(chooseLevelBtn).width(buttonSize.getWidth()).height(buttonSize.getHeight());
        KidLearnInAppPurchaseTable inAppPurchaseTable = new KidLearnInAppPurchaseTable(buttonSize.getWidth());
        if (!Utils.isValidExtraContent() && level.isLocked()) {
            chooseLevelBtn.setDisabled(true);
            chooseLevelBtnTable = inAppPurchaseTable.createForProVersion(chooseLevelBtnTable, true);
        }
        btnTable.add(chooseLevelBtnTable).colspan(colspan)
                .padLeft(padSide / 2).padRight(padSide / 2).width(buttonSize.getWidth() * 1.5f).height(buttonSize.getHeight() * 1.5f);
        if (i == 0) {
            btnTable.padBottom(padSide);
        }
    }

    public static Class<KidLearnEngWordsLevel> difficultyLevelClass() {
        return KidLearnEngWordsLevel.class;
    }

    private <T extends Enum & KidLearnLevel & KidLearnEngLevel> MyButton createChooseLevelBtn(final T level, int totalQuestions) {
        ButtonSkin skin = level.buttonSkin();
        ButtonSize btnSize = getChooseLevelBtnSize();
        int levelScore = kidLearnPreferencesManager.getLevelScore(level);
        Color mainFontColor = levelScore == totalQuestions ? FontColor.LIGHT_GREEN.getColor() : FontColor.WHITE.getColor();
        Color borderColor = levelScore == totalQuestions ? FontColor.DARK_GRAY.getColor() : FontColor.GRAY.getColor();
        Color backColor = levelScore == totalQuestions ? RGBColor.LIGHT_GREEN.toColor(0.9f) : RGBColor.LIGHT_BLUE.toColor(0.9f);
        MyButton btn = new ImageButtonBuilder(skin, Game.getInstance().getAbstractScreen())
                .textBackground(MainResource.popup_background)
                .setFixedButtonSize(btnSize)
                .setFontConfig(KidLearnControlsUtils.getBtnLevelFontConfig(level.title()))
                .setWrappedText(level.category().getText(), btnSize.getWidth() * 2)
                .build();
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getScreenManager().showGameScreen(new KidLearnGameContext(level));
            }
        });
        btn.getCenterRow().row();
        btn.getCenterRow().setBackground(GraphicUtils.getColorBackground(backColor));
        MyWrappedLabel categoryLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(mainFontColor, borderColor,
                        Math.round(KidLearnControlsUtils.getBtnLevelFontSize(level.title()) * 0.9f), 3f))
                .setWrappedLineLabel(btnSize.getWidth() * 2.5f)
                .setText(level.title())
                .build());
        btn.getCenterRow().padTop(MainDimen.vertical_general_margin.getDimen()).add(categoryLabel).width(btnSize.getWidth());
        return btn;
    }

    private ButtonSize getChooseLevelBtnSize() {
        return SkelClassicButtonSize.KIDLEARN_ENG_WORDS_LEVEL;
    }


    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
