package libgdx.campaign;

import java.util.ArrayList;
import java.util.List;

import libgdx.utils.EnumUtils;

public class CampaignService {

    private CampaignStoreService campaignStoreService = new CampaignStoreService();

    public List<CampaignStoreLevel> processAndGetAllLevels() {
        List<CampaignStoreLevel> allPlayedLevels = getFinishedCampaignLevels();
        CampaignLevel[] values = (CampaignLevel[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum());
        if (getCampaignLevel(values[0].getIndex(), allPlayedLevels) == null) {
            campaignStoreService.createCampaignLevel(values[0]);
            allPlayedLevels.add(new CampaignStoreLevel(values[0]));
        } else {
            //In case new levels were added and the user already finished the libgdx.game
            CampaignStoreLevel maxFinishedLevel = getMaxFinishedLevel(allPlayedLevels);
            if (maxFinishedLevel != null && maxFinishedLevel.getLevel() < values.length - 1
                    &&
                    getCampaignLevel(maxFinishedLevel.getLevel() + 1, allPlayedLevels) == null) {
                levelFinished(0, getCampaignLevelEnum(maxFinishedLevel.getLevel()));
                CampaignLevel campaignLevelEnum = getCampaignLevelEnum(maxFinishedLevel.getLevel() + 1);
                if (campaignLevelEnum != null) {
                    allPlayedLevels.add(new CampaignStoreLevel(campaignLevelEnum));
                }
            }
        }
        return allPlayedLevels;
    }

    public List<CampaignStoreLevel> getFinishedCampaignLevels() {
        return campaignStoreService.getAllCampaignLevels();
    }

    public int getTotalWonStars(List<CampaignStoreLevel> list) {
        int total = 0;
        for (CampaignStoreLevel level : list) {
            total = total + level.getStarsWon();
        }
        return total;
    }

    public void levelFinished(int starsWon, CampaignLevel level) {
        Integer storeCrossWordLevel = getCrosswordLevel(level);
        campaignStoreService.updateLevel(level);
        campaignStoreService.updateStatus(level, CampaignLevelStatusEnum.FINISHED);
        if (storeCrossWordLevel == -1 || starsWon > campaignStoreService.getStarsWon(level)) {
            campaignStoreService.updateStarsWon(level, starsWon);
        }
        List<Integer> finishedLevels = new ArrayList<>();
        for (CampaignStoreLevel storeLevel:getFinishedCampaignLevels()){
            finishedLevels.add(storeLevel.getLevel());
        }
        CampaignLevel nextLevel = CampaignLevelEnumService.getNextLevel(level);
        if (!finishedLevels.contains(nextLevel.getIndex())) {
            campaignStoreService.createCampaignLevel(nextLevel);
        }
    }

    public Integer getCrosswordLevel(CampaignLevel level) {
        return campaignStoreService.getCrosswordLevel(level);
    }

    public CampaignStoreLevel getMaxOpenedLevel(List<CampaignStoreLevel> allPlayedLevels) {
        CampaignStoreLevel maxFinishedLevel = null;
        for (CampaignStoreLevel campaignStoreLevel : allPlayedLevels) {
            if (campaignStoreLevel.getStatus() == CampaignLevelStatusEnum.IN_PROGRESS.getStatus() && (maxFinishedLevel == null || maxFinishedLevel.getLevel() < campaignStoreLevel.getLevel())) {
                maxFinishedLevel = campaignStoreLevel;
            }
        }
        return maxFinishedLevel;
    }

    public CampaignStoreLevel getMaxFinishedLevel(List<CampaignStoreLevel> allPlayedLevels) {
        CampaignStoreLevel maxFinishedLevel = null;
        for (CampaignStoreLevel campaignStoreLevel : allPlayedLevels) {
            if (campaignStoreLevel.getStatus() == CampaignLevelStatusEnum.FINISHED.getStatus() && (maxFinishedLevel == null || maxFinishedLevel.getLevel() < campaignStoreLevel.getLevel())) {
                maxFinishedLevel = campaignStoreLevel;
            }
        }
        return maxFinishedLevel;
    }

    private CampaignLevel getCampaignLevelEnum(int level) {
        CampaignLevel[] values = (CampaignLevel[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum());
        for (CampaignLevel campaignLevelEnum : values) {
            if (campaignLevelEnum.getIndex() == level) {
                return campaignLevelEnum;
            }
        }
        return null;
    }


    public CampaignStoreLevel getCampaignLevel(int level, List<CampaignStoreLevel> allPlayedLevels) {
        for (CampaignStoreLevel campaignStoreLevel : allPlayedLevels) {
            if (campaignStoreLevel.getLevel() == level) {
                return campaignStoreLevel;
            }
        }
        return null;
    }
}
