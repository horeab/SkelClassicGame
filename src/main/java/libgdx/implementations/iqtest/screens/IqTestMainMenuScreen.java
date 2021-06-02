package libgdx.implementations.iqtest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ButtonWithIconBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.iqtest.IqTestGameLabel;
import libgdx.implementations.iqtest.IqTestScreenManager;
import libgdx.implementations.iqtest.IqTestSpecificResource;
import libgdx.implementations.iqtest.spec.IqTestGameType;
import libgdx.implementations.iqtest.spec.IqTestPreferencesManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.SettingsUtils;
import libgdx.utils.SoundUtils;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

import java.util.Arrays;

public class IqTestMainMenuScreen extends AbstractScreen<IqTestScreenManager> {

    private IqTestPreferencesManager iqTestPreferencesManager = new IqTestPreferencesManager();

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        addScreenBackground();
        Table table = new Table();
        table.setFillParent(true);
        table.add(createAllTable()).expand();
        addActor(table);
        SettingsUtils settingsUtils = new SettingsUtils();
        settingsUtils.setSoundMusicButtonsTable(SoundUtils.createSoundMusicButtonsTable(IqTestSpecificResource.background_music));
        settingsUtils.addSettingsTable(getAbstractScreen());
        setBackgroundColor(RGBColor.BLUE);
    }

    private void addScreenBackground() {
        Image image = GraphicUtils.getImage(IqTestSpecificResource.main_backgr_img);
        float imgHeight = image.getHeight();
        float imgWidth = image.getWidth();
        image.setWidth(ScreenDimensionsManager.getScreenWidth());
        image.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(image.getWidth(), imgWidth, imgHeight));
        image.toBack();
        image.setY(ScreenDimensionsManager.getScreenHeight(15));
        addActor(image);
    }

    private Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        Image image = GraphicUtils.getImage(IqTestSpecificResource.background_texture);
        stack.addActor(image);
        stack.addActor(titleLabel);
        stack.setWidth(ScreenDimensionsManager.getScreenWidth());
        return stack;
    }

    private Table createAllTable() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(MainResource.background_texture);
        Table table = new Table();
        String appName = Game.getInstance().getAppInfoService().getAppName();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(RGBColor.LIGHT_RED1.toColor(),
                        Color.SCARLET, Math.round(FontConfig.FONT_SIZE * (appName.length() >= 20 ? 2 : 2.2f)),
                        5f, 3, 3,
                        RGBColor.BLACK.toColor(0.8f)))
                .setText(appName).build());

        float dimen = MainDimen.vertical_general_margin.getDimen();
        Stack titleStack = createTitleStack(titleLabel);
        table.add(titleStack).height(ScreenDimensionsManager.getScreenHeight(10))
                .padBottom(dimen * 2)
                .width(titleStack.getWidth()).padTop(dimen).row();

        table.row();
        table.add(createLevelButtonsTable()).height(ScreenDimensionsManager.getScreenHeight(70));
        return table;
    }

    private Table createLevelButtonsTable() {
        Table table = new Table();

        for (IqTestGameType gameType : IqTestGameType.values()) {
            Table toAdd = createLevelBtnTable(gameType);
            table.add(toAdd).width(toAdd.getWidth()).height(toAdd.getHeight()).grow().row();
        }

        return table;
    }

    private Table createLevelBtnTable(final IqTestGameType iqTest) {
        SkelClassicButtonSize buttonSize = SkelClassicButtonSize.IQTEST_LEVEL_BTN;
        float btnLabelWidth = ScreenDimensionsManager.getScreenWidth(45);
        String text = iqTest.getName().getText();
        MyButton iconButton = new ButtonWithIconBuilder(text, iqTest.getIcon())
                .setLabelWidth(btnLabelWidth)
                .setFontConfig(new FontConfig(RGBColor.BLACK.toColor(),
                        FontConfig.FONT_SIZE / (text.length() > 20 ? 1.3f : 1.2f)))
                .setFixedButtonSize(buttonSize).build();

        iconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showGameScreen(iqTest);
            }
        });
        Table table = new Table();
        float padHoriz = MainDimen.horizontal_general_margin.getDimen() * 2;
        Table btnTable = new Table();
        float btnWidth = buttonSize.getWidth() * 1.7f + btnLabelWidth;
        float btnHeight = ScreenDimensionsManager.getScreenHeight(15);
        btnTable.add(iconButton).width(btnWidth).height(btnHeight);
        btnTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        btnTable.setWidth(btnWidth);
        btnTable.setHeight(btnHeight);
        table.add(btnTable).padLeft(padHoriz * 2).width(btnTable.getWidth()).height(btnTable.getHeight());
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(Color.WHITE, Color.BLACK,
                        FontConfig.FONT_SIZE * 2.1f, FontConfig.STANDARD_BORDER_WIDTH * 8.5f))
                .setWrappedLineLabel(ScreenDimensionsManager.getScreenWidth(10))
                .setText(iqTestPreferencesManager.getLevelScore(iqTest) + "").build()))
                .growX();
        table.setHeight(iconButton.getHeight());
        table.setWidth(ScreenDimensionsManager.getScreenWidth());
        return table;
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }
}
