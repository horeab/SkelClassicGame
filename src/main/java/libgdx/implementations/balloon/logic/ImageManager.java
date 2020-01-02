package libgdx.implementations.balloon.logic;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.balloon.BalloonSpecificResource;
import libgdx.implementations.balloon.model.MatrixValue;
import libgdx.resources.Res;
import libgdx.utils.model.FontColor;

public class ImageManager {


    public Table getImage(MatrixValue matrixValue) {
        return createImgView(matrixValue);
    }

    public Table getFinalPositionImageWithPoints(int points, int imageViewId, MatrixValue finalPlayerValue) {
        Table container = new Table();
        Table imageView = createImgView(finalPlayerValue);
        MyWrappedLabel textView = new MyWrappedLabel(points + "");
        FontColor textColor = FontColor.WHITE;
        if (finalPlayerValue == MatrixValue.FINAL_PLAYER_2) {
            textColor = FontColor.BLACK;
        }
        textView.setTextColor(textColor);
        container.add(imageView);
        container.add(textView);

        return container;
    }

    private Table createImgView(MatrixValue matrixValue) {
        Table image = new Table();
        Res resID = BalloonSpecificResource.valueOf(matrixValue.getImageName());
        image.add(GraphicUtils.getImage(resID));
        return image;
    }

}
