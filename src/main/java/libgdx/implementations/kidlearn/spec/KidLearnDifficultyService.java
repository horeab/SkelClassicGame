package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.EnumUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

public class KidLearnDifficultyService {

    private KidLearnPreferencesManager kidLearnPreferencesManager = new KidLearnPreferencesManager();

    public Table createDifficultyButtonsTable(Class<? extends Enum> levelType, boolean vertical, KidLearnQuestionDifficultyLevel currentDifficultyLevel) {
        Table table = new Table();
        float marginDimen = MainDimen.horizontal_general_margin.getDimen();
        SkelClassicButtonSize kidlearnDifficultySize = SkelClassicButtonSize.KIDLEARN_DIFFICULTY;
        for (KidLearnQuestionDifficultyLevel difficultyLevel : KidLearnQuestionDifficultyLevel.values()) {
            Actor toAdd;
            if (difficultyLevel == currentDifficultyLevel) {
                toAdd = new Image(SkelClassicButtonSkin.valueOf("KIDLEARN_DIFF_LEVEL_" + difficultyLevel.getIndex()).getImgDown());

            } else {
                toAdd = createDifficultyButton(levelType, difficultyLevel);
                ((MyButton) toAdd).setTransform(true);
                new ActorAnimation(toAdd, Game.getInstance().getAbstractScreen()).animateZoomInZoomOut(0.1f);
            }
            table.add(toAdd).width(kidlearnDifficultySize.getWidth()).pad(marginDimen * 2).height(kidlearnDifficultySize.getHeight());
            if (vertical) {
                table.padBottom(marginDimen * 6).row();
            }
        }
        return table;
    }

    public <L extends Enum & KidLearnLevel> List<L> getLevelsForDifficulty(Class<L> levelClass, Class<? extends Enum> difficultyLevelClass) {
        List<L> allLevels = new ArrayList<>();
        for (L val : EnumUtils.getValues(levelClass)) {
            if (val.difficulty() == kidLearnPreferencesManager.getDifficultyLevel(difficultyLevelClass)) {
                allLevels.add(val);
            }
        }
        return allLevels;
    }

    private MyButton createDifficultyButton(final Class<? extends Enum> levelType, final KidLearnQuestionDifficultyLevel difficultyLevel) {
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
        final AbstractScreen abstractScreen = Game.getInstance().getAbstractScreen();
        SkelClassicButtonSize btnSize = SkelClassicButtonSize.KIDLEARN_DIFFICULTY;
        Color mainColor = index == kidLearnPreferencesManager.getDifficultyLevel(levelType).getIndex() ? fontColor.getColor() : FontColor.WHITE.getColor();
        MyButton btn = new ImageButtonBuilder(SkelClassicButtonSkin.valueOf("KIDLEARN_DIFF_LEVEL_" + index), abstractScreen)
                .setFontConfig(KidLearnControlsUtils.getSubTitleFontConfig(mainColor,
                        KidLearnControlsUtils.getTitleStandardFontSize() / 2.2f))
                .setFixedButtonSize(btnSize)
//                .setWrappedText(labelText, ScreenDimensionsManager.getScreenWidthValue(20))
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                kidLearnPreferencesManager.putDifficultyLevel(levelType, difficultyLevel);
                ((KidLearnScreenManager) abstractScreen.getScreenManager()).showLevelScreen(levelType);
                setBackgroundDiff(difficultyLevel, Game.getInstance().getAbstractScreen().getBackgroundStage());
            }
        });
        return btn;
    }

    public FontColor getTitleBorderColor(Class<? extends Enum> difficultyLevelClass) {
        FontColor color = FontColor.GREEN;
        KidLearnQuestionDifficultyLevel difficultyLevel = kidLearnPreferencesManager.getDifficultyLevel(difficultyLevelClass);
        if (difficultyLevel == KidLearnQuestionDifficultyLevel._1) {
            color = FontColor.BLUE;
        } else if (difficultyLevel == KidLearnQuestionDifficultyLevel._2) {
            color = FontColor.RED;
        }
        return color;
    }

    public void setBackgroundDiff(KidLearnQuestionDifficultyLevel difficultyLevel, Stage backgroundStage) {
        Res backgr = MainResource.background_texture;
        Container backgrContainer = backgroundStage.getRoot().findActor(AbstractScreen.BACKGROUND_CONTAINER_NAME);
        if (difficultyLevel == KidLearnQuestionDifficultyLevel._0) {
            backgr = KidLearnSpecificResource.background_texture_diff0;
        } else if (difficultyLevel == KidLearnQuestionDifficultyLevel._1) {
            backgr = KidLearnSpecificResource.background_texture_diff1;
        } else if (difficultyLevel == KidLearnQuestionDifficultyLevel._2) {
            backgr = KidLearnSpecificResource.background_texture_diff2;
        }
        backgrContainer.setBackground(GraphicUtils.addTiledImage(backgr, 0, Texture.TextureWrap.Repeat, ScreenDimensionsManager.getExternalDeviceHeightValue(60))
                .getDrawable());
    }
}
