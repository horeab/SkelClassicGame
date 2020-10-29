package libgdx.implementations.kidlearn.spec;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import libgdx.resources.Res;

public class KidLearnMultipleItemsConfigCreator {

    public Map<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> create(KidLearnGameContext gameContext, List<Pair<Integer, KidLearnWordImgConfig>> containerConfigs, int totalItemsOfType) {
        LinkedHashMap<String, String> words = KidLearnUtils.getWords(gameContext.level);
        KidLearnUtils.shuffleMap(words);
        Map<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> configs = new HashMap<>();
        for (Pair<Integer, KidLearnWordImgConfig> config : containerConfigs) {
            configs.put(config.getRight(), new ArrayList<>());
        }
        for (Pair<Integer, KidLearnWordImgConfig> config : containerConfigs) {
            for (String v : words.keySet()) {
                String[] split = v.split(":");
                String word = split[1];
                Res img = KidLearnUtils.getResource(words.get(v));
                if (split[0].equals(String.valueOf(config.getKey()))
                        && configs.get(config.getRight()).size() < totalItemsOfType
                        && !gameContext.playedValues.containsKey(word)) {
                    configs.get(config.getRight()).add(new KidLearnWordImgConfig(word, img));
                    gameContext.playedValues.put(word, words.get(v));
                }
            }
        }
        return configs;
    }
}
