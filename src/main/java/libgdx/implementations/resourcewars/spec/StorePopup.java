package libgdx.implementations.resourcewars.spec;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.popup.MyPopup;
import libgdx.implementations.resourcewars.spec.logic.HealthManager;
import libgdx.implementations.resourcewars.spec.logic.InGameStoreManager;
import libgdx.implementations.resourcewars.spec.logic.WeaponTransactionsManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.enums.OtherType;
import libgdx.implementations.resourcewars.spec.model.enums.WeaponType;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;
import libgdx.utils.model.FontColor;

public class StorePopup extends MyPopup<AbstractScreen, AbstractScreenManager> {


    private final CurrentGame currentGame;

    public StorePopup(CurrentGame currentGame, AbstractScreen abstractScreen) {
        super(abstractScreen);
        this.currentGame = currentGame;
    }

    @Override
    protected void addButtons() {
        for (WeaponType weaponType : WeaponType.values()) {
            boolean btnIsEnabled = true;
            int currentPrice = weaponType.getCurrentPrice(currentGame.getDaysPassed());
            if (currentGame.getMyInventory().getBudget() < currentPrice) {
                btnIsEnabled = false;
            }
            String displayName = weaponType.toString() + " (" + currentPrice + ")";
            String description = "Reduces threat with " + weaponType.getThreatReduce() + "%";
            MyButton myButton = new ButtonBuilder().setFontColor(btnIsEnabled ? FontColor.BLACK : FontColor.GRAY)
                    .setDisabled(!btnIsEnabled).setText(displayName + "\n" + description).build();
            myButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    buyItem(weaponType, null);
                    hide();
                }
            });
            addButton(myButton);
        }
        for (OtherType otherType : OtherType.values()) {
            int currentPrice = otherType.getCurrentPrice(currentGame.getDaysPassed());
            String displayName = otherType.toString() + " (" + otherType.getCurrentPrice(currentGame.getDaysPassed()) + ")";
//            String description = otherType.getDescription();
            String description = otherType.toString();
            boolean btnIsEnabled = true;
            boolean isMaxHealthContainer = otherType == OtherType.HEALTH_INCREASE_50;
            if (currentGame.getMyInventory().getBudget() < currentPrice) {
                btnIsEnabled = false;
            }
            if (isMaxHealthContainer && new InGameStoreManager().isMaxHealthContainerBought()) {
                btnIsEnabled = false;
                description = "alreadyBought";
            }
            MyButton myButton = new ButtonBuilder().setFontColor(btnIsEnabled ? FontColor.BLACK : FontColor.GRAY)
                    .setDisabled(!btnIsEnabled).setText(displayName + "\n" + description).build();
            myButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    buyItem(null, otherType);
                    hide();
                }
            });
            addButton(myButton);
        }
    }

    private void buyItem(WeaponType weaponType, OtherType otherType) {
        if (weaponType != null) {
            WeaponTransactionsManager weaponTransactionsManager = new WeaponTransactionsManager();
            weaponTransactionsManager.buyWeapon(weaponType, currentGame);
        } else if (otherType != null) {
            HealthManager healthManager = new HealthManager(currentGame);
            if (otherType == OtherType.HEALTH_INCREASE_50) {
                healthManager.increaseMaxHealthContainerBought();
                new InGameStoreManager().maxHealthContainerBought();
            } else if (otherType == OtherType.HEALTH_POTION_10) {
                healthManager.increaseHealthBought();
            }
        }
    }


    @Override
    protected String getLabelText() {
        return "Store";
    }
}
