package libgdx.implementations.kidlearn;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum KidLearnSpecificResource implements SpecificResource {

    // @formatter:off

    diff_btn_0_down("buttons/diff_btn_0_down.png", Texture.class),
    diff_btn_0_up("buttons/diff_btn_0_up.png", Texture.class),
    diff_btn_1_down("buttons/diff_btn_1_down.png", Texture.class),
    diff_btn_1_up("buttons/diff_btn_1_up.png", Texture.class),
    diff_btn_2_down("buttons/diff_btn_2_down.png", Texture.class),
    diff_btn_2_up("buttons/diff_btn_2_up.png", Texture.class),

    //math
    cater_body("math/cater_body.png", Texture.class),
    cater_head("math/cater_head.png", Texture.class),
    cater_tail("math/cater_tail.png", Texture.class),
    cater_unk("math/cater_unk.png", Texture.class),
    level_finished("math/level_finished.png", Texture.class),
    level_not_finished("math/level_not_finished.png", Texture.class),

    //science
    arrow_left("science/arrow_left.png", Texture.class),

    //eng
    btn_hangman_up("buttons/btn_hangman_up.png", Texture.class),
    btn_hangman_down("buttons/btn_hangman_down.png", Texture.class),
    noun_container("eng/noun_container.png", Texture.class),
    verb_container("eng/verb_container.png", Texture.class),
    word_unk("eng/word_unk.png", Texture.class),

    //questions
    cat("questions/img/cat.png", Texture.class),
    cow("questions/img/cow.png", Texture.class),
    dog("questions/img/dog.png", Texture.class),
    horse("questions/img/horse.png", Texture.class),
    lion("questions/img/lion.png", Texture.class),
    crow("questions/img/crow.png", Texture.class),
    chicken("questions/img/chicken.png", Texture.class),
    pigeon("questions/img/pigeon.png", Texture.class),
    mountain("questions/img/mountain.png", Texture.class),
    house("questions/img/house.png", Texture.class),
    pencil("questions/img/pencil.png", Texture.class),
    running("questions/img/running.png", Texture.class),
    writing("questions/img/writing.png", Texture.class),
    singing("questions/img/singing.png", Texture.class),

    specific_labels("labels/labels", I18NBundle.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    KidLearnSpecificResource(String path, Class<?> classType) {
        this.path = path;
        this.classType = classType;
    }

    @Override
    public Class<?> getClassType() {
        return classType;
    }

    @Override
    public String getPath() {
        return Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + path;
    }

}
