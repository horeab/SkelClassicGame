package libgdx.implementations.kidlearn.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedLevel;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.SoundUtils;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnMainMenuScreen extends AbstractScreen<KidLearnScreenManager> {


    @Override
    public void buildStage() {
        setUpAllTable();
        getAllTable().row();
        addTitle(getAllTable());
        getAllTable().row();
        getAllTable().add(createLevelsTable()).padBottom(MainDimen.vertical_general_margin.getDimen() * 5);
        SoundUtils.addSoundTable(getAbstractScreen(), KidLearnSpecificResource.background_music);
    }

    private Table createLevelsTable() {
        Table table = new Table();
        float margin = MainDimen.horizontal_general_margin.getDimen() * 3;
        table.add(createOptionBtn(KidLearnMathCaterLevel.class, KidLearnGameLabel.l_math_title.getText())).pad(margin);
        table.add(createOptionBtn(KidLearnEngLevel.class, KidLearnGameLabel.l_eng_title.getText())).pad(margin);
        table.add(createOptionBtn(KidLearnSciFeedLevel.class, KidLearnGameLabel.l_sci_title.getText())).pad(margin);
        return table;
    }

    private void addTitle(Table table) {
        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = appName.length() > 10 ? 2.1f : 2.5f;
        mult = appName.length() > 16 ? 1.8f : mult;
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                        FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE * mult * 1.2f),
                        8f)).setText(appName).build())).padBottom(MainDimen.vertical_general_margin.getDimen() * 1).row();
    }

    private <L extends Enum & KidLearnMathCaterLevel> Table createOptionBtn(Class<?> levelType, String text) {
        Table table = new Table();
        MyButton btn = new ImageButtonBuilder(SkelClassicButtonSkin.KIDLEARN_MAIN_MENU_LEVEL, Game.getInstance().getAbstractScreen())
                .padBetweenImageAndText(1.5f)
                .textBackground(MainResource.transparent_background)
                .setFixedButtonSize(getLevelBtnSize())
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * 4f, 7f))
                .setText(text)
                .build();
        btn.getCenterRow().row();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((KidLearnScreenManager) getScreenManager()).showLevelScreen(levelType);
            }
        });
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        return table;
    }

    private SkelClassicButtonSize getLevelBtnSize() {
        return SkelClassicButtonSize.KIDLEARN_MAIN_MENU_LEVEL;
    }

    private FontConfig createBtnFontConfig(float fontSize, float borderWidth) {
        return new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                Math.round(fontSize), borderWidth);
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
