package libgdx.implementations.kidlearn.screens;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnMultipleAnswersConfig;
import libgdx.implementations.kidlearn.spec.KidLearnUtils;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnArrowConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnArrowsConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciBodyGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciBodyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateLevel;
import libgdx.resources.Res;
import libgdx.screen.AbstractScreen;

public class KidLearnSciGameScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnGameContext gameContext;


    public KidLearnSciGameScreen(KidLearnGameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        if (gameContext.level instanceof KidLearnSciFeedLevel) {
            KidLearnSciFeedLevel inst = (KidLearnSciFeedLevel) gameContext.level;
            List<String> wordsToPlay = KidLearnUtils.getLevelListValsToPlay(gameContext, inst.nrOfCorrectUnknownWords, KidLearnUtils.getWords(gameContext.level));
            List<Pair<KidLearnWordImgConfig, KidLearnWordImgConfig>> configs = new ArrayList<>();
            for (String word : wordsToPlay) {
                String[] split = word.split(":");
                configs.add(Pair.of(new KidLearnWordImgConfig(split[0], KidLearnUtils.getResource(split[0])),
                        new KidLearnWordImgConfig(split[1], KidLearnUtils.getResource(split[1]))));
            }
            new KidLearnSciFeedGameCreator(gameContext, new KidLearnSciFeedConfig(configs)).create();
        } else if (gameContext.level instanceof KidLearnSciBodyLevel) {
            KidLearnSciBodyLevel inst = (KidLearnSciBodyLevel) gameContext.level;
            List<String> wordsToPlay = KidLearnUtils.getLevelListValsToPlay(gameContext, inst.nrOfCorrectUnknownWords, KidLearnUtils.getWords(gameContext.level));
            Collections.reverse(wordsToPlay);
            List<KidLearnArrowConfig> configs = new ArrayList<>();
            for (String word : wordsToPlay) {
                String[] split = word.split(":");
                configs.add(new KidLearnArrowConfig(split[0], Float.valueOf(split[1]), Float.valueOf(split[2])));
            }
            new KidLearnSciBodyGameCreator(gameContext, new KidLearnArrowsConfig(inst.mainImg, configs)).create();
        } else if (gameContext.level instanceof KidLearnSciStateLevel) {
            List<String> words = KidLearnUtils.getWords(gameContext.level);
            Collections.shuffle(words);
            KidLearnWordImgConfig solidConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_sci_state_solid.getText(), KidLearnSpecificResource.solid_container);
            KidLearnWordImgConfig liqConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_sci_state_liquid.getText(), KidLearnSpecificResource.liquid_container);
            KidLearnWordImgConfig gasConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_sci_state_gas.getText(), KidLearnSpecificResource.gas_container);
            Map<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> configs = new HashMap<>();
            configs.put(liqConfig, new ArrayList<>());
            configs.put(solidConfig, new ArrayList<>());
            configs.put(gasConfig, new ArrayList<>());
            for (String v : words) {
                String[] split = v.split(":");
                String word = split[1];
                Res img = KidLearnUtils.getResource(word);
                List<KidLearnWordImgConfig> solidConfigs = configs.get(solidConfig);
                List<KidLearnWordImgConfig> liqConfigs = configs.get(liqConfig);
                List<KidLearnWordImgConfig> gasConfigs = configs.get(gasConfig);
                if (split[0].equals("0")
                        && solidConfigs.size() < KidLearnSciStateGameCreator.TOTAL_ITEMS_OF_TYPE
                        && !gameContext.playedValues.contains(word)) {
                    solidConfigs.add(new KidLearnWordImgConfig(word, img));
                    gameContext.playedValues.add(word);
                } else if (split[0].equals("1")
                        && liqConfigs.size() < KidLearnSciStateGameCreator.TOTAL_ITEMS_OF_TYPE
                        && !gameContext.playedValues.contains(word)) {
                    liqConfigs.add(new KidLearnWordImgConfig(word, img));
                    gameContext.playedValues.add(word);
                } else if (split[0].equals("2")
                        && gasConfigs.size() < KidLearnSciStateGameCreator.TOTAL_ITEMS_OF_TYPE
                        && !gameContext.playedValues.contains(word)) {
                    gasConfigs.add(new KidLearnWordImgConfig(word, img));
                    gameContext.playedValues.add(word);
                }
            }
            new KidLearnSciStateGameCreator(gameContext, new KidLearnMultipleAnswersConfig(configs)).create();
        }
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(gameContext.level.getClass());
    }

}
