package libgdx.implementations.iqtest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.iqtest.IqTestScreenManager;
import libgdx.implementations.iqtest.IqTestSpecificResource;
import libgdx.implementations.iqtest.spec.IqTestGameType;
import libgdx.implementations.iqtest.spec.IqTestPreferencesManager;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

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

        float btnHeight = getBtnHeightValue();
        float dimen = MainDimen.vertical_general_margin.getDimen();
        Stack titleStack = createTitleStack(titleLabel);
        table.add(titleStack).height(btnHeight / 2).width(titleStack.getWidth()).padTop(dimen * 3).row();
        table.row();
        table.add(createLevelButtonsTable()).growX();
        return table;
    }

    private Table createLevelButtonsTable() {
        Table table = new Table();
        float pad = ScreenDimensionsManager.getScreenWidthValue(10);

        MyButton levelBtn1 = createLevelBtn(SkelClassicButtonSkin.IQTEST_LEVEL_MAIN_0, IqTestGameType.IQ_TEST);
        table.add(levelBtn1).pad(pad).width(levelBtn1.getWidth()).height(levelBtn1.getHeight()).colspan(2).row();

        MyButton levelBtn2 = createLevelBtn(SkelClassicButtonSkin.IQTEST_LEVEL_MAIN_1, IqTestGameType.NUM_SEQ);
        table.add(levelBtn2).pad(pad).width(levelBtn2.getWidth()).height(levelBtn2.getHeight());

        MyButton levelBtn3 = createLevelBtn(SkelClassicButtonSkin.IQTEST_LEVEL_MAIN_2, IqTestGameType.MEM_NUM);
        table.add(levelBtn3).pad(pad).width(levelBtn3.getWidth()).height(levelBtn3.getHeight());

        return table;
    }

    private MyButton createLevelBtn(SkelClassicButtonSkin buttonSkin, final IqTestGameType iqTest) {
        SkelClassicButtonSize buttonSize = SkelClassicButtonSize.IQTEST_LEVEL_BTN;
        MyButton button = new ImageButtonBuilder(buttonSkin, getAbstractScreen())
                .textButtonWidth(buttonSize.getWidth() * 1.3f)
                .setFontScale(FontManager.getNormalFontDim())
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(buttonSize)
                .setText("text")
                .build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showGamScreen(iqTest);
            }
        });
        return button;
    }

    private float getBtnHeightValue() {
        return ScreenDimensionsManager.getScreenHeightValue(50);
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
