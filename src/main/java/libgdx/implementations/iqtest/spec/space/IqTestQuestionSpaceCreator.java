package libgdx.implementations.iqtest.spec.space;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.iqtest.IqTestSpaceImageQcQuestionIncrementRes;
import libgdx.implementations.iqtest.IqTestSpaceImageQwQuestionIncrementRes;
import libgdx.implementations.iqtest.spec.IqTestBaseLevelCreator;
import libgdx.implementations.iqtest.spec.IqTestGameType;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.RGBColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IqTestQuestionSpaceCreator extends IqTestBaseLevelCreator {

    public static final String CELL_NAME = "cell";
    private int currentQuestion = 0;
    private int TOTAL_OPTIONS = 4;
    private int correctAnswers = 0;

    private final static String MAIN_TABLE_NAME = "MAIN_TABLE_NAME";
    private AbstractScreen abstractScreen;
    private ActorAnimation actorAnimation;

    public IqTestQuestionSpaceCreator(AbstractScreen abstractScreen) {
        this.abstractScreen = abstractScreen;
        actorAnimation = new ActorAnimation(abstractScreen);
        addQuestionScreen(currentQuestion);
        abstractScreen.setBackgroundColor(RGBColor.LIGHT_BLUE);
    }

    @Override
    public void refreshLevel() {
        Group root = Game.getInstance().getAbstractScreen().getStage().getRoot();
        root.findActor(MAIN_TABLE_NAME).remove();
        addQuestionScreen(currentQuestion);
    }

    @Override
    protected int getCurrentQuestionToDisplay() {
        return currentQuestion + 1;
    }

    @Override
    protected void startNewGame() {
        currentQuestion = 0;
        correctAnswers = 0;
        refreshLevel();
    }

    @Override
    protected String getScore() {
        String prefix = correctAnswers > 0 ? "+" : "";
        return prefix + correctAnswers;
    }


    @Override
    public void addQuestionScreen(int questionNr) {

        Table table = new Table();
        table.setName(MAIN_TABLE_NAME);
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(createHeader()).pad(verticalGeneralMarginDimen).width(screenWidth).row();
        table.add().growY().row();
        float allTableWidth = ScreenDimensionsManager.getScreenWidth(75);
        Table optionsTable = createOptionsTable(allTableWidth);
        optionsTable.setVisible(false);
        actorAnimation.animateFadeIn(optionsTable, 1f);
        table.add(optionsTable)
                .padBottom(verticalGeneralMarginDimen * 5)
                .width(allTableWidth)
                .row();
        table.add().growY().row();
        table.setFillParent(true);
        abstractScreen.addActor(table);
    }

    private Table createOptionsTable(float totalWidth) {
        Table table = new Table();

        Res qwRes = resourceService.getByName(String.format(IqTestSpaceImageQwQuestionIncrementRes.QW, currentQuestion));
        Res qcRes = resourceService.getByName(String.format(IqTestSpaceImageQcQuestionIncrementRes.QC, currentQuestion));
        Image wrongImage = GraphicUtils.getImage(qwRes);

        Image correctImage1 = GraphicUtils.getImage(qcRes);
        Image correctImage2 = GraphicUtils.getImage(qcRes);
        Image correctImage3 = GraphicUtils.getImage(qcRes);

        List<Image> correctImgList = new ArrayList<>(Arrays.asList(correctImage1, correctImage2, correctImage3));

        Random random = new Random();
        int randomWrongImgPos = random.nextInt(TOTAL_OPTIONS);
        int pos = 0;
        int colsRows = 2;
        float sideDimen = totalWidth / colsRows;
        for (int i = 0; i < colsRows; i++) {
            if (i > 0) {
                table.row();
            }
            for (int j = 0; j < colsRows; j++) {
                Table cell = new Table();
                cell.setTouchable(Touchable.enabled);
                Image img;
                final boolean isWrongImagePressed;
                if (pos == randomWrongImgPos) {
                    isWrongImagePressed = true;
                    img = wrongImage;
                } else {
                    isWrongImagePressed = false;
                    img = correctImgList.get(0);
                    correctImgList.remove(img);
                }

                float imgHeight = img.getHeight();
                float imgWidth = img.getWidth();
                if (imgHeight > imgWidth) {
                    img.setHeight(sideDimen);
                    img.setWidth(ScreenDimensionsManager.getNewWidthForNewHeight(img.getHeight(), imgWidth, imgHeight));
                } else {
                    img.setWidth(sideDimen);
                    img.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(img.getWidth(), imgWidth, imgHeight));
                }
                MyButton cellBtn = new ButtonBuilder()
                        .setButtonSkin(new ButtonSkin() {
                            @Override
                            public Drawable getImgUp() {
                                return img.getDrawable();
                            }

                            @Override
                            public Drawable getImgDown() {
                                return img.getDrawable();
                            }

                            @Override
                            public Drawable getImgChecked() {
                                return img.getDrawable();
                            }

                            @Override
                            public Drawable getImgDisabled() {
                                return img.getDrawable();
                            }

                            @Override
                            public FontColor getButtonDisabledFontColor() {
                                return null;
                            }
                        }).build();

                cellBtn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        currentQuestion++;
                        if (isWrongImagePressed) {
                            correctAnswers++;
                            iqTestPreferencesManager.putLevelScore(getIqTestGameType(), correctAnswers);
                            scoreLabel.setText(getScore());
                            processCorrectAnswerPressed(new Runnable() {
                                @Override
                                public void run() {
                                    refreshLevel();
                                }
                            });
                        } else {
                            actorAnimation.animatePulse();
                            refreshLevel();
                        }
                    }
                });

                cellBtn.setWidth(img.getWidth());
                cellBtn.setHeight(img.getHeight());
                cell.add(cellBtn).width(img.getWidth()).height(img.getHeight());
                setRandomStartingRotation(cellBtn, random);
                table.add(cell).pad(sideDimen / 4).width(img.getWidth()).height(img.getHeight());
                pos++;
            }
        }
        return table;
    }

    private void setRandomStartingRotation(MyButton image, Random random) {
        image.setTransform(true);
        image.setOrigin(Align.center);
        image.addAction(Actions.rotateBy(random.nextInt(150)));
        rotateImg(image, random.nextInt(20) + 5);
    }

    private void rotateImg(final MyButton image, final int duration) {
        RunnableAction run = new RunnableAction();
        run.setRunnable(new ScreenRunnable(abstractScreen) {
            @Override
            public void executeOperations() {
                rotateImg(image, duration);
            }
        });
        image.addAction(Actions.sequence(Actions.rotateBy(150f, duration), run));
    }


    @Override
    protected IqTestGameType getIqTestGameType() {
        return IqTestGameType.SPACE;
    }

}
