package libgdx.implementations.balloon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.balloon.BalloonScreenManager;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class BalloonMainMenuScreen extends AbstractScreen<BalloonScreenManager> {

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        addButtons();
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
    }

    private void addButtons() {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        new BackButtonBuilder().addHoverBackButton(this);
        table.setFillParent(true);
        addTitle(table);
        float btnHeight = ScreenDimensionsManager.getScreenHeightValue(17);
        float btnWidth = ScreenDimensionsManager.getScreenWidthValue(40);
        table.add(createStartGameBtn()).height(btnHeight).width(btnWidth).padTop(verticalGeneralMarginDimen * 4).row();
        table.add(createMultiplayetBtn()).height(btnHeight).width(btnWidth).padTop(verticalGeneralMarginDimen*2).row();
        addActor(table);
    }

    private void addTitle(Table table) {
        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = 6.5f;
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.DARK_RED.getColor(),
                        FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE * mult),
                        3f)).setText(appName).build())).padBottom(MainDimen.vertical_general_margin.getDimen() * 3).row();
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder().setSingleLineText(MainGameLabel.l_play.getText() + "!", FontManager.getBigFontDim() * 2)
                .setButtonSkin(SkelClassicButtonSkin.BALLOON_MENU).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showCampaignScreen();
            }
        });
        return button;
    }

    private MyButton createMultiplayetBtn() {
        //TODO
        MyButton button = new ButtonBuilder().setSingleLineText("Multiplayer", FontManager.getBigFontDim() * 2)
                .setButtonSkin(SkelClassicButtonSkin.BALLOON_MENU).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new CampaignStoreService().reset();
                screenManager.showMainScreen();
            }
        });
        return button;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Utils.createChangeLangPopup();
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

}
