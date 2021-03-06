package libgdx.implementations.buylow.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.constants.Language;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.buylow.BuyLowGame;
import libgdx.implementations.buylow.BuyLowScreenManager;
import libgdx.implementations.buylow.BuyLowSpecificResource;
import libgdx.implementations.buylow.spec.BuyLowHighScorePreferencesManager;
import libgdx.implementations.kidlearn.KidLearnGame;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class BuyLowMainMenuScreen extends AbstractScreen<BuyLowScreenManager> {


    @Override
    public void buildStage() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(BuyLowSpecificResource.scroll_background_main);
        setBackgroundColor(new RGBColor(1, 206, 255, 211));
        addAllTable();
        executeOnFirstTimeMainMenuDisplayed();
    }

    private void executeOnFirstTimeMainMenuDisplayed() {
        BuyLowGame instance = BuyLowGame.getInstance();
        if (instance.isFirstTimeMainMenuDisplayed()) {
            instance.getMainDependencyManager().createRatingService(this).appLaunched();
            instance.setFirstTimeMainMenuDisplayed(false);
        }
    }


    private void addTitle(Table table) {

        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = appName.length() > 10 ? 1.9f : 2.5f;
        mult = appName.length() > 16 ? 1.8f : mult;
        mult = Game.getInstance().getAppInfoService().getLanguage().equals(Language.es.name()) ? 1.5f : mult;
        mult = Game.getInstance().getAppInfoService().getLanguage().equals(Language.fi.name()) ? 1.6f : mult;
        mult = Game.getInstance().getAppInfoService().getLanguage().equals(Language.sv.name()) ? 1.6f : mult;
        mult = Game.getInstance().getAppInfoService().getLanguage().equals(Language.th.name()) ? 1.6f : mult;
        mult = Game.getInstance().getAppInfoService().getLanguage().equals(Language.ms.name()) ? 1.4f : mult;
        mult = Game.getInstance().getAppInfoService().getLanguage().equals(Language.uk.name()) ? 1.4f : mult;
        mult = Game.getInstance().getAppInfoService().getLanguage().equals(Language.ru.name()) ? 1.4f : mult;
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(RGBColor.WHITE.toColor(),
                        Color.SCARLET, Math.round(FontConfig.FONT_SIZE * mult * 1.8f),
                        8f, 3, 3,
                        RGBColor.BLACK.toColor(0.8f))).setText(appName).build())).padBottom(MainDimen.vertical_general_margin.getDimen() * 1).row();
    }

    private void addAllTable() {
        Table table = new Table();
        table.setFillParent(true);
        addTitle(table);
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();

        table.add(createStartGameBtn())
                .width(SkelClassicButtonSize.BUYLOW_MENU_BTN.getWidth())
                .height(SkelClassicButtonSize.BUYLOW_MENU_BTN.getHeight())
                .padTop(verticalGeneralMarginDimen * 4).row();
        BuyLowHighScorePreferencesManager highScorePreferencesManager = new BuyLowHighScorePreferencesManager();
        MyWrappedLabel highScoreLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 1.5f))
                        .setText(MainGameLabel.l_highscore.getText(BuyLowGameScreen.formatNrToCurrencyWithDollar(highScorePreferencesManager.getMaxScore()))).build());

        table.add(highScoreLabel).padTop(verticalGeneralMarginDimen * 2).row();
        addActor(table);
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder()
                .setFixedButtonSize(SkelClassicButtonSize.BUYLOW_MENU_BTN)
                .setButtonSkin(SkelClassicButtonSkin.BUYLOW_MENU).build();
        button.add(createBtnText(MainGameLabel.l_new_game.getText() + "!"));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showGameScreen();
            }
        });
        return button;
    }

    private MyWrappedLabel createBtnText(String text) {
        return new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 1.6f))
                        .setText(text)
                        .setWrappedLineLabel(SkelClassicButtonSize.BUYLOW_MENU_BTN.getWidth()).build());
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }
}
