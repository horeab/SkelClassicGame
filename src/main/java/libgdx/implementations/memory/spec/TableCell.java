package libgdx.implementations.memory.spec;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TableCell {

    private Table cell;
    private MatrixElement matrixElement;

    public TableCell(Table cell, MatrixElement matrixElement) {
        this.cell = cell;
        this.matrixElement = matrixElement;
    }

    public Table getCell() {
        return cell;
    }

    public void setCell(Table cell) {
        this.cell = cell;
    }

    public MatrixElement getMatrixElement() {
        return matrixElement;
    }

    public void setMatrixElement(MatrixElement matrixElement) {
        this.matrixElement = matrixElement;
    }
}
