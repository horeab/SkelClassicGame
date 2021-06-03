package libgdx.implementations.iqtest.spec.memnum;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyLabel;
import libgdx.controls.labelimage.LabelImageConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.iqtest.IqTestSpecificResource;
import libgdx.implementations.iqtest.spec.IqTestBaseLevelCreator;
import libgdx.implementations.iqtest.spec.IqTestGameType;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ActorPositionManager;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

import java.util.*;

public class IqTestQuestionMemNumCreator extends IqTestBaseLevelCreator {

    public static final String CELL_NAME = "cell";
    private int currentQuestion = 0;

    private final static String MAIN_TABLE_NAME = "MAIN_TABLE_NAME";
    private AbstractScreen abstractScreen;
    private ActorAnimation actorAnimation;
    private List<Integer> currentAvailableNrs = new ArrayList<>();
    private List<Integer> answersToPress;
    private static final int ROWS = 4;
    private static final int COLS = 4;
    private List<MyLabel> allCellTextLabels = new ArrayList<>();
    private List<MyButton> allCellBtns = new ArrayList<>();

    public IqTestQuestionMemNumCreator(AbstractScreen abstractScreen) {
        this.abstractScreen = abstractScreen;
        actorAnimation = new ActorAnimation(abstractScreen);
        addQuestionScreen(0);
        abstractScreen.setBackgroundColor(RGBColor.LIGHT_BLUE);
    }

    @Override
    public void refreshLevel() {
        currentAvailableNrs.clear();
        Group root = Game.getInstance().getAbstractScreen().getStage().getRoot();
        root.findActor(MAIN_TABLE_NAME).remove();
        int maxInitNumber = 3;
        for (int i = 1; i <= maxInitNumber; i++) {
            currentAvailableNrs.add(i);

        }
        for (int i = 1; i <= currentQuestion; i++) {
            currentAvailableNrs.add(i + maxInitNumber);
        }
        addQuestionScreen(currentQuestion);
    }

    @Override
    protected int getCurrentQuestionToDisplay() {
        return currentQuestion + 1;
    }

    @Override
    protected void startNewGame() {
        currentQuestion = 0;
        refreshLevel();
    }

    private Map<Integer, Integer> createRandomPositionsForNumbers() {
        Map<Integer, Integer> map = new HashMap<>();
        Random random = new Random();
        int totalCells = COLS * ROWS;
        for (Integer nr : currentAvailableNrs) {
            int randPos = random.nextInt(totalCells);
            while (map.containsKey(randPos)) {
                randPos = random.nextInt(totalCells);
            }
            map.put(randPos, nr);
        }
        return map;
    }

    @Override
    protected String getScore() {
        String prefix = currentQuestion > 0 ? "+" : "";
        return prefix + currentQuestion;
    }

    private void clearPressedLetters() {
        if (answersToPress != null) {
            answersToPress.clear();
        }
    }

