package libgdx.implementations.kidlearn.spec.cater;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.implementations.kidlearn.spec.cater.ord.KidLearnMathCaterOrdLevel;
import libgdx.implementations.kidlearn.spec.cater.ord.KidLearnMathCaterService;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnMathCaterMenuCreator {

    int ORD_NROFCORRECTUNKNOWNNUMBERS = 4;
    int SEQ_NROFCORRECTUNKNOWNNUMBERS = 3;

    private Table allTable;
    private AbstractScreen screen;

    KidLearnMathCaterService kidLearnMathCaterService = new KidLearnMathCaterService();

    public KidLearnMathCaterMenuCreator(AbstractScreen screen) {
        this.screen = screen;
    }

    public void create() {
        allTable = new Table();
        allTable.setFillParent(true);
        allTable.add(createLevelTable()).grow();
        screen.addActor(allTable);
    }

    private Table createLevelTable() {
        Table table = new Table();
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setWidth(getLevelTableWidth())
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText("Order").build());
        table.add(title).row();
        for (KidLearnMathCaterOrdLevel level : KidLearnMathCaterOrdLevel.values()) {
            MyButton optionBtn = createOptionBtn(level);
            table.add(optionBtn).width(optionBtn.getWidth()).height(optionBtn.getHeight()).row();
        }
        return table;
    }

    private float getLevelTableWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(30);
    }

    private MyButton createOptionBtn(KidLearnMathCaterOrdLevel level) {
        int min = level.asc ? level.min : level.max;
        int max = level.asc ? level.max : level.min;
        MyButton btn = new ButtonBuilder(min + " to " + max + " in "
                + getInterval(level.interval))
                .setDefaultButton()
                .setFixedButtonSize(MainButtonSize.ONE_ROW_BUTTON_SIZE)
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                KidLearnMathCaterConfig config = new KidLearnMathCaterConfig();
                config.nrOfCorrectUnknownNumbers = ORD_NROFCORRECTUNKNOWNNUMBERS;
                config.asc = level.asc;
                config.allCorrectNumbers = kidLearnMathCaterService.generateCorrectNumbers(level);
                config.wrongNumbers = new ArrayList<>();
                allTable.clear();
                new KidLearnMathCaterCreator(config).create();
            }
        });
        return btn;
    }

    private String getInterval(float interval) {
        return interval % 1 == 0 ? String.valueOf(Math.round(interval)) : String.valueOf(interval);
    }
}
