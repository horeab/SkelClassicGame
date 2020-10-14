package libgdx.implementations.kidlearn.spec.cater;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.implementations.kidlearn.spec.cater.ord.KidLearnMathCaterOrdLevel;
import libgdx.implementations.kidlearn.spec.cater.seq.KidLearnMathCaterSeqLevel;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.EnumUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnMathCaterMenuCreator {

    private Table allTable;
    private AbstractScreen screen;
    private KidLearnPreferencesManager kidLearnPreferencesManager = new KidLearnPreferencesManager();

    public KidLearnMathCaterMenuCreator(AbstractScreen screen) {
        this.screen = screen;
    }

    public void createChooseLevelMenu() {
        setUpAllTable();
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
        new ActorAnimation(table, screen).animateFastFadeIn();
        allTable.add(table).grow();
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
                createLevelMenu(mathCaterLevelClass, text);
            }
        });
        return btn;
    }

    private ButtonSize getChooseLevelBtnSize() {
        return SkelClassicButtonSize.KIDLEARN_CHOOSE_LEVEL;
    }

    private void setUpAllTable() {
        if (allTable != null) {
            allTable.clear();
        }
        allTable = new Table();
        allTable.setFillParent(true);
        screen.addActor(allTable);
    }

    public <T extends Enum & KidLearnMathCaterLevel> void createLevelMenu(Class<T> mathCaterLevelClass, String titleText) {
        setUpAllTable();
        Table table = new Table();
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(titleText).build());
        table.add(title).pad(MainDimen.horizontal_general_margin.getDimen()).row();
        T[] allLevelValues = EnumUtils.getValues(mathCaterLevelClass);
        Table levelsContainer = new Table();
        float screenWidth = ScreenDimensionsManager.getScreenWidthValue(90);
        levelsContainer.add(createLevelContainer(allLevelValues, "Forwards", true)).width(screenWidth / 2);
        levelsContainer.add(createLevelContainer(allLevelValues, "Backwards", false)).width(screenWidth / 2);
        table.add(levelsContainer);
        new ActorAnimation(table, screen).animateFastFadeIn();
        allTable.add(table).grow();
    }

    private <T extends Enum & KidLearnMathCaterLevel> Table createLevelContainer(T[] levelValues, String text, boolean ascLevels) {
        Table table = new Table();
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(text).build());
        table.add(title).pad(MainDimen.horizontal_general_margin.getDimen()).row();
        for (T level : levelValues) {
            if (ascLevels == level.asc()) {
                int levelMax;
                int levelMin;
                boolean asc;
                float interval;
                if (level instanceof KidLearnMathCaterOrdLevel) {
                    KidLearnMathCaterOrdLevel inst = (KidLearnMathCaterOrdLevel) level;
                    levelMax = inst.max;
                    levelMin = inst.min;
                    asc = inst.asc;
                    interval = inst.interval;
                } else {
                    KidLearnMathCaterSeqLevel inst = (KidLearnMathCaterSeqLevel) level;
                    levelMax = inst.max;
                    levelMin = inst.min;
                    asc = inst.asc;
                    interval = inst.interval != null ? inst.interval : inst.upTo;
                }
                Table optionBtn = createOptionBtn(level, levelMax, levelMin, asc, interval);
                table.add(optionBtn).row();
            }
        }
        return table;
    }

    private <T extends Enum & KidLearnMathCaterLevel> Table createOptionBtn(T level, int levelMax, int levelMin, final boolean asc, float interval) {
        Table table = new Table();
        MyButton btn = new ButtonBuilder(getLevelText(level, levelMax, levelMin, asc, interval))
                .setDefaultButton()
                .setFixedButtonSize(SkelClassicButtonSize.KIDLEARN_MATH_CATER_LEVEL)
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                KidLearnGameContext gameContext = new KidLearnGameContext();
                gameContext.level = level;
                ((KidLearnScreenManager) screen.getScreenManager()).showGameScreen(gameContext);
            }
        });
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        float checkImgSideDimen = MainDimen.horizontal_general_margin.getDimen() * 3;
        Res res = kidLearnPreferencesManager.getLevelScore(level) == KidLearnMathCaterGameCreator.TOTAL_QUESTIONS ? MainResource.refresh_up : MainResource.error;
        table.add(GraphicUtils.getImage(res)).width(checkImgSideDimen).height(checkImgSideDimen);
        return table;
    }

    private <T extends Enum & KidLearnMathCaterLevel> String getLevelText(T level, int levelMax, int levelMin, final boolean asc, float interval) {
        String text = "";
        int min = asc ? levelMin : levelMax;
        int max = asc ? levelMax : levelMin;
        text = min + " to " + max + " in " + getInterval(interval);
        if (level instanceof KidLearnMathCaterSeqLevel) {
            KidLearnMathCaterSeqLevel inst = (KidLearnMathCaterSeqLevel) level;
            if (inst.evenNr) {
                text = "Even Numbers";
            }
        } else {
            KidLearnMathCaterOrdLevel inst = (KidLearnMathCaterOrdLevel) level;
        }
        return text;
    }

    private String getInterval(float interval) {
        return interval % 1 == 0 ? String.valueOf(Math.round(interval)) : String.valueOf(interval);
    }
}
