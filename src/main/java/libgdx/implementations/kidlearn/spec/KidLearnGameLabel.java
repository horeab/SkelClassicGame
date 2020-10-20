package libgdx.implementations.kidlearn.spec;

import libgdx.game.Game;
import libgdx.resources.gamelabel.GameLabelUtils;

public enum KidLearnGameLabel implements libgdx.resources.gamelabel.GameLabel {

    l_math_ord_asc,
    l_math_ord_desc,
    l_math_ord_decimal,
    l_math_seq_add,
    l_math_seq_sub,
    l_eng_findword,
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
