package libgdx.implementations.iqtest.spec.iqtest;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.iqtest.IqTestDimen;
import libgdx.implementations.iqtest.IqTestGameLabel;
import libgdx.implementations.iqtest.IqTestSpecificResource;
import libgdx.implementations.iqtest.spec.IqTestCurrentGame;
import libgdx.implementations.iqtest.spec.IqTestLevelCreator;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;

public class IqTestGameCreator extends IqTestLevelCreator {

    public IqTestGameCreator(IqTestCurrentGame iqTestCurrentGame) {
        super(iqTestCurrentGame);
        addQuestionScreen(iqTestCurrentGame.getCurrentQuestion());
    }

    public void addQuestionScreen(int questionNr) {
        Table table = new Table();
        table.setName(MAIN_TABLE_NAME);
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(createHeader()).pad(verticalGeneralMarginDimen).width(screenWidth).row();
        table.add().growY().row();
        table.add(createQuestionImage(resourceService.getByName("q" + questionNr))).fillX().row();
        table.add().growY().row();
        table.add(createAnswersImages(questionNr)).padBottom(verticalGeneralMarginDimen * 2).width(screenWidth);
        table.setFillParent(true);
        Game.getInstance().getAbstractScreen().addActor(table);
    }

    @Override
    protected String getScore() {
        return "xxx";
    }

    private Table createAnswersImages(int questionNr) {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setText((IqTestGameLabel.answerchoice.getText())).setFontScale(FontManager.getSmallFontDim()).setSingleLineLabel().build())).center()
                .pad(verticalGeneralMarginDimen)
                .growX()
                .colspan(4)
                .row();
        int answerNr = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                Image questionImage = GraphicUtils.getImage(resourceService.getByName("q" + questionNr + "a" + answerNr));
                float sideRatio = questionImage.getHeight() / ((float) questionImage.getWidth());
                questionImage.setWidth(IqTestDimen.side_answer_img.getDimen());
                questionImage.setHeight(IqTestDimen.side_answer_img.getDimen());
                final int btnAnswerNr = answerNr;
                questionImage.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        answerClick(btnAnswerNr);
                    }

                });
                table.add(questionImage).pad(verticalGeneralMarginDimen / 3).height(questionImage.getHeight() * sideRatio).width(questionImage.getWidth());
                answerNr++;
            }
            table.row();
        }
        return table;
    }

    private Table createQuestionImage(Res questionRes) {
        Image questionImage = GraphicUtils.getImage(questionRes);
        Table table = new Table();
        float sideRatio = questionImage.getHeight() / ((float) questionImage.getWidth());
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        table.add(questionImage).width(screenWidth).height(screenWidth * sideRatio);
        return table;
    }


    private void answerClick(int answerNr) {
        iqTestCurrentGame.getQuestionWithAnswer().put(iqTestCurrentGame.getCurrentQuestion(), answerNr);
        goToNextLevel();
    }

}
