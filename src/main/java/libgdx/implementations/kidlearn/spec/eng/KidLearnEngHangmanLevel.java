package libgdx.implementations.kidlearn.spec.eng;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnEngHangmanLevel implements KidLearnLevel {

    L0(KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_hangman),
    ;

    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnEngHangmanLevel(KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public String title() {
        return title.getText();
    }
}
