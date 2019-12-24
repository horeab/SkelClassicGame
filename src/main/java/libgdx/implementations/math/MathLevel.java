package libgdx.implementations.math;

import java.util.ArrayList;
import java.util.List;

public enum MathLevel {


    _1_COMB(null, null, null, null, 10, null),

    _0(10, null, null, null, null, null),

    _1(10, _1_COMB, null, null, null, null),
    ;

    private Integer sumMaxVal;
    private MathLevel sumCombine;

    private Integer subMaxVal;
    private MathLevel subCombine;

    private Integer mulMaxVal;

    private Integer divMaxVal;

    MathLevel(Integer sumMaxVal, MathLevel sumCombine, Integer subMaxVal, MathLevel subCombine, Integer mulMaxVal, Integer divMaxVal) {
        this.sumMaxVal = sumMaxVal;
        this.sumCombine = sumCombine;
        this.subMaxVal = subMaxVal;
        this.subCombine = subCombine;
        this.mulMaxVal = mulMaxVal;
        this.divMaxVal = divMaxVal;
    }

    public List<Operation> getAvailableOperations(MathLevel mathLevel) {
        List<Operation> res = new ArrayList<>();
        if (sumMaxVal != null) {
            res.add(Operation.SUM);
        }
        if (subMaxVal != null) {
            res.add(Operation.SUB);
        }
        if (mulMaxVal != null) {
            res.add(Operation.MUL);
        }
        if (divMaxVal != null) {
            res.add(Operation.DIV);
        }
        return res;
    }

    public List<Operation> getAvailableOperations() {
        return getAvailableOperations(this);
    }

    public Integer getSumMaxVal() {
        return sumMaxVal;
    }

    public void setSumMaxVal(Integer sumMaxVal) {
        this.sumMaxVal = sumMaxVal;
    }

    public MathLevel getSumCombine() {
        return sumCombine;
    }

    public void setSumCombine(MathLevel sumCombine) {
        this.sumCombine = sumCombine;
    }

    public Integer getSubMaxVal() {
        return subMaxVal;
    }

    public void setSubMaxVal(Integer subMaxVal) {
        this.subMaxVal = subMaxVal;
    }

    public MathLevel getSubCombine() {
        return subCombine;
    }

    public void setSubCombine(MathLevel subCombine) {
        this.subCombine = subCombine;
    }

    public Integer getMulMaxVal() {
        return mulMaxVal;
    }

    public void setMulMaxVal(Integer mulMaxVal) {
        this.mulMaxVal = mulMaxVal;
    }

    public Integer getDivMaxVal() {
        return divMaxVal;
    }

    public void setDivMaxVal(Integer divMaxVal) {
        this.divMaxVal = divMaxVal;
    }
}
