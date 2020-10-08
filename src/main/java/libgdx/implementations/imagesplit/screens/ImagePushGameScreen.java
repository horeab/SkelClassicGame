package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import libgdx.implementations.imagesplit.spec.SwipeDirection;

public class ImagePushGameScreen extends ImageSplitGameScreen {


    public ImagePushGameScreen() {
        super();
    }

    @Override
    List<Image> getImagesToProcessSwipeUp(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord, getDirectionNeighb(coord, direction), direction);
    }

    @Override
    List<Image> getImagesToProcessSwipeDown(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord, getDirectionNeighb(coord, direction), direction);
    }

    @Override
    List<Image> getImagesToProcessSwipeLeft(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord, getDirectionNeighb(coord, direction), direction);
    }

    @Override
    List<Image> getImagesToProcessSwipeRight(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord, getDirectionNeighb(coord, direction), direction);
    }

    private Pair<Integer, Integer> getDirectionNeighb(Pair<Integer, Integer> coord, SwipeDirection direction) {
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

    private List<Image> getImagesToProcessSwipe(Pair<Integer, Integer> pressedCoord, Pair<Integer, Integer> neighbCoord, SwipeDirection direction) {
        List<Image> res = new ArrayList<>();
        for (Pair<Integer, Integer> key : new TreeSet<Pair<Integer, Integer>>(imageParts.keySet())) {
            if (pressedCoord.equals(key) && imageParts.containsKey(neighbCoord) && !imageParts.get(neighbCoord).isVisible()) {
                res.add(imageParts.get(key));
                break;
            }
        }
        return res;
    }

    @Override
    void processAfterMoveImg(Pair<Integer, Integer> pressedCoord, SwipeDirection direction) {
        Pair<Integer, Integer> neighb = getDirectionNeighb(pressedCoord, direction);
        Image neighbImg = imageParts.get(neighb);
        Image pressedImg = imageParts.get(pressedCoord);
        imageParts.put(pressedCoord, neighbImg);
        imageParts.put(neighb, pressedImg);
    }

    @Override
    Table createImageTable() {
        int i = 0;
        int randomEmptyCell = new Random().nextInt(totalCols * totalRows);
        for (Map.Entry<Pair<Integer, Integer>, Image> part : imageParts.entrySet()) {
            if (i != 0 && i % totalCols == 0) {
                imgTable.row();
            }
            Image image = part.getValue();
            imgTable.add(image).pad(getPartPad()).width(image.getWidth()).height(image.getHeight());
            if (i == randomEmptyCell) {
                image.setVisible(false);
            }
            i++;
        }
        return imgTable;
    }

    @Override
    void processClonedImages(List<Image> images, SwipeDirection direction, Pair<Integer, Integer> coord, float amount, float duration) {
    }
}
