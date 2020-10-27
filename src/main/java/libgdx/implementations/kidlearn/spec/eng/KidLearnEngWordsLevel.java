package libgdx.implementations.kidlearn.spec.eng;

import libgdx.controls.button.ButtonSkin;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnEngWordsLevel implements KidLearnLevel, KidLearnEngLevel {

    L0(KidLearnGameLabel.l_eng_animals, SkelClassicButtonSkin.KIDLEARN_ENG_ANIMALS, 5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_words),
    L1(KidLearnGameLabel.l_eng_fruits, SkelClassicButtonSkin.KIDLEARN_ENG_FRUITS, 5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_words),
    L2(KidLearnGameLabel.l_eng_numbers, SkelClassicButtonSkin.KIDLEARN_ENG_NUMBERS, 5, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_words),
    L3(KidLearnGameLabel.l_eng_shapes, SkelClassicButtonSkin.KIDLEARN_ENG_SHAPES, 5, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_words),
    ;

    public GameLabel category;
    public ButtonSkin buttonSkin;
    public int totalUnknownItems;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnEngWordsLevel(GameLabel category, ButtonSkin buttonSkin, int totalUnknownItems, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.category = category;
        this.buttonSkin = buttonSkin;
        this.totalUnknownItems = totalUnknownItems;
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public ButtonSkin buttonSkin() {
        return buttonSkin;
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
    public GameLabel category() {
        return category;
    }
}
