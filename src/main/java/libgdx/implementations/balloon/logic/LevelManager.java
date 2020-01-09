package libgdx.implementations.balloon.logic;

import java.util.List;

import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.implementations.balloon.BalloonCampaignLevelEnum;

public class LevelManager {


    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> finishedCampaignLevelStores;

    public LevelManager() {
        finishedCampaignLevelStores = campaignService.getFinishedCampaignLevels();
    }


    public boolean isGameFinished() {
        return finishedCampaignLevelStores.size() == BalloonCampaignLevelEnum.values().length;
    }


    public int lastStageUnlocked() {
        int lastStageUnlocked = 0;
        for (CampaignStoreLevel storeLevel : finishedCampaignLevelStores) {
            BalloonCampaignLevelEnum balloonCampaignLevelEnum = BalloonCampaignLevelEnum.valueOf(storeLevel.getName());
            if (balloonCampaignLevelEnum.getStageNr() > lastStageUnlocked) {
                lastStageUnlocked = balloonCampaignLevelEnum.getStageNr();
            }
        }
        if (BalloonCampaignLevelEnum.getNrOfLevelsForStage(lastStageUnlocked) == getFinishedLevelsForStage(lastStageUnlocked)) {
            lastStageUnlocked++;
        }
        return lastStageUnlocked > BalloonCampaignLevelEnum.getNrOfStages() - 1 ? BalloonCampaignLevelEnum.getNrOfStages() - 1 : lastStageUnlocked;
    }

    public boolean isStageUnlocked(int stageNr) {
        if (stageNr == 0) {
            return true;
        }
        int nrOfLevels = BalloonCampaignLevelEnum.getNrOfLevelsForStage(stageNr);
        int finishedLevelsForStage = getFinishedLevelsForStage(stageNr);
        return nrOfLevels == finishedLevelsForStage;
    }

    private int getFinishedLevelsForStage(int stageNr) {
        int finishedLevelsForStage = 0;
        for (CampaignStoreLevel storeLevel : finishedCampaignLevelStores) {
            if (BalloonCampaignLevelEnum.valueOf(storeLevel.getName()).getStageNr() == stageNr) {
                finishedLevelsForStage++;
            }
        }
        return finishedLevelsForStage;
    }

    public boolean isLevelForStageUnlocked(int stageNr, int levelNr) {
        if (levelNr == 0) {
            return true;
        }
        boolean isLevelForStageUnlocked = false;
        for (CampaignStoreLevel storeLevel : finishedCampaignLevelStores) {
            BalloonCampaignLevelEnum balloonCampaignLevelEnum = BalloonCampaignLevelEnum.valueOf(storeLevel.getName());
            if (balloonCampaignLevelEnum.getStageNr() == stageNr && balloonCampaignLevelEnum.getLevelNr() == levelNr) {
                isLevelForStageUnlocked = true;
            }
        }
        return isLevelForStageUnlocked;
    }
}
