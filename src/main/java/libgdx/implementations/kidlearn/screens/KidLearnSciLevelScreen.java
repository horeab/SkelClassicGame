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
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciBodyGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciBodyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciRecyGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciRecyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateLevel;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

public class KidLearnSciLevelScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    private KidLearnPreferencesManager kidLearnPreferencesManager = new KidLearnPreferencesManager();
    private KidLearnDifficultyService kidLearnDifficultyService = new KidLearnDifficultyService();

    @Override
    public void buildStage() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(KidLearnSpecificResource.scroll_background_sci);
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        setUpAllTable();
        createChooseLevelMenu();
        kidLearnDifficultyService.setBackgroundDiff(new KidLearnPreferencesManager().getDifficultyLevel(difficultyLevelClass()), getBackgroundStage());
    }

    private void createChooseLevelMenu() {
        Table table = new Table();
        Table btnTable = new Table();
        List<KidLearnSciBodyLevel> bodyLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnSciBodyLevel.class, difficultyLevelClass());
        List<KidLearnSciFeedLevel> feedLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnSciFeedLevel.class, difficultyLevelClass());
        List<KidLearnSciRecyLevel> recyLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnSciRecyLevel.class, difficultyLevelClass());
        List<KidLearnSciStateLevel> stateLevels = kidLearnDifficultyService.getLevelsForDifficulty(KidLearnSciStateLevel.class, difficultyLevelClass());
        for (KidLearnSciBodyLevel level : bodyLevels) {
            addLevelToTable(btnTable, level, KidLearnSciBodyGameCreator.TOTAL_QUESTIONS);
        }
        for (KidLearnSciFeedLevel level : feedLevels) {
            addLevelToTable(btnTable, level, KidLearnSciFeedGameCreator.TOTAL_QUESTIONS);
        }
        for (KidLearnSciRecyLevel level : recyLevels) {
            addLevelToTable(btnTable, level, KidLearnSciRecyGameCreator.TOTAL_QUESTIONS);
        }
        for (KidLearnSciStateLevel level : stateLevels) {
            addLevelToTable(btnTable, level, KidLearnSciStateGameCreator.TOTAL_QUESTIONS);
        }
        float btnTableHeight = ScreenDimensionsManager.getScreenHeightValue(70);
        float extraHeight = ScreenDimensionsManager.getScreenHeight() - btnTableHeight;
        table.add(KidLearnControlsUtils.createGameTitleAllWidth(
                KidLearnGameLabel.l_sci_title.getText(), kidLearnDifficultyService.getTitleBorderColor(difficultyLevelClass()))).height(extraHeight / 2).row();
        table.add().height(extraHeight / 2).row();
        Table contentContainer = new Table();
        Table difficultyButtonsTable = kidLearnDifficultyService.createDifficultyButtonsTable(difficultyLevelClass(), false, new KidLearnPreferencesManager().getDifficultyLevel(difficultyLevelClass()));
        contentContainer.add(difficultyButtonsTable).height(ScreenDimensionsManager.getScreenHeightValue(10)).top().row();
        contentContainer.add(btnTable).top().height(btnTableHeight);
        table.add(contentContainer).top().height(btnTableHeight);
        new ActorAnimation(table, getAbstractScreen()).animateFastFadeIn();
        getAllTable().add(table).grow();
    }

    private <T extends Enum & KidLearnLevel & KidLearnSciLevel> void addLevelToTable(Table btnTable, T level, int totalQuestions) {
        int colspan = 1;
        Table chooseLevelBtn = createChooseLevelBtn(level, totalQuestions);
        float padSide = MainDimen.horizontal_general_margin.getDimen();
        ButtonSize buttonSize = getChooseLevelBtnSize();
        btnTable.add(chooseLevelBtn).colspan(colspan)
                .padLeft(padSide).padBottom(padSide).width(buttonSize.getWidth()).height(buttonSize.getHeight());
        chooseLevelBtn.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
    }

    public static Class<KidLearnSciFeedLevel> difficultyLevelClass() {
        return KidLearnSciFeedLevel.class;
    }

    private <T extends Enum & KidLearnLevel & KidLearnSciLevel> Table createChooseLevelBtn(final T level, int totalQuestions) {
        ButtonSize btnSize = getChooseLevelBtnSize();
        MyButton btn = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.KIDLEARN_SCI_LEVEL)
                .setFixedButtonSize(btnSize)
                .setFontConfig(KidLearnControlsUtils.getBtnLevelFontConfig(level.title()))
                .setText(level.title())
                .build();
        btn.getCenterRow().row();
        float imgSideDimen = MainDimen.side_btn_image.getDimen() * 2;
        Table imgTable = new Table();
        int levelScore = kidLearnPreferencesManager.getLevelScore(level);
        Res categImg = levelScore == totalQuestions ? level.categImg() : KidLearnSpecificResource.valueOf(level.categImg().name() + "_unk");
        imgTable.add(GraphicUtils.getImage(categImg)).height(imgSideDimen).width(imgSideDimen);
        btn.getCenterRow().add(imgTable).padTop(MainDimen.vertical_general_margin.getDimen() * 1.5f);
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getScreenManager().showGameScreen(new KidLearnGameContext(level));
            }
        });
        Table chooseLevelBtnTable = new Table();
        chooseLevelBtnTable.add(btn).width(btnSize.getWidth()).height(btnSize.getHeight());
        KidLearnInAppPurchaseTable inAppPurchaseTable = new KidLearnInAppPurchaseTable(btnSize.getHeight() / 1.3f);
        if (!Utils.isValidExtraContent() && level.isLocked()) {
            btn.setDisabled(true);
            chooseLevelBtnTable = inAppPurchaseTable.createForProVersion(chooseLevelBtnTable);
        }
        return chooseLevelBtnTable;
    }

    private ButtonSize getChooseLevelBtnSize() {
        return SkelClassicButtonSize.KIDLEARN_SCI_LEVEL;
    }


    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
