package libgdx.implementations.kidlearn.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
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
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.EnumUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnEngWordsLevelScreen extends AbstractScreen<KidLearnScreenManager> {

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
        ButtonSize btnSize = getChooseLevelBtnSize();
        Table btnTable = new Table();
        float btnTableHeight = ScreenDimensionsManager.getScreenHeightValue(70);
        float padSide = MainDimen.horizontal_general_margin.getDimen() * 5;
        int i = 0;
        List<KidLearnEngWordsLevel> allLevels = new ArrayList<>();
        Class<KidLearnEngWordsLevel> enumClass = KidLearnEngWordsLevel.class;
        for (KidLearnEngWordsLevel val : EnumUtils.getValues(enumClass)) {
            if (val.difficulty == kidLearnPreferencesManager.getDifficultyLevel(enumClass)) {
                allLevels.add(val);
            }
        }
        for (KidLearnEngWordsLevel level : allLevels) {
            if (i > 0 && i % 2 == 0) {
                btnTable.row();
            }
            MyButton chooseLevelBtn = createChooseLevelBtn(level, i);
            btnTable.add(chooseLevelBtn)
                    .padLeft(padSide / 2).padRight(padSide / 2).width(btnSize.getWidth() * 1.5f).height(btnSize.getHeight() * 1.5f);
            if (i == 0) {
                btnTable.padBottom(padSide);
            }
            i++;
            chooseLevelBtn.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        }
        float extraHeight = ScreenDimensionsManager.getScreenHeight() - btnTableHeight;
        table.add(createGameTitle()).height(extraHeight / 2).row();
        table.add().height(extraHeight / 2).row();
        Table contentContainer = new Table();
        contentContainer.add(btnTable).top().height(btnTableHeight);
        Table difficultyButtonsTable = kidLearnDifficultyService.createDifficultyButtonsTable(enumClass, true);
        contentContainer.add(difficultyButtonsTable).top().height(btnTableHeight);
        table.add(contentContainer).top().height(btnTableHeight);
        new ActorAnimation(table, getAbstractScreen()).animateFastFadeIn();
        getAllTable().add(table).grow();
    }

    private Table createGameTitle() {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        FontConfig.FONT_SIZE * 2f, 8f)).setText("Caterpillar ordering").build());
    }

    private MyButton createChooseLevelBtn(KidLearnEngWordsLevel level, int index) {
        SkelClassicButtonSkin skin = SkelClassicButtonSkin.valueOf("KIDLEARN_ENGWORDS_" + index);
        skin = kidLearnPreferencesManager.getLevelScore(level) == KidLearnEngWordsGameCreator.TOTAL_QUESTIONS ?
                SkelClassicButtonSkin.valueOf("KIDLEARN_ENGWORDS_FIN_" + index) : skin;
        MyButton btn = new ImageButtonBuilder(skin, Game.getInstance().getAbstractScreen())
                .padBetweenImageAndText(1.3f)
                .textBackground(MainResource.transparent_background)
                .setFixedButtonSize(getChooseLevelBtnSize())
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        FontConfig.FONT_SIZE * 3f, 7f))
                .setText(level.category)
                .build();
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getScreenManager().showGameScreen(new KidLearnGameContext(level));
            }
        });
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
