package libgdx.implementations.math.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.campaign.CampaignLevel;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.screen.AbstractScreen;

public class MathGameScreen extends AbstractScreen<MathScreenManager> {

    public static int TOTAL_QUESTIONS = 5;
    private CampaignLevel campaignLevel;
    private Table allTable;


    @Override
    public void buildStage() {
        new BackButtonBuilder().addHoverBackButton(this);
    }

    @Override
    public void onBackKeyPress() {

    }
}
