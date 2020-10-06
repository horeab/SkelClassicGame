package libgdx.implementations.imagesplit.spec;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import libgdx.game.Game;
import libgdx.resources.Res;

public class ImageSplitService {

    public Image crop(Res res, int totalCols, int totalRows, float totalImgWidth, float totalImgHeight, int col, int row) {
        Texture texture = getTexture(res, totalImgWidth, totalImgHeight);

        int partWidth = texture.getWidth() / totalCols;
        int partHeight = texture.getHeight() / totalRows;

        TextureRegion region = new TextureRegion(texture, partWidth * col, partHeight * row, partWidth, partHeight);
        return new Image(region);
    }

    public static Texture getTexture(Res image, float width, float height) {
        Pixmap pixmapOrignal = new Pixmap(Gdx.files.internal(Game.getInstance().getMainDependencyManager().createResourceService().getOverridableRes(image).getPath()));
        pixmapOrignal.setBlending(Pixmap.Blending.None);
        Pixmap pixmapResized = new Pixmap(Math.round(width), Math.round(height), pixmapOrignal.getFormat());
        pixmapResized.setBlending(Pixmap.Blending.None);
        pixmapResized.drawPixmap(pixmapOrignal,
                0, 0, pixmapOrignal.getWidth(), pixmapOrignal.getHeight(),
                0, 0, pixmapResized.getWidth(), pixmapResized.getHeight()
        );
        Texture texture = new Texture(pixmapResized);
        pixmapOrignal.dispose();
        pixmapResized.dispose();
        return texture;
    }
}
