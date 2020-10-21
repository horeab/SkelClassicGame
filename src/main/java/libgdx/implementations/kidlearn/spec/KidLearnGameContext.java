package libgdx.implementations.kidlearn.spec;

import java.util.ArrayList;
import java.util.List;

public class KidLearnGameContext<T extends Enum & KidLearnLevel> {

    public KidLearnGameContext(T level) {
        this.level = level;
    }

    public List<Object> playedValues = new ArrayList<>();
    public int score;
    public T level;
}
