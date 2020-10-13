package libgdx.implementations.kidlearn.spec.cater.ord;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KidLearnMathCaterService {

    public List<Float> generateCorrectNumbers(KidLearnMathCaterOrdLevel level) {
        List<Float> res = new ArrayList<>();
        while (res.size() < level.totalNrOfNumbers) {
            float val = getRandomVal(level);
            while (res.contains(val)) {
                val = getRandomVal(level);
            }
            res.add(val);
        }
        Collections.sort(res);
        if (!level.asc) {
            Collections.reverse(res);
        }
        return res;
    }

    private float getRandomVal(KidLearnMathCaterOrdLevel level) {
        float val = new Random().nextInt((level.max + 1) - level.min) + level.min;
        if (level.interval % 1 != 0) {
            float f = new Random().nextFloat();
            val = val + f < level.max ? val + f : val;
        } else {
            val = Math.round(val / level.interval) * Math.round(level.interval);
        }
        return val;
    }

    public List<Integer> generateWrongNumbers(KidLearnMathCaterOrdLevel level) {
        List<Integer> res = new ArrayList<>();
        while (res.size() < level.totalNrOfNumbers) {
            res.add(new Random().nextInt(level.max - level.min) + level.min);
        }
        return res;
    }
}
