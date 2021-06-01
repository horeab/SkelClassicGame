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
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class IqTestMainMenuScreen extends AbstractScreen<IqTestScreenManager> {

    private IqTestPreferencesManager iqTestPreferencesManager = new IqTestPreferencesManager();

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        table.setFillParent(true);
        table.add(createAllTable()).expand();
        addActor(table);
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
                .setFontConfig(new FontConfig(
                        FontColor.LIGHT_GREEN.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * (appName.length() >= 10 ? 2 : 2.4f),
                        4f))
                .setText(appName).build());

        float dimen = MainDimen.vertical_general_margin.getDimen();
        Stack titleStack = createTitleStack(titleLabel);
        table.add(titleStack).height(ScreenDimensionsManager.getScreenHeight(10))
                .width(titleStack.getWidth()).padTop(dimen).row();
        table.row();
        table.add(createLevelButtonsTable()).grow();
        return table;
    }

    private Table createLevelButtonsTable() {
        Table table = new Table();
        float pad = ScreenDimensionsManager.getScreenWidth(8);

        int index = 0;
        Table levelBtn1 = createLevelBtnTable(SkelClassicButtonSkin.IQTEST_LEVEL_MAIN_0, IqTestGameType.IQ_TEST,
                IqTestGameLabel.gametype_iqtest.getText(), index);
        table.add(levelBtn1).pad(pad).width(levelBtn1.getWidth()).height(levelBtn1.getHeight()).row();

        index++;
        Table levelBtn3 = createLevelBtnTable(SkelClassicButtonSkin.IQTEST_LEVEL_MAIN_3, IqTestGameType.SPACE,
                IqTestGameLabel.gametype_space.getText(), index);
        table.add(levelBtn3).pad(pad).width(levelBtn3.getWidth()).height(levelBtn3.getHeight()).row();

        index++;
        Table levelBtn2 = createLevelBtnTable(SkelClassicButtonSkin.IQTEST_LEVEL_MAIN_1, IqTestGameType.NUM_SEQ,
                IqTestGameLabel.gametype_numseq.getText(), index);
        table.add(levelBtn2).pad(pad).width(levelBtn2.getWidth()).height(levelBtn2.getHeight()).row();

        index++;
        Table levelBtn4 = createLevelBtnTable(SkelClassicButtonSkin.IQTEST_LEVEL_MAIN_2, IqTestGameType.MEM_NUM,
                IqTestGameLabel.gametype_memnum.getText(), index);
        table.add(levelBtn4).pad(pad).width(levelBtn4.getWidth()).height(levelBtn4.getHeight());

        return table;
    }

    private Table createLevelBtnTable(SkelClassicButtonSkin buttonSkin, final IqTestGameType iqTest, String text, int index) {
        SkelClassicButtonSize buttonSize = SkelClassicButtonSize.IQTEST_LEVEL_BTN;
        float btnLabelWidth = ScreenDimensionsManager.getScreenWidth(45);
        MyButton iconButton = new ButtonWithIconBuilder(text, buttonSkin.getImgUpRes())
                .setLabelWidth(btnLabelWidth)
                .setFontConfig(new FontConfig(RGBColor.BLACK.toColor(),
                        FontConfig.FONT_SIZE))
                .setFixedButtonSize(buttonSize).build();


        iconButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showGamScreen(iqTest);
            }
        });
        Table table = new Table();
        float padHoriz = MainDimen.horizontal_general_margin.getDimen() * 2;
        Table btnTable = new Table();
        float btnWidth = buttonSize.getWidth() * 1.3f + btnLabelWidth;
        float btnHeight = ScreenDimensionsManager.getScreenHeight(15);
        btnTable.add(iconButton).width(btnWidth);
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
