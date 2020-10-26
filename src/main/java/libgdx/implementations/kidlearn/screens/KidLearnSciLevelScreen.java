package libgdx.implementations.kidlearn.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.List;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnDifficultyService;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciBodyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciRecyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateLevel;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnSciLevelScreen extends AbstractScreen<KidLearnScreenManager> {

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
        List<KidLearnSciBodyLevel> bodyLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnSciBodyLevel.class, difficultyLevelClass());
        List<KidLearnSciFeedLevel> feedLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnSciFeedLevel.class, difficultyLevelClass());
        List<KidLearnSciRecyLevel> recyLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnSciRecyLevel.class, difficultyLevelClass());
        List<KidLearnSciStateLevel> stateLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnSciStateLevel.class, difficultyLevelClass());
        int i = 0;
        for (KidLearnSciBodyLevel level : bodyLevels) {
            addLevelToTable(btnTable, i, level);
            i++;
        }
        for (KidLearnSciFeedLevel level : feedLevels) {
            addLevelToTable(btnTable, i, level);
            i++;
        }
        for (KidLearnSciRecyLevel level : recyLevels) {
            addLevelToTable(btnTable, i, level);
            i++;
        }
        for (KidLearnSciStateLevel level : stateLevels) {
            addLevelToTable(btnTable, i, level);
            i++;
        }
        float btnTableHeight = ScreenDimensionsManager.getScreenHeightValue(70);
        float extraHeight = ScreenDimensionsManager.getScreenHeight() - btnTableHeight;
        table.add(createGameTitle()).height(extraHeight / 2).row();
        table.add().height(extraHeight / 2).row();
        Table contentContainer = new Table();
        Table difficultyButtonsTable = kidLearnDifficultyService.createDifficultyButtonsTable(difficultyLevelClass(), false);
        contentContainer.add(difficultyButtonsTable).height(ScreenDimensionsManager.getScreenHeightValue(10)).top().row();
        contentContainer.add(btnTable).top().height(btnTableHeight);
        table.add(contentContainer).top().height(btnTableHeight);
        new ActorAnimation(table, getAbstractScreen()).animateFastFadeIn();
        getAllTable().add(table).grow();
    }

    private <T extends Enum & KidLearnLevel & KidLearnSciLevel> void addLevelToTable(Table btnTable, int i, T level) {
        int colspan = 1;
        MyButton chooseLevelBtn = createChooseLevelBtn(level, i);
        float padSide = MainDimen.horizontal_general_margin.getDimen();
        ButtonSize buttonSize = getChooseLevelBtnSize();
        btnTable.add(chooseLevelBtn).colspan(colspan)
                .padLeft(padSide).padBottom(padSide).width(buttonSize.getWidth()).height(buttonSize.getHeight());
        chooseLevelBtn.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
    }

    public static Class<KidLearnSciFeedLevel> difficultyLevelClass() {
        return KidLearnSciFeedLevel.class;
    }

    private Table createGameTitle() {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        FontConfig.FONT_SIZE * 2f, 8f)).setText(KidLearnGameLabel.l_sci_title.getText()).build());
    }

    private <T extends Enum & KidLearnLevel & KidLearnSciLevel> MyButton createChooseLevelBtn(T level, int index) {
        ButtonSize btnSize = getChooseLevelBtnSize();
        MyButton btn = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.KIDLEARN_SCI_LEVEL)
                .setFixedButtonSize(btnSize)
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        FontConfig.FONT_SIZE, 4f))
                .setText(level.title())
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
        return SkelClassicButtonSize.KIDLEARN_SCI_LEVEL;
    }


    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
