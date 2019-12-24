package libgdx.implementations.math;

import com.badlogic.gdx.utils.Align;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.resources.FontManager;
import libgdx.screen.AbstractScreen;
import libgdx.utils.model.FontColor;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class CountDownCounter {

    private long initialMillis;
    private long countdownAmountMillis;
    private String finalSecondText;
    private AbstractScreen currentScreen;
    private MyWrappedLabel countdownCounterLabel;
    private float fontScale = FontManager.getBigFontDim();

    public CountDownCounter(long countdownAmountMillis,
                            final String finalSecondText,
                            AbstractScreen currentScreen) {
        this.countdownAmountMillis = countdownAmountMillis;
        this.initialMillis = countdownAmountMillis;
        this.finalSecondText = finalSecondText;
        this.currentScreen = currentScreen;
        countdownCounterLabel = new MyWrappedLabel();
    }

    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
    }

    public void start() {
        countdownCounterLabel.setFontScale(fontScale);
        countdownCounterLabel.setStyleDependingOnContrast();
        countdownCounterLabel.setAlignment(Align.center);
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        final CountDownCounter thisCounter = this;
        executorService.scheduleAtFixedRate(new ScreenRunnable(currentScreen) {
            @Override
            public void executeOperations() {
                String text = thisCounter.initialMillis > 60000 ? processDisplayString(thisCounter.countdownAmountMillis) : thisCounter.countdownAmountMillis / 1000 + "";
                if (thisCounter.countdownAmountMillis <= -1000) {
                    text = finalSecondText;
                    executorService.shutdown();
                    executeAfterCountDownCounter();
                } else if (thisCounter.countdownAmountMillis <= 0) {
                    executeOnZeroSeconds();
                    countdownCounterLabel.setTextColor(FontColor.RED);
                    countdownCounterLabel.setFontScale(fontScale * 1.2f);
                    text = getFinalSecondText(text);
                }
                countdownCounterLabel.setText(text);
                thisCounter.countdownAmountMillis = thisCounter.countdownAmountMillis - 1000;
            }

            @Override
            public void executeOperationsAfterScreenChanged() {
                executorService.shutdown();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private String getFinalSecondText(String currentText) {
        return StringUtils.isNotBlank(finalSecondText) ? finalSecondText : currentText;
    }

    private String processDisplayString(long totalMillis) {
        int toSec = (int) totalMillis / 1000;
        int minutes = (toSec % 3600) / 60;
        int seconds = toSec % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public MyWrappedLabel getCountdownCounterLabel() {
        return countdownCounterLabel;
    }

    public abstract void executeAfterCountDownCounter();

    public void executeOnZeroSeconds() {
    }
}
