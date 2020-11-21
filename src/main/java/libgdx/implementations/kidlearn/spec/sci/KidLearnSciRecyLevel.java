package libgdx.implementations.kidlearn.spec.sci;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.Res;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnSciRecyLevel implements KidLearnLevel, KidLearnSciLevel {

    L0(KidLearnSpecificResource.btn_sci_recy1, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_sci_recy),
    L1(KidLearnSpecificResource.btn_sci_recy2, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_sci_recy),
    ;

    public Res categImg;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnSciRecyLevel(Res categImg, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.categImg = categImg;
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

    @Override
    public Res categImg() {
        return categImg;
    }
}
