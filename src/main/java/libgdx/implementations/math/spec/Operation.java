package libgdx.implementations.math.spec;

public enum Operation {

    SUM("+"),
    SUB("-"),
    MUL("*"),
    DIV("/");

    String expr;

    Operation(String expr) {
        this.expr = expr;
    }

    public String getExpr() {
        return expr;
    }
}
