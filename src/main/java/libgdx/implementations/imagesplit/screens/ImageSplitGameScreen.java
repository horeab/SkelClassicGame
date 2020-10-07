package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.imagesplit.ImageSplitScreenManager;
import libgdx.implementations.imagesplit.ImageSplitSpecificResource;
import libgdx.implementations.imagesplit.spec.ImageSplitService;
import libgdx.implementations.imagesplit.spec.SimpleDirectionGestureDetector;
import libgdx.implementations.imagesplit.spec.SwipeDirection;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

public class ImageSplitGameScreen extends AbstractScreen<ImageSplitScreenManager> {


    private Table allTable;
    private Res imgRes;
    private MyButton hoverBackButton;
    private ImageSplitService imageSplitService = new ImageSplitService();
    private float totalImgWidth;
    private float totalImgHeight;
    private int totalCols;
    private int totalRows;
    private Table imgTable = new Table();
    private Map<Pair<Integer, Integer>, Image> imageParts = new LinkedHashMap<>();

    public ImageSplitGameScreen() {
        init();
    }

    private void init() {
        imgRes = ImageSplitSpecificResource.i0;
        addDirectionGestureListener();
        totalImgWidth = ScreenDimensionsManager.getScreenWidth();
        totalImgHeight = ScreenDimensionsManager.getNewHeightForNewWidth(totalImgWidth, GraphicUtils.getImage(imgRes));
        totalCols = 4;
        totalRows = 3;
    }

