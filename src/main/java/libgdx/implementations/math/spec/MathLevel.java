package libgdx.implementations.math.spec;

import java.util.ArrayList;
import java.util.List;

public enum MathLevel {


    COMB_1(20, null, 20, null, null, null),
    COMB_2(20, null, 20, null, 10, 10),

    _0(10, null, null, null, null, null),
    _1(20, null, null, null, null, null),
    _2(20, null, 20, null, null, null),
    _3(40, null, 40, null, 10, null),
    _4(40, null, 40, null, 10, 10),
    _5(20, COMB_1, 20, COMB_1, null, null),
    _6(20, COMB_2, 20, COMB_2, null, null),
    _7(40, COMB_2, 40, COMB_2, 15, 15),
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

    public static List<MathLevel> getPlayableLevels() {
        List<MathLevel> res = new ArrayList<>();
        for (MathLevel mathLevel : values()) {
            if (mathLevel.name().startsWith("_")) {
                res.add(mathLevel);
            }
        }
        return res;
    }

    public Integer getMaxValForOperation(Operation operation) {
        if (operation == Operation.SUM) {
            return sumMaxVal;
        }
        if (operation == Operation.SUB) {
            return subMaxVal;
        }
        if (operation == Operation.MUL) {
            return mulMaxVal;
        }
        if (operation == Operation.DIV) {
            return divMaxVal;
        }
        return null;
    }

    public List<Operation> getAvailableOperations(MathLevel mathLevel) {
        List<Operation> res = new ArrayList<>();
        if (mathLevel.sumMaxVal != null) {
            res.add(Operation.SUM);
        }
        if (mathLevel.subMaxVal != null) {
            res.add(Operation.SUB);
        }
        if (mathLevel.mulMaxVal != null) {
            res.add(Operation.MUL);
        }
        if (mathLevel.divMaxVal != null) {
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
