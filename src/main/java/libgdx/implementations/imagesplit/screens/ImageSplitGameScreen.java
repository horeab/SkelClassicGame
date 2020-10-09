package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
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
import libgdx.controls.popup.MyPopup;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.imagesplit.ImageSplitCampaignLevelEnum;
import libgdx.implementations.imagesplit.ImageSplitScreenManager;
import libgdx.implementations.imagesplit.ImageSplitSpecificResource;
import libgdx.implementations.imagesplit.spec.ImageMoveConfig;
import libgdx.implementations.imagesplit.spec.ImageSplitService;
import libgdx.implementations.imagesplit.spec.SimpleDirectionGestureDetector;
import libgdx.implementations.imagesplit.spec.SwipeDirection;
import libgdx.implementations.resourcewars.ResourceWarsScreenManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public abstract class ImageSplitGameScreen extends AbstractScreen<ImageSplitScreenManager> {


    private MutableLong totalSeconds;
    private MyWrappedLabel totalSecondsLabel;
    private int totalMoves;
    private MyWrappedLabel totalMovesLabel;
    private MyButton replayBtn;
    private MyButton vieImgBtn;
    private ScheduledExecutorService executorService;
    private Table allTable;
    Res imgRes;
    private MyButton hoverBackButton;
    ImageSplitService imageSplitService = new ImageSplitService();
    float totalImgWidth;
    float totalImgHeight;
    int totalCols;
    int totalRows;
    Map<Pair<Integer, Integer>, Image> correctImageParts;
    Map<Pair<Integer, Integer>, Image> imageParts;
    ImageSplitCampaignLevelEnum campaignLevelEnum;

    ImageSplitGameScreen(ImageSplitCampaignLevelEnum campaignLevelEnum) {
        this.campaignLevelEnum = campaignLevelEnum;
        init();
    }

    abstract void processClonedImages(List<ImageMoveConfig> imageMoveConfigs, Pair<Integer, Integer> coord, float duration, SwipeDirection swipeDirection);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeUp(Pair<Integer, Integer> coord, SwipeDirection direction);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeDown(Pair<Integer, Integer> coord, SwipeDirection direction);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeLeft(Pair<Integer, Integer> coord, SwipeDirection direction);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeRight(Pair<Integer, Integer> coord, SwipeDirection direction);

    void init() {
        totalMoves = 0;
        totalSeconds = new MutableLong(0);
        correctImageParts = new LinkedHashMap<>();
        imageParts = new LinkedHashMap<>();
        executorService = Executors.newSingleThreadScheduledExecutor();
        imgRes = campaignLevelEnum.getRes();
        addDirectionGestureListener();
        totalImgWidth = ScreenDimensionsManager.getScreenWidth();
        totalImgHeight = ScreenDimensionsManager.getNewHeightForNewWidth(totalImgWidth, GraphicUtils.getImage(imgRes));
        totalCols = campaignLevelEnum.getCols();
        totalRows = campaignLevelEnum.getRows();
        initImageParts();
        initTotalSecondsLabel();
        initTotalMovessLabel();
        initButtons();
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
                totalMoves++;
                totalMovesLabel.setText(totalMoves + "");
                afterOneMoveExecuted = true;
                processAfterMoveImg(pressedCoord, imageMoveConfig.getDirection());
            }
        }
        processClonedImages(imageMoveConfigs, pressedCoord, duration, swipeDirection);
    }

    float getMoveDirectionAmount(SwipeDirection direction) {
        float amount = upDownSwipe(direction) ? getPartHeight() : getPartWidth();
        amount = amount + getPartPad();
        amount = direction == SwipeDirection.DOWN || direction == SwipeDirection.LEFT ? -amount : amount;
        return amount;
    }

    void moveImg(ImageMoveConfig imageMoveConfig, float duration, Runnable afterMoveBy) {
        SwipeDirection direction = imageMoveConfig.getDirection();
        Image image = imageMoveConfig.getImage();
        image.addAction(Actions.sequence(Actions.moveTo(leftRightSwipe(direction) ? image.getX() + getMoveDirectionAmount(direction) : image.getX(),
                upDownSwipe(direction) ? image.getY() + getMoveDirectionAmount(direction) : image.getY(), duration), Utils.createRunnableAction(afterMoveBy)));
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
                btnPressed(x, y);
            }
        }));
    }

    @Override
    public void buildStage() {
        createAllTable();
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        controlsToFront();
    }

    private void createAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        addActor(allTable);
        if (hoverBackButton != null) {
            hoverBackButton.toFront();
        }
        createImageTable();
        createAllMarginTable();
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

    private void createAllMarginTable() {
        float extHeight = ScreenDimensionsManager.getExternalDeviceHeight();
        float extWidth = ScreenDimensionsManager.getExternalDeviceWidth();
        Table leftMarginTable = createMarginTable();
        leftMarginTable.setWidth(extWidth / 2);
        leftMarginTable.setHeight(extHeight);
        leftMarginTable.setX(getImageTableStartX() - extWidth / 2);
        leftMarginTable.setY(0);
        addActor(leftMarginTable);
        Table rightMarginTable = createMarginTable();
        rightMarginTable.setWidth(extWidth / 2);
        rightMarginTable.setHeight(extHeight);
        rightMarginTable.setX(getImageTableStartX() + totalImgWidth + getPartPad() * totalCols);
        rightMarginTable.setY(0);
        addActor(rightMarginTable);
        Table topMarginTable = createMarginTable();
        topMarginTable.setWidth(extWidth);
        topMarginTable.setHeight(extHeight / 2);
        topMarginTable.setX(0);
        topMarginTable.setY(getImageTableStartY() + getPartHeight() + getPartPad());
        addActor(topMarginTable);
        Table bottomMarginTable = createMarginTable();
        bottomMarginTable.setWidth(extWidth);
        bottomMarginTable.setHeight(extHeight / 2);
        bottomMarginTable.setX(0);
        bottomMarginTable.setY(-(extHeight / 2) + (extHeight - (totalImgHeight + getPartPad() * (totalRows + 3))) / 2);
        addActor(bottomMarginTable);
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
        return MainDimen.horizontal_general_margin.getDimen() / 4f;
//        return 0;
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

    private void initTotalMovessLabel() {
        totalMovesLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(), Math.round(FontConfig.FONT_SIZE * 2), 4f)).setText(totalMoves + "").build());
        totalMovesLabel.setX(getHeaderSideMargin());
        totalMovesLabel.setY(getHeaderY());
        totalMovesLabel.toFront();
        addActor(totalMovesLabel);
    }

    private void initButtons() {
        MainButtonSize btnSize = getBtnSize();
        replayBtn = new ImageButtonBuilder(MainButtonSkin.REFRESH, Game.getInstance().getAbstractScreen())
                .setFixedButtonSize(btnSize)
                .build();
        vieImgBtn = new ImageButtonBuilder(MainButtonSkin.REFRESH, Game.getInstance().getAbstractScreen())
                .setFixedButtonSize(btnSize)
                .build();
        float btnStartX = getBtnStartX();
        for (MyButton btn : Arrays.asList(vieImgBtn, replayBtn)) {
            btn.setX(btnStartX);
            btn.setY(getBtnStartY());
            btn.toFront();
            addActor(btn);
            btnStartX = btnStartX + getNextBtnStartX(1);
        }
    }

    private float getNextBtnStartX(int index) {
        return (getBtnSize().getWidth() + MainDimen.horizontal_general_margin.getDimen()) * index;
    }

    private MainButtonSize getBtnSize() {
        return MainButtonSize.STANDARD_IMAGE;
    }

    private float getBtnStartY() {
        return getHeaderY() - getBtnSize().getHeight() / 2;
    }

    private float getBtnStartX() {
        return ScreenDimensionsManager.getScreenWidth() / 2 - (getBtnSize().getWidth() / 2 * 2);
    }

    private void btnPressed(float x, float y) {
        float btnStartX = getBtnStartX();
        float btnStartY = getBtnStartY();
        y = ScreenDimensionsManager.getExternalDeviceHeight() - y - (ScreenDimensionsManager.getExternalDeviceHeight() - ScreenDimensionsManager.getScreenHeight()) / 2;
        x = x - (ScreenDimensionsManager.getExternalDeviceWidth() - ScreenDimensionsManager.getScreenWidth()) / 2;
        replayBtnPressed(x, y, btnStartX, btnStartY);
        viewImgBtnPressed(x, y, btnStartX + getNextBtnStartX(1), btnStartY);
    }

    private void viewImgBtnPressed(float x, float y, float btnStartX, float btnStartY) {
        MainButtonSize buttonSize = getBtnSize();
        if (x > btnStartX && x < btnStartX + buttonSize.getWidth()
                && y > btnStartY && y < btnStartY + buttonSize.getHeight()) {

            Image origImg = GraphicUtils.getImage(imgRes);
            origImg.setWidth(totalImgWidth);
            origImg.setHeight(totalImgHeight);
            MyPopup<AbstractScreen, ImageSplitScreenManager> myPopup = new MyPopup<AbstractScreen, ImageSplitScreenManager>(getAbstractScreen()) {
                @Override
                protected String getLabelText() {
                    return "";
                }

                @Override
                protected void setBackground() {
                    setBackground(GraphicUtils.getNinePatch(MainResource.transparent_background));
                }

                @Override
                protected void addButtons() {
                }
            };
            myPopup.getContentTable().add(origImg).width(origImg.getWidth()).height(origImg.getHeight());
            myPopup.addToPopupManager();
        }
    }

    private void replayBtnPressed(float x, float y, float btnStartX, float btnStartY) {
        MainButtonSize buttonSize = getBtnSize();
        if (x > btnStartX && x < btnStartX + buttonSize.getWidth()
                && y > btnStartY && y < btnStartY + buttonSize.getHeight()) {
            executorService.shutdown();
            float duration = 0.25f;
            totalSecondsLabel.addAction(Actions.fadeOut(duration));
            totalMovesLabel.addAction(Actions.fadeOut(duration));
            fadeOutImageParts(duration);
            addAction(Actions.sequence(Actions.delay(duration), Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    init();
                    createAllTable();
                    controlsToFront();
                }
            })));
        }
    }

    void fadeOutImageParts(float duration) {
        for (Map.Entry<Pair<Integer, Integer>, Image> part : imageParts.entrySet()) {
            part.getValue().addAction(Actions.fadeOut(duration));
        }
    }

    private void controlsToFront() {
        hoverBackButton.toFront();
        totalSecondsLabel.toFront();
        totalMovesLabel.toFront();
        replayBtn.toFront();
        vieImgBtn.toFront();
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

    private void createImageTable() {
        int i = 0;
        float imageTableStartX = getImageTableStartX();
        float imgX = imageTableStartX;
        float imgY = getImageTableStartY();
        for (Map.Entry<Pair<Integer, Integer>, Image> part : imageParts.entrySet()) {
            if (i != 0 && i % totalCols == 0) {
                imgY = imgY - getPartHeight() - getPartPad();
                imgX = imageTableStartX;
            }
            Image image = part.getValue();
            image.setX(imgX);
            image.setY(imgY);
            addActor(image);
            image.toBack();
            imgX = imgX + getPartWidth() + getPartPad();
            i++;
        }
    }

    private float getImageTableStartY() {
        int screenHeight = ScreenDimensionsManager.getScreenHeight();
        return screenHeight - getPartHeight() - (screenHeight - totalImgHeight) / 2;
    }

    private float getImageTableStartX() {
        return ScreenDimensionsManager.getScreenHeightValue(0);
    }
}
