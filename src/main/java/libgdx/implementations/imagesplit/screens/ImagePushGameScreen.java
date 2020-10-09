package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import libgdx.implementations.imagesplit.spec.ImageMoveConfig;
import libgdx.implementations.imagesplit.spec.SwipeDirection;

public class ImagePushGameScreen extends ImageSplitGameScreen {


    public ImagePushGameScreen() {
        super();
    }

    @Override
    List<ImageMoveConfig> getImagesToProcessSwipeUp(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord, getDirectionNeighb(coord, direction), direction);
    }

    @Override
    List<ImageMoveConfig> getImagesToProcessSwipeDown(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord, getDirectionNeighb(coord, direction), direction);
    }

    @Override
    List<ImageMoveConfig> getImagesToProcessSwipeLeft(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord, getDirectionNeighb(coord, direction), direction);
    }

    @Override
    List<ImageMoveConfig> getImagesToProcessSwipeRight(Pair<Integer, Integer> coord, SwipeDirection direction) {
        return getImagesToProcessSwipe(coord, getDirectionNeighb(coord, direction), direction);
    }

    private List<ImageMoveConfig> getImagesToProcessSwipe(Pair<Integer, Integer> pressedCoord, Pair<Integer, Integer> neighbCoord, SwipeDirection direction) {
        List<ImageMoveConfig> res = new ArrayList<>();
        for (Pair<Integer, Integer> key : new TreeSet<Pair<Integer, Integer>>(imageParts.keySet())) {
            if (pressedCoord.equals(key) && imageParts.containsKey(neighbCoord) && !imageParts.get(neighbCoord).isVisible()) {
                res.add(new ImageMoveConfig(imageParts.get(key), direction));
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
        if (correctImageParts.equals(imageParts)) {
            imgTable.addAction(Actions.fadeOut(1f));
        }
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
    void processClonedImages(List<ImageMoveConfig> imageMoveConfigs, Pair<Integer, Integer> coord, float duration, SwipeDirection swipeDirection) {
    }

}
