package libgdx.implementations.kidlearn.spec.sci;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.Res;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnSciStateLevel implements KidLearnLevel, KidLearnSciLevel {

    L0(KidLearnSpecificResource.btn_sci_state, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_sci_state, true),;

    public Res categImg;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;
    public boolean isLocked;

    KidLearnSciStateLevel(Res categImg, KidLearnQuestionDifficultyLevel difficulty, GameLabel title, boolean isLocked) {
        this.isLocked = isLocked;
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

    @Override
    public boolean isLocked() {
        return isLocked;
    }
}