    @Override
    public void addQuestionScreen(int questionNr) {
        clearPressedLetters();
        answersToPress = new ArrayList<>(currentAvailableNrs);

        Table table = new Table();
        table.setName(MAIN_TABLE_NAME);
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(createHeader()).pad(verticalGeneralMarginDimen).width(screenWidth).row();
        table.add().growY().row();
        float allTableWidth = ScreenDimensionsManager.getScreenWidth(75);


        Image eye = GraphicUtils.getImage(IqTestSpecificResource.eye);
        abstractScreen.addActor(eye);
        final float durationEye = 2f;
        final float durationShowNr = 1f;
        actorAnimation.animateFadeOut(eye, durationEye, true);
        ActorPositionManager.setActorCenterScreen(eye);
        abstractScreen.addAction(Actions.delay(durationEye, Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                table.add(createCellsTable(allTableWidth))
                        .padBottom(verticalGeneralMarginDimen * 5)
                        .width(allTableWidth)
                        .row();
                abstractScreen.addAction(Actions.delay(durationShowNr, Utils.createRunnableAction(new Runnable() {
                    @Override
                    public void run() {
                        showAllCellTextLabels(false);
                        disabledAllCellTextButtons(false);
                    }
                })));
            }
        })));


        table.add().growY().row();
        table.setFillParent(true);
        abstractScreen.addActor(table);
    }

    private Table createCellsTable(float totalWidth) {
        Table table = new Table();
        Map<Integer, Integer> randomPositionsForNumbers = createRandomPositionsForNumbers();
        float pad = MainDimen.horizontal_general_margin.getDimen() * 1.3f;
        int pos = 0;
        float sideDimen = totalWidth / COLS;
        for (int i = 0; i < ROWS; i++) {
            if (i > 0) {
                table.row();
            }
            for (int j = 0; j < COLS; j++) {
                Table cell = new Table();
                if (randomPositionsForNumbers.containsKey(pos)) {
                    Integer nr = randomPositionsForNumbers.get(pos);
                    MyButton cellBtn = new ButtonBuilder()
                            .setWrappedText(new LabelImageConfigBuilder().setText(nr + "").setFontConfig(new FontConfig(Color.WHITE, Color.BLACK,
                                    FontConfig.FONT_SIZE * 2.1f, FontConfig.STANDARD_BORDER_WIDTH * 8.5f)))
                            .setButtonSkin(SkelClassicButtonSkin.IQTEST_MEM_NUM_BLUE_BTN).build();
                    MyLabel cellTextLabel = cellBtn.getCenterRowLabels().get(0);
                    allCellTextLabels.add(cellTextLabel);
                    allCellBtns.add(cellBtn);
                    cell.add(cellBtn).width(sideDimen).height(sideDimen);
                    cellBtn.setName(CELL_NAME + nr);
                    cell.setTouchable(Touchable.enabled);
                    cellBtn.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (!answersToPress.isEmpty()) {
                                if (isCorrectPressed(nr)) {
                                    answersToPress.remove(nr);
                                    cellTextLabel.setVisible(true);
                                    if (answersToPress.isEmpty()) {
                                        for (MyButton button : allCellBtns) {
                                            button.setButtonSkin(SkelClassicButtonSkin.IQTEST_MEM_NUM_GREEN_BTN);
                                        }
                                        disabledAllCellTextButtons(true);
                                        currentQuestion++;
                                        iqTestPreferencesManager.putLevelScore(getIqTestGameType(), currentQuestion);
                                        scoreLabel.setText(getScore());
                                        processCorrectAnswerPressed(new Runnable() {
                                            @Override
                                            public void run() {
                                            }
                                        });
                                        abstractScreen.addAction(Actions.delay(2f, Utils.createRunnableAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                refreshLevel();
                                            }
                                        })));
                                    }
                                } else {
                                    disabledAllCellTextButtons(true);
                                    showAllCellTextLabels(true);
                                    cellBtn.setButtonSkin(SkelClassicButtonSkin.IQTEST_MEM_NUM_RED_BTN);
                                    MyButton correctAnsw = abstractScreen.getRoot().findActor(CELL_NAME + Collections.min(answersToPress));
                                    correctAnsw.setButtonSkin(SkelClassicButtonSkin.IQTEST_MEM_NUM_GREEN_BTN);
                                    newGameBtn.setTransform(true);
                                    actorAnimation.animateZoomInZoomOut(newGameBtn);
                                }
                            }
                        }
                    });
                }
                table.add(cell).pad(pad).width(sideDimen).height(sideDimen);
                pos++;
            }
        }
        disabledAllCellTextButtons(true);
        return table;
    }

    private boolean isCorrectPressed(int nr) {
        return Collections.min(answersToPress) == nr;
    }

    private void disabledAllCellTextButtons(boolean disabled) {
        for (MyButton button : allCellBtns) {
            button.setDisabled(disabled);
            button.setTouchable(disabled ? Touchable.disabled : Touchable.enabled);
        }
    }

    private void showAllCellTextLabels(boolean visible) {
        for (MyLabel label : allCellTextLabels) {
            label.setVisible(visible);
        }
    }

    @Override
    protected IqTestGameType getIqTestGameType() {
        return IqTestGameType.MEM_NUM;
    }

}
