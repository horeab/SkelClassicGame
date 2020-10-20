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

    //science
    arrow_left("science/arrow_left.png", Texture.class),

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
