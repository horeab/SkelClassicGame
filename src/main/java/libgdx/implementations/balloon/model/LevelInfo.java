package libgdx.implementations.balloon.model;

import java.util.Random;

import libgdx.implementations.balloon.BalloonCampaignLevelEnum;

public class LevelInfo {

    private boolean isMultiplayer;
    private int nrRowsForMultiplayer;
    private int nrColsForMultiplayer;

    private BalloonCampaignLevelEnum levelEnum;

    public LevelInfo(boolean isMultiplayer, BalloonCampaignLevelEnum levelEnum) {
        this.isMultiplayer = isMultiplayer;
        this.levelEnum = levelEnum;
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public void setMultiplayer(boolean isMultiplayer) {
        this.isMultiplayer = isMultiplayer;
    }

    public int getNrRowsForMultiplayer() {
        return nrRowsForMultiplayer;
    }

    public void setNrRowsForMultiplayer(int nrRowsForMultiplayer) {
        this.nrRowsForMultiplayer = nrRowsForMultiplayer;
    }

    public int getNrColsForMultiplayer() {
        return nrColsForMultiplayer;
    }

    public void setNrColsForMultiplayer(int nrColsForMultiplayer) {
        this.nrColsForMultiplayer = nrColsForMultiplayer;
    }

    public BalloonCampaignLevelEnum getLevelEnum() {
        return levelEnum;
    }

    public void setLevelEnum(BalloonCampaignLevelEnum levelEnum) {
        this.levelEnum = levelEnum;
    }

    public boolean isOnePlayerLevel() {
        if (!isMultiplayer) {
            return levelEnum.isOnePlayerLevel();
        } else {
            return false;
        }
    }

    public boolean isEasyLevel() {
        if (!isMultiplayer) {
            return levelEnum.isEasyLevel();
        } else {
            return false;
        }
    }

    public boolean decideWhatPlayerStarts() {
        if (isMultiplayer || levelEnum.getPlayer1Starts() == null) {
            return new Random().nextBoolean();
        } else {
            return levelEnum.getPlayer1Starts();
        }
    }

    public boolean isPlayer2Computer() {
        return !isMultiplayer;
    }

    public Integer getNrOfRowsForMatrix() {
        if (isMultiplayer) {
            return nrRowsForMultiplayer;
        }
        return levelEnum.getNrOfRows();
    }

    public Integer getNrOfColumnsForMatrix() {
        if (isMultiplayer) {
            return nrColsForMultiplayer;
        }
        return levelEnum.getNrOfColumns();
    }

}
