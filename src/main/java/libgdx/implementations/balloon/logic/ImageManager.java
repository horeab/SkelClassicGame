package libgdx.implementations.balloon.logic;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.balloon.BalloonSpecificResource;
import libgdx.implementations.balloon.model.MatrixValue;
import libgdx.resources.FontManager;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class ImageManager {


    public Table getImage(MatrixValue matrixValue) {
        return createImgView(matrixValue);
    }

    public Table getFinalPositionImageWithPoints(int points, MatrixValue finalPlayerValue, int nrOfCols) {
        Table imageView = createImgView(finalPlayerValue);
        float fontScale = FontManager.calculateMultiplierStandardFontSize(nrOfCols/4);
        String text = points + "";
        MyWrappedLabel textView = new MyWrappedLabel(text);
        textView.setFontScale(fontScale);
        FontColor textColor = FontColor.WHITE;
        if (finalPlayerValue == MatrixValue.FINAL_PLAYER_2) {
            textColor = FontColor.BLACK;
        }
        textView.setTextColor(textColor);
        textView.setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.BLACK.getColor(), FontConfig.FONT_SIZE, 3f));
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
        imageTable.add(img).grow();
        Table resTable = new Table();
        resTable.add(imageTable).grow();
        return resTable;
    }

}
