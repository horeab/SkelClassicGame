package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.apache.commons.lang3.mutable.MutableBoolean;
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
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.imagesplit.ImageSplitCampaignLevelEnum;
import libgdx.implementations.imagesplit.ImageSplitScreenManager;
import libgdx.implementations.imagesplit.ImageSplitSpecificResource;
import libgdx.implementations.imagesplit.spec.ImageMoveConfig;
import libgdx.implementations.imagesplit.spec.ImageSplitGameType;
import libgdx.implementations.imagesplit.spec.ImageSplitPreferencesManager;
import libgdx.implementations.imagesplit.spec.ImageSplitService;
import libgdx.implementations.imagesplit.spec.SimpleDirectionGestureDetector;
import libgdx.implementations.imagesplit.spec.SwipeDirection;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public abstract class ImageSplitGameScreen extends AbstractScreen<ImageSplitScreenManager> {


    static final float MOVE_IMG_PART_DURATION = 0.15f;
    static final float TUTORIAL_WAIT_BETWEEN_STEPS = MOVE_IMG_PART_DURATION * 3f;
    static final float TUTORIAL_INITIAL_DELAY = 0.4f;
    private MutableLong totalSeconds;
    private MyWrappedLabel totalSecondsLabel;
    private int totalMoves;
    private MyWrappedLabel totalMovesLabel;
    private MyButton replayBtn;
    private MyButton vieImgBtn;
    private ScheduledExecutorService executorService;
    private Table allTable;
    private Res imgRes;
    private MyButton hoverBackButton;
    private ImageSplitPreferencesManager imageSplitPreferencesManager = new ImageSplitPreferencesManager();
    private ImageSplitService imageSplitService = new ImageSplitService();
    private float totalImgWidth;
    private float totalImgHeight;
    int totalCols;
    int totalRows;
    Map<Pair<Integer, Integer>, Image> correctImageParts;
    Map<Pair<Integer, Integer>, Image> imageParts;
    private ImageSplitCampaignLevelEnum campaignLevelEnum;
    private ImageSplitGameType gameType;
    private boolean popupDisplayed;
    private MyPopup<AbstractScreen, ImageSplitScreenManager> viewImgPopup;
    private boolean tutorialRunning;

    ImageSplitGameScreen(ImageSplitCampaignLevelEnum campaignLevelEnum, ImageSplitGameType gameType) {
        this.campaignLevelEnum = campaignLevelEnum;
        this.gameType = gameType;
        init();
    }

    abstract void processClonedImages(List<ImageMoveConfig> imageMoveConfigs, Pair<Integer, Integer> coord, float duration, SwipeDirection swipeDirection);

    abstract void simulateMoveStep();

    abstract List<ImageMoveConfig> getImagesToProcessSwipeUp(Pair<Integer, Integer> coord, SwipeDirection direction);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeDown(Pair<Integer, Integer> coord, SwipeDirection direction);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeLeft(Pair<Integer, Integer> coord, SwipeDirection direction);

    abstract List<ImageMoveConfig> getImagesToProcessSwipeRight(Pair<Integer, Integer> coord, SwipeDirection direction);

    void init() {
        popupDisplayed = false;
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
        initTotalSecondsTable();
        initTotalMovesLabel();
        initButtons();
        countdownProcess();
    }

    void processImageSwipe(List<ImageMoveConfig> imageMoveConfigs, Pair<Integer, Integer> pressedCoord, SwipeDirection swipeDirection) {
        float duration = MOVE_IMG_PART_DURATION;
        boolean afterOneMoveExecuted = false;
        for (ImageMoveConfig imageMoveConfig : imageMoveConfigs) {
            moveImgConfig(imageMoveConfig, duration, new Runnable() {
                @Override
                public void run() {
                }
            });
            if (!afterOneMoveExecuted) {
                if (!tutorialRunning) {
                    totalMoves++;
                    totalMovesLabel.setText(totalMoves + "");
                }
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

    void moveImgConfig(ImageMoveConfig imageMoveConfig, float duration, Runnable afterMoveBy) {
        SwipeDirection direction = imageMoveConfig.getDirection();
        Image image = imageMoveConfig.getImage();
        moveImg(duration, afterMoveBy, direction, image, getMoveDirectionAmount(direction));
    }

    void moveImg(float duration, Runnable afterMoveBy, SwipeDirection direction, Image image, float moveAmount) {
        image.addAction(Actions.sequence(Actions.moveTo(leftRightSwipe(direction) ? image.getX() + moveAmount : image.getX(),
                upDownSwipe(direction) ? image.getY() + moveAmount : image.getY(), duration), Utils.createRunnableAction(afterMoveBy)));
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
                if (!tutorialRunning) {
                    processImageSwipe(getImagesToProcessSwipeUp(coord, direction), coord, direction);
                }
            }

            @Override
            public void onDown() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                SwipeDirection direction = SwipeDirection.DOWN;
                if (!tutorialRunning) {
                    processImageSwipe(getImagesToProcessSwipeDown(coord, direction), coord, direction);
                }
            }

            @Override
            public void onRight() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                SwipeDirection direction = SwipeDirection.RIGHT;
                if (!tutorialRunning) {
                    processImageSwipe(getImagesToProcessSwipeRight(coord, direction), coord, direction);
                }
            }

            @Override
            public void onLeft() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                SwipeDirection direction = SwipeDirection.LEFT;
                if (!tutorialRunning) {
                    processImageSwipe(getImagesToProcessSwipeLeft(coord, direction), coord, direction);
                }
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
        if (!imageSplitPreferencesManager.isTutorialPlayed(gameType)) {
            imageSplitPreferencesManager.putTutorialPlayed(gameType);
            simulateMoveStep();
        }
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

    private void initTotalSecondsTable() {
        totalSecondsLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                        FontColor.GREEN.getColor(), Math.round(FontConfig.FONT_SIZE * 2), 4f)).setText(totalSeconds.intValue() + "").build());
        totalSecondsLabel.setX(ScreenDimensionsManager.getScreenWidth() - getHeaderSideMargin());
        totalSecondsLabel.setY(getHeaderY());
        totalSecondsLabel.toFront();
        addActor(totalSecondsLabel);
    }

    private void initTotalMovesLabel() {
        totalMovesLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                        FontColor.LIGHT_BLUE.getColor(), Math.round(FontConfig.FONT_SIZE * 2), 4f)).setText(totalMoves + "").build());
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
        vieImgBtn = new ImageButtonBuilder(SkelClassicButtonSkin.IMAGE_SPLIT_VIEW_IMG, Game.getInstance().getAbstractScreen())
                .setFixedButtonSize(btnSize)
                .build();
        float btnStartX = getBtnStartX();
        for (MyButton btn : Arrays.asList(replayBtn, vieImgBtn)) {
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
        if (!popupDisplayed) {
            replayBtnPressed(x, y, btnStartX, btnStartY);
            viewImgBtnPressed(x, y, btnStartX + getNextBtnStartX(1), btnStartY);
        } else {
            if (viewImgPopup != null) {
                viewImgPopup.hide();
            }
        }
    }

    private void viewImgBtnPressed(float x, float y, float btnStartX, float btnStartY) {
        MainButtonSize buttonSize = getBtnSize();
        if (x > btnStartX && x < btnStartX + buttonSize.getWidth()
                && y > btnStartY && y < btnStartY + buttonSize.getHeight()) {

            Image origImg = GraphicUtils.getImage(imgRes);
            origImg.setWidth(totalImgWidth);
            origImg.setHeight(totalImgHeight);
            popupDisplayed = true;
            viewImgPopup = new MyPopup<AbstractScreen, ImageSplitScreenManager>(getAbstractScreen()) {
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

                @Override
                public void hide() {
                    super.hide();
                    popupDisplayed = false;
                }

                @Override
                public float getPrefWidth() {
                    return ScreenDimensionsManager.getScreenWidth();
                }
            };
            viewImgPopup.getContentTable().add(origImg).width(origImg.getWidth()).height(origImg.getHeight());
            viewImgPopup.addToPopupManager();
        }
    }

    private void replayBtnPressed(float x, float y, float btnStartX, float btnStartY) {
        MainButtonSize buttonSize = getBtnSize();
        if (x > btnStartX && x < btnStartX + buttonSize.getWidth()
                && y > btnStartY && y < btnStartY + buttonSize.getHeight()) {
            float duration = 0.25f;
            totalSecondsLabel.addAction(Actions.fadeOut(duration));
            totalMovesLabel.addAction(Actions.fadeOut(duration));
            fadeOutImageParts(duration);
            addAction(Actions.sequence(Actions.delay(duration), Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    screenManager.showGameScreen(gameType, campaignLevelEnum);
                }
            })));
        }
    }

    void levelFinished() {
        final MutableBoolean secondsRecord = new MutableBoolean(false);
        final MutableBoolean movesRecord = new MutableBoolean(false);
        executorService.shutdown();
        int maxSeconds = imageSplitPreferencesManager.getMaxSeconds(gameType, campaignLevelEnum);
        if (maxSeconds == 0 || totalSeconds.intValue() < maxSeconds) {
            secondsRecord.setTrue();
            imageSplitPreferencesManager.putMaxSeconds(gameType, campaignLevelEnum, totalSeconds.intValue());
        }
        int maxMoves = imageSplitPreferencesManager.getMaxMoves(gameType, campaignLevelEnum);
        if (maxMoves == 0 || totalMoves < maxMoves) {
            movesRecord.setTrue();
            imageSplitPreferencesManager.putMaxMoves(gameType, campaignLevelEnum, totalMoves);
        }
        imageSplitPreferencesManager.incrementWithAdPopupValue(campaignLevelEnum.getShowPopupAdIncrementValue());
        Gdx.input.setInputProcessor(getStage());
        showOriginalImage(secondsRecord.booleanValue(), movesRecord.booleanValue());
    }

    private void showOriginalImage(final boolean secondsRecord, final boolean movesRecord) {
        fadeOutImageParts(0.5f);
        Image origImg = GraphicUtils.getImage(imgRes);
        origImg.setWidth(totalImgWidth);
        origImg.setHeight(totalImgHeight);
        origImg.setX(0);
        origImg.setY((ScreenDimensionsManager.getExternalDeviceHeight() - totalImgHeight) / 2);
        origImg.addAction(Actions.fadeOut(0f));
        origImg.addAction(Actions.sequence(Actions.delay(0.5f), Actions.fadeIn(0.5f), Actions.delay(1f, Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                final Runnable showPopup = new Runnable() {
                    @Override
                    public void run() {
                        popupDisplayed = true;
                        addLevelFinishedPopup(secondsRecord, movesRecord);
                    }
                };
                int showAdPopupValue = imageSplitPreferencesManager.getShowAdPopupValue();
                if (showAdPopupValue > 0 && showAdPopupValue % 4 == 0) {
                    Game.getInstance().getAppInfoService().showPopupAd(new Runnable() {
                        @Override
                        public void run() {
                            showPopup.run();
                        }
                    });
                } else {
                    showPopup.run();
                }
            }
        }))));
        addActor(origImg);
    }

    private void addLevelFinishedPopup(boolean secondsRecord, boolean movesRecord) {
        MyPopup levelFinishedPopup = new MyPopup<AbstractScreen, ImageSplitScreenManager>(getAbstractScreen()) {
            @Override
            protected String getLabelText() {
                return "";
            }

            @Override
            protected void addButtons() {
                MyButton playAgain = new libgdx.controls.button.ButtonBuilder(SkelGameLabel.play_again.getText())
                        .setDefaultButton()
                        .build();
                playAgain.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        screenManager.showGameScreen(gameType, campaignLevelEnum);
                    }
                });
                MyButton goBack = new libgdx.controls.button.ButtonBuilder(SkelGameLabel.go_back.getText())
                        .setDefaultButton()
                        .build();
                goBack.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Game.getInstance().getScreenManager().showMainScreen();
                    }
                });
                addButton(playAgain);
                addButton(goBack);
            }

            @Override
            public void hide() {
                Game.getInstance().getScreenManager().showMainScreen();
            }


            @Override
            public MyPopup addToPopupManager() {
                addText();
                addButtons();
                padBottom(MainDimen.vertical_general_margin.getDimen());
                padTop(MainDimen.vertical_general_margin.getDimen());
                setBackground();
                getPopupManager().addPopupToDisplay(this);
                return this;
            }
        };
        levelFinishedPopup.getContentTable().add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(
                new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 1.6f))
                .setText(secondsRecord || movesRecord ? MainGameLabel.l_highscore_record.getText() : "").build())).row();
        levelFinishedPopup.getContentTable().add(ImageSplitMainMenuScreen.createScoresTable(totalSeconds.intValue(), totalMoves, secondsRecord, movesRecord))
                .pad(MainDimen.vertical_general_margin.getDimen() * 2);
        levelFinishedPopup.addToPopupManager();
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
        return MainDimen.horizontal_general_margin.getDimen() * 8;
    }

    private float getHeaderY() {
        return ScreenDimensionsManager.getScreenHeightValue(92);
    }

    private void countdownProcess() {
        final int period = 1;
        executorService.scheduleAtFixedRate(new ScreenRunnable(getAbstractScreen()) {
            @Override
            public void executeOperations() {
                if (!tutorialRunning) {
                    totalSeconds.add(period);
                    totalSecondsLabel.setText(totalSeconds.intValue() + "");
                }
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

    void simulateMoveFinger(final List<Pair<Integer, Integer>> coords, final List<SwipeDirection> directions, final int directionIndex) {
        if (directionIndex < directions.size()) {
            tutorialRunning = true;
            final SwipeDirection direction = directions.get(directionIndex);
            Pair<Integer, Integer> coord = coords.get(directionIndex);
            final Image pressFinger = GraphicUtils.getImage(ImageSplitSpecificResource.tutorial_swipe_finger);
            float imgDim = MainDimen.horizontal_general_margin.getDimen() * 10;
            pressFinger.setWidth(imgDim);
            pressFinger.setHeight(imgDim);
            pressFinger.setX(imageParts.get(coord).getX() + getPartWidth() / 4);
            pressFinger.setY(imageParts.get(coord).getY() + getPartHeight() / 4);
            addAction(Actions.sequence(
                    Actions.delay(directionIndex == 0 ? TUTORIAL_INITIAL_DELAY : TUTORIAL_WAIT_BETWEEN_STEPS),
                    Utils.createRunnableAction(new Runnable() {
                        @Override
                        public void run() {
                            moveImg(TUTORIAL_WAIT_BETWEEN_STEPS, new Runnable() {
                                @Override
                                public void run() {
                                    simulateMoveFinger(coords, directions, directionIndex + 1);
                                }
                            }, direction, pressFinger, getMoveDirectionAmount(direction) / 2);
                            pressFinger.addAction(Actions.fadeOut(MOVE_IMG_PART_DURATION * 3.5f));
                        }
                    })));
            addActor(pressFinger);
        } else {
            tutorialRunning = false;
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
