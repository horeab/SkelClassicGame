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
import libgdx.implementations.kidlearn.spec.KidLearnControlsUtils;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnUtils;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedLevel;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.SoundUtils;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

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
        table.add(createOptionBtn(KidLearnMathCaterLevel.class, KidLearnGameLabel.l_math_title.getText(), SkelClassicButtonSkin.KIDLEARN_MAIN_MENU_MATH_LEVEL)).pad(margin);
        table.add(createOptionBtn(KidLearnEngLevel.class, KidLearnGameLabel.l_eng_title.getText(), SkelClassicButtonSkin.KIDLEARN_MAIN_MENU_ENG_LEVEL)).pad(margin);
        table.add(createOptionBtn(KidLearnSciFeedLevel.class, KidLearnGameLabel.l_sci_title.getText(), SkelClassicButtonSkin.KIDLEARN_MAIN_MENU_SCI_LEVEL)).pad(margin);
        return table;
    }

    private void addTitle(Table table) {
        String appName = Game.getInstance().getAppInfoService().getAppName();
        float fontSize = KidLearnUtils.getFontSize(appName, 99, FontConfig.FONT_SIZE * 3f, 0.05f);
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWidth(ScreenDimensionsManager.getScreenWidthValue(55))
                .setFontConfig( new FontConfig(RGBColor.LIGHT_BLUE.toColor(), FontColor.BLUE.getColor(),
                        fontSize, 4f)).setText(appName).build()))
                .padBottom(MainDimen.vertical_general_margin.getDimen() * 1).row();
    }

    private <L extends Enum & KidLearnMathCaterLevel> Table createOptionBtn(Class<?> levelType, String text, SkelClassicButtonSkin buttonSkin) {
        Table table = new Table();
        SkelClassicButtonSize levelBtnSize = getLevelBtnSize();
        MyButton btn = new ImageButtonBuilder(buttonSkin, Game.getInstance().getAbstractScreen())
                .textBackground(MainResource.transparent_background)
                .setFixedButtonSize(levelBtnSize)
                .setFontConfig(KidLearnControlsUtils.getBtnLevelFontConfig(text))
                .setWrappedText(text, levelBtnSize.getWidth() * 1.5f)
                .build();
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
