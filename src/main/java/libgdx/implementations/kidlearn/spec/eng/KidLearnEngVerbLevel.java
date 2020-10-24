package libgdx.implementations.kidlearn.spec.eng;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnEngVerbLevel implements KidLearnLevel, KidLearnEngLevel {

    L0(KidLearnGameLabel.l_eng_school, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_verb),
    L1(KidLearnGameLabel.l_eng_sport, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_verb),
    ;

    public GameLabel category;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnEngVerbLevel(GameLabel category, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.category = category;
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public String title() {
        return title.getText();
    }

    @Override
    public KidLearnQuestionDifficultyLevel difficulty() {
        return difficulty;
    }

    @Override
    public GameLabel category() {
        return category;
    }
}
