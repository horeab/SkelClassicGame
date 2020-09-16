package libgdx.implementations.applepie.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import org.apache.commons.lang3.mutable.MutableLong;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.applepie.ApplePieScreenManager;
import libgdx.implementations.applepie.spec.ApplePieIngredient;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class ApplePieGameScreen extends AbstractScreen<ApplePieScreenManager> {

    public static final String PRESS_BTN = "PressBtn";
    private Table allTable;
    private MyButton hoverBackButton;
    private static final int NR_INGR_ROW = 4;
    private Map<ApplePieIngredient, Integer> ingrNeeded = new LinkedHashMap<>();
    private Map<ApplePieIngredient, Integer> ingrPressIndex = new LinkedHashMap<>();
    private Set<ApplePieIngredient> ingredientToIncrement = new HashSet<>();
    public static final int NR_OF_ROWS_FOR_INGREDIENT = 1;
    private int cookedPercent = 0;
    private MutableLong countdownAmountMillis;
    private ScheduledExecutorService executorService;

    public ApplePieGameScreen() {
        init();
    }

    private void init() {
        for (ApplePieIngredient res : ApplePieIngredient.values()) {
            ingrNeeded.put(res, 10);
            ingrPressIndex.put(res, 0);
        }
        ingredientToIncrement.add(new ArrayList<>(ingrNeeded.keySet()).get(0));
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void buildStage() {
        createAllTable();
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
    }

    private void createAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        addActor(allTable);
        if (hoverBackButton != null) {
            hoverBackButton.toFront();
        }

        allTable.add(createObjectiveTable()).width(getObjectiveLabelWidth()).row();
        allTable.add(createAllIngredientTable(NR_OF_ROWS_FOR_INGREDIENT)).width(ScreenDimensionsManager.getScreenWidth());
        allTable.row();
        addBuyBtn(NR_OF_ROWS_FOR_INGREDIENT);
        autoPressBtn();
    }

    private void addBuyBtn(int nrOfRows) {
        MyButton pressBtn = createPressBtn();
        pressBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (ApplePieIngredient ingredient : ingredientToIncrement) {
                    int pressIndex = ingrPressIndex.get(ingredient);
                    int i = (int) Math.ceil(pressIndex / NR_INGR_ROW);
                    int j = Integer.parseInt(String.valueOf(pressIndex).substring(String.valueOf(pressIndex).length() - 1));
                    Actor img = getRoot().findActor(getIngredientImgName(ingredient, i, j));
                    if (img != null) {
                        img.setVisible(true);
                    }
                    System.out.println(pressIndex + " " + i + " " + j);
                    if (pressIndex == nrOfRows * NR_INGR_ROW) {
                        pressIndex = 1;
                        for (int a = 0; a < nrOfRows; a++) {
                            for (int b = 1; b < NR_INGR_ROW; b++) {
                                getRoot().findActor(getIngredientImgName(ingredient, a, b)).setVisible(false);
                            }
                        }
                    } else {
                        pressIndex++;
                    }
                    ingrNeeded.put(ingredient, ingrNeeded.get(ingredient) - 1);
                    ingrPressIndex.put(ingredient, pressIndex);
                    if (ingrNeeded.get(ingredient) == 0) {
                        break;
                    }
                }
                boolean allIngredientsFull = true;
                for (Map.Entry<ApplePieIngredient, Integer> e : ingrNeeded.entrySet()) {
                    if (e.getValue() == 0) {
                        ingredientToIncrement.remove(e.getKey());
                    } else {
                        allIngredientsFull = false;
                        ingredientToIncrement.add(e.getKey());
                        break;
                    }
                }
                if (allIngredientsFull) {
                    cookPie();
                }
            }
        });
        allTable.add(pressBtn).width(pressBtn.getWidth()).height(pressBtn.getHeight()).row();
    }

    private MyButton createPressBtn() {
        return new ButtonBuilder()
                .setButtonName(PRESS_BTN)
                .setButtonSkin(SkelClassicButtonSkin.APPLEPIE_PRESS_BTN)
                .setFixedButtonSize(SkelClassicButtonSize.APPLEPIE_PRESS_BTN).build();
    }

    private Table createObjectiveTable() {
        MyWrappedLabel objectiveLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(getObjectiveLabelWidth()).setFontConfig(new FontConfig(FontColor.BLUE.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.2f))).setText("Cook 5 apple pies!").build());
        Table table = new Table();
        table.add(objectiveLabel).width(getObjectiveLabelWidth());
        return table;
    }

    private float getObjectiveLabelWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(90);
    }

    private Table createAllIngredientTable(int nrOfRows) {
        Table table = new Table();
        for (ApplePieIngredient ingredient : ApplePieIngredient.values()) {
            table.add(createIngredientTable(nrOfRows, ingredient)).growX().height(ScreenDimensionsManager.getScreenHeightValue(10)).row();
        }
        return table;
    }

    private Table createIngredientTable(int nrOfRows, ApplePieIngredient ingredient) {
        Table table = new Table();
        Table rowsTable = new Table();
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        float addIngrDimen = dimen * 4f;
        for (int i = 0; i < nrOfRows; i++) {
            for (int j = 0; j < NR_INGR_ROW; j++) {
                Image image = GraphicUtils.getImage(MainResource.btn_lowcolor_down);
                image.setName(getIngredientImgName(ingredient, i, j));
                image.setVisible(false);
                rowsTable.add(image).pad(dimen / 3).width(addIngrDimen).height(addIngrDimen);
            }
            rowsTable.row();
        }
        float iconMult = 1.2f;
        float iconDimen = addIngrDimen * iconMult;
        table.add(createIngredientIcon(ingredient, NR_INGR_ROW, iconDimen)).width(iconDimen).height(iconDimen);
        table.add(rowsTable).width(ScreenDimensionsManager.getScreenWidth() - iconDimen);
        return table;
    }

    private Stack createIngredientIcon(ApplePieIngredient ingr, int amountNeeded, float iconDimen) {
        Stack stack = new Stack();
        stack.add(GraphicUtils.getImage(MainResource.heart_full));
        MyWrappedLabel amountLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(iconDimen).setFontConfig(new FontConfig(FontColor.BLUE.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.2f))).setText(amountNeeded + " " + ingr.name()).build());
        stack.add(amountLabel);
        return stack;
    }

    private Stack cookPie() {
        allTable.clear();
        float cookPieSideDimen = ScreenDimensionsManager.getScreenWidthValue(60);
        Stack stack = new Stack();
        MyWrappedLabel percentCooked = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(cookPieSideDimen).setFontConfig(new FontConfig(FontColor.BLUE.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.2f))).setText(cookedPercent + "%").build());
        Image cookImage = GraphicUtils.getImage(MainResource.heart_full);
        cookImage.addAction(Actions.alpha(cookedPercent / 100f));
        stack.add(cookImage);
        stack.add(percentCooked);
        allTable.add(stack).width(cookPieSideDimen).height(cookPieSideDimen).row();
        if (cookedPercent == 100) {
            percentCooked.setText("Sell!");
            addAction(Actions.sequence(Actions.delay(1f), Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    ActorAnimation.pressFinger(ScreenDimensionsManager.getScreenWidth() / 2, ScreenDimensionsManager.getScreenHeight() / 2);
                }
            })));
        }
        MyButton pressBtn = createPressBtn();
        pressBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (cookedPercent == 100) {
                    cookedPercent = -10;
                }
                cookedPercent = cookedPercent + 10;
                cookPie();
            }
        });
        allTable.add(pressBtn).width(pressBtn.getWidth()).height(pressBtn.getHeight()).row();
        return stack;
    }

    private void autoPressBtn() {
        int seconds = 30;
        countdownAmountMillis = new MutableLong(seconds * 1000);
        final int period = 500;
        executorService.scheduleAtFixedRate(new ScreenRunnable(getAbstractScreen()) {
            @Override
            public void executeOperations() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (countdownAmountMillis.getValue() <= 0) {
                                executorService.shutdown();
                            }

                            Actor btn = allTable.findActor(PRESS_BTN);
                            if (btn != null) {
                                triggerButtonClicked(btn);
                            }
                            countdownAmountMillis.subtract(period);
                        }catch (Exception e){
                            int i=0;
                        }

                    }
                });
            }

            @Override
            public void executeOperationsAfterScreenChanged() {
                executorService.shutdown();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    public void triggerButtonClicked(Actor button) {
        InputEvent inputEvent = new InputEvent();
        inputEvent.setType(InputEvent.Type.touchDown);
        button.fire(inputEvent);
        inputEvent.setType(InputEvent.Type.touchUp);
        button.fire(inputEvent);
    }

    private String getIngredientImgName(ApplePieIngredient ingredient, int i, int j) {
        return ingredient.name() + "_" + i + "_" + j;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
