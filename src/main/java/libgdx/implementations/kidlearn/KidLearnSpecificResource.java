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
    btn_main_menu_math_up("buttons/btn_math_up.png", Texture.class),
    btn_main_menu_math_down("buttons/btn_math_down.png", Texture.class),
    btn_main_menu_eng_up("buttons/btn_eng_up.png", Texture.class),
    btn_main_menu_eng_down("buttons/btn_eng_down.png", Texture.class),
    btn_main_menu_sci_up("buttons/btn_sci_up.png", Texture.class),
    btn_main_menu_sci_down("buttons/btn_sci_down.png", Texture.class),


    background_texture_diff0("background/background_texture_diff0.png", Texture.class),
    background_texture_diff1("background/background_texture_diff1.png", Texture.class),
    background_texture_diff2("background/background_texture_diff2.png", Texture.class),
    scroll_background_math("background/scroll_background_math.png", Texture.class),

    //math
    btn_math_ord_level_up("buttons/btn_math_ord_level_up.png", Texture.class),
    btn_math_ord_level_down("buttons/btn_math_ord_level_down.png", Texture.class),
    btn_math_seq_level_up("buttons/btn_math_seq_level_up.png", Texture.class),
    btn_math_seq_level_down("buttons/btn_math_seq_level_down.png", Texture.class),
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
    plastic_container("sci/plastic_container.png", Texture.class),
    glass_container("sci/glass_container.png", Texture.class),
    organic_container("sci/organic_container.png", Texture.class),
    paper_container("sci/paper_container.png", Texture.class),
    human_body("questions/img/apple.png", Texture.class),
    plant_parts("questions/img/apple.png", Texture.class),
    human_skeleton("questions/img/apple.png", Texture.class),
    categ_human_body("sci/arrow_left.png", Texture.class),
    categ_plant_parts("sci/arrow_left.png", Texture.class),
    categ_human_skeleton("sci/arrow_left.png", Texture.class),
    categ_feed1("sci/arrow_left.png", Texture.class),
    categ_feed2("sci/arrow_left.png", Texture.class),
    categ_recy1("sci/arrow_left.png", Texture.class),
    categ_recy2("sci/arrow_left.png", Texture.class),
    categ_state("sci/arrow_left.png", Texture.class),

    //eng
    btn_eng_animals_up("buttons/btn_hangman_up.png", Texture.class),
    btn_eng_animals_down("buttons/btn_hangman_down.png", Texture.class),
    btn_eng_fruits_up("buttons/btn_hangman_up.png", Texture.class),
    btn_eng_fruits_down("buttons/btn_hangman_down.png", Texture.class),
    btn_eng_vegetables_up("buttons/btn_hangman_up.png", Texture.class),
    btn_eng_vegetables_down("buttons/btn_hangman_down.png", Texture.class),
    btn_eng_numbers_up("buttons/btn_hangman_up.png", Texture.class),
    btn_eng_numbers_down("buttons/btn_hangman_down.png", Texture.class),
    btn_eng_colors_up("buttons/btn_hangman_up.png", Texture.class),
    btn_eng_colors_down("buttons/btn_hangman_down.png", Texture.class),
    btn_eng_school_up("buttons/btn_hangman_up.png", Texture.class),
    btn_eng_school_down("buttons/btn_hangman_down.png", Texture.class),
    btn_eng_shapes_up("buttons/btn_hangman_up.png", Texture.class),
    btn_eng_shapes_down("buttons/btn_hangman_down.png", Texture.class),
    btn_eng_kitchen_up("buttons/btn_hangman_up.png", Texture.class),
    btn_eng_kitchen_down("buttons/btn_hangman_down.png", Texture.class),
    btn_eng_sport_up("buttons/btn_hangman_up.png", Texture.class),
    btn_eng_sport_down("buttons/btn_hangman_down.png", Texture.class),
    btn_hangman_up("buttons/btn_hangman_up.png", Texture.class),
    btn_hangman_down("buttons/btn_hangman_down.png", Texture.class),
    noun_container("eng/noun_container.png", Texture.class),
    verb_container("eng/verb_container.png", Texture.class),
    word_unk("eng/word_unk.png", Texture.class),

    background_music("questions/sound/background.mp3", Music.class),
    level_fail("questions/sound/level_fail.mp3", Sound.class),
    level_success("questions/sound/level_success.mp3", Sound.class),
    //sounds,
    apple_sound("questions/sound/apple.mp3", Sound.class),
    ball_sound("questions/sound/ball.mp3", Sound.class),
    banana_sound("questions/sound/banana.mp3", Sound.class),
    bear_sound("questions/sound/bear.mp3", Sound.class),
    bicycle_sound("questions/sound/bicycle.mp3", Sound.class),
    black_sound("questions/sound/black.mp3", Sound.class),
    blackberry_sound("questions/sound/blackberry.mp3", Sound.class),
    blue_sound("questions/sound/blue.mp3", Sound.class),
    book_sound("questions/sound/book.mp3", Sound.class),
    bottle_sound("questions/sound/bottle.mp3", Sound.class),
    bow_sound("questions/sound/bow.mp3", Sound.class),
    brown_sound("questions/sound/brown.mp3", Sound.class),
    cabbage_sound("questions/sound/cabbage.mp3", Sound.class),
    carrot_sound("questions/sound/carrot.mp3", Sound.class),
    cat_sound("questions/sound/cat.mp3", Sound.class),
    cherry_sound("questions/sound/cherry.mp3", Sound.class),
    circle_sound("questions/sound/circle.mp3", Sound.class),
    coconut_sound("questions/sound/coconut.mp3", Sound.class),
    corn_sound("questions/sound/corn.mp3", Sound.class),
    cow_sound("questions/sound/cow.mp3", Sound.class),
    cucumber_sound("questions/sound/cucumber.mp3", Sound.class),
    cup_sound("questions/sound/cup.mp3", Sound.class),
    diamond_sound("questions/sound/diamond.mp3", Sound.class),
    dive_sound("questions/sound/dive.mp3", Sound.class),
    dog_sound("questions/sound/dog.mp3", Sound.class),
    eggplant_sound("questions/sound/eggplant.mp3", Sound.class),
    eight_sound("questions/sound/eight.mp3", Sound.class),
    elephant_sound("questions/sound/elephant.mp3", Sound.class),
    eleven_sound("questions/sound/eleven.mp3", Sound.class),
    fifteen_sound("questions/sound/fifteen.mp3", Sound.class),
    five_sound("questions/sound/five.mp3", Sound.class),
    fork_sound("questions/sound/fork.mp3", Sound.class),
    four_sound("questions/sound/four.mp3", Sound.class),
    fourteen_sound("questions/sound/fourteen.mp3", Sound.class),
    fridge_sound("questions/sound/fridge.mp3", Sound.class),
    garlic_sound("questions/sound/garlic.mp3", Sound.class),
    geography_sound("questions/sound/geography.mp3", Sound.class),
    giraffe_sound("questions/sound/giraffe.mp3", Sound.class),
    glass_sound("questions/sound/glass.mp3", Sound.class),
    glasses_sound("questions/sound/glasses.mp3", Sound.class),
    grapes_sound("questions/sound/grapes.mp3", Sound.class),
    green_sound("questions/sound/green.mp3", Sound.class),
    grey_sound("questions/sound/grey.mp3", Sound.class),
    heart_sound("questions/sound/heart.mp3", Sound.class),
    hexagon_sound("questions/sound/hexagon.mp3", Sound.class),
    hippo_sound("questions/sound/hippo.mp3", Sound.class),
    horse_sound("questions/sound/horse.mp3", Sound.class),
    jar_sound("questions/sound/jar.mp3", Sound.class),
    jump_sound("questions/sound/jump.mp3", Sound.class),
    kiwi_sound("questions/sound/kiwi.mp3", Sound.class),
    knife_sound("questions/sound/knife.mp3", Sound.class),
    learn_sound("questions/sound/learn.mp3", Sound.class),
    lemon_sound("questions/sound/lemon.mp3", Sound.class),
    lion_sound("questions/sound/lion.mp3", Sound.class),
    listen_sound("questions/sound/listen.mp3", Sound.class),
    mango_sound("questions/sound/mango.mp3", Sound.class),
    math_sound("questions/sound/math.mp3", Sound.class),
    monkey_sound("questions/sound/monkey.mp3", Sound.class),
    mushroom_sound("questions/sound/mushroom.mp3", Sound.class),
    napkin_sound("questions/sound/napkin.mp3", Sound.class),
    nine_sound("questions/sound/nine.mp3", Sound.class),
    notebook_sound("questions/sound/notebook.mp3", Sound.class),
    octagon_sound("questions/sound/octagon.mp3", Sound.class),
    one_sound("questions/sound/one.mp3", Sound.class),
    onion_sound("questions/sound/onion.mp3", Sound.class),
    orange_sound("questions/sound/orange.mp3", Sound.class),
    oval_sound("questions/sound/oval.mp3", Sound.class),
    peach_sound("questions/sound/peach.mp3", Sound.class),
    pear_sound("questions/sound/pear.mp3", Sound.class),
    pen_sound("questions/sound/pen.mp3", Sound.class),
    pencil_sound("questions/sound/pencil.mp3", Sound.class),
    pentagon_sound("questions/sound/pentagon.mp3", Sound.class),
    pink_sound("questions/sound/pink.mp3", Sound.class),
    play_sound("questions/sound/play.mp3", Sound.class),
    plum_sound("questions/sound/plum.mp3", Sound.class),
    potato_sound("questions/sound/potato.mp3", Sound.class),
    pumpkin_sound("questions/sound/pumpkin.mp3", Sound.class),
    purple_sound("questions/sound/purple.mp3", Sound.class),
    read_sound("questions/sound/read.mp3", Sound.class),
    rectangle_sound("questions/sound/rectangle.mp3", Sound.class),
    red_sound("questions/sound/red.mp3", Sound.class),
    rhino_sound("questions/sound/rhino.mp3", Sound.class),
    ride_sound("questions/sound/ride.mp3", Sound.class),
    run_sound("questions/sound/run.mp3", Sound.class),
    seven_sound("questions/sound/seven.mp3", Sound.class),
    shoes_sound("questions/sound/shoes.mp3", Sound.class),
    sing_sound("questions/sound/sing.mp3", Sound.class),
    six_sound("questions/sound/six.mp3", Sound.class),
    snake_sound("questions/sound/snake.mp3", Sound.class),
    snowboard_sound("questions/sound/snowboard.mp3", Sound.class),
    spoon_sound("questions/sound/spoon.mp3", Sound.class),
    square_sound("questions/sound/square.mp3", Sound.class),
    star_sound("questions/sound/star.mp3", Sound.class),
    strawberry_sound("questions/sound/strawberry.mp3", Sound.class),
    swim_sound("questions/sound/swim.mp3", Sound.class),
    talk_sound("questions/sound/talk.mp3", Sound.class),
    teapot_sound("questions/sound/teapot.mp3", Sound.class),
    ten_sound("questions/sound/ten.mp3", Sound.class),
    thirteen_sound("questions/sound/thirteen.mp3", Sound.class),
    three_sound("questions/sound/three.mp3", Sound.class),
    tiger_sound("questions/sound/tiger.mp3", Sound.class),
    tomato_sound("questions/sound/tomato.mp3", Sound.class),
    tortoise_sound("questions/sound/tortoise.mp3", Sound.class),
    triangle_sound("questions/sound/triangle.mp3", Sound.class),
    twelve_sound("questions/sound/twelve.mp3", Sound.class),
    two_sound("questions/sound/two.mp3", Sound.class),
    watermelon_sound("questions/sound/watermelon.mp3", Sound.class),
    white_sound("questions/sound/white.mp3", Sound.class),
    wolf_sound("questions/sound/wolf.mp3", Sound.class),
    write_sound("questions/sound/write.mp3", Sound.class),
    yellow_sound("questions/sound/yellow.mp3", Sound.class),

    //questions images
    newspaper("questions/img/newspaper.png", Texture.class),
    milk_bottle("questions/img/milk_bottle.png", Texture.class),
    plastic_bag("questions/img/plastic_bag.png", Texture.class),
    ice("questions/img/ice.png", Texture.class),
    paper("questions/img/paper.png", Texture.class),
    car("questions/img/car.png", Texture.class),
    water("questions/img/water.png", Texture.class),
    rain("questions/img/rain.png", Texture.class),
    tea("questions/img/tea.png", Texture.class),
    milk("questions/img/milk.png", Texture.class),
    steam("questions/img/steam.png", Texture.class),
    cloud("questions/img/cloud.png", Texture.class),
    air("questions/img/air.png", Texture.class),
    apple("questions/img/apple.png", Texture.class),
    acorn("questions/img/acorn.png", Texture.class),
    ball("questions/img/ball.png", Texture.class),
    banana("questions/img/banana.png", Texture.class),
    bear("questions/img/bear.png", Texture.class),
    bicycle("questions/img/bicycle.png", Texture.class),
    black("questions/img/black.png", Texture.class),
    blackberry("questions/img/blackberry.png", Texture.class),
    blue("questions/img/blue.png", Texture.class),
    book("questions/img/book.png", Texture.class),
    bone("questions/img/bone.png", Texture.class),
    bottle("questions/img/bottle.png", Texture.class),
    bow("questions/img/bow.png", Texture.class),
    brown("questions/img/brown.png", Texture.class),
    cabbage("questions/img/cabbage.png", Texture.class),
    carrot("questions/img/carrot.png", Texture.class),
    cat("questions/img/cat.png", Texture.class),
    cherry("questions/img/cherry.png", Texture.class),
    circle("questions/img/circle.png", Texture.class),
    coconut("questions/img/coconut.png", Texture.class),
    corn("questions/img/corn.png", Texture.class),
    cow("questions/img/cow.png", Texture.class),
    cucumber("questions/img/cucumber.png", Texture.class),
    cup("questions/img/cup.png", Texture.class),
    diamond("questions/img/diamond.png", Texture.class),
    dive("questions/img/dive.png", Texture.class),
    dog("questions/img/dog.png", Texture.class),
    dolphin("questions/img/dolphin.png", Texture.class),
    eggplant("questions/img/eggplant.png", Texture.class),
    eight("questions/img/eight.png", Texture.class),
    elephant("questions/img/elephant.png", Texture.class),
    eleven("questions/img/apple.png", Texture.class),//////
    fifteen("questions/img/apple.png", Texture.class),/////
    five("questions/img/five.png", Texture.class),
    fish("questions/img/fish.png", Texture.class),
    mosquito("questions/img/mosquito.png", Texture.class),
    fork("questions/img/fork.png", Texture.class),
    four("questions/img/four.png", Texture.class),
    fourteen("questions/img/apple.png", Texture.class),////
    banana_peel("questions/img/banana_peel.png", Texture.class),
    fridge("questions/img/fridge.png", Texture.class),
    frog("questions/img/frog.png", Texture.class),
    garlic("questions/img/garlic.png", Texture.class),
    geography("questions/img/geography.png", Texture.class),
    giraffe("questions/img/giraffe.png", Texture.class),
    glass("questions/img/glass.png", Texture.class),
    glasses("questions/img/glasses.png", Texture.class),
    grapes("questions/img/grapes.png", Texture.class),
    green("questions/img/green.png", Texture.class),
    grey("questions/img/grey.png", Texture.class),
    heart("questions/img/heart.png", Texture.class),
    hexagon("questions/img/hexagon.png", Texture.class),
    hippo("questions/img/hippo.png", Texture.class),
    horse("questions/img/horse.png", Texture.class),
    jar("questions/img/jar.png", Texture.class),
    jump("questions/img/jump.png", Texture.class),
    kiwi("questions/img/kiwi.png", Texture.class),
    knife("questions/img/knife.png", Texture.class),
    learn("questions/img/learn.png", Texture.class),
    leaves("questions/img/leaves.png", Texture.class),
    lemon("questions/img/lemon.png", Texture.class),
    lion("questions/img/lion.png", Texture.class),
    listen("questions/img/listen.png", Texture.class),
    mango("questions/img/mango.png", Texture.class),
    math("questions/img/math.png", Texture.class),
    meat("questions/img/meat.png", Texture.class),
    monkey("questions/img/monkey.png", Texture.class),
    mouse("questions/img/mouse.png", Texture.class),
    mushroom("questions/img/mushroom.png", Texture.class),
    napkin("questions/img/napkin.png", Texture.class),
    nine("questions/img/nine.png", Texture.class),
    notebook("questions/img/notebook.png", Texture.class),
    octagon("questions/img/octagon.png", Texture.class),
    one("questions/img/one.png", Texture.class),
    onion("questions/img/onion.png", Texture.class),
    orange("questions/img/orange.png", Texture.class),
    oval("questions/img/oval.png", Texture.class),
    peach("questions/img/peach.png", Texture.class),
    pear("questions/img/pear.png", Texture.class),
    pen("questions/img/pen.png", Texture.class),
    pencil("questions/img/pencil.png", Texture.class),
    penguin("questions/img/penguin.png", Texture.class),
    pentagon("questions/img/pentagon.png", Texture.class),
    pink("questions/img/pink.png", Texture.class),
    play("questions/img/play.png", Texture.class),
    plum("questions/img/plum.png", Texture.class),
    potato("questions/img/potato.png", Texture.class),
    pumpkin("questions/img/pumpkin.png", Texture.class),
    purple("questions/img/purple.png", Texture.class),
    rabbit("questions/img/rabbit.png", Texture.class),
    read("questions/img/read.png", Texture.class),
    rectangle("questions/img/rectangle.png", Texture.class),
    red("questions/img/red.png", Texture.class),
    rhino("questions/img/rhino.png", Texture.class),
    ride("questions/img/ride.png", Texture.class),
    run("questions/img/run.png", Texture.class),
    seeds("questions/img/seeds.png", Texture.class),
    seven("questions/img/seven.png", Texture.class),
    shoes("questions/img/shoes.png", Texture.class),
    sing("questions/img/sing.png", Texture.class),
    six("questions/img/six.png", Texture.class),
    snake("questions/img/snake.png", Texture.class),
    snowboard("questions/img/snowboard.png", Texture.class),
    spoon("questions/img/spoon.png", Texture.class),
    square("questions/img/square.png", Texture.class),
    squirrel("questions/img/squirrel.png", Texture.class),
    star("questions/img/star.png", Texture.class),
    strawberry("questions/img/strawberry.png", Texture.class),
    swim("questions/img/swim.png", Texture.class),
    talk("questions/img/talk.png", Texture.class),
    teapot("questions/img/teapot.png", Texture.class),
    ten("questions/img/apple.png", Texture.class),///
    thirteen("questions/img/apple.png", Texture.class),///
    three("questions/img/three.png", Texture.class),
    tiger("questions/img/tiger.png", Texture.class),
    tomato("questions/img/tomato.png", Texture.class),
    tortoise("questions/img/tortoise.png", Texture.class),
    triangle("questions/img/triangle.png", Texture.class),
    twelve("questions/img/apple.png", Texture.class),///
    two("questions/img/two.png", Texture.class),
    watermelon("questions/img/watermelon.png", Texture.class),
    white("questions/img/white.png", Texture.class),
    wolf("questions/img/wolf.png", Texture.class),
    write("questions/img/write.png", Texture.class),
    yellow("questions/img/yellow.png", Texture.class),
    shark("questions/img/shark.png", Texture.class),
    bee("questions/img/bee.png", Texture.class),
    flower_pollen("questions/img/flower_pollen.png", Texture.class),
    antelope("questions/img/antelope.png", Texture.class),
    grass("questions/img/grass.png", Texture.class),
    panda("questions/img/panda.png", Texture.class),
    bamboo("questions/img/bamboo.png", Texture.class),
    koala("questions/img/koala.png", Texture.class),
    eucalyptus("questions/img/eucalyptus.png", Texture.class),
    polar_bear("questions/img/polar_bear.png", Texture.class),
    chicken("questions/img/chicken.png", Texture.class),
    earthworm("questions/img/earthworm.png", Texture.class),
    cheetah("questions/img/cheetah.png", Texture.class),
    reindeer("questions/img/reindeer.png", Texture.class),
    broken_mug("questions/img/broken_mug.png", Texture.class),
    juice_bottle("questions/img/juice_bottle.png", Texture.class),
    fruit_scraps("questions/img/fruit_scraps.png", Texture.class),
    milk_box("questions/img/milk_box.png", Texture.class),
    broken_glass("questions/img/broken_glass.png", Texture.class),
    shampoo_bottle("questions/img/shampoo_bottle.png", Texture.class),
    ketchup_bottle("questions/img/ketchup_bottle.png", Texture.class),
    food_scraps("questions/img/food_scraps.png", Texture.class),
    natural_gas("questions/img/natural_gas.png", Texture.class),

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
