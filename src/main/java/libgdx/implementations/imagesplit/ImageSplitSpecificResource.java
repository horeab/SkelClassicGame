package libgdx.implementations.imagesplit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum ImageSplitSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),

    gametype_swap("buttons/gametype_swap.png", Texture.class),
    gametype_swap_clicked("buttons/gametype_swap_clicked.png", Texture.class),
    gametype_swap_disabled("buttons/gametype_swap_disabled.png", Texture.class),
    gametype_push("buttons/gametype_push.png", Texture.class),
    gametype_push_clicked("buttons/gametype_push_clicked.png", Texture.class),
    gametype_push_disabled("buttons/gametype_push_disabled.png", Texture.class),
    gametype_slide("buttons/gametype_slide.png", Texture.class),
    gametype_slide_clicked("buttons/gametype_slide_clicked.png", Texture.class),
    gametype_slide_disabled("buttons/gametype_slide_disabled.png", Texture.class),
    viewimg("buttons/viewimg.png", Texture.class),
    tutorial_swipe_finger("tutorial_swipe_finger.png", Texture.class),
    seconds_icon("seconds_icon.png", Texture.class),
    moves_icon("moves_icon.png", Texture.class),

    leftarrow("buttons/leftarrow.png", Texture.class),
    rightarrow("buttons/rightarrow.png", Texture.class),

    i0("images/i0.jpg", Texture.class),
    i1("images/i1.jpg", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    ImageSplitSpecificResource(String path, Class<?> classType) {
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
