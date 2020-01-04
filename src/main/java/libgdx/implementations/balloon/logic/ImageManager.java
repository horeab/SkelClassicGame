package libgdx.implementations.balloon.logic;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.balloon.BalloonSpecificResource;
import libgdx.implementations.balloon.model.MatrixValue;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

public class ImageManager {


    public static final float IMG_SIDE_DIMEN = ScreenDimensionsManager.getScreenWidthValue(4);

    private int nrOfRows;
    private int nrOfCols;

    public ImageManager(int nrOfRows, int nrOfCols) {
        this.nrOfRows = nrOfRows;
        this.nrOfCols = nrOfCols;
    }

    public Table getImage(MatrixValue matrixValue) {
        return createImgView(matrixValue);
    }

    public Table getFinalPositionImageWithPoints(int points,  MatrixValue finalPlayerValue) {
        Table imageView = createImgView(finalPlayerValue);
        MyWrappedLabel textView = new MyWrappedLabel(points + "");
        FontColor textColor = FontColor.WHITE;
        if (finalPlayerValue == MatrixValue.FINAL_PLAYER_2) {
            textColor = FontColor.BLACK;
        }
        textView.setTextColor(textColor);
        Stack stack = new Stack();
        stack.add(imageView);
        stack.add(textView);

        Table resTable = new Table();
        resTable.add(stack).grow();
        return resTable;
    }

    private Table createImgView(MatrixValue matrixValue) {
        Table imageTable = new Table();
        Res resID = BalloonSpecificResource.valueOf(matrixValue.getImageName());
        Image img = GraphicUtils.getImage(resID);
        imageTable.add(img);
        Table resTable = new Table();
        resTable.add(imageTable).grow();
        return resTable;
    }

}
