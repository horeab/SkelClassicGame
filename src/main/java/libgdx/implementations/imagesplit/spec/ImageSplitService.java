package libgdx.implementations.imagesplit.spec;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import libgdx.graphics.GraphicUtils;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;

public class ImageSplitService {

    public Image crop(Res res, int totalCols, int totalRows, int col, int row) {
        Texture texture = GraphicUtils.getTexture(res, ScreenDimensionsManager.getScreenWidth());

        int partWidth = texture.getWidth() / totalCols;
        int partHeight = texture.getHeight() / totalRows;

        TextureRegion region = new TextureRegion(texture, partWidth * col, partHeight * row, partWidth, partHeight);
        return new Image(region);
    }

}
