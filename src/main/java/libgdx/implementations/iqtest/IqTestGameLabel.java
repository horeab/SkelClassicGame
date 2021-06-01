package libgdx.implementations.iqtest;

import libgdx.game.Game;
import libgdx.resources.gamelabel.GameLabelUtils;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;

public enum IqTestGameLabel implements libgdx.resources.gamelabel.GameLabel {


    new_game,
    skip,
    answerchoice,
    finalscoreexplanation,
    verylow,
    low,
    normal,
    high,
    veryhigh,
    gametype_iqtest,
    gametype_numseq,
    gametype_memnum,
    gametype_memnum_descr,
    gametype_space,
    gametype_space_descr,
    ;

    @Override
    public String getText(Object... params) {
        String language = Game.getInstance().getAppInfoService().getLanguage();
        return GameLabelUtils.getText(getKey(), language, SpecificPropertiesUtils.getLabelFilePath(), params);
    }

    @Override
    public String getKey() {
        return name().toLowerCase();
    }
}
