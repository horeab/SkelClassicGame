package libgdx.implementations.balloon.model;

import org.apache.commons.lang3.tuple.MutablePair;

public class CoordinateInfo {

    private MutablePair<Integer, Integer> point;
    private MatrixValue matrixValue;

    public MutablePair<Integer, Integer> getPoint() {
        return point;
    }

    public void setPoint(MutablePair point) {
        this.point = point;
    }

    public MatrixValue getMatrixValue() {
        return matrixValue;
    }

    public void setMatrixValue(MatrixValue matrixValue) {
        this.matrixValue = matrixValue;
    }
}
