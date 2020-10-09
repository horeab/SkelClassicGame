package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.imagesplit.ImageSplitScreenManager;
import libgdx.implementations.imagesplit.ImageSplitSpecificResource;
import libgdx.implementations.imagesplit.spec.ImageMoveConfig;
import libgdx.implementations.imagesplit.spec.ImageSplitService;
import libgdx.implementations.imagesplit.spec.SimpleDirectionGestureDetector;
import libgdx.implementations.imagesplit.spec.SwipeDirection;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public abstract class ImageSplitGameScreen extends AbstractScreen<ImageSplitScreenManager> {


    private MutableLong totalSeconds;
    private MyWrappedLabel totalSecondsLabel;
    private MyButton replayBtn;
    private ScheduledExecutorService executorService;
    private Table allTable;
    Res imgRes;
    private MyButton hoverBackButton;
    ImageSplitService imageSplitService = new ImageSplitService();
    float totalImgWidth;
    float totalImgHeight;
    int totalCols;
    int totalRows;
    Table imgTable;
    Map<Pair<Integer, Integer>, Image> correctImageParts;
    Map<Pair<Integer, Integer>, Image> imageParts;

    ImageSplitGameScreen() {
        init();
    }

    abstract Table createImageTable();

    abstract void processClonedImages(List<ImageMoveConfig> imageMoveConfigs, Pair<Integer, Integer> coord, float duration, SwipeDirection swipeDirection);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeUp(Pair<Integer, Integer> coord, SwipeDirection direction);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeDown(Pair<Integer, Integer> coord, SwipeDirection direction);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeLeft(Pair<Integer, Integer> coord, SwipeDirection direction);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeRight(Pair<Integer, Integer> coord, SwipeDirection direction);

    private void init() {
        totalSeconds = new MutableLong(0);
        imgTable = new Table();
        correctImageParts = new LinkedHashMap<>();
        imageParts = new LinkedHashMap<>();
        executorService = Executors.newSingleThreadScheduledExecutor();
        imgRes = ImageSplitSpecificResource.i0;
        addDirectionGestureListener();
        totalImgWidth = ScreenDimensionsManager.getScreenWidth();
        totalImgHeight = ScreenDimensionsManager.getNewHeightForNewWidth(totalImgWidth, GraphicUtils.getImage(imgRes));
        totalCols = 4;
        totalRows = 6;
        initImageParts();
        initTotalSecondsLabel();
        initReplayLevelBtn();
        countdownProcess();
    }

    private void processImageSwipe(List<ImageMoveConfig> imageMoveConfigs, Pair<Integer, Integer> pressedCoord, SwipeDirection swipeDirection) {
        float duration = 0.15f;
        boolean afterOneMoveExecuted = false;
        for (ImageMoveConfig imageMoveConfig : imageMoveConfigs) {
            moveImg(imageMoveConfig, duration, new Runnable() {
                @Override
                public void run() {
                }
            });
            if (!afterOneMoveExecuted) {
                afterOneMoveExecuted = true;
                processAfterMoveImg(pressedCoord, imageMoveConfig.getDirection());
            }
        }
        processClonedImages(imageMoveConfigs, pressedCoord, duration, swipeDirection);
    }

    float getMoveDirectionAmount(SwipeDirection direction) {
        float amount = upDownSwipe(direction) ? getPartHeight() : getPartWidth();
        amount = amount + getPartPad() * 2;
        amount = direction == SwipeDirection.DOWN || direction == SwipeDirection.LEFT ? -amount : amount;
        return amount;
    }

    void moveImg(ImageMoveConfig imageMoveConfig, float duration, Runnable afterMoveBy) {
        SwipeDirection direction = imageMoveConfig.getDirection();
        float amount = getMoveDirectionAmount(direction);
        imageMoveConfig.getImage().addAction(Actions.sequence(Actions.moveBy(leftRightSwipe(direction) ? amount : 0,
                upDownSwipe(direction) ? amount : 0, duration), Utils.createRunnableAction(afterMoveBy)));
    }

    void processAfterMoveImg(Pair<Integer, Integer> pressedCoord, SwipeDirection direction) {
    }

    private void addDirectionGestureListener() {
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            float pressedX;
            float pressedY;

            @Override
            public void onUp() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                SwipeDirection direction = SwipeDirection.UP;
                processImageSwipe(getImagesToProcessSwipeUp(coord, direction), coord, direction);
            }

            @Override
            public void onDown() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                SwipeDirection direction = SwipeDirection.DOWN;
                processImageSwipe(getImagesToProcessSwipeDown(coord, direction), coord, direction);
            }

            @Override
            public void onRight() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                SwipeDirection direction = SwipeDirection.RIGHT;
                processImageSwipe(getImagesToProcessSwipeRight(coord, direction), coord, direction);
            }

            @Override
            public void onLeft() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                SwipeDirection direction = SwipeDirection.LEFT;
                processImageSwipe(getImagesToProcessSwipeLeft(coord, direction), coord, direction);
            }

            @Override
            public void onTouchDown(float x, float y) {
                pressedX = x;
                pressedY = y;
                replayBtnPressed(x, y);
            }
        }));
    }

    @Override
    public void buildStage() {
        createAllTable();
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        totalSecondsLabel.toFront();
        replayBtn.toFront();
    }

    private void createAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        addActor(allTable);
        if (hoverBackButton != null) {
            hoverBackButton.toFront();
        }
        Stack imageContainer = createImageContainer();
        allTable.add(imageContainer).width(ScreenDimensionsManager.getScreenWidth()).height(ScreenDimensionsManager.getScreenHeight());
    }

    Pair<Integer, Integer> getDirectionNeighb(Pair<Integer, Integer> coord, SwipeDirection direction) {
        if (direction == SwipeDirection.UP) {
            return Pair.of(coord.getLeft(), coord.getRight() - 1);
        } else if (direction == SwipeDirection.DOWN) {
            return Pair.of(coord.getLeft(), coord.getRight() + 1);
        } else if (direction == SwipeDirection.LEFT) {
            return Pair.of(coord.getLeft() - 1, coord.getRight());
        } else if (direction == SwipeDirection.RIGHT) {
            return Pair.of(coord.getLeft() + 1, coord.getRight());
        }
        return null;
    }

    SwipeDirection getOppositeDirection(SwipeDirection direction) {
        if (direction == SwipeDirection.UP) {
            return SwipeDirection.DOWN;
        } else if (direction == SwipeDirection.DOWN) {
            return SwipeDirection.UP;
        } else if (direction == SwipeDirection.LEFT) {
            return SwipeDirection.RIGHT;
        } else if (direction == SwipeDirection.RIGHT) {
            return SwipeDirection.LEFT;
        }
        return null;
    }

    private Stack createImageContainer() {
        Stack stack = new Stack();
        stack.add(createImageTable());
        stack.add(createAllMarginTable());
        return stack;
    }

    private Table createAllMarginTable() {
        float extHeight = ScreenDimensionsManager.getExternalDeviceHeight();
        float extWidth = ScreenDimensionsManager.getExternalDeviceWidth();
        Table topMarginTable = createMarginTable();
        Table bottomMarginTable = createMarginTable();
        Table leftMarginTable = createMarginTable();
        Table rightMarginTable = createMarginTable();
        Table middleTable = new Table();
        middleTable.add(leftMarginTable).width(extWidth / 2).grow();
        middleTable.add().width(totalImgWidth).height(totalImgHeight);
        middleTable.add(rightMarginTable).width(extWidth / 2).grow();
        Table marginTable = new Table();
        marginTable.add(topMarginTable).height(extHeight / 2).grow();
        marginTable.row();
        marginTable.add(middleTable).height(totalImgHeight * 2);
        marginTable.row();
        marginTable.add(bottomMarginTable).height(extHeight / 2).grow();
        return marginTable;
    }

    private Table createMarginTable() {
        Table topMarginTable = new Table();
        topMarginTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        return topMarginTable;
    }

    private void initImageParts() {
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalCols; col++) {
                Image image = imageSplitService.crop(imgRes, totalCols, totalRows, totalImgWidth, totalImgHeight, col, row);
                Pair<Integer, Integer> coord = Pair.of(col, row);
                correctImageParts.put(coord, image);
            }
        }
        while (imageParts.isEmpty() || imageParts.equals(correctImageParts)) {
            List<Image> shuffledImages = new ArrayList<>(correctImageParts.values());
            Collections.shuffle(shuffledImages);
            int i = 0;
            for (Map.Entry<Pair<Integer, Integer>, Image> part : correctImageParts.entrySet()) {
                imageParts.put(part.getKey(), shuffledImages.get(i));
                i++;
            }
        }
    }

    float getPartPad() {
//        return MainDimen.horizontal_general_margin.getDimen() / 8;
        return 0;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }


    boolean leftRightSwipe(SwipeDirection direction) {
        return direction == SwipeDirection.RIGHT || direction == SwipeDirection.LEFT;
    }

    boolean upDownSwipe(SwipeDirection direction) {
        return direction == SwipeDirection.UP || direction == SwipeDirection.DOWN;
    }

    @Override
    public void show() {
    }

    Pair<Integer, Integer> getCoordForXY(float x, float y) {
        y = y - (ScreenDimensionsManager.getExternalDeviceHeight() - totalImgHeight - getPartPad() * totalRows) / 2;
        x = x - (ScreenDimensionsManager.getExternalDeviceWidth() - totalImgWidth - getPartPad() * totalCols) / 2;
        int coordCol = (int) Math.ceil(x / (getPartWidth() + getPartPad())) - 1;
        int coordRow = (int) Math.ceil(y / (getPartHeight() + getPartPad())) - 1;
        return Pair.of(coordCol, coordRow);
    }

    float getPartHeight() {
        return totalImgHeight / totalRows;
    }

    float getPartWidth() {
        return totalImgWidth / totalCols;
    }

    private void initTotalSecondsLabel() {
        totalSecondsLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(), Math.round(FontConfig.FONT_SIZE * 2), 4f)).setText(totalSeconds.intValue() + "").build());
        totalSecondsLabel.setX(ScreenDimensionsManager.getScreenWidth() - getHeaderSideMargin());
        totalSecondsLabel.setY(getHeaderY());
        totalSecondsLabel.toFront();
        addActor(totalSecondsLabel);
    }

    private void initReplayLevelBtn() {
        replayBtn = new ImageButtonBuilder(MainButtonSkin.REFRESH, Game.getInstance().getAbstractScreen())
                .setFixedButtonSize(getReplayBtnSize())
                .build();
        replayBtn.setX(getReplayBtnStartX());
        replayBtn.setY(getReplayBtnStartY());
        replayBtn.toFront();
        addActor(replayBtn);
    }

    private MainButtonSize getReplayBtnSize() {
        return MainButtonSize.STANDARD_IMAGE;
    }

    private float getReplayBtnStartY() {
        return getHeaderY() - getReplayBtnSize().getHeight() / 2;
    }

    private float getReplayBtnStartX() {
        return ScreenDimensionsManager.getScreenWidth() / 2 - getReplayBtnSize().getWidth() / 2;
    }

    private void replayBtnPressed(float x, float y) {
        float replayBtnStartX = getReplayBtnStartX();
        float replayBtnStartY = getReplayBtnStartY();
        y = ScreenDimensionsManager.getExternalDeviceHeight() - y - (ScreenDimensionsManager.getExternalDeviceHeight() - ScreenDimensionsManager.getScreenHeight()) / 2;
        x = x - (ScreenDimensionsManager.getExternalDeviceWidth() - ScreenDimensionsManager.getScreenWidth()) / 2;
        MainButtonSize replayBtnSize = getReplayBtnSize();
        if (x > replayBtnStartX && x < replayBtnStartX + replayBtnSize.getWidth()
                && y > replayBtnStartY && y < replayBtnStartY + replayBtnSize.getHeight()) {
            executorService.shutdown();
            float duration = 0.25f;
            totalSecondsLabel.addAction(Actions.fadeOut(duration));
            imgTable.addAction(Actions.sequence(Actions.fadeOut(duration), Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    allTable.clear();
                    init();
                    createAllTable();
                    hoverBackButton.toFront();
                    totalSecondsLabel.toFront();
                    replayBtn.toFront();
                }
            })));
        }
    }

    private float getHeaderSideMargin() {
        return MainDimen.horizontal_general_margin.getDimen() * 2;
    }

    private float getHeaderY() {
        return ScreenDimensionsManager.getScreenHeightValue(92);
    }

    private void countdownProcess() {
        final int period = 1;
        executorService.scheduleAtFixedRate(new ScreenRunnable(getAbstractScreen()) {
            @Override
            public void executeOperations() {
                totalSeconds.add(period);
                totalSecondsLabel.setText(totalSeconds.intValue() + "");
            }

            @Override
            public void executeOperationsAfterScreenChanged() {
                executorService.shutdown();
            }
        }, 0, period, TimeUnit.SECONDS);
    }

}
