package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnDifficultyService {

    private KidLearnPreferencesManager kidLearnPreferencesManager = new KidLearnPreferencesManager();

    public Table createDifficultyButtonsTable(Class<? extends Enum> levelType, boolean vertical) {
        Table table = new Table();
        float marginDimen = MainDimen.horizontal_general_margin.getDimen();
        for (KidLearnQuestionDifficultyLevel difficultyLevel : KidLearnQuestionDifficultyLevel.values()) {
            MyButton difficultyButton = createDifficultyButton(levelType, difficultyLevel);
            table.add(difficultyButton).width(difficultyButton.getWidth()).pad(marginDimen * 2).height(difficultyButton.getHeight());
            if (vertical) {
                table.padBottom(marginDimen * 4).row();
            }
        }
        return table;
    }

    private MyButton createDifficultyButton(Class<? extends Enum> levelType, KidLearnQuestionDifficultyLevel difficultyLevel) {
        String labelText = difficultyLevel.name() + "";
        FontColor fontColor = FontColor.WHITE;
        if (difficultyLevel == KidLearnQuestionDifficultyLevel._0) {
            labelText = MainGameLabel.l_easy.getText();
            fontColor = FontColor.LIGHT_GREEN;
        } else if (difficultyLevel == KidLearnQuestionDifficultyLevel._1) {
            labelText = MainGameLabel.l_normal.getText();
            fontColor = FontColor.LIGHT_BLUE;
        } else if (difficultyLevel == KidLearnQuestionDifficultyLevel._2) {
            labelText = MainGameLabel.l_difficult.getText();
            fontColor = FontColor.RED;
        }
        int index = difficultyLevel.getIndex();
        AbstractScreen abstractScreen = Game.getInstance().getAbstractScreen();
        MyButton btn = new ImageButtonBuilder(SkelClassicButtonSkin.valueOf("KIDLEARN_DIFF_LEVEL_" + index), abstractScreen)
                .setFontConfig(new FontConfig(
                        index == kidLearnPreferencesManager.getDifficultyLevel(levelType).getIndex() ? fontColor.getColor() : FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * 1f,
                        3f))
                .setFixedButtonSize(SkelClassicButtonSize.KIDLEARN_DIFFICULTY)
                .setWrappedText(labelText, ScreenDimensionsManager.getScreenWidthValue(SkelClassicButtonSize.KIDLEARN_DIFFICULTY.getWidth()))
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                kidLearnPreferencesManager.putDifficultyLevel(levelType, difficultyLevel);
                ((KidLearnScreenManager) abstractScreen.getScreenManager()).showLevelScreen(levelType);
            }
        });
        return btn;
    }

}
