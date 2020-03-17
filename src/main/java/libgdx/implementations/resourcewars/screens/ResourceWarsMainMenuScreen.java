package libgdx.implementations.resourcewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.balloon.BalloonCampaignLevelEnum;
import libgdx.implementations.balloon.logic.LevelManager;
import libgdx.implementations.balloon.model.LevelInfo;
import libgdx.implementations.resourcewars.ResourceWarsScreenManager;
import libgdx.implementations.resourcewars.spec.creator.ContainerManager;
import libgdx.implementations.resourcewars.spec.logic.InGameStoreManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class ResourceWarsMainMenuScreen extends AbstractScreen<ResourceWarsScreenManager> {


    @Override
    public void buildStage() {
        setBackgroundColor(new RGBColor(1, 206, 255, 211));
        addAllTable();
    }

    private void addAllTable() {
        Table table = new Table();
        table.setFillParent(true);
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        float btnHeight = ScreenDimensionsManager.getScreenHeightValue(14);
        float btnWidth = ScreenDimensionsManager.getScreenWidthValue(35);
        table.add(createStartGameBtn()).height(btnHeight).width(btnWidth).padTop(verticalGeneralMarginDimen * 4).row();
        table.add(createContinueGameBtn()).height(btnHeight).width(btnWidth).padTop(verticalGeneralMarginDimen * 2).row();
        addActor(table);
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.RESOURCEWARS_MENU).build();
        button.add(createBtnText(MainGameLabel.l_new_game.getText() + "!"));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new InGameStoreManager().reset();
                screenManager.showGameScreen(new CurrentGame());
            }
        });
        return button;
    }

    private MyButton createContinueGameBtn() {
        MyButton button = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.RESOURCEWARS_MENU).build();
        button.add(createBtnText(MainGameLabel.l_continue.getText()));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showGameScreen(new InGameStoreManager().getSavedGame());
            }
        });
        return button;
    }

    private MyWrappedLabel createBtnText(String text) {
        return new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), Math.round(text.length() > 12 ? FontConfig.FONT_SIZE * 2f : FontConfig.FONT_SIZE * 2.5f)))
                        .setText(text).build());
    }


    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }
}
