package libgdx.implementations.iqtest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.constants.Language;
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
import libgdx.resources.gamelabel.MainGameLabel;
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
        Image image = GraphicUtils.getImage(IqTestSpecificResource.title_clouds_background);
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
                        Color.SCARLET, Math.round(FontConfig.FONT_SIZE * getTitleFontScale(appName)),
                        5f, 3, 3,
                        RGBColor.BLACK.toColor(0.8f)))
                .setText(appName).build());

        Stack titleStack = createTitleStack(titleLabel);
        table.add(titleStack).height(ScreenDimensionsManager.getScreenHeight(20))
                .width(titleStack.getWidth()).padTop(0).row();

        table.row();
        table.add(createLevelButtonsTable()).height(ScreenDimensionsManager.getScreenHeight(80));
        return table;
    }

    private float getTitleFontScale(String appName) {
        if (appName.length() >= 20
                || (appName.length() > 13 && !appName.contains(" "))
                || Language.th.name().equals(Game.getInstance().getAppInfoService().getLanguage())) {
            return 2;
        } else {
            return 2.2f;
        }
    }

    private Table createLevelButtonsTable() {
        Table table = new Table();

        for (IqTestGameType gameType : IqTestGameType.values()) {
            Table toAdd = createLevelBtnTable(gameType);
            table.add(toAdd).width(toAdd.getWidth()).height(toAdd.getHeight()).grow().row();
        }
        table.padBottom(MainDimen.vertical_general_margin.getDimen() * 3);
        return table;
    }

    private Table createLevelBtnTable(final IqTestGameType gameType) {
        SkelClassicButtonSize buttonSize = SkelClassicButtonSize.IQTEST_LEVEL_BTN;
        float btnLabelWidth = ScreenDimensionsManager.getScreenWidth(65);
        String text = gameType.name.getText();
        MyButton iconButton = new ButtonWithIconBuilder(text, gameType.icon)
                .setLabelWidth(btnLabelWidth)
                .setFontConfig(new FontConfig(RGBColor.RED.toColor(),
                        RGBColor.DARK_RED.toColor(), FontConfig.FONT_SIZE / (text.length() > 20 ? 1.1f : 1.0f),
                        FontConfig.STANDARD_BORDER_WIDTH * 3, 0, 0,
                        RGBColor.BLACK.toColor(0.2f)))
                .setFixedButtonSize(buttonSize).build();

        iconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showGameScreen(gameType);
            }
        });
        float allTableHeight = ScreenDimensionsManager.getScreenHeight(17);
        Table table = new Table();
        float btnWidth = ScreenDimensionsManager.getScreenWidth(90);
        Table btnTable = new Table();
        btnTable.setHeight(allTableHeight / 1.9f);
        btnTable.add(iconButton).width(btnWidth).height(btnTable.getHeight());
        btnTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        table.add(btnTable);
        table.setWidth(btnWidth);
        table.setHeight(allTableHeight);
        table.row();
        int levelScore = iqTestPreferencesManager.getLevelScore(gameType);
        String scoreText = getScoreText(gameType, levelScore);
        Table scoreTable = new Table();
        scoreTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(levelScore > 0 ? RGBColor.LIGHT_GREEN.toColor() : Color.WHITE,
                        levelScore > 0 ? RGBColor.DARK_GREEN.toColor() : Color.BLACK,
                        FontConfig.FONT_SIZE * 1.3f, FontConfig.STANDARD_BORDER_WIDTH * 5.5f))
                .setSingleLineLabel()
                .setText(scoreText).build()));
        scoreTable.setBackground(GraphicUtils.getColorBackground(RGBColor.LIGHT_RED2.toColor(0.8f)));
        table.add(scoreTable).growX();
        return table;
    }

    private String getScoreText(IqTestGameType gameType, int levelScore) {
        return gameType == IqTestGameType.IQ_TEST
                ? IqTestGameLabel.gametype_iqtest.getText() + " " + (levelScore > 0 ? levelScore : "?")
                : MainGameLabel.l_score.getText((levelScore > 0 ? levelScore + "/" + gameType.totalQuestions : "?"));
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
