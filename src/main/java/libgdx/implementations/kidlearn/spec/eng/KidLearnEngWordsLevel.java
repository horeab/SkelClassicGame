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

public enum KidLearnEngWordsLevel implements KidLearnLevel {

    L0("Animals", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_findword),
    L1("Numbers", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_findword),
    L2("Fruits", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_findword),
    L3("Substantive Verbs", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_findword),
    L4("Animals", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_findword),
    L5("Numbers", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_findword),
    L6("Fruits", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_findword),
    L7("Colors", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_findword),
    L8("Animals", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_findword),
    L9("Numbers", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_findword),
    L10("Fruits", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_findword),
    L11("Colors", Arrays.asList(
            Pair.of("Dog", MainResource.sound_on),
            Pair.of("Cat", MainResource.sound_off),
            Pair.of("Cow", MainResource.remove),
            Pair.of("Horse", MainResource.refresh_down),
            Pair.of("Lion", MainResource.heart_full)), KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_findword),
    ;

    public String category;
    public List<Pair<String, Res>> vals;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnEngWordsLevel(String category, List<Pair<String, Res>> vals, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.category = category;
        this.vals = vals;
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public String title() {
        return title.getText();
    }
}
