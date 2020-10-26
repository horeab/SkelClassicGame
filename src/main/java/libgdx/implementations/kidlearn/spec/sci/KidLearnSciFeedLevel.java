package libgdx.implementations.kidlearn.spec.sci;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnSciFeedLevel implements KidLearnLevel, KidLearnSciLevel {

    L0(5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_sci_feed),
    L1(5, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_sci_feed),
    ;

    public int nrOfCorrectUnknownWords;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnSciFeedLevel(int nrOfCorrectUnknownWords, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.nrOfCorrectUnknownWords = nrOfCorrectUnknownWords;
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public KidLearnQuestionDifficultyLevel difficulty() {
        return difficulty;
    }
    @Override
    public String title() {
        return title.getText();
    }
}