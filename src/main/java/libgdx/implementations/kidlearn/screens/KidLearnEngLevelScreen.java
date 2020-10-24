package libgdx.implementations.kidlearn.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.List;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnDifficultyService;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnEngLevelScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    private KidLearnPreferencesManager kidLearnPreferencesManager = new KidLearnPreferencesManager();
    private KidLearnDifficultyService kidLearnDifficultyService = new KidLearnDifficultyService();

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        setUpAllTable();
        createChooseLevelMenu();
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
            addLevelToTable(btnTable, i, level);
            i++;
        }
        for (KidLearnEngHangmanLevel level : hangmanLevels) {
            addLevelToTable(btnTable, i, level);
            i++;
        }
        for (KidLearnEngVerbLevel level : verbLevels) {
            addLevelToTable(btnTable, i, level);
            i++;
        }
        float extraHeight = ScreenDimensionsManager.getScreenHeight() - btnTableHeight;
        table.add(createGameTitle()).height(extraHeight / 2).row();
        table.add().height(extraHeight / 2).row();
        Table contentContainer = new Table();
        contentContainer.add(btnTable).top().height(btnTableHeight);
        Table difficultyButtonsTable = kidLearnDifficultyService.createDifficultyButtonsTable(difficultyLevelClass(), true);
        difficultyButtonsTable.toBack();
        contentContainer.add(difficultyButtonsTable).width(ScreenDimensionsManager.getScreenWidthValue(20)).top().height(btnTableHeight);
        table.add(contentContainer).top().height(btnTableHeight);
        new ActorAnimation(table, getAbstractScreen()).animateFastFadeIn();
        getAllTable().add(table).grow();
    }

    private <T extends Enum & KidLearnLevel & KidLearnEngLevel> void addLevelToTable(Table btnTable, int i, T level) {
        int colspan = 1;
        if (i == 2) {
            btnTable.row();
            colspan = 2;
        }
        MyButton chooseLevelBtn = createChooseLevelBtn(level, i);
        float padSide = MainDimen.horizontal_general_margin.getDimen() * 5;
        ButtonSize buttonSize = getChooseLevelBtnSize();
        btnTable.add(chooseLevelBtn).colspan(colspan)
                .padLeft(padSide / 2).padRight(padSide / 2).width(buttonSize.getWidth() * 1.5f).height(buttonSize.getHeight() * 1.5f);
        if (i == 0) {
            btnTable.padBottom(padSide);
        }
        chooseLevelBtn.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
    }

    public static Class<KidLearnEngWordsLevel> difficultyLevelClass() {
        return KidLearnEngWordsLevel.class;
    }

    private Table createGameTitle() {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        FontConfig.FONT_SIZE * 2f, 8f)).setText(KidLearnGameLabel.l_eng_title.getText()).build());
    }

    private <T extends Enum & KidLearnLevel & KidLearnEngLevel> MyButton createChooseLevelBtn(T level, int index) {
        SkelClassicButtonSkin skin = SkelClassicButtonSkin.valueOf("KIDLEARN_ENGWORDS_" + index);
        skin = kidLearnPreferencesManager.getLevelScore(level) == KidLearnEngWordsGameCreator.TOTAL_QUESTIONS ?
                SkelClassicButtonSkin.valueOf("KIDLEARN_ENGWORDS_FIN_" + index) : skin;
        ButtonSize btnSize = getChooseLevelBtnSize();
        MyButton btn = new ImageButtonBuilder(skin, Game.getInstance().getAbstractScreen())
                .padBetweenImageAndText(1.3f)
                .textBackground(MainResource.transparent_background)
                .setFixedButtonSize(btnSize)
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        FontConfig.FONT_SIZE * 3f, 7f))
                .setText(level.title())
                .build();
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getScreenManager().showGameScreen(new KidLearnGameContext(level));
            }
        });
        btn.getCenterRow().row();
        MyWrappedLabel categoryLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE / 1.05f), 2f))
                .setWrappedLineLabel(btnSize.getWidth() * 1.5f)
                .setText(level.category().getText())
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
