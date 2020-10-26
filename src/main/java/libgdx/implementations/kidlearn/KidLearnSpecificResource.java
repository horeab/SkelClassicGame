package libgdx.implementations.kidlearn;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    btn_sci_level_up("buttons/btn_sci_level_up.png", Texture.class),
    btn_sci_level_down("buttons/btn_sci_level_down.png", Texture.class),

    //math
    cater_body("math/cater_body.png", Texture.class),
    cater_head("math/cater_head.png", Texture.class),
    cater_tail("math/cater_tail.png", Texture.class),
    cater_unk("math/cater_unk.png", Texture.class),
    level_finished("math/level_finished.png", Texture.class),
    level_not_finished("math/level_not_finished.png", Texture.class),

    //science
    arrow_left("sci/arrow_left.png", Texture.class),
    vertical_unk("sci/vertical_unk.png", Texture.class),
    liquid_container("sci/liquid_container.png", Texture.class),
    solid_container("sci/solid_container.png", Texture.class),
    gas_container("sci/gas_container.png", Texture.class),

    //eng
    btn_hangman_up("buttons/btn_hangman_up.png", Texture.class),
    btn_hangman_down("buttons/btn_hangman_down.png", Texture.class),
    noun_container("eng/noun_container.png", Texture.class),
    verb_container("eng/verb_container.png", Texture.class),
    word_unk("eng/word_unk.png", Texture.class),

    //sounds
    background_music("questions/sound/background.mp3", Music.class),
    apple_sound("questions/sound/apple.mp3", Sound.class),
    background_sound("questions/sound/apple.mp3", Sound.class),
    ball_sound("questions/sound/apple.mp3", Sound.class),
    banana_sound("questions/sound/apple.mp3", Sound.class),
    bear_sound("questions/sound/apple.mp3", Sound.class),
    bicycle_sound("questions/sound/apple.mp3", Sound.class),
    black_sound("questions/sound/apple.mp3", Sound.class),
    blackberry_sound("questions/sound/apple.mp3", Sound.class),
    blue_sound("questions/sound/apple.mp3", Sound.class),
    book_sound("questions/sound/apple.mp3", Sound.class),
    bottle_sound("questions/sound/apple.mp3", Sound.class),
    bow_sound("questions/sound/apple.mp3", Sound.class),
    brown_sound("questions/sound/apple.mp3", Sound.class),
    cabbage_sound("questions/sound/apple.mp3", Sound.class),
    carrot_sound("questions/sound/apple.mp3", Sound.class),
    cat_sound("questions/sound/apple.mp3", Sound.class),
    cherry_sound("questions/sound/apple.mp3", Sound.class),
    circle_sound("questions/sound/apple.mp3", Sound.class),
    coconut_sound("questions/sound/apple.mp3", Sound.class),
    corn_sound("questions/sound/apple.mp3", Sound.class),
    cow_sound("questions/sound/apple.mp3", Sound.class),
    cucumber_sound("questions/sound/apple.mp3", Sound.class),
    cup_sound("questions/sound/apple.mp3", Sound.class),
    diamond_sound("questions/sound/apple.mp3", Sound.class),
    dive_sound("questions/sound/apple.mp3", Sound.class),
    dog_sound("questions/sound/apple.mp3", Sound.class),
    eggplant_sound("questions/sound/apple.mp3", Sound.class),
    eight_sound("questions/sound/apple.mp3", Sound.class),
    elephant_sound("questions/sound/apple.mp3", Sound.class),
    eleven_sound("questions/sound/apple.mp3", Sound.class),
    fifteen_sound("questions/sound/apple.mp3", Sound.class),
    five_sound("questions/sound/apple.mp3", Sound.class),
    fork_sound("questions/sound/apple.mp3", Sound.class),
    four_sound("questions/sound/apple.mp3", Sound.class),
    fourteen_sound("questions/sound/apple.mp3", Sound.class),
    fridge_sound("questions/sound/apple.mp3", Sound.class),
    garlic_sound("questions/sound/apple.mp3", Sound.class),
    geography_sound("questions/sound/apple.mp3", Sound.class),
    giraffe_sound("questions/sound/apple.mp3", Sound.class),
    glass_sound("questions/sound/apple.mp3", Sound.class),
    glasses_sound("questions/sound/apple.mp3", Sound.class),
    grapes_sound("questions/sound/apple.mp3", Sound.class),
    green_sound("questions/sound/apple.mp3", Sound.class),
    grey_sound("questions/sound/apple.mp3", Sound.class),
    heart_sound("questions/sound/apple.mp3", Sound.class),
    hexagon_sound("questions/sound/apple.mp3", Sound.class),
    hippo_sound("questions/sound/apple.mp3", Sound.class),
    horse_sound("questions/sound/apple.mp3", Sound.class),
    jar_sound("questions/sound/apple.mp3", Sound.class),
    jump_sound("questions/sound/apple.mp3", Sound.class),
    kiwi_sound("questions/sound/apple.mp3", Sound.class),
    knife_sound("questions/sound/apple.mp3", Sound.class),
    learn_sound("questions/sound/apple.mp3", Sound.class),
    lemon_sound("questions/sound/apple.mp3", Sound.class),

    human_body("questions/img/human_body.png", Texture.class),

    //questions images
    ice("questions/img/apple.png", Texture.class),
    paper("questions/img/apple.png", Texture.class),
    car("questions/img/apple.png", Texture.class),
    water("questions/img/apple.png", Texture.class),
    rain("questions/img/apple.png", Texture.class),
    tea("questions/img/apple.png", Texture.class),
    milk("questions/img/apple.png", Texture.class),
    steam("questions/img/apple.png", Texture.class),
    cloud("questions/img/apple.png", Texture.class),
    air("questions/img/apple.png", Texture.class),
    water_vapor("questions/img/apple.png", Texture.class),
    apple("questions/img/apple.png", Texture.class),
    acorn("questions/img/apple.png", Texture.class),
    ball("questions/img/apple.png", Texture.class),
    banana("questions/img/apple.png", Texture.class),
    bear("questions/img/apple.png", Texture.class),
    bicycle("questions/img/apple.png", Texture.class),
    black("questions/img/apple.png", Texture.class),
    blackberry("questions/img/apple.png", Texture.class),
    blue("questions/img/apple.png", Texture.class),
    book("questions/img/apple.png", Texture.class),
    bone("questions/img/apple.png", Texture.class),
    bottle("questions/img/apple.png", Texture.class),
    bow("questions/img/apple.png", Texture.class),
    brown("questions/img/apple.png", Texture.class),
    cabbage("questions/img/apple.png", Texture.class),
    carrot("questions/img/apple.png", Texture.class),
    cat("questions/img/apple.png", Texture.class),
    cherry("questions/img/apple.png", Texture.class),
    circle("questions/img/apple.png", Texture.class),
    coconut("questions/img/apple.png", Texture.class),
    corn("questions/img/apple.png", Texture.class),
    cow("questions/img/apple.png", Texture.class),
    cucumber("questions/img/apple.png", Texture.class),
    cup("questions/img/apple.png", Texture.class),
    diamond("questions/img/apple.png", Texture.class),
    dive("questions/img/apple.png", Texture.class),
    dog("questions/img/apple.png", Texture.class),
    dolphin("questions/img/apple.png", Texture.class),
    eggplant("questions/img/apple.png", Texture.class),
    eight("questions/img/apple.png", Texture.class),
    elephant("questions/img/apple.png", Texture.class),
    eleven("questions/img/apple.png", Texture.class),
    fifteen("questions/img/apple.png", Texture.class),
    five("questions/img/apple.png", Texture.class),
    fish("questions/img/apple.png", Texture.class),
    fly("questions/img/apple.png", Texture.class),
    fork("questions/img/apple.png", Texture.class),
    four("questions/img/apple.png", Texture.class),
    fourteen("questions/img/apple.png", Texture.class),
    fridge("questions/img/apple.png", Texture.class),
    frog("questions/img/apple.png", Texture.class),
    garlic("questions/img/apple.png", Texture.class),
    geography("questions/img/apple.png", Texture.class),
    giraffe("questions/img/apple.png", Texture.class),
    glass("questions/img/apple.png", Texture.class),
    glasses("questions/img/apple.png", Texture.class),
    grapes("questions/img/apple.png", Texture.class),
    green("questions/img/apple.png", Texture.class),
    grey("questions/img/apple.png", Texture.class),
    heart("questions/img/apple.png", Texture.class),
    hexagon("questions/img/apple.png", Texture.class),
    hippo("questions/img/apple.png", Texture.class),
    horse("questions/img/apple.png", Texture.class),
    jar("questions/img/apple.png", Texture.class),
    jump("questions/img/apple.png", Texture.class),
    kiwi("questions/img/apple.png", Texture.class),
    knife("questions/img/apple.png", Texture.class),
    learn("questions/img/apple.png", Texture.class),
    leaves("questions/img/apple.png", Texture.class),
    lemon("questions/img/apple.png", Texture.class),
    lion("questions/img/apple.png", Texture.class),
    listen("questions/img/apple.png", Texture.class),
    mango("questions/img/apple.png", Texture.class),
    math("questions/img/apple.png", Texture.class),
    meat("questions/img/apple.png", Texture.class),
    monkey("questions/img/apple.png", Texture.class),
    mouse("questions/img/apple.png", Texture.class),
    mushroom("questions/img/apple.png", Texture.class),
    napkin("questions/img/apple.png", Texture.class),
    nine("questions/img/apple.png", Texture.class),
    notebook("questions/img/apple.png", Texture.class),
    octagon("questions/img/apple.png", Texture.class),
    one("questions/img/apple.png", Texture.class),
    onion("questions/img/apple.png", Texture.class),
    orange("questions/img/apple.png", Texture.class),
    oval("questions/img/apple.png", Texture.class),
    peach("questions/img/apple.png", Texture.class),
    pear("questions/img/apple.png", Texture.class),
    pen("questions/img/apple.png", Texture.class),
    pencil("questions/img/apple.png", Texture.class),
    penguin("questions/img/apple.png", Texture.class),
    pentagon("questions/img/apple.png", Texture.class),
    pink("questions/img/apple.png", Texture.class),
    play("questions/img/apple.png", Texture.class),
    plum("questions/img/apple.png", Texture.class),
    potato("questions/img/apple.png", Texture.class),
    pumpkin("questions/img/apple.png", Texture.class),
    purple("questions/img/apple.png", Texture.class),
    rabbit("questions/img/apple.png", Texture.class),
    read("questions/img/apple.png", Texture.class),
    rectangle("questions/img/apple.png", Texture.class),
    red("questions/img/apple.png", Texture.class),
    rhino("questions/img/apple.png", Texture.class),
    ride("questions/img/apple.png", Texture.class),
    run("questions/img/apple.png", Texture.class),
    seeds("questions/img/apple.png", Texture.class),
    seven("questions/img/apple.png", Texture.class),
    shoes("questions/img/apple.png", Texture.class),
    sing("questions/img/apple.png", Texture.class),
    six("questions/img/apple.png", Texture.class),
    snake("questions/img/apple.png", Texture.class),
    snowboard("questions/img/apple.png", Texture.class),
    spoon("questions/img/apple.png", Texture.class),
    square("questions/img/apple.png", Texture.class),
    squirrel("questions/img/apple.png", Texture.class),
    star("questions/img/apple.png", Texture.class),
    strawberry("questions/img/apple.png", Texture.class),
    swim("questions/img/apple.png", Texture.class),
    talk("questions/img/apple.png", Texture.class),
    teapot("questions/img/apple.png", Texture.class),
    ten("questions/img/apple.png", Texture.class),
    thirteen("questions/img/apple.png", Texture.class),
    three("questions/img/apple.png", Texture.class),
    tiger("questions/img/apple.png", Texture.class),
    tomato("questions/img/apple.png", Texture.class),
    tortoise("questions/img/apple.png", Texture.class),
    triangle("questions/img/apple.png", Texture.class),
    twelve("questions/img/apple.png", Texture.class),
    two("questions/img/apple.png", Texture.class),
    watermelon("questions/img/apple.png", Texture.class),
    white("questions/img/apple.png", Texture.class),
    wolf("questions/img/apple.png", Texture.class),
    write("questions/img/apple.png", Texture.class),
    yellow("questions/img/apple.png", Texture.class),

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
