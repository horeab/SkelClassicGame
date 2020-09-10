package libgdx.implementations.applepie.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.applepie.ApplePieScreenManager;
import libgdx.implementations.applepie.spec.ApplePieIngredient;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;

public class ApplePieGameScreen extends AbstractScreen<ApplePieScreenManager> {

    private Table allTable;
    private MyButton hoverBackButton;
    private static final int NR_INGR_ROW = 10;
    private int btnPressIndex = 0;

    public ApplePieGameScreen() {
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

        int nrOfRows = 2;
        ApplePieIngredient ingredient = ApplePieIngredient.APPLE;
        allTable.add(createIngredientTable(nrOfRows, ingredient)).growX().height(ScreenDimensionsManager.getScreenHeightValue(10)).row();
        MyButton pressBtn = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.APPLEPIE_PRESS_BTN)
                .setFixedButtonSize(SkelClassicButtonSize.APPLEPIE_PRESS_BTN).build();
        pressBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int i = (int) Math.ceil(btnPressIndex / NR_INGR_ROW);
                int j = Integer.parseInt(String.valueOf(btnPressIndex).substring(String.valueOf(btnPressIndex).length() - 1));
                getRoot().findActor(getIngredientImgName(ingredient, i, j)).setVisible(true);
                System.out.println(btnPressIndex + " " + i + " " + j);
                if (btnPressIndex == nrOfRows * NR_INGR_ROW) {
                    btnPressIndex = 0;
                    for (int a = 0; a < nrOfRows; a++) {
                        for (int b = 0; b < NR_INGR_ROW; b++) {
                            getRoot().findActor(getIngredientImgName(ingredient, a, b)).setVisible(false);
                        }
                    }
                }
                btnPressIndex++;
            }
        });
        allTable.add(pressBtn).width(pressBtn.getWidth()).height(pressBtn.getHeight()).row();
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
        table.add(GraphicUtils.getImage(MainResource.heart_full)).width(addIngrDimen * iconMult).height(addIngrDimen * iconMult);
        table.add(rowsTable);
        return table;
    }

    private String getIngredientImgName(ApplePieIngredient ingredient, int i, int j) {
        return ingredient.name() + "_" + i + "_" + j;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
