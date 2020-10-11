package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import libgdx.implementations.imagesplit.ImageSplitCampaignLevelEnum;
import libgdx.implementations.imagesplit.spec.ImageMoveConfig;
import libgdx.implementations.imagesplit.spec.ImageSplitGameType;
import libgdx.implementations.imagesplit.spec.SwipeDirection;

public class ImageSlideGameScreen extends ImageSplitGameScreen {

    public ImageSlideGameScreen(ImageSplitCampaignLevelEnum campaignLevelEnum) {
        super(campaignLevelEnum, ImageSplitGameType.SLIDE);
    }

    @Override
    void processClonedImages(List<ImageMoveConfig> imageMoveConfigs, Pair<Integer, Integer> pressedCoord, float duration, SwipeDirection swipeDirection) {
        if (imageMoveConfigs != null && !imageMoveConfigs.isEmpty()) {
            ImageMoveConfig startImg = swipeDirection == SwipeDirection.DOWN || swipeDirection == SwipeDirection.RIGHT ? imageMoveConfigs.get(0) : imageMoveConfigs.get(imageMoveConfigs.size() - 1);
            ImageMoveConfig finishImg = swipeDirection == SwipeDirection.UP || swipeDirection == SwipeDirection.LEFT ? imageMoveConfigs.get(0) : imageMoveConfigs.get(imageMoveConfigs.size() - 1);
            Image copyImg = new Image(finishImg.getImage().getDrawable());
            SwipeDirection copyImgDirection = finishImg.getDirection();
            float initCopyImgPos = upDownSwipe(swipeDirection) ? getPartHeight() : getPartWidth();
            initCopyImgPos = initCopyImgPos + getPartPad();
            initCopyImgPos = swipeDirection == SwipeDirection.UP || swipeDirection == SwipeDirection.RIGHT ? -initCopyImgPos : initCopyImgPos;
            copyImg.setX(startImg.getImage().getX() + (leftRightSwipe(swipeDirection) ? initCopyImgPos : 0));
            copyImg.setY(startImg.getImage().getY() + (upDownSwipe(swipeDirection) ? initCopyImgPos : 0));
            addActor(copyImg);
            copyImg.toBack();
            moveImgConfig(new ImageMoveConfig(copyImg, copyImgDirection), duration, new Runnable() {
                @Override
                public void run() {
                    finishImg.getImage().setX(copyImg.getX());
                    finishImg.getImage().setY(copyImg.getY());
                    copyImg.setVisible(false);
                    changeImgCoords(pressedCoord, copyImgDirection);
                    if (correctImageParts.equals(imageParts)) {
                        levelFinished();
                    }
                }
            });
        }
    }

    @Override
    void simulateMoveStep() {
        Pair<Integer, Integer> coord = Pair.of(0, 0);
        simulateMoveFinger(Arrays.asList(coord, coord), Arrays.asList(SwipeDirection.DOWN, SwipeDirection.RIGHT), 0);
    }

    @Override
    List<ImageMoveConfig> getImagesToProcessSwipeUp(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord.getLeft(), null, direction);
    }

    @Override
    List<ImageMoveConfig> getImagesToProcessSwipeDown(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord.getLeft(), null, direction);
    }

    @Override
    List<ImageMoveConfig> getImagesToProcessSwipeLeft(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(null, coord.getRight(), direction);
    }

    @Override
    List<ImageMoveConfig> getImagesToProcessSwipeRight(Pair<Integer, Integer> coord, SwipeDirection direction) {
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

    private List<ImageMoveConfig> getImagesToProcessSwipe(Integer col, Integer row, SwipeDirection direction) {
        List<ImageMoveConfig> res = new ArrayList<>();
        for (Pair<Integer, Integer> key : new TreeSet<Pair<Integer, Integer>>(imageParts.keySet())) {
            if (col != null && key.getLeft().equals(col)) {
                res.add(new ImageMoveConfig(imageParts.get(key), direction));
            }
            if (row != null && key.getRight().equals(row)) {
                res.add(new ImageMoveConfig(imageParts.get(key), direction));
            }
        }
        return res;
    }

}
