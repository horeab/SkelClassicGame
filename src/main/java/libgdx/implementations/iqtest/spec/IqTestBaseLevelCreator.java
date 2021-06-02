package libgdx.implementations.iqtest.spec;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.iqtest.screens.IqTestGameOverScreen;
import libgdx.resources.MainResource;
import libgdx.resources.ResourceService;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public abstract class IqTestBaseLevelCreator {

    protected final static String MAIN_TABLE_NAME = "MAIN_TABLE_NAME";

    protected IqTestPreferencesManager iqTestPreferencesManager = new IqTestPreferencesManager();
    protected ResourceService resourceService = Game.getInstance().getMainDependencyManager().createResourceService();
    protected MyWrappedLabel scoreLabel;
    protected MyButton newGameBtn;

    public abstract void refreshLevel();

    protected abstract IqTestGameType getIqTestGameType();

    protected abstract int getTotalQuestions();

    protected abstract int getCurrentQuestionToDisplay();

    protected abstract void startNewGame();

    public abstract void addQuestionScreen(int currentQuestion);

    protected abstract String getScore();

    private Image createMugImg(float mugDimen) {
        final Image mug = GraphicUtils.getImage(MainResource.mug_black_border);
        mug.setWidth(mugDimen);
        mug.setHeight(mugDimen);
        new ActorAnimation(Game.getInstance().getAbstractScreen()).animateZoomInZoomOut(mug);
        mug.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                IqTestGameOverScreen.displayInAppPurchasesPopup(new Runnable() {
                    @Override
                    public void run() {
                        refreshLevel();
                    }
                });
            }
        });
        if (!Utils.isValidExtraContent()) {
            mug.setVisible(false);
        }
        return mug;
    }

    protected Table createHeader() {
        Table table = new Table();
        MyButton newGameBtn = createNewGameBtn();
        MyButton skipBtn = createSkipBtn();
        float horizDimen = MainDimen.horizontal_general_margin.getDimen();

        float mugDimen = horizDimen * 7;
        final Image mug = createMugImg(mugDimen);

        Table firstRow = new Table();
        firstRow.add(mug).padLeft(MainButtonSize.BACK_BUTTON.getWidth() * 1.2f).growX().width(mugDimen).height(mugDimen);
        firstRow.add().growX();
        firstRow.add().growX();
        firstRow.add(newGameBtn).pad(horizDimen).width(newGameBtn.getWidth()).height(newGameBtn.getHeight());
        if (skipBtn != null) {
            firstRow.add(skipBtn).pad(horizDimen).width(skipBtn.getWidth()).height(skipBtn.getHeight());
        }

        Table secondRow = createHeaderSecondRow();

        table.add(firstRow).growX();
        table.row();
        table.add(secondRow).growX();
        return table;
    }

    private Table createHeaderSecondRow() {

        scoreLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(getScore())
                .setFontConfig(new FontConfig(RGBColor.LIGHT_GREEN.toColor(), RGBColor.GREEN.toColor(),
                        FontConfig.FONT_SIZE * 2.1f, FontConfig.STANDARD_BORDER_WIDTH * 8.5f))
                .setSingleLineLabel().build());
        scoreLabel.setTransform(true);

        MyWrappedLabel currentQLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(getCurrentQuestionToDisplay() + "/" + getTotalQuestions())
                .setFontConfig(new FontConfig(Color.WHITE, Color.BLACK,
                        FontConfig.FONT_SIZE * 1.5f, FontConfig.STANDARD_BORDER_WIDTH * 8.5f))
                .setSingleLineLabel().build());

        MyWrappedLabel descr = null;
        if (getIqTestGameType().getDescr() != null) {
            descr = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setText(getIqTestGameType().getDescr().getText())
                    .setWrappedLineLabel(ScreenDimensionsManager.getScreenWidth(55))
                    .setFontConfig(new FontConfig(Color.BLACK,
                            FontConfig.FONT_SIZE * 1.1f)).build());
        }

        Table secondRow = new Table();
        float marginDimen = MainDimen.horizontal_general_margin.getDimen();
        float scoreLevelWidth = ScreenDimensionsManager.getScreenWidth(20);
        secondRow.add(scoreLabel).padLeft(marginDimen).width(scoreLevelWidth);
        secondRow.add().growX();
        if (descr != null) {
            secondRow.add(descr).growX();
        } else {
            secondRow.add().growX();
        }
        secondRow.add().growX();
        secondRow.add(currentQLabel).padRight(marginDimen).width(scoreLevelWidth);
        return secondRow;
    }

    protected void processCorrectAnswerPressed(Runnable afterAnimation) {
        float scaleFactor = 0.3f;
        float duration = 0.2f;
        if (scoreLabel != null) {
            scoreLabel.addAction(Actions.sequence(Actions.scaleBy(scaleFactor, scaleFactor, duration),
                    Actions.scaleBy(-scaleFactor, -scaleFactor, duration), Utils.createRunnableAction(afterAnimation)));
        }
    }

    private MyButton createNewGameBtn() {
        newGameBtn = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.IQTEST_NEW_GAME_BTN)
                .setFixedButtonSize(SkelClassicButtonSize.IQTEST_HEADER_IMG_BUTTON).build();
        newGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startNewGame();
            }

        });
        return newGameBtn;
    }

    protected MyButton createSkipBtn() {
        return null;
    }
}
