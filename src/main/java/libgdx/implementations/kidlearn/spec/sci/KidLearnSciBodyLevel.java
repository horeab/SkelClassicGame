package libgdx.implementations.kidlearn.spec.sci;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.Res;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnSciBodyLevel implements KidLearnLevel, KidLearnSciLevel {

    L0(5, KidLearnSpecificResource.human_body, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_sci_body),
    L1(5, KidLearnSpecificResource.human_body, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_sci_plant),
    L2(5, KidLearnSpecificResource.human_body, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_sci_skel),
    ;

    public int nrOfCorrectUnknownWords;
    public Res mainImg;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnSciBodyLevel(int nrOfCorrectUnknownWords, Res mainImg, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.nrOfCorrectUnknownWords = nrOfCorrectUnknownWords;
        this.mainImg = mainImg;
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