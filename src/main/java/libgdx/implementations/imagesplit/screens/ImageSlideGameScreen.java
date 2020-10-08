package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import libgdx.implementations.imagesplit.spec.SwipeDirection;

public class ImageSlideGameScreen extends ImageSplitGameScreen {


    public ImageSlideGameScreen() {
        super();
    }

    @Override
    void processClonedImages(List<Image> images, SwipeDirection direction, Pair<Integer, Integer> pressedCoord, float amount, float duration) {
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
            copyImg.toBack();
            moveImg(direction, pressedCoord, amount, duration, copyImg, new Runnable() {
                @Override
                public void run() {
                    finishImg.setX(copyImg.getX());
                    finishImg.setY(copyImg.getY());
                    copyImg.setVisible(false);
                    changeImgCoords(pressedCoord, direction);
                    if (correctImageParts.equals(imageParts)) {
                        imgTable.addAction(Actions.fadeOut(1f));
                    }
                }
            });
        }
    }

    @Override
    List<Image> getImagesToProcessSwipeUp(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord.getLeft(), null, direction);
    }

    @Override
    List<Image> getImagesToProcessSwipeDown(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord.getLeft(), null, direction);
    }

    @Override
    List<Image> getImagesToProcessSwipeLeft(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(null, coord.getRight(), direction);
    }

    @Override
    List<Image> getImagesToProcessSwipeRight(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(null, coord.getRight(), direction);
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

    private List<Image> getImagesToProcessSwipe(Integer col, Integer row, SwipeDirection direction) {
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
    Table createImageTable() {
        int r = 0;
        for (Map.Entry<Pair<Integer, Integer>, Image> part : imageParts.entrySet()) {
            if (r != 0 && r % totalCols == 0) {
                imgTable.row();
            }
            Image image = part.getValue();
            imgTable.add(image).pad(getPartPad()).width(image.getWidth()).height(image.getHeight());
            r++;
        }
        return imgTable;
    }
}
