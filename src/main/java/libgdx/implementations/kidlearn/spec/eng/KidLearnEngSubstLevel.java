package libgdx.implementations.kidlearn.spec.eng;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.resources.MainResource;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnEngSubstLevel implements KidLearnLevel {

    L0(Pair.of(new KidLearnWordImgConfig("Substantives", MainResource.heart_full),
            Arrays.asList(
                    new KidLearnWordImgConfig("Mountain", MainResource.heart_full),
                    new KidLearnWordImgConfig("River", MainResource.heart_full),
                    new KidLearnWordImgConfig("House", MainResource.heart_full)
            )),
            Pair.of(new KidLearnWordImgConfig("Verbs", MainResource.heart_full),
                    Arrays.asList(
                            new KidLearnWordImgConfig("Running", MainResource.heart_full),
                            new KidLearnWordImgConfig("Swimming", MainResource.heart_full),
                            new KidLearnWordImgConfig("Talking", MainResource.heart_full)
                    ))
            , KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_findword),
    ;

    public Pair<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> subst;
    public Pair<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> verbs;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnEngSubstLevel(Pair<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> subst,
                          Pair<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> verbs,
                          KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.subst = subst;
        this.verbs = verbs;
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public String title() {
        return title.getText();
    }
}
