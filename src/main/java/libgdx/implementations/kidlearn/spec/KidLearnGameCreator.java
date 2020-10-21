package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public abstract class KidLearnGameCreator {

    protected AbstractScreen screen;
    protected MyWrappedLabel scoreLabel;
    protected KidLearnGameContext gameContext;


    public KidLearnGameCreator(KidLearnGameContext gameContext) {
        this.screen = Game.getInstance().getAbstractScreen();
        this.gameContext = gameContext;
    }

    protected abstract int getTotalQuestions();

    public void create() {
        createTitle();
        createScoreLabel();
    }

    private void createTitle() {
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(getLevelTitle()).build());
        title.setY(getHeaderY());
        title.setX(ScreenDimensionsManager.getScreenWidth() / 2);
        addActorToScreen(title);
    }

    protected String getLevelTitle() {
        return ((KidLearnLevel) gameContext.level).title();
    }

    private void createScoreLabel() {
        scoreLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(getScoreLabelText()).build());
        scoreLabel.setY(getHeaderY());
        scoreLabel.setX(ScreenDimensionsManager.getScreenWidth() - MainDimen.horizontal_general_margin.getDimen() * 5);
        addActorToScreen(scoreLabel);
    }

    protected void addActorToScreen(Actor actor) {
        new ActorAnimation(actor, screen).animateFastFadeIn();
        screen.addActor(actor);
    }

    private Table createCorrectAnswerPopup() {
        Table table = new Table();
        float popupWidth = ScreenDimensionsManager.getScreenWidthValue(60);
        table.setWidth(popupWidth);
        table.setHeight(ScreenDimensionsManager.getScreenHeightValue(30));
        table.setX(ScreenDimensionsManager.getScreenWidth() / 2 - popupWidth / 2);
        table.setY(ScreenDimensionsManager.getScreenHeight() / 2 - table.getHeight() / 2);
        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        MyWrappedLabel msg = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWidth(popupWidth / 1.1f)
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText("That's correct, well done!").build());
        table.add(msg);
        return table;
    }


    protected void executeCorrectAnswer() {
        gameContext.score = gameContext.score + 1;
        scoreLabel.setText(getScoreLabelText());
        new KidLearnPreferencesManager().putLevelScore(gameContext.level, gameContext.score);
        Table correctPopup = createCorrectAnswerPopup();
        correctPopup.setVisible(false);
        screen.addActor(correctPopup);
        float duration = 0.2f;
        AlphaAction fadeOut = Actions.fadeOut(duration);
        fadeOut.setAlpha(0f);
        correctPopup.addAction(Actions.sequence(fadeOut, Utils.createRunnableAction(new ScreenRunnable(screen) {
            @Override
            public void executeOperations() {
                correctPopup.setVisible(true);
            }
        }), Actions.fadeIn(0.5f), Actions.delay(1.5f), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                KidLearnScreenManager screenManager = (KidLearnScreenManager) screen.getScreenManager();
                if (gameContext.score == getTotalQuestions()) {
                    screenManager.showLevelScreen(gameContext.level.getClass());
                } else {
                    screen.addAction(Actions.sequence(Actions.fadeOut(0.5f), Utils.createRunnableAction(new Runnable() {
                        @Override
                        public void run() {
                            screenManager.showGameScreen(gameContext);
                        }
                    })));
                }
            }
        })));
    }

    private float getHeaderY() {
        return ScreenDimensionsManager.getScreenHeightValue(93);
    }

    String getScoreLabelText() {
        return gameContext.score + "/" + getTotalQuestions();
    }

}
