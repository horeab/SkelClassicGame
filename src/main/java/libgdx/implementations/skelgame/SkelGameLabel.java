package libgdx.implementations.skelgame;

import libgdx.game.Game;
import libgdx.resources.gamelabel.GameLabelUtils;

public enum SkelGameLabel implements libgdx.resources.gamelabel.GameLabel {

    level_finished,
    level_failed,
    game_finished,
    go_back,
    next_level,
    play_again,
    l_next_day,
    l_remaining_days,
    l_budget,
    l_inventory,
    l_market,
    l_reputation,
    l_unlock,
    l_travel,
    l_empty,
    l_reachbudget,
    l_highscorebudget,
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
