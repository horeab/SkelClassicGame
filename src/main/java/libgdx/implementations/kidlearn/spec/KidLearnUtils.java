package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.Gdx;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import libgdx.game.Game;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciBodyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciRecyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateLevel;
import libgdx.resources.Res;
import libgdx.utils.SoundUtils;

public class KidLearnUtils {

    public static <T> Map.Entry<T, T> getLevelValsToPlay(KidLearnGameContext gameContext, LinkedHashMap<T, T> allVals) {
        Map.Entry<T, T> rand = new ArrayList<>(allVals.entrySet()).get(new Random().nextInt(allVals.size()));
        while (gameContext.playedValues.containsKey(rand.getKey())) {
            rand = new ArrayList<>(allVals.entrySet()).get(new Random().nextInt(allVals.size()));
        }
        gameContext.playedValues.put(rand.getKey(), rand.getValue());
        return rand;
    }

    public static <T> LinkedHashMap<T, T> getRandomLevelListValsToPlay(KidLearnGameContext gameContext, int resListSize, LinkedHashMap<T, T> allVals) {
        LinkedHashMap<T, T> res = new LinkedHashMap<>();
        while (res.size() < resListSize) {
            Map.Entry<T, T> rand = new ArrayList<>(allVals.entrySet()).get(new Random().nextInt(allVals.size()));
            while (gameContext.playedValues.containsKey(rand.getKey())) {
                rand = new ArrayList<>(allVals.entrySet()).get(new Random().nextInt(allVals.size()));
            }
            res.put(rand.getKey(), rand.getValue());
            gameContext.playedValues.put(rand.getKey(), rand.getValue());
        }
        return res;
    }

    public static <T> LinkedHashMap<T, T> getLevelListValsToPlay(KidLearnGameContext gameContext, int resListSize, LinkedHashMap<T, T> allVals) {
        LinkedHashMap<T, T> res = new LinkedHashMap<>();
        int i = 0;
        while (res.size() < resListSize) {
            Map.Entry<T, T> el = new ArrayList<>(allVals.entrySet()).get(i);
            while (gameContext.playedValues.containsKey(el.getKey())) {
                i++;
                el = new ArrayList<>(allVals.entrySet()).get(i);
            }
            res.put(el.getKey(), el.getValue());
            gameContext.playedValues.put(el.getKey(), el.getValue());
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

    public static LinkedHashMap<String, String> getWords(Enum level) {
        String gameId = null;
        String categId = null;
        String engWordsPath = null;

        if (level instanceof KidLearnEngHangmanLevel) {
            gameId = "eng";
            categId = "hangman";
        } else if (level instanceof KidLearnEngWordsLevel) {
            gameId = "eng";
            categId = "words";
        } else if (level instanceof KidLearnEngVerbLevel) {
            gameId = "eng";
            categId = "verb";
        } else {
            String gamePrefix = "sci/";
            engWordsPath = gamePrefix + "en";
            String SCI_PREFIX = gamePrefix + Game.getInstance().getAppInfoService().getLanguage();
            if (level instanceof KidLearnSciFeedLevel) {
                gameId = SCI_PREFIX;
                categId = "feed";
            } else if (level instanceof KidLearnSciBodyLevel) {
                gameId = SCI_PREFIX;
                categId = "body";
            } else if (level instanceof KidLearnSciStateLevel) {
                gameId = SCI_PREFIX;
                categId = "state";
            } else if (level instanceof KidLearnSciRecyLevel) {
                gameId = SCI_PREFIX;
                categId = "recy";
            }
        }
        LinkedHashMap<String, String> res = new LinkedHashMap<>();
        List<String> translatedWords = getWords(level, categId, gameId);
        List<String> pairWords = translatedWords;
        if (StringUtils.isNotBlank(engWordsPath)) {
            pairWords = getWords(level, categId, engWordsPath);
        }
        for (int i = 0; i < translatedWords.size(); i++) {
            res.put(translatedWords.get(i), pairWords.get(i));
        }
        return res;
    }

    private static ArrayList<String> getWords(Enum levelNr, String gameCateg, String gamePath) {
        return new ArrayList<>(Arrays.asList(Gdx.files.internal(Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder()
                + "questions/" + gamePath + "/" + gameCateg + "/" + levelNr.name().toLowerCase() + ".txt")
                .readString().split("\n")));
    }

    public static LinkedHashMap<String, String> shuffleMap(LinkedHashMap<String, String> map) {
        LinkedHashMap<String, String> res = new LinkedHashMap<>();
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.shuffle(keys);
        for (String key : keys) {
            res.put(key, map.get(key));
        }
        return res;
    }


    public static LinkedHashMap<String, String> reverseMap(LinkedHashMap<String, String> map) {
        LinkedHashMap<String, String> res = new LinkedHashMap<>();
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.reverse(keys);
        for (String key : keys) {
            res.put(key, map.get(key));
        }
        return res;
    }
}
