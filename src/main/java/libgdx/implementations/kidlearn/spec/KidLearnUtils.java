package libgdx.implementations.kidlearn.spec;

import java.util.List;
import java.util.Random;

public class KidLearnUtils {

    public static <T> T getLevelValsToPlay(KidLearnGameContext gameContext, List<T> vals) {
        T rand = vals.get(new Random().nextInt(vals.size()));
        while (gameContext.playedValues.contains(rand)) {
            rand = vals.get(new Random().nextInt(vals.size()));
        }
        gameContext.playedValues.add(rand);
        return rand;
    }
}
