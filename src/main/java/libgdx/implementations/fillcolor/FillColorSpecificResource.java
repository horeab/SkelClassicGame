package libgdx.implementations.fillcolor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum FillColorSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),

    color_bucket("color_bucket.png", Texture.class),
    color_bucket_selected("color_bucket_selected.png", Texture.class),
    star("star.png", Texture.class),
    progress_table_background1("progress_table_background1.png", Texture.class),
    progress_table_background2("progress_table_background2.png", Texture.class),
    progress_table_fill("progress_table_fill.png", Texture.class),
    level_nr_background("level_nr_background.png", Texture.class),

    img0("img/img0.png", Texture.class),
    img0_fill("img/img0_fill.png", Texture.class),
    img1("img/img1.png", Texture.class),
    img1_fill("img/img1_fill.png", Texture.class),
    img2("img/img2.png", Texture.class),
    img2_fill("img/img2_fill.png", Texture.class),
    img3("img/img3.png", Texture.class),
    img3_fill("img/img3_fill.png", Texture.class),
    img4("img/img4.png", Texture.class),
    img4_fill("img/img4_fill.png", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    FillColorSpecificResource(String path, Class<?> classType) {
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
