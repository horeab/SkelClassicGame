package libgdx.implementations.kidlearn.spec;

import libgdx.game.Game;
import libgdx.resources.gamelabel.GameLabelUtils;

public enum KidLearnGameLabel implements libgdx.resources.gamelabel.GameLabel {

    l_reset,
    l_verify,
    l_success,

    l_math_title,
    l_math_seq_title,
    l_math_ord_title,
    l_math_ord_asc,
    l_math_ord_desc,
    l_math_ord_decimal,
    l_math_seq_add_nr,
    l_math_seq_sub_nr,
    l_math_seq_add,
    l_math_seq_sub,
    l_math_level,
    l_math_ten,
    l_math_decimal,

    l_eng_title,
    l_eng_words,
    l_eng_verb,
    l_eng_verb_noun,
    l_eng_verb_verb,
    l_eng_hangman,
    l_eng_animals,
    l_eng_fruits,
    l_eng_shapes,
    l_eng_numbers,
    l_eng_school,
    l_eng_sport,
    l_eng_colors,
    l_eng_vegetables,
    l_eng_kitchen,

    l_sci_title,
    l_sci_feed,
    l_sci_recy,
    l_sci_recy_paper,
    l_sci_recy_glass,
    l_sci_recy_plastic,
    l_sci_recy_organic,
    l_sci_state,
    l_sci_state_liquid,
    l_sci_state_solid,
    l_sci_state_gas,
    l_sci_body,
    l_sci_plant,
    l_sci_skel,
    ;

    @Override
    public String getText(Object... params) {
        String language = Game.getInstance().getAppInfoService().getLanguage();
        return GameLabelUtils.getText(getKey(), language, GameLabelUtils.getLabelRes(language).getPath(), params);
    }

    @Override
    public String getKey() {
        return name().toLowerCase();
    }
}
