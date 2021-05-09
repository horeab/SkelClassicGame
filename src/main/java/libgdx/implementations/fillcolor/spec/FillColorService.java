package libgdx.implementations.fillcolor.spec;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import libgdx.controls.animations.ActorAnimation;
import libgdx.game.Game;
import libgdx.implementations.fillcolor.screens.FillColorGameScreen;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.RGBColor;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class FillColorService {

    private Pixmap processedPixmap;
    private Stack stack;
    private static float IMG_HEIGHT = ScreenDimensionsManager.getScreenHeightValue(55);
    private Map<Pair<Integer, Integer>, RGBColor> correctColors;
    private Map<RGBColor, Integer> correctColorsPressed = new HashMap<>();
    private int wrongColorsPressed;
    private Res imgRes;

    public FillColorService(Res imgRes, Map<Pair<Integer, Integer>, RGBColor> correctColors) {
        this.correctColors = correctColors;
        this.processedPixmap = getPixMapFromRes(imgRes);
        this.imgRes = imgRes;
    }

    public static int convertPixmapY(int y) {
        return Math.round(IMG_HEIGHT) - y;
    }

    public int getNrOfCorrectColorsPressed() {
        int val = 0;
        for (Map.Entry<RGBColor, Integer> v : correctColorsPressed.entrySet()) {
            val = val + v.getValue();
        }
        return val;
    }

    public int getWrongColorsPressed() {
        return wrongColorsPressed;
    }

    public Stack fillWithCorrectColors() {
        Stack stack = null;
        for (Map.Entry<Pair<Integer, Integer>, RGBColor> e : correctColors.entrySet()) {
            stack = fillWithColor(e.getValue(), e.getKey().getLeft(), convertPixmapY(e.getKey().getRight()));
        }
        return stack;
    }

    public Stack fillWithColor(RGBColor colorToFill, int x, int y) {
        System.out.println("correctColors.put(Pair.of(" + x + ", FillColorService.convertPixmapY(" + y + ")), RGBColor.GREEN);");
        int pixmapY = convertPixmapY(y);
        floodFill(processedPixmap, Pair.of(x, pixmapY), processedPixmap.getPixel(x, pixmapY), colorToFill);

        return getStackFromImage(getImageFromPixmap(processedPixmap));
    }

    public static Pixmap getPixMapFromRes(Res res) {
        Pixmap pixmapOrignal = new Pixmap(Gdx.files.internal(Game.getInstance().getMainDependencyManager().createResourceService().getOverridableRes(res).getPath()));
        int newWidth = Math.round(ScreenDimensionsManager.getNewWidthForNewHeight(IMG_HEIGHT, pixmapOrignal.getWidth(), pixmapOrignal.getHeight()));
        Pixmap pixmapResized = new Pixmap(newWidth, Math.round(IMG_HEIGHT), pixmapOrignal.getFormat());
        pixmapResized.setBlending(Pixmap.Blending.None);
        pixmapResized.setFilter(Pixmap.Filter.NearestNeighbour);
        pixmapResized.drawPixmap(pixmapOrignal,
                0, 0, pixmapOrignal.getWidth(), pixmapOrignal.getHeight(),
                0, 0, pixmapResized.getWidth(), pixmapResized.getHeight()
        );
        pixmapOrignal.dispose();
        return pixmapResized;
    }

    public Stack getStackFromImage(Image image) {
        if (stack == null) {
            stack = new Stack();
            stack.setName(FillColorGameScreen.IMAGE_STACK_NAME);
        } else {
            stack.clear();
        }
        stack.add(image);
        image.setName(FillColorGameScreen.MAIN_IMG_NAME);
        stack.setWidth(image.getWidth());
        stack.setHeight(image.getHeight());
        return stack;
    }

    public static Image getImageFromPixmap(Pixmap pixmap) {
        Texture texture = new Texture(pixmap);
        TextureRegion region = new TextureRegion(texture, pixmap.getWidth(), pixmap.getHeight());
        return new Image(region);
    }

    private SectionFillStatus isColorCorrect(Pixmap image, Pair<Integer, Integer> node, int targetColor,
                                             int replacementColor) {
        List<Integer> colorsToIgnore = Arrays.asList(Color.rgba8888(RGBColor.BLACK.toColor()));
        int width = image.getWidth();
        int height = image.getHeight();
        if (targetColor != replacementColor && !colorsToIgnore.contains(targetColor)) {
            Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
            do {
                int x = node.getLeft();
                int y = node.getRight();
                while (x > 0 && image.getPixel(x - 1, y) == targetColor) {
                    x--;
                }
                boolean spanUp = false;
                boolean spanDown = false;
                while (x < width && image.getPixel(x, y) == targetColor) {
                    Pair<Integer, Integer> processedNode = Pair.of(x, y);
                    if (correctColors.containsKey(processedNode)) {
                        if (Color.rgba8888(correctColors.get(processedNode).toColor()) == replacementColor) {
                            return SectionFillStatus.CORRECT;
                        }
                    }
                    image.drawPixel(x, y, replacementColor);
                    if (!spanUp && y > 0 && image.getPixel(x, y - 1) == targetColor) {
                        queue.add(new MutablePair<>(x, y - 1));
                        spanUp = true;
                    } else if (spanUp && y > 0
                            && image.getPixel(x, y - 1) != targetColor) {
                        spanUp = false;
                    }
                    if (!spanDown && y < height - 1
                            && image.getPixel(x, y + 1) == targetColor) {
                        queue.add(new MutablePair(x, y + 1));
                        spanDown = true;
                    } else if (spanDown && y < height - 1
                            && image.getPixel(x, y + 1) != targetColor) {
                        spanDown = false;
                    }
                    x++;
                }
            } while ((node = queue.poll()) != null);
        }
        if (targetColor != 0) {
            return SectionFillStatus.ALREADY_FILLED;
        }
        return SectionFillStatus.WRONG;
    }

    private void floodFill(Pixmap image, Pair<Integer, Integer> node, int targetColor,
                           RGBColor replacementColorRgb) {
        List<Integer> colorsToIgnore = Arrays.asList(Color.rgba8888(RGBColor.BLACK.toColor()));
        int width = image.getWidth();
        int height = image.getHeight();
        int replacementColor = Color.rgba8888(replacementColorRgb.toColor());
        SectionFillStatus isColorCorrect = isColorCorrect(getPixMapFromRes(imgRes), node, targetColor, replacementColor);
        if (isColorCorrect == SectionFillStatus.CORRECT) {
            int beforeVal = 0;
            if (correctColorsPressed.containsKey(replacementColorRgb)) {
                beforeVal = correctColorsPressed.get(replacementColorRgb);
            }
            correctColorsPressed.put(replacementColorRgb, beforeVal + 1);
        } else if (isColorCorrect == SectionFillStatus.WRONG) {
            wrongColorsPressed++;
        }
        if (isColorCorrect == SectionFillStatus.CORRECT && targetColor != replacementColor && !colorsToIgnore.contains(targetColor)) {
            Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
            do {
                int x = node.getLeft();
                int y = node.getRight();
                while (x > 0 && image.getPixel(x - 1, y) == targetColor) {
                    x--;
                }
                boolean spanUp = false;
                boolean spanDown = false;
                while (x < width && image.getPixel(x, y) == targetColor) {
                    image.drawPixel(x, y, replacementColor);
                    if (!spanUp && y > 0 && image.getPixel(x, y - 1) == targetColor) {
                        queue.add(new MutablePair<>(x, y - 1));
                        spanUp = true;
                    } else if (spanUp && y > 0
                            && image.getPixel(x, y - 1) != targetColor) {
                        spanUp = false;
                    }
                    if (!spanDown && y < height - 1
                            && image.getPixel(x, y + 1) == targetColor) {
                        queue.add(new MutablePair(x, y + 1));
                        spanDown = true;
                    } else if (spanDown && y < height - 1
                            && image.getPixel(x, y + 1) != targetColor) {
                        spanDown = false;
                    }
                    x++;
                }
            } while ((node = queue.poll()) != null);
        } else if (isColorCorrect == SectionFillStatus.WRONG) {
            new ActorAnimation(Game.getInstance().getAbstractScreen()).animatePulse();
        }
    }

    private enum SectionFillStatus {
        CORRECT,
        WRONG,
        ALREADY_FILLED
    }
}
