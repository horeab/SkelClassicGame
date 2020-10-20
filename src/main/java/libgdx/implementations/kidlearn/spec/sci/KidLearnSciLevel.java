package libgdx.implementations.kidlearn.spec.sci;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnSciLevel implements KidLearnLevel {

    L0(5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_sci_feed),//ce mananca fiecare animal
    L1(5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_sci_recy),// cosuri gunoi si clasificarea gunoaielor
    L2(5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_sci_body),//
    L3(5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_sci_state), // clasificarea imaginilor (cub gheata, ceai, abur) solid/lichid/aer
    ;

    public int nrOfCorrectUnknownWords;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnSciLevel(int nrOfCorrectUnknownWords, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.nrOfCorrectUnknownWords = nrOfCorrectUnknownWords;
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public String title() {
        return title.getText();
    }
}
