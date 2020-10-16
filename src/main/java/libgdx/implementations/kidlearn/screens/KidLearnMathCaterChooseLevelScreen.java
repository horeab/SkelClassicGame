package libgdx.implementations.kidlearn.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterOrdLevel;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterSeqLevel;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnMathCaterChooseLevelScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        setUpAllTable();
        createChooseLevelMenu();
    }

    public void createChooseLevelMenu() {
        Table table = new Table();
        ButtonSize btnSize = getChooseLevelBtnSize();
        Table btnTable = new Table();
        float btnTableHeight = ScreenDimensionsManager.getScreenHeightValue(80);
        float padSide = MainDimen.horizontal_general_margin.getDimen() * 5;
        btnTable.add(createChooseLevelBtn("Number order", KidLearnMathCaterOrdLevel.class)).padBottom(padSide).width(btnSize.getWidth()).height(btnSize.getHeight());
        btnTable.add().padLeft(padSide).padRight(padSide).height(btnTableHeight);
        btnTable.add(createChooseLevelBtn("Number sequence", KidLearnMathCaterSeqLevel.class)).padBottom(padSide).width(btnSize.getWidth()).height(btnSize.getHeight());
        table.add(createGameTitle()).height(ScreenDimensionsManager.getScreenHeightValue(20)).row();
        table.add(btnTable).height(btnTableHeight);
        new ActorAnimation(table, getAbstractScreen()).animateFastFadeIn();
        getAllTable().add(table).grow();
    }

    private Table createGameTitle() {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        FontConfig.FONT_SIZE * 2f, 8f)).setText("Caterpillar ordering").build());
    }

    private <T extends Enum & KidLearnMathCaterLevel> MyButton createChooseLevelBtn(String text, Class<T> mathCaterLevelClass) {
        MyButton btn = new ImageButtonBuilder(MainButtonSkin.REFRESH, Game.getInstance().getAbstractScreen())
                .setFixedButtonSize(getChooseLevelBtnSize())
                .setFontConfig(new FontConfig(FontConfig.FONT_SIZE * 2.5f))
                .setText(text)
                .build();
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getScreenManager().showLevelScreen(mathCaterLevelClass);
            }
        });
        return btn;
    }

    private ButtonSize getChooseLevelBtnSize() {
        return SkelClassicButtonSize.KIDLEARN_CHOOSE_LEVEL;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
