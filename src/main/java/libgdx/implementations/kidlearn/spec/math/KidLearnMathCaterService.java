package libgdx.implementations.kidlearn.spec.math;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KidLearnMathCaterService {

    private static final int SEQ_LEVEL_TOTAL_NR_OF_NUMBERS = 6;

    public <T extends Enum & KidLearnMathCaterLevel> List<Float> generateCorrectNumbers(T level) {
        List<Float> res = new ArrayList<>();
        if (level instanceof KidLearnMathCaterOrdLevel) {
            res.addAll(generateOrdCorrectNumbers((KidLearnMathCaterOrdLevel) level));
        } else if (level instanceof KidLearnMathCaterSeqLevel) {
            res.addAll(generateSeqCorrectNumbers((KidLearnMathCaterSeqLevel) level));
        }
        return res;
    }

    private List<Float> generateSeqCorrectNumbers(KidLearnMathCaterSeqLevel inst) {
        List<Float> res = new ArrayList<>();
        Float interval = inst.interval;
        if (inst.interval == null) {
            float minInterval = (float) Math.floor(inst.upTo / 3);
            interval = new Random().nextInt(Math.round((inst.upTo + 1) - minInterval)) + minInterval;
        }
        float firstVal = getFirstSeqRandomVal(inst, interval);
        while (res.size() < SEQ_LEVEL_TOTAL_NR_OF_NUMBERS) {
            res.add(firstVal);
            firstVal = firstVal + interval;
        }
        Collections.sort(res);
        if (!inst.asc) {
            Collections.reverse(res);
        }
        return res;
    }

    private float getFirstSeqRandomVal(KidLearnMathCaterSeqLevel level, float interval) {
        float val = new Random().nextInt((level.max + 1) - level.min) + level.min;
        while (val + interval * SEQ_LEVEL_TOTAL_NR_OF_NUMBERS > level.max) {
            val = new Random().nextInt((level.max + 1) - level.min) + level.min;
        }
        return val;
    }

    private List<Float> generateOrdCorrectNumbers(KidLearnMathCaterOrdLevel inst) {
        List<Float> res = new ArrayList<>();
        while (res.size() < inst.totalNrOfNumbers) {
            float val = getOrdRandomVal(inst);
            while (res.contains(val)) {
                val = getOrdRandomVal(inst);
            }
            res.add(val);
        }
        Collections.sort(res);
        if (!inst.asc) {
            Collections.reverse(res);
        }
        return res;
    }

    private float getOrdRandomVal(KidLearnMathCaterOrdLevel level) {
        float val = new Random().nextInt((level.max + 1) - level.min) + level.min;
        if (level.interval % 1 != 0) {
            float f = new Random().nextFloat();
            val = val + f < level.max ? val + f : val;
        } else {
            val = Math.round(val / level.interval) * Math.round(level.interval);
        }
        return val;
    }

    public <T extends Enum & KidLearnMathCaterLevel> List<Float> generateWrongNumbers(T level, List<Float> correctNumbers) {
        List<Float> res = new ArrayList<>();
        if (level instanceof KidLearnMathCaterSeqLevel) {
            KidLearnMathCaterSeqLevel inst = (KidLearnMathCaterSeqLevel) level;
            float max = inst.max;
            float min = inst.min;
            while (res.size() < 3) {
                float val = new Random().nextInt(Math.round(max - min)) + min;
                while (correctNumbers.contains(val)) {
                    val = new Random().nextInt(Math.round(max - min)) + min;
                }
                res.add(val);
            }
        }
        return res;
    }
}
