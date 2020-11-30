package libgdx.implementations.kidlearn.screens;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnDifficultyService;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnMultipleAnswersConfig;
import libgdx.implementations.kidlearn.spec.KidLearnMultipleItemsConfigCreator;
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.implementations.kidlearn.spec.KidLearnUtils;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnArrowConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnArrowsConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciBodyGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciBodyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciRecyGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciRecyLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateGameCreator;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateLevel;
import libgdx.screen.AbstractScreen;

public class KidLearnSciGameScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnGameContext gameContext;
    private KidLearnDifficultyService kidLearnDifficultyService = new KidLearnDifficultyService();


    public KidLearnSciGameScreen(KidLearnGameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void buildStage() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(KidLearnSpecificResource.scroll_background_sci);
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        if (gameContext.level instanceof KidLearnSciFeedLevel) {
            KidLearnSciFeedLevel inst = (KidLearnSciFeedLevel) gameContext.level;
            LinkedHashMap<String, String> wordsToPlay = KidLearnUtils.getLevelListValsToPlay(gameContext, inst.nrOfCorrectUnknownWords, KidLearnUtils.getWords(gameContext.level));
            List<Pair<KidLearnWordImgConfig, KidLearnWordImgConfig>> configs = new ArrayList<>();
            for (String word : wordsToPlay.keySet()) {
                String[] split = word.split(":");
                String[] transSplit = wordsToPlay.get(word).split(":");
                configs.add(Pair.of(new KidLearnWordImgConfig(split[0], KidLearnUtils.getResource(transSplit[0])),
                        new KidLearnWordImgConfig(split[1], KidLearnUtils.getResource(transSplit[1]))));
            }
            new KidLearnSciFeedGameCreator(gameContext, new KidLearnSciFeedConfig(configs)).create();
        } else if (gameContext.level instanceof KidLearnSciBodyLevel) {
            KidLearnSciBodyLevel inst = (KidLearnSciBodyLevel) gameContext.level;
            LinkedHashMap<String, String> wordsToPlay = KidLearnUtils.getLevelListValsToPlay(gameContext, inst.nrOfCorrectUnknownWords, KidLearnUtils.getWords(gameContext.level));
            KidLearnUtils.reverseMap(wordsToPlay);
            List<KidLearnArrowConfig> configs = new ArrayList<>();
            for (String word : wordsToPlay.keySet()) {
                String[] split = word.split(":");
                configs.add(new KidLearnArrowConfig(split[0], Float.parseFloat(split[1]), Float.parseFloat(split[2])));
            }
            Collections.sort(configs, new Comparator<KidLearnArrowConfig>() {
                @Override
                public int compare(final KidLearnArrowConfig o1, KidLearnArrowConfig o2) {
                    return Float.compare(o1.wordY, o2.wordY);
                }
            });
            new KidLearnSciBodyGameCreator(gameContext, new KidLearnArrowsConfig(inst.mainImg, configs)).create();
        } else if (gameContext.level instanceof KidLearnSciStateLevel) {
            KidLearnWordImgConfig solidConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_sci_state_solid.getText(), KidLearnSpecificResource.solid_container);
            KidLearnWordImgConfig liqConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_sci_state_liquid.getText(), KidLearnSpecificResource.liquid_container);
            KidLearnWordImgConfig gasConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_sci_state_gas.getText(), KidLearnSpecificResource.gas_container);
            Map<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> configs = new KidLearnMultipleItemsConfigCreator().create(gameContext, Arrays.asList(Pair.of(0, solidConfig),
                    Pair.of(1, liqConfig), Pair.of(2, gasConfig)), KidLearnSciStateGameCreator.TOTAL_ITEMS_OF_TYPE);
            new KidLearnSciStateGameCreator(gameContext, new KidLearnMultipleAnswersConfig(configs)).create();
        } else if (gameContext.level instanceof KidLearnSciRecyLevel) {
            KidLearnWordImgConfig paperConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_sci_recy_paper.getText(), KidLearnSpecificResource.paper_container);
            KidLearnWordImgConfig plasticConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_sci_recy_plastic.getText(), KidLearnSpecificResource.plastic_container);
            KidLearnWordImgConfig glassConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_sci_recy_glass.getText(), KidLearnSpecificResource.glass_container);
            KidLearnWordImgConfig organicConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_sci_recy_organic.getText(), KidLearnSpecificResource.organic_container);
            Map<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> configs = new KidLearnMultipleItemsConfigCreator().create(gameContext,
                    Arrays.asList(Pair.of(0, paperConfig), Pair.of(1, glassConfig), Pair.of(2, plasticConfig),
                            Pair.of(3, organicConfig)), KidLearnSciRecyGameCreator.TOTAL_ITEMS_OF_TYPE);
            new KidLearnSciRecyGameCreator(gameContext, new KidLearnMultipleAnswersConfig(configs)).create();
        }
        kidLearnDifficultyService.setBackgroundDiff(new KidLearnPreferencesManager().getDifficultyLevel(KidLearnSciLevelScreen.difficultyLevelClass()), getBackgroundStage());
    }


    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(gameContext.level.getClass());
    }

}
