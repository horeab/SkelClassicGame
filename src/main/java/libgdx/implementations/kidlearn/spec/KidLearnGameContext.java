package libgdx.implementations.kidlearn.spec;

import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;

public class KidLearnGameContext<T extends Enum & KidLearnMathCaterLevel> {

    public int score;
    public T level;
}
