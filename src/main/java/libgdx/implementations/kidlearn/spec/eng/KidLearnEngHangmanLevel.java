package libgdx.implementations.kidlearn.spec.eng;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnEngHangmanLevel implements KidLearnLevel {

    L0(Arrays.asList(
            Pair.of("Horse", MainResource.heart_full),
            Pair.of("Cow", MainResource.heart_full),
            Pair.of("Dog", MainResource.heart_full),
            Pair.of("Lion", MainResource.heart_full)
    ), KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_findword),
    ;

    public List<Pair<String, Res>> vals;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnEngHangmanLevel(List<Pair<String, Res>> vals, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.vals = vals;
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public String title() {
        return title.getText();
    }
}
