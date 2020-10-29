package libgdx.implementations.kidlearn.spec;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class KidLearnGameContext<T extends Enum & KidLearnLevel> {

    public KidLearnGameContext(T level) {
        this.level = level;
    }

    public LinkedHashMap<Object, Object> playedValues = new LinkedHashMap<>();
    public int score;
    public T level;
}
