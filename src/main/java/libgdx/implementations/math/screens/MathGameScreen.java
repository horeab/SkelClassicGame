package libgdx.implementations.math.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.CampaignService;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.math.MathCampaignLevelEnum;
import libgdx.implementations.math.spec.MathLevel;
import libgdx.implementations.math.spec.MathLevelFinishedPopup;
import libgdx.implementations.math.spec.Operation;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import org.apache.commons.lang3.mutable.MutableLong;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MathGameScreen extends AbstractScreen<MathScreenManager> {

    private static final String SCORE_LABEL = "SCORE_LABEL";

    private static final int LEVEL_GOAL = 15;
    private Table allTable;
    private MyButton hoverBackButton;
    private MutableLong countdownAmountMillis;
    private ScheduledExecutorService executorService;
    private Long totalScore = 0L;
    private int totalLevel = 0;
    private MathLevel mathLevel;
    private MathCampaignLevelEnum mathCampaignLevelEnum;
    private CampaignService campaignService = new CampaignService();
    private boolean countdownFinished = false;
    private boolean finishedLevelPopupDisplayed = false;

    public MathGameScreen(MathCampaignLevelEnum mathCampaignLevelEnum) {
        this.mathLevel = mathCampaignLevelEnum.getMathLevel();
        this.mathCampaignLevelEnum = mathCampaignLevelEnum;
    }

    @Override
    public void buildStage() {
        createAllTable();
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        if (Game.getInstance().getCurrentUser() != null) {
            new GameStatsDbApiService().incrementGameStatsQuestionsWon(Game.getInstance().getCurrentUser().getId(), Long.valueOf(DateUtils.getNowMillis()).toString());
        }
        executorService = Executors.newSingleThreadScheduledExecutor();
        allTable = new Table();
        allTable.add(headerTable()).pad(MainDimen.horizontal_general_margin.getDimen() * 2).row();
        allTable.add(countdownProcess()).growX().row();
        allTable.add(calcTable()).grow().row();
        allTable.setFillParent(true);
        addActor(allTable);
    }

    private String processExpression() {
        List<Operation> avOp = mathLevel.getAvailableOperations();
        Collections.shuffle(avOp);
        Operation op = avOp.get(0);
        Operation combOp = null;
        Integer val1 = getRandomVal(mathLevel.getMaxValForOperation(op), op);
        Integer val2 = getRandomVal(mathLevel.getMaxValForOperation(op), op);
        int ind = 0;
        while (val2 >= val1 && ind < 100) {
            val1 = getRandomVal(mathLevel.getMaxValForOperation(op), op);
            val2 = getRandomVal(mathLevel.getMaxValForOperation(op), op);
            ind++;
        }

        Integer comb = null;
        boolean useComb = new Random().nextBoolean();
        Integer maxCombVal = null;
        if (op == Operation.SUM && useComb && mathLevel.getSumCombine() != null) {
            List<Operation> combOps = mathLevel.getAvailableOperations(mathLevel.getSumCombine());
            Collections.shuffle(combOps);
            combOp = combOps.get(0);
            maxCombVal = mathLevel.getSumCombine().getMaxValForOperation(combOp);
            comb = getCombVal(val2, maxCombVal, combOp);
        } else if (op == Operation.SUB && useComb && mathLevel.getSubCombine() != null) {
            List<Operation> combOps = mathLevel.getAvailableOperations(mathLevel.getSubCombine());
            Collections.shuffle(combOps);
            combOp = combOps.get(0);
            maxCombVal = mathLevel.getSubCombine().getMaxValForOperation(combOp);
            comb = getCombVal(val2, maxCombVal, combOp);
        }
        String expression = createExpression(op, combOp, val1, val2, comb);
        while (calcExpression(expression) == null) {
            val1 = getRandomVal(mathLevel.getMaxValForOperation(op), op);
            val2 = getRandomVal(mathLevel.getMaxValForOperation(op), op);
            ind = 0;
            while (val2 >= val1 && ind < 100) {
                val1 = getRandomVal(mathLevel.getMaxValForOperation(op), op);
                val2 = getRandomVal(mathLevel.getMaxValForOperation(op), op);
                ind++;
            }
            if (maxCombVal != null) {
                comb = getCombVal(val2, maxCombVal, combOp);
            }
            expression = createExpression(op, combOp, val1, val2, comb);
        }
        return expression;
    }

    private Integer getCombVal(int val2, int maxCombVal, Operation combOp) {
        int comb = getRandomVal(maxCombVal, combOp);
        int ind = 0;
        while (val2 != 1 && comb >= val2 && ind < 100) {
            comb = getRandomVal(maxCombVal, combOp);
            ind++;
        }
        return comb;
    }

    private int getRandomVal(Integer maxValForOperation, Operation operation) {
        int res = new Random().nextInt(maxValForOperation - 1) + 1;
        if (operation == Operation.MUL || operation == Operation.DIV) {
            int ind = 0;
            while (res == 1 && ind < 100) {
                res = new Random().nextInt(maxValForOperation - 1) + 1;
                ind++;
            }
        }
        return res;
    }

    private String createExpression(Operation op, Operation combOp, Integer val1, Integer val2, Integer comb) {
        String expression = val1 + op.getExpr() + val2;
        if (comb != null) {
            boolean useParentheses = new Random().nextBoolean();
            if (useParentheses) {
                boolean useParenthesesForFirstGroup = new Random().nextInt(10) < 8;
                if (useParenthesesForFirstGroup) {
                    expression = "(" + val1 + op.getExpr() + val2 + ")" + combOp.getExpr() + comb;
                } else {
                    expression = val1 + op.getExpr() + "(" + val2 + combOp.getExpr() + comb + ")";
                }
            } else {
                expression = expression + combOp.getExpr() + comb;
            }
        }
        return expression;
    }

    private Integer calcExpression(String expr) {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            Object eval = engine.eval(expr);
            return (Integer) eval;
        } catch (Exception e) {
            return null;
        }
    }

    public MathCampaignLevelEnum getMathCampaignLevelEnum() {
        return mathCampaignLevelEnum;
    }

    private Table calcTable() {
        Table table = new Table();
        Integer correctSum = null;
        String expression = "";
        while (correctSum == null) {
            expression = processExpression();
            correctSum = calcExpression(expression);
        }
        boolean displayCorrectSum = new Random().nextBoolean();
        if (!displayCorrectSum) {
            int bound = correctSum / 8;
            int varSum = new Random().nextInt(bound == 0 ? 1 : Math.abs(bound)) + 1;
            int wrongSum = new Random().nextBoolean() ? correctSum - varSum : correctSum + varSum;
            expression = expression + "=" + wrongSum;
        } else {
            expression = expression + "=" + correctSum;
        }
        float fontScale = 3f;
        if (expression.length() > 13) {
            fontScale = 2.0f;
        } else if (expression.length() > 12) {
            fontScale = 2.1f;
        } else if (expression.length() > 11) {
            fontScale = 2.2f;
        } else if (expression.length() > 10) {
            fontScale = 2.3f;
        } else if (expression.length() > 9) {
            fontScale = 2.5f;
        }
        MyWrappedLabel expressionLabel = createLabel(fontScale, expression, FontColor.WHITE.getColor());
        expressionLabel.setVisible(false);
        new ActorAnimation(expressionLabel, getAbstractScreen()).animateFastFadeIn();
        table.add(expressionLabel).grow().row();

        Table answerTable = new Table();
        MyButton correctButton = new ImageButtonBuilder(SkelClassicButtonSkin.MATH_CORRECT, Game.getInstance().getAbstractScreen())
                .setFixedButtonSize(SkelClassicButtonSize.MATH_CORRECT_WRONG_IMG_BTN)
                .build();
        correctButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (displayCorrectSum) {
                    correctAnswerPressed();
                } else {
                    wrongAnswerPressed();
                }
            }
        });
        MyButton wrongButton = new ImageButtonBuilder(SkelClassicButtonSkin.MATH_WRONG, Game.getInstance().getAbstractScreen())
                .setFixedButtonSize(SkelClassicButtonSize.MATH_CORRECT_WRONG_IMG_BTN)
                .build();
        wrongButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!displayCorrectSum) {
                    correctAnswerPressed();
                } else {
                    wrongAnswerPressed();
                }
            }
        });
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        answerTable.add(correctButton).pad(horizontalGeneralMarginDimen * 3).width(correctButton.getWidth()).height(correctButton.getHeight());
        answerTable.add(wrongButton).pad(horizontalGeneralMarginDimen * 3).width(wrongButton.getWidth()).height(wrongButton.getHeight());
        table.add(answerTable).growX();
        return table;
    }

    private void wrongAnswerPressed() {
        executorService.shutdown();
        new MathLevelFinishedPopup(this, false).addToPopupManager();
    }

    private void correctAnswerPressed() {
        totalLevel++;
        executorService.shutdown();
        totalScore = totalScore + countdownAmountMillis.getValue();
        if (totalLevel >= LEVEL_GOAL) {
            campaignService.levelFinished(totalScore, mathCampaignLevelEnum);
            new MathLevelFinishedPopup(this, true).addToPopupManager();
            MyWrappedLabel scoreLabel = getRoot().findActor(SCORE_LABEL);
            scoreLabel.setText(getScoreText(totalLevel));
        } else {
            allTable.remove();
            createAllTable();
        }
    }

    private Table headerTable() {
        Table table = new Table();
        int percent = 45;
        table.add(createLabel(0.9f, MainGameLabel.l_score.getText("" + totalScore), FontColor.WHITE.getColor())).width(ScreenDimensionsManager.getScreenWidthValue(percent));
        MyWrappedLabel scoreLabel = createLabel(0.9f, getScoreText(totalLevel), FontColor.WHITE.getColor());
        scoreLabel.setName(SCORE_LABEL);
        table.add(scoreLabel).width(ScreenDimensionsManager.getScreenWidthValue(percent));
        return table;
    }

    private String getScoreText(int totalLevel) {
        return totalLevel + "/" + LEVEL_GOAL;
    }

    private Table countdownProcess() {
        Table table = new Table();
        MyWrappedLabel countdownAmountMillisLabel = createLabel(2f, "1", FontColor.LIGHT_GREEN.getColor());
        table.add(countdownAmountMillisLabel);
        int seconds = 9;
        countdownAmountMillis = new MutableLong(seconds * 1000);
        final int period = 100;
        final MathGameScreen screen = this;
        executorService.scheduleAtFixedRate(new ScreenRunnable(getAbstractScreen()) {
            @Override
            public void executeOperations() {
                if (countdownAmountMillis.getValue() <= 0) {
                    countdownFinished = true;
                    executorService.shutdown();
                }
                countdownAmountMillisLabel.setText(countdownAmountMillis.getValue() <= 0 ? "0" : countdownAmountMillis.toString());
                countdownAmountMillis.subtract(period);
            }

            @Override
            public void executeOperationsAfterScreenChanged() {
                executorService.shutdown();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
        return table;
    }

    private MyWrappedLabel createLabel(float fontScale, String text, Color fontColor) {
        return new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(
                        fontColor,
                        FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE * fontScale),
                        2f)).setText(text).build());
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (countdownFinished && !finishedLevelPopupDisplayed) {
            finishedLevelPopupDisplayed = true;
            wrongAnswerPressed();
        }
    }
}
