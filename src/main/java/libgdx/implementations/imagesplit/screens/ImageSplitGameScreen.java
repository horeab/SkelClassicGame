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

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.imagesplit.ImageSplitScreenManager;
import libgdx.implementations.imagesplit.ImageSplitSpecificResource;
import libgdx.implementations.imagesplit.spec.ImageSplitService;
import libgdx.implementations.imagesplit.spec.SimpleDirectionGestureDetector;
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
    private int totalCols = 2;
    private int totalRows = 3;

    private Map<Pair<Integer, Integer>, Image> imageParts = new LinkedHashMap<>();

    public ImageSplitGameScreen() {
        init();
    }

    private void init() {
        imgRes = ImageSplitSpecificResource.i0;
        addDirectionGestureListener();
        totalImgWidth = ScreenDimensionsManager.getScreenWidth();
        totalImgHeight = ScreenDimensionsManager.getNewHeightForNewWidth(totalImgWidth, GraphicUtils.getImage(imgRes));
        totalCols = 2;
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
        allTable.setWidth(ScreenDimensionsManager.getScreenWidth());
        allTable.setHeight(ScreenDimensionsManager.getScreenHeight());
        addActor(allTable);
        if (hoverBackButton != null) {
            hoverBackButton.toFront();
        }
        allTable.add(createImageContainer()).grow();
    }

    private Stack createImageContainer() {
        Stack stack = new Stack();
        float extHeight = ScreenDimensionsManager.getExternalDeviceHeight();
        float extWidth = ScreenDimensionsManager.getExternalDeviceWidth();
        stack.add(createImageTable());
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
        marginTable.add(middleTable).height(totalImgHeight);
        marginTable.row();
        marginTable.add(bottomMarginTable).height(extHeight / 2).grow();
        stack.add(marginTable);
        return stack;
    }

    private Table createMarginTable() {
        Table topMarginTable = new Table();
        topMarginTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        return topMarginTable;
    }

    private float getLeftRightMargin() {
        return ScreenDimensionsManager.getScreenWidthValue(2);
    }

    private float getUpDownMargin() {
        return ScreenDimensionsManager.getScreenHeightValue(2);
    }


    private Table createImageTable() {
        Table imgTable = new Table();
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalCols; col++) {
                Image image = imageSplitService.crop(imgRes, totalCols, totalRows, totalImgWidth, totalImgHeight, col, row);
                Pair<Integer, Integer> coord = new MutablePair<>(col, row);
                imageParts.put(coord, image);
                imgTable.add(image).pad(MainDimen.horizontal_general_margin.getDimen() / 10).width(image.getWidth()).height(image.getHeight());
            }
            imgTable.row();
        }
        return imgTable;
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
                moveImages(getImages(coord.getLeft(), null), 0);
            }

            @Override
            public void onDown() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                moveImages(getImages(coord.getLeft(), null), 1);
            }

            @Override
            public void onRight() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                moveImages(getImages(null, coord.getRight()), 2);
            }

            @Override
            public void onLeft() {
                Pair<Integer, Integer> coord = getCoordForXY(pressedX, pressedY);
                moveImages(getImages(null, coord.getRight()), 3);
            }

            @Override
            public void onTouchDown(float x, float y) {
                pressedX = x;
                pressedY = y;

            }
        }));
    }

    private void moveImages(List<Image> images, int direction) {
        float amount = direction == 0 || direction == 1 ? getPartHeight() : getPartWidth();
        amount = direction == 1 || direction == 3 ? -amount : amount;
        float duration = 0.15f;
        for (Image img : images) {
            moveImg(direction, amount, duration, img, new Runnable() {
                @Override
                public void run() {
                }
            });
        }
        Image firstImg = images.get(0);
        Image lastImg = images.get(images.size() - 1);
        Image copyImg = new Image(lastImg.getDrawable());
        copyImg.setX(firstImg.getImageX() - getPartWidth());
        copyImg.setY(firstImg.getY() - totalImgHeight / 2);
        addActor(copyImg);
        System.out.println("" + copyImg.getX());
        moveImg(direction, amount, duration, copyImg, new Runnable() {
            @Override
            public void run() {
                System.out.println("" + copyImg.getX());
                lastImg.setX(copyImg.getImageX());
                lastImg.setY(copyImg.getY());
                copyImg.remove();
            }
        });
    }

    private void moveImg(int direction, float amount, float duration, Image img, Runnable afterMoveBy) {
        img.addAction(Actions.sequence(Actions.moveBy(direction == 2 || direction == 3 ? amount : 0,
                direction == 0 || direction == 1 ? amount : 0, duration), Utils.createRunnableAction(afterMoveBy)));
    }

    private List<Image> getImages(Integer col, Integer row) {
        List<Image> res = new ArrayList<>();
        for (Map.Entry<Pair<Integer, Integer>, Image> p : imageParts.entrySet()) {
            if (col != null && p.getKey().getLeft().equals(col)) {
                res.add(p.getValue());
            }
            if (row != null && p.getKey().getRight().equals(row)) {
                res.add(p.getValue());
            }
        }
        return res;
    }

    @Override
    public void show() {
    }

    private Pair<Integer, Integer> getCoordForXY(float x, float y) {
        int coordCol = Math.round(x / getPartWidth());
        int coordRow = Math.round(y / getPartHeight());
        return new MutablePair<>(coordCol - 1, coordRow - 1);
    }

    private float getPartHeight() {
        return totalImgHeight / totalRows;
    }

    private float getPartWidth() {
        return totalImgWidth / totalCols;
    }

}
