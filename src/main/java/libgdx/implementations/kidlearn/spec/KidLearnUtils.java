package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.Gdx;

import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import libgdx.game.Game;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciBodyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateLevel;
import libgdx.resources.Res;
import libgdx.utils.SoundUtils;

public class KidLearnUtils {

    public static <T> T getLevelValsToPlay(KidLearnGameContext gameContext, List<T> vals) {
        T rand = vals.get(new Random().nextInt(vals.size()));
        while (gameContext.playedValues.contains(rand)) {
            rand = vals.get(new Random().nextInt(vals.size()));
        }
        gameContext.playedValues.add(rand);
        return rand;
    }

    public static <T> List<T> getRandomLevelListValsToPlay(KidLearnGameContext gameContext, int resListSize, List<T> allVals) {
        List<T> res = new ArrayList<>();
        while (res.size() < resListSize) {
            T rand = allVals.get(new Random().nextInt(allVals.size()));
            while (gameContext.playedValues.contains(rand)) {
                rand = allVals.get(new Random().nextInt(allVals.size()));
            }
            res.add(rand);
            gameContext.playedValues.add(rand);
        }
        return res;
    }

    public static <T> List<T> getLevelListValsToPlay(KidLearnGameContext gameContext, int resListSize, List<T> allVals) {
        List<T> res = new ArrayList<>();
        int i = 0;
        while (res.size() < resListSize) {
            T el = allVals.get(i);
            while (gameContext.playedValues.contains(el)) {
                el = allVals.get(new Random().nextInt(allVals.size()));
            }
            res.add(el);
            gameContext.playedValues.add(el);
            i++;
        }
        return res;
    }

    public static Res getResource(String word) {
        return KidLearnSpecificResource.valueOf(word.toLowerCase().replace(" ", "_"));
    }

    public static void playSoundForEnum(String enumName) {
        String soundEnum = enumName.toLowerCase() + "_sound";
        if (EnumUtils.isValidEnum(KidLearnSpecificResource.class, soundEnum)) {
            SoundUtils.playSound(KidLearnSpecificResource.valueOf(soundEnum));
        }
    }

    public static List<String> getWords(Enum level) {
        String gameId = null;
        String categId = null;

        if (level instanceof KidLearnEngHangmanLevel) {
            gameId = "eng";
            categId = "hangman";
        } else if (level instanceof KidLearnEngWordsLevel) {
            gameId = "eng";
            categId = "words";
        } else if (level instanceof KidLearnEngVerbLevel) {
            gameId = "eng";
            categId = "verb";
        } else if (level instanceof KidLearnSciFeedLevel) {
            gameId = "sci";
            categId = "feed";
        } else if (level instanceof KidLearnSciBodyLevel) {
            gameId = "sci";
            categId = "body";
        } else if (level instanceof KidLearnSciStateLevel) {
            gameId = "sci";
            categId = "state";
        }
        return new ArrayList<>(Arrays.asList(Gdx.files.internal(Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/" + gameId + "/" + categId + "/" + level.name().toLowerCase() + ".txt").readString().split("\n")));
    }

}
