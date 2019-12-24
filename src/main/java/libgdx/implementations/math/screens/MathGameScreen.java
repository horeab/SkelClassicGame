package libgdx.implementations.math.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.implementations.math.MathLevel;
import libgdx.implementations.math.Operation;
import libgdx.screen.AbstractScreen;
import libgdx.utils.DateUtils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import org.apache.commons.lang3.mutable.MutableLong;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MathGameScreen extends AbstractScreen<MathScreenManager> {

    private Table allTable;
    private MyButton hoverBackButton;
    private MutableLong countdownAmountMillis;
    private ScheduledExecutorService executorService;
    private Long totalScore = 0L;
    private int totalLevel = 0;
    private MathLevel mathLevel;

    public MathGameScreen(MathLevel mathLevel) {
        this.mathLevel = mathLevel;
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
        allTable.add(headerTable()).growX().row();
        allTable.add(countdownProcess(9)).growX().row();
        allTable.add(calcTable()).grow().row();
        allTable.setFillParent(true);
        addActor(allTable);
    }

    private String processCalcString() {
        List<Operation> avOp = mathLevel.getAvailableOperations();
        Collections.shuffle(avOp);
        Operation op = avOp.get(0);
        Operation combOp = null;
        Integer val1 = null;
        Integer val2 = null;
        Integer comb = null;
        if (op == Operation.SUM) {
            if (mathLevel.getSumCombine() != null) {
                List<Operation> combOps = mathLevel.getAvailableOperations(mathLevel.getSumCombine());
                Collections.shuffle(combOps);
                combOp = combOps.get(0);

            }
        } else if (op == Operation.SUB) {
            if (mathLevel.getSubCombine() != null) {
                List<Operation> combOps = mathLevel.getAvailableOperations(mathLevel.getSubCombine());
                Collections.shuffle(combOps);
                combOp = combOps.get(0);
            }
        } else if (op == Operation.MUL) {

        } else if (op == Operation.DIV) {

        }
    }

    private Table calcTable() {
        Table table = new Table();
        int maxVal = mathLevel.getSubMaxVal();
        int val1 = new Random().nextInt(maxVal);
        int val2 = new Random().nextInt(maxVal);
        int correctSumVal1Val2 = val1 + val2;
        boolean correctSum = new Random().nextBoolean();
        String text = val1 + "+" + val2 + "=" + correctSumVal1Val2;
        if (!correctSum) {
            int varSum = new Random().nextInt(maxVal / 3) + 1;
            int wrongSumVal1Val2 = new Random().nextBoolean() ? correctSumVal1Val2 - varSum : correctSumVal1Val2 + varSum;
            text = val1 + "+" + val2 + "=" + wrongSumVal1Val2;
        }
        table.add(createLabel(1f, text)).grow().row();

        Table answerTable = new Table();
        MyButton correctButton = new ImageButtonBuilder(MainButtonSkin.SOUND_ON, Game.getInstance().getAbstractScreen())
                .setFixedButtonSize(MainButtonSize.STANDARD_IMAGE)
                .build();
        correctButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (correctSum) {
                    correctAnswerPressed();
                } else {

                }
            }
        });
        MyButton wrongButton = new ImageButtonBuilder(MainButtonSkin.SOUND_OFF, Game.getInstance().getAbstractScreen())
                .setFixedButtonSize(MainButtonSize.STANDARD_IMAGE)
                .build();
        wrongButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!correctSum) {
                    correctAnswerPressed();
                } else {

                }
            }
        });
        answerTable.add(correctButton).growX();
        answerTable.add(wrongButton).growX();
        table.add(answerTable).growX();
        return table;
    }

    private void correctAnswerPressed() {
        totalScore = totalScore + countdownAmountMillis.getValue();
        totalLevel++;
        executorService.shutdown();
        allTable.remove();
        createAllTable();
    }

    private Table headerTable() {
        Table table = new Table();
        table.add(createLabel(0.9f, "Score: " + totalScore)).growX();
        table.add(createLabel(0.9f, totalLevel + "/20")).growX();
        return table;
    }

    private Table countdownProcess(int seconds) {
        Table table = new Table();
        MyWrappedLabel countdownAmountMillisLabel = createLabel(1.1f, "1");
        table.add(countdownAmountMillisLabel);
        countdownAmountMillis = new MutableLong(seconds * 1000);
        final int period = 100;
        executorService.scheduleAtFixedRate(new ScreenRunnable(getAbstractScreen()) {
            @Override
            public void executeOperations() {
                if (countdownAmountMillis.getValue() <= 0) {
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

    private MyWrappedLabel createLabel(float fontScale, String text) {
        return new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(
                        FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE * fontScale),
                        2f)).setText(text).build());
    }

    @Override
    public void onBackKeyPress() {

    }
}
