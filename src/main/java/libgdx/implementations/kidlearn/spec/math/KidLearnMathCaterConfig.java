package libgdx.implementations.kidlearn.spec.math;

import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnLevelConfig;

public class KidLearnMathCaterConfig<T extends Enum & KidLearnMathCaterLevel> extends KidLearnLevelConfig {
    public KidLearnMathCaterConfig(Class<? extends KidLearnMathCaterLevel> gameType, List<Float> allCorrectNumbers, List<Float> wrongNumbers, int nrOfCorrectUnknownNumbers, boolean asc) {
        this.gameType = gameType;
        this.allCorrectNumbers = allCorrectNumbers;
        this.wrongNumbers = wrongNumbers;
        this.nrOfCorrectUnknownNumbers = nrOfCorrectUnknownNumbers;
        this.asc = asc;
    }

    public Class<? extends KidLearnMathCaterLevel> gameType;
    public List<Float> allCorrectNumbers;
    public List<Float> wrongNumbers;
    public int nrOfCorrectUnknownNumbers;
    public boolean asc;

}