    @Override
    public void buildStage() {
        createAllTable();
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
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

    private Table createImageTable() {
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalCols; col++) {
                Image image = imageSplitService.crop(imgRes, totalCols, totalRows, totalImgWidth, totalImgHeight, col, row);
                Pair<Integer, Integer> coord = new MutablePair<>(col, row);
                imageParts.put(coord, image);
                imgTable.add(image).pad(getPartPad()).width(image.getWidth()).height(image.getHeight());
            }
            imgTable.row();
        }
        return imgTable;
    }

    private float getPartPad() {
        return MainDimen.horizontal_general_margin.getDimen() / 8;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    private void addDirectionGestureListener() {
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            float pressedX;
            float pressedY;

            @Override
            public void onUp() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                moveImages(getImages(coord.getLeft(), null), coord, SwipeDirection.UP);
            }

            @Override
            public void onDown() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                moveImages(getImages(coord.getLeft(), null), coord, SwipeDirection.DOWN);
            }

            @Override
            public void onRight() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                moveImages(getImages(null, coord.getRight()), coord, SwipeDirection.RIGHT);
            }

            @Override
            public void onLeft() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                moveImages(getImages(null, coord.getRight()), coord, SwipeDirection.LEFT);
            }

            @Override
            public void onTouchDown(float x, float y) {
                pressedX = x;
                pressedY = y;
                System.out.println("" + x);
            }
        }));
    }

    private void moveImages(List<Image> images, Pair<Integer, Integer> coord, SwipeDirection direction) {
        float amount = upDownSwipe(direction) ? getPartHeight() : getPartWidth();
        amount = amount + getPartPad() * 2;
        amount = direction == SwipeDirection.DOWN || direction == SwipeDirection.LEFT ? -amount : amount;
        float duration = 0.15f;
        for (Image img : images) {
            moveImg(direction, amount, duration, img, new Runnable() {
                @Override
                public void run() {
                }
            });
        }
        processCopyImage(images, direction, coord, amount, duration);
    }

    private void processCopyImage(List<Image> images, SwipeDirection direction, Pair<Integer, Integer> coord, float amount, float duration) {
        if (images != null && !images.isEmpty()) {
            Image startImg = direction == SwipeDirection.DOWN || direction == SwipeDirection.RIGHT ? images.get(0) : images.get(images.size() - 1);
            Image finishImg = direction == SwipeDirection.UP || direction == SwipeDirection.LEFT ? images.get(0) : images.get(images.size() - 1);
            Image copyImg = new Image(finishImg.getDrawable());
            float initCopyImgPos = upDownSwipe(direction) ? getPartHeight() : getPartWidth();
            initCopyImgPos = initCopyImgPos + getPartPad() * 2;
            initCopyImgPos = direction == SwipeDirection.UP || direction == SwipeDirection.RIGHT ? -initCopyImgPos : initCopyImgPos;
            copyImg.setX(startImg.getX() + (leftRightSwipe(direction) ? initCopyImgPos : 0));
            copyImg.setY(startImg.getY() + (upDownSwipe(direction) ? initCopyImgPos : 0));
            addActor(copyImg);
            moveImg(direction, amount, duration, copyImg, new Runnable() {
                @Override
                public void run() {
                    finishImg.setX(copyImg.getX());
                    finishImg.setY(copyImg.getY());
                    copyImg.setVisible(false);
                    changeImgCoords(coord, direction);
                }
            });
        }
    }

    private void changeImgCoords(Pair<Integer, Integer> pressedCoord, SwipeDirection direction) {
        Map<Pair<Integer, Integer>, Image> newImageParts = new LinkedHashMap<>();
        for (Map.Entry<Pair<Integer, Integer>, Image> p : imageParts.entrySet()) {
            Pair<Integer, Integer> imgCoord = p.getKey();
            if (leftRightSwipe(direction) && imgCoord.getRight().equals(pressedCoord.getRight())) {
                int newCoord = imgCoord.getLeft() + (direction == SwipeDirection.RIGHT ? +1 : -1);
                newCoord = processNewCoord(newCoord, totalCols);
                newImageParts.put(Pair.of(newCoord, imgCoord.getRight()), p.getValue());
            } else if (upDownSwipe(direction) && imgCoord.getLeft().equals(pressedCoord.getLeft())) {
                int newCoord = imgCoord.getRight() + (direction == SwipeDirection.DOWN ? +1 : -1);
                newCoord = processNewCoord(newCoord, totalRows);
                newImageParts.put(Pair.of(imgCoord.getLeft(), newCoord), p.getValue());
            } else {
                newImageParts.put(Pair.of(imgCoord.getLeft(), imgCoord.getRight()), p.getValue());
            }
        }
        imageParts = newImageParts;
    }

    private int processNewCoord(int newCoord, int dimension) {
        if (newCoord > dimension - 1) {
            newCoord = 0;
        }
        if (newCoord < 0) {
            newCoord = dimension - 1;
        }
        return newCoord;
    }

    private void moveImg(SwipeDirection direction, float amount, float duration, Image img, Runnable afterMoveBy) {
        img.addAction(Actions.sequence(Actions.moveBy(leftRightSwipe(direction) ? amount : 0,
                upDownSwipe(direction) ? amount : 0, duration), Utils.createRunnableAction(afterMoveBy)));
    }

    private boolean leftRightSwipe(SwipeDirection direction) {
        return direction == SwipeDirection.RIGHT || direction == SwipeDirection.LEFT;
    }

    private boolean upDownSwipe(SwipeDirection direction) {
        return direction == SwipeDirection.UP || direction == SwipeDirection.DOWN;
    }

    private List<Image> getImages(Integer col, Integer row) {
        List<Image> res = new ArrayList<>();
        for (Pair<Integer, Integer> key : new TreeSet<Pair<Integer, Integer>>(imageParts.keySet())) {
            if (col != null && key.getLeft().equals(col)) {
                res.add(imageParts.get(key));
            }
            if (row != null && key.getRight().equals(row)) {
                res.add(imageParts.get(key));
            }
        }
        return res;
    }

    @Override
    public void show() {
    }

    private Pair<Integer, Integer> getCoordForXY(float x, float y) {
        y = y - (ScreenDimensionsManager.getExternalDeviceHeight() - totalImgHeight - getPartPad() * totalRows) / 2;
        x = x - (ScreenDimensionsManager.getExternalDeviceWidth() - totalImgWidth - getPartPad() * totalCols) / 2;
        int coordCol = (int) Math.ceil(x / (getPartWidth() + getPartPad())) - 1;
        int coordRow = (int) Math.ceil(y / (getPartHeight() + getPartPad())) - 1;
        System.out.println("col " + coordCol);
        System.out.println("xxx " + x);
        System.out.println("getPartWidth " + (getPartWidth() + getPartPad()));
        return new MutablePair<>(coordCol, coordRow);
    }

    private float getPartHeight() {
        return totalImgHeight / totalRows;
    }

    private float getPartWidth() {
        return totalImgWidth / totalCols;
    }

}
