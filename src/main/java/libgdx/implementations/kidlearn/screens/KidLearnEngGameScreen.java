package libgdx.implementations.kidlearn.screens;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsConfig;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciStateGameCreator;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;

public class KidLearnEngGameScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnGameContext gameContext;
    private KidLearnDifficultyService kidLearnDifficultyService = new KidLearnDifficultyService();


    public KidLearnEngGameScreen(KidLearnGameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void buildStage() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(KidLearnSpecificResource.scroll_background_eng);
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        if (gameContext.level instanceof KidLearnEngWordsLevel) {
            KidLearnEngWordsLevel inst = (KidLearnEngWordsLevel) gameContext.level;
            LinkedHashMap<String, String> wordsToPlay = KidLearnUtils.getRandomLevelListValsToPlay(gameContext, inst.totalUnknownItems, KidLearnUtils.getWords(gameContext.level));
            KidLearnUtils.shuffleMap(wordsToPlay);
            List<KidLearnWordImgConfig> configs = new ArrayList<>();
            for (String word : wordsToPlay.keySet()) {
                configs.add(new KidLearnWordImgConfig(word, KidLearnUtils.getResource(wordsToPlay.get(word))));
            }
            Collections.shuffle(configs);
            new KidLearnEngWordsGameCreator(gameContext, new KidLearnEngWordsConfig(configs)).create();
        } else if (gameContext.level instanceof KidLearnEngVerbLevel) {
            KidLearnWordImgConfig nounConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_eng_verb_noun.getText(), KidLearnSpecificResource.noun_container);
            KidLearnWordImgConfig verbConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_eng_verb_verb.getText(), KidLearnSpecificResource.verb_container);
            Map<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> configs = new KidLearnMultipleItemsConfigCreator().create(gameContext,
                    Arrays.asList(Pair.of(0, nounConfig), Pair.of(1, verbConfig)), KidLearnEngVerbGameCreator.TOTAL_ITEMS_OF_TYPE);
            new KidLearnEngVerbGameCreator(gameContext, new KidLearnMultipleAnswersConfig(configs)).create();
        } else {
            LinkedHashMap<String, String>  words = KidLearnUtils.getWords(gameContext.level);
            KidLearnUtils.shuffleMap(words);
            Map.Entry<String, String> rand = KidLearnUtils.getLevelValsToPlay(gameContext, words);
            setUpAllTable();
            getAllTable().add(new KidLearnEngHangmanGameCreator(gameContext, new KidLearnWordImgConfig(rand.getKey(),
                    KidLearnUtils.getResource(rand.getValue()))).createTable())
                    .padTop(MainDimen.vertical_general_margin.getDimen() * 4);
        }
        kidLearnDifficultyService.setBackgroundDiff(new KidLearnPreferencesManager().getDifficultyLevel(KidLearnMathLevelScreen.difficultyLevelClass()), getBackgroundStage());
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(gameContext.level.getClass());
    }

}
