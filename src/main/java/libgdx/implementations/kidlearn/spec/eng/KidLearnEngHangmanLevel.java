package libgdx.implementations.kidlearn.spec.eng;

import libgdx.controls.button.ButtonSkin;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnEngHangmanLevel implements KidLearnLevel, KidLearnEngLevel {

    L0(KidLearnGameLabel.l_eng_vegetables, SkelClassicButtonSkin.KIDLEARN_ENG_VEGETABLES, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_hangman, false),
    L1(KidLearnGameLabel.l_eng_colors, SkelClassicButtonSkin.KIDLEARN_ENG_COLORS, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_hangman, true),
    L2(KidLearnGameLabel.l_eng_kitchen, SkelClassicButtonSkin.KIDLEARN_ENG_KITCHEN, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_hangman, true),;

    public GameLabel category;
    public ButtonSkin buttonSkin;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;
    public boolean isLocked;

    KidLearnEngHangmanLevel(GameLabel category, ButtonSkin buttonSkin, KidLearnQuestionDifficultyLevel difficulty, GameLabel title, boolean isLocked) {
        this.isLocked = isLocked;
        this.category = category;
        this.buttonSkin = buttonSkin;
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

    @Override
    public boolean isLocked() {
        return isLocked;
    }
}
