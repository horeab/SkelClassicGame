package libgdx.implementations.kidlearn.screens;

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
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngVerbLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsConfig;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsGameCreator;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.implementations.kidlearn.spec.KidLearnMultipleItemsGameCreator;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;

public class KidLearnEngGameScreen extends AbstractScreen<KidLearnScreenManager> {

    private MyButton hoverBackButton;
    KidLearnGameContext gameContext;


    public KidLearnEngGameScreen(KidLearnGameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void buildStage() {
        hoverBackButton = new BackButtonBuilder().addHoverBackButton(this);
        hoverBackButton.toFront();
        if (gameContext.level instanceof KidLearnEngWordsLevel) {
            List<KidLearnWordImgConfig> configs = new ArrayList<>();
            KidLearnEngWordsLevel inst = (KidLearnEngWordsLevel) gameContext.level;
            for (String v : KidLearnUtils.getWords(gameContext.level)) {
                configs.add(new KidLearnWordImgConfig(v, KidLearnUtils.getResource(v)));
            }
            Collections.shuffle(configs);
            new KidLearnEngWordsGameCreator(gameContext, new KidLearnEngWordsConfig(configs.subList(0, inst.totalUnknownItems))).create();
        } else if (gameContext.level instanceof KidLearnEngVerbLevel) {
            KidLearnWordImgConfig nounConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_eng_verb_noun.getText(), KidLearnSpecificResource.noun_container);
            KidLearnWordImgConfig verbConfig = new KidLearnWordImgConfig(KidLearnGameLabel.l_eng_verb_verb.getText(), KidLearnSpecificResource.verb_container);
            Map<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> configs = new HashMap<>();
            configs.put(nounConfig, new ArrayList<>());
            configs.put(verbConfig, new ArrayList<>());
            KidLearnEngVerbLevel inst = (KidLearnEngVerbLevel) gameContext.level;
            for (String v : KidLearnUtils.getWords(gameContext.level)) {
                String[] split = v.split(":");
                KidLearnWordImgConfig key = null;
                String word = split[1];
                Res img = KidLearnUtils.getResource(word);
                if (split[0].equals("0")) {
                    configs.get(nounConfig).add(new KidLearnWordImgConfig(word, img));
                } else {
                    configs.get(verbConfig).add(new KidLearnWordImgConfig(word, img));
                }
            }
            new KidLearnEngVerbGameCreator(gameContext, new KidLearnMultipleAnswersConfig(configs)).create();
        } else {
            String rand = KidLearnUtils.getLevelValsToPlay(gameContext, KidLearnUtils.getWords(gameContext.level));
            setUpAllTable();
            getAllTable().add(new KidLearnEngHangmanGameCreator(gameContext, new KidLearnWordImgConfig(rand,
                    KidLearnUtils.getResource(rand))).createTable())
                    .padTop(MainDimen.vertical_general_margin.getDimen() * 4);
        }
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showLevelScreen(gameContext.level.getClass());
    }

}
