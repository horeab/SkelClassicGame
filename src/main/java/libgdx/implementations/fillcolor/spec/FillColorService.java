package libgdx.implementations.fillcolor.spec;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.Queue;

import libgdx.game.Game;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.RGBColor;

public class FillColorService {

    private Pixmap processedPixmap;
    private float imgHeight = ScreenDimensionsManager.getScreenHeightValue(55);

    public FillColorService() {

    }

    public Stack fillWithColor(Image imgToDisplay, Res imgToFill, RGBColor colorToFill, int x, int y) {
        if (processedPixmap == null) {
            processedPixmap = getPixMapFromRes(imgToFill);
        }
        int pixmapY = processedPixmap.getHeight() - y;
        floodFill(processedPixmap, Pair.of(x, pixmapY), processedPixmap.getPixel(x, pixmapY),
                Color.rgba8888(colorToFill.toColor()));
        Stack stackFromImage = getStackFromImage(getImageFromPixmap(processedPixmap));
        stackFromImage.add(imgToDisplay);
        return stackFromImage;
    }

    public Pixmap getPixMapFromRes(Res res) {
        Pixmap pixmapOrignal = new Pixmap(Gdx.files.internal(Game.getInstance().getMainDependencyManager().createResourceService().getOverridableRes(res).getPath()));
        int newWidth = Math.round(ScreenDimensionsManager.getNewWidthForNewHeight(imgHeight, pixmapOrignal.getWidth(), pixmapOrignal.getHeight()));
        Pixmap pixmapResized = new Pixmap(newWidth, Math.round(imgHeight), pixmapOrignal.getFormat());
        pixmapResized.setBlending(Pixmap.Blending.None);
        pixmapResized.drawPixmap(pixmapOrignal,
                0, 0, pixmapOrignal.getWidth(), pixmapOrignal.getHeight(),
                0, 0, pixmapResized.getWidth(), pixmapResized.getHeight()
        );
        pixmapOrignal.dispose();
        return pixmapResized;
    }

    public Stack getStackFromImage(Image image) {
        Stack stack = new Stack();
        stack.add(image);
        stack.setWidth(image.getWidth());
        stack.setHeight(image.getHeight());
        return stack;
    }

    public Image getImageFromPixmap(Pixmap pixmap) {
        Texture texture = new Texture(pixmap);
        TextureRegion region = new TextureRegion(texture, pixmap.getWidth(), pixmap.getHeight());
        return new Image(region);
    }

    public void floodFill(Pixmap image, Pair<Integer, Integer> node, int targetColor,
                          int replacementColor) {
        int width = image.getWidth();
        int height = image.getHeight();
        int target = targetColor;
        int replacement = replacementColor;
        if (target != replacement) {
            Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
            do {
                int x = node.getLeft();
                int y = node.getRight();
                while (x > 0 && image.getPixel(x - 1, y) == target) {
                    x--;
                }
                boolean spanUp = false;
                boolean spanDown = false;
                while (x < width && image.getPixel(x, y) == target) {
                    image.drawPixel(x, y, replacement);
                    if (!spanUp && y > 0 && image.getPixel(x, y - 1) == target) {
                        queue.add(new MutablePair<>(x, y - 1));
                        spanUp = true;
                    } else if (spanUp && y > 0
                            && image.getPixel(x, y - 1) != target) {
                        spanUp = false;
                    }
                    if (!spanDown && y < height - 1
                            && image.getPixel(x, y + 1) == target) {
                        queue.add(new MutablePair(x, y + 1));
                        spanDown = true;
                    } else if (spanDown && y < height - 1
                            && image.getPixel(x, y + 1) != target) {
                        spanDown = false;
                    }
                    x++;
                }
            } while ((node = queue.poll()) != null);
        }
    }
}
