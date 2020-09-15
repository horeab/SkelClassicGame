package libgdx.implementations.skelgame;

import libgdx.controls.popup.RatingPopup;
import libgdx.controls.popup.RatingService;
import libgdx.screen.AbstractScreen;

public class SkelGameRatingService extends RatingService {


    public SkelGameRatingService(AbstractScreen abstractScreen) {
        super(abstractScreen);
    }

    @Override
    protected RatingPopup createRatingPopup() {
        return new RatingPopup(getScreen()) {
            @Override
            protected void addExtraButtons() {
            }
        };
    }
}
