package libgdx.implementations.balloon;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.implementations.balloon.model.MatrixValue;
import libgdx.resources.Res;
import libgdx.resources.SpecificResource;
import sun.net.www.content.image.png;

public enum BalloonSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),

    air("images/air.png", Texture.class),
    balloonbackgr("images/balloonbackgr.png", Texture.class),
    balloonp1("images/balloonp1.png", Texture.class),
    balloonp2("images/balloonp2.png", Texture.class),
    blockingcloud("images/blockingcloud.png", Texture.class),
    bluebtn_clicked("images/bluebtn_clicked.png", Texture.class),
    bluebtn_locked("images/bluebtn_locked.png", Texture.class),
    bluebtn_normal("images/bluebtn_normal.png", Texture.class),
    cloudleft("images/cloudleft.png", Texture.class),
    cloudright("images/cloudright.png", Texture.class),
    cloudstxt("images/cloudstxt.png", Texture.class),
    destroyedballoonp1("images/destroyedballoonp1.png", Texture.class),
    destroyedballoonp2("images/destroyedballoonp2.png", Texture.class),
    greenbtn_clicked("images/greenbtn_clicked.png", Texture.class),
    greenbtn_locked("images/greenbtn_locked.png", Texture.class),
    greenbtn_normal("images/greenbtn_normal.png", Texture.class),
    icon("images/icon.png", Texture.class),
    light_blue_backgr("images/light_blue_backgr.png", Texture.class),
    light_green_backgr("images/light_green_backgr.png", Texture.class),
    lightcloudstxt("images/lightcloudstxt.png", Texture.class),
    plane("images/plane.png", Texture.class),
    points("images/points.png", Texture.class),
    redbtn_clicked("images/redbtn_clicked.png", Texture.class),
    redbtn_locked("images/redbtn_locked.png", Texture.class),
    redbtn_normal("images/redbtn_normal.png", Texture.class),
    soundoff("images/soundoff.png", Texture.class),
    soundon("images/soundon.png", Texture.class),
    tornado("images/tornado.png", Texture.class),
    tornadoballoon1p1("images/tornadoballoon1p1.png", Texture.class),
    tornadoballoon1p2("images/tornadoballoon1p2.png", Texture.class),
    hot_air_balloon("images/hot_air_balloon.png", Texture.class),

    l0("sounds/l0.wav", Sound.class),
    l1("sounds/l1.wav", Sound.class),
    l2("sounds/l2.wav", Sound.class),
    l3("sounds/l3.wav", Sound.class),
    l4("sounds/l4.wav", Sound.class),
    l5("sounds/l5.wav", Sound.class),
    l6("sounds/l6.wav", Sound.class),
    l7("sounds/l7.wav", Sound.class),
    l8("sounds/l8.wav", Sound.class),
    l9("sounds/l9.wav", Sound.class),
    l10("sounds/l10.wav", Sound.class),
    l11("sounds/l11.wav", Sound.class),
    l12("sounds/l12.wav", Sound.class),
    l13("sounds/l13.wav", Sound.class),
    l14("sounds/l14.wav", Sound.class),
    l15("sounds/l15.wav", Sound.class),
    l16("sounds/l16.wav", Sound.class),
    explosion("sounds/explosion.wav", Sound.class),
    coin("sounds/coin.wav", Sound.class),
    tornadosound("sounds/tornado.wav", Sound.class),;

    // @formatter:on


    public static Res getSoundForPosition(int row, int cellValue) {
        if (cellValue == MatrixValue.POINTS.getValue()) {
            return coin;
        } else if (cellValue == MatrixValue.TORNADO.getValue()) {
            return tornadosound;
        } else if (MatrixValue.isDestroyed(cellValue)) {
            return explosion;
        }
        switch (row) {
            case 0:
                return l0;
            case 1:
                return l1;
            case 2:
                return l2;
            case 3:
                return l3;
            case 4:
                return l4;
            case 5:
                return l5;
            case 6:
                return l6;
            case 7:
                return l7;
            case 8:
                return l8;
            case 9:
                return l9;
            case 10:
                return l10;
            case 11:
                return l11;
            case 12:
                return l12;
            case 13:
                return l13;
            case 14:
                return l14;
            case 15:
                return l15;
            case 16:
                return l16;
            default:
                return l16;
        }
    }

    private String path;
    private Class<?> classType;

    BalloonSpecificResource(String path, Class<?> classType) {
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
