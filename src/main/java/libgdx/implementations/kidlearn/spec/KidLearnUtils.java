package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import libgdx.game.Game;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.resources.Res;

public class KidLearnUtils {

    public static <T> T getLevelValsToPlay(KidLearnGameContext gameContext, List<T> vals) {
        T rand = vals.get(new Random().nextInt(vals.size()));
        while (gameContext.playedValues.contains(rand)) {
            rand = vals.get(new Random().nextInt(vals.size()));
        }
        gameContext.playedValues.add(rand);
        return rand;
    }

    public static <T> List<T> getLevelListValsToPlay(KidLearnGameContext gameContext, int resListSize, List<T> allVals) {
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

    public static Res getResource(String word) {
        return KidLearnSpecificResource.valueOf(word.toLowerCase().replace(" ", "_"));
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
        }
        return new ArrayList<>(Arrays.asList(Gdx.files.internal(Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/" + gameId + "/" + categId + "/" + level.name().toLowerCase() + ".txt").readString().split("\n")));
    }
}
