package libgdx.implementations.imagesplit.spec;


import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ImageMoveConfig {

    private Image image;
    private SwipeDirection direction;

    public ImageMoveConfig(Image image, SwipeDirection direction) {
        this.image = image;
        this.direction = direction;
    }

    public Image getImage() {
        return image;
    }

    public SwipeDirection getDirection() {
        return direction;
    }
}
