package libgdx.implementations.applepie.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.tuple.MutablePair;

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
import libgdx.implementations.applepie.spec.ApplePieRecipe;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class ApplePieGameScreen extends AbstractScreen<ApplePieScreenManager> {

    public static final String PRESS_BTN = "PressBtn";
    public static final int MAX_NR_INGR_ROW = 10;
    private Table allTable;
    private MyButton hoverBackButton;
    private Map<ApplePieIngredient, Integer> ingrNeeded = new LinkedHashMap<>();
    private Map<ApplePieIngredient, Integer> ingrNeededOriginal = new LinkedHashMap<>();
    private Map<ApplePieIngredient, Integer> ingrPressIndex = new LinkedHashMap<>();
    private Map<Integer, Integer> piesToCook = new LinkedHashMap<>();
    private Set<ApplePieIngredient> ingredientToIncrement = new HashSet<>();
    public static final int NR_OF_ROWS_FOR_INGREDIENT = 1;
    private int cookedPercent = 0;
    private MutableLong countdownAmountMillis;
    private ScheduledExecutorService executorService;
    private float delayBtnPressClick = 0;
    private int nrOfPiesToCook = 1;

    public ApplePieGameScreen() {
        init();
    }

    private void init() {
        for (MutablePair<ApplePieIngredient, Integer> res : ApplePieRecipe.APPLE_PIE.getApplePieIngredients()) {
            ingrNeeded.put(res.left, res.right * nrOfPiesToCook);
            ingrNeededOriginal.put(res.left, res.right * nrOfPiesToCook);
            ingrPressIndex.put(res.left, 0);
        }
        for (int i = 0; i < nrOfPiesToCook; i++) {
            piesToCook.put(i, 0);
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
        allTable.add(createAllIngredientTable()).width(ScreenDimensionsManager.getScreenWidth());
        allTable.row();
        addBuyBtn();
        autoPressBtn();
    }

    private void addBuyBtn() {
        MyButton pressBtn = createPressBtn();
        pressBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                createIngredient();
            }
        });
        allTable.add(pressBtn).width(pressBtn.getWidth()).height(pressBtn.getHeight()).row();
    }

    private void createIngredient() {
        for (ApplePieIngredient ingredient : ingredientToIncrement) {
            int nrOfIngrNeeded = ingrNeededOriginal.get(ingredient);
            int pressIndex = ingrPressIndex.get(ingredient);
            int j = Integer.parseInt(String.valueOf(pressIndex).substring(String.valueOf(pressIndex).length() - 1));
            Actor img = getRoot().findActor(getIngredientImgName(ingredient, j));
            if (img != null) {
                img.setVisible(true);
            }
            System.out.println(getIngredientImgName(ingredient, j));
            System.out.println(nrOfIngrNeeded + "");
            if (pressIndex > 0 && pressIndex % MAX_NR_INGR_ROW == 0) {
                resetIngrSquare(ingredient);
            }
            if (pressIndex == nrOfIngrNeeded) {
                pressIndex = 1;
                resetIngrSquare(ingredient);
            } else {
                pressIndex++;
            }
            ingrNeeded.put(ingredient, ingrNeeded.get(ingredient) - 1);
            ingrPressIndex.put(ingredient, pressIndex);
            if (ingrNeeded.get(ingredient) == 0) {
                break;
            }
        }
        if (allIngredientsFull()) {
            addAction(Actions.sequence(Actions.delay(1f), Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    createCookPieTable();
                }
            })));
        }
    }

    private void resetIngrSquare(ApplePieIngredient ingredient) {
        for (int b = 1; b < MAX_NR_INGR_ROW; b++) {
            Actor actor = getRoot().findActor(getIngredientImgName(ingredient, b));
            if (actor != null) {
                actor.setVisible(false);
            } else {
                break;
            }
        }
    }

    private boolean allIngredientsFull() {
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
        return allIngredientsFull;
    }

    private MyButton createPressBtn() {
        final MyButton btn = new ButtonBuilder()
                .setButtonName(PRESS_BTN)
                .setButtonSkin(SkelClassicButtonSkin.APPLEPIE_PRESS_BTN)
                .setFixedButtonSize(SkelClassicButtonSize.APPLEPIE_PRESS_BTN).build();
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (delayBtnPressClick > 0) {
                    btn.setDisabled(true);
                    btn.addAction(Actions.sequence(Actions.delay(delayBtnPressClick), Utils.createRunnableAction(new Runnable() {
                        @Override
                        public void run() {
                            btn.setDisabled(false);
                        }
                    })));
                }
            }
        });
        return btn;
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
        return ScreenDimensionsManager.getScreenWidth(90);
    }

    private Table createAllIngredientTable() {
        Table table = new Table();
        for (ApplePieIngredient ingredient : ApplePieIngredient.values()) {
            table.add(createIngredientTable(ingredient)).growX().height(ScreenDimensionsManager.getScreenHeight(10)).row();
        }
        return table;
    }

    private Table createIngredientTable(ApplePieIngredient ingredient) {
        Table table = new Table();
        Table rowsTable = new Table();
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        float addIngrDimen = dimen * 4f;
        for (int j = 0; j < MAX_NR_INGR_ROW; j++) {
            Image image = GraphicUtils.getImage(MainResource.btn_lowcolor_down);
            image.setName(getIngredientImgName(ingredient, j));
            image.setVisible(false);
            rowsTable.add(image).pad(dimen / 3).width(addIngrDimen).height(addIngrDimen);
        }
        rowsTable.row();
        float iconMult = 1.2f;
        float iconDimen = addIngrDimen * iconMult;
        table.add(createIngredientIcon(ingredient, iconDimen)).width(iconDimen).height(iconDimen);
        table.add(rowsTable).width(ScreenDimensionsManager.getScreenWidth() - iconDimen);
        return table;
    }

    private Stack createIngredientIcon(ApplePieIngredient ingr, float iconDimen) {
        Stack stack = new Stack();
        stack.add(GraphicUtils.getImage(MainResource.heart_full));
        MyWrappedLabel amountLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(iconDimen).setFontConfig(new FontConfig(FontColor.BLUE.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.2f))).setText(ingrNeeded.get(ingr) + " " + ingr.name()).build());
        stack.add(amountLabel);
        return stack;
    }

    private Table createCookedPiesTable() {
        Table table = new Table();
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        float dimen = horizontalGeneralMarginDimen * 2;
        for (Map.Entry<Integer, Integer> e : piesToCook.entrySet()) {
            table.add(createCookedPieIcon(e.getValue(), dimen)).pad(horizontalGeneralMarginDimen);
        }
        return table;
    }

    private Integer getFirstUncookedPie() {
        Integer pie = null;
        for (Map.Entry<Integer, Integer> e : piesToCook.entrySet()) {
            if (e.getValue() < 100) {
                return e.getKey();
            }
        }
        return pie;
    }

    private Stack createCookedPieIcon(int percent, float dimen) {
        Stack stack = new Stack();
        stack.add(GraphicUtils.getImage(MainResource.heart_full));
        MyWrappedLabel amountLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(dimen).setFontConfig(new FontConfig(FontColor.BLUE.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.2f))).setText(percent + "%").build());
        stack.add(amountLabel);
        return stack;
    }

    private Stack createCookPieTable() {
        allTable.clear();
        float cookPieSideDimen = ScreenDimensionsManager.getScreenWidth(60);
        Stack stack = new Stack();
        MyWrappedLabel percentCooked = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWidth(cookPieSideDimen).setFontConfig(new FontConfig(FontColor.BLUE.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.2f))).setText(cookedPercent + "%").build());
        Image cookImage = GraphicUtils.getImage(MainResource.heart_full);
        cookImage.addAction(Actions.alpha(cookedPercent / 100f));
        stack.add(cookImage);
        stack.add(percentCooked);
        allTable.add(createCookedPiesTable()).row();
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
                cookPie();
            }
        });
        allTable.add(pressBtn).width(pressBtn.getWidth()).height(pressBtn.getHeight()).row();
        return stack;
    }

    private void cookPie() {
        if (cookedPercent == 100) {
            cookedPercent = -10;
        }
        cookedPercent = cookedPercent + 10;
        piesToCook.put(getFirstUncookedPie(), cookedPercent);
        createCookPieTable();
    }

    private void autoPressBtn() {
        int seconds = 30;
        countdownAmountMillis = new MutableLong(seconds * 1000);
        final int period = 300;
        executorService.scheduleAtFixedRate(new ScreenRunnable(getAbstractScreen()) {
            @Override
            public void executeOperations() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (countdownAmountMillis.getValue() <= 0) {
                            executorService.shutdown();
                        }
                        createIngredient();
                        if (allIngredientsFull()) {
                            cookPie();
                        }
                        countdownAmountMillis.subtract(period);

                    }
                });
            }

            @Override
            public void executeOperationsAfterScreenChanged() {
                executorService.shutdown();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    private String getIngredientImgName(ApplePieIngredient ingredient, int j) {
        return ingredient.name() + "_" + j;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
