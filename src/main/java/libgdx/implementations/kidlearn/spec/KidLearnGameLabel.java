package libgdx.implementations.kidlearn.spec;

import libgdx.game.Game;
import libgdx.resources.gamelabel.GameLabelUtils;

public enum KidLearnGameLabel implements libgdx.resources.gamelabel.GameLabel {

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

    l_sci_title,
    l_sci_feed,
    l_sci_recy,
    l_sci_state,
    l_sci_body,
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
