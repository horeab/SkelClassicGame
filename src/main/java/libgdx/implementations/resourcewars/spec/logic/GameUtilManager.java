package libgdx.implementations.resourcewars.spec.logic;

public class GameUtilManager {

	public static final int DAYS_TO_CHANGE = 1;

	public static final int DAYS_TO_PASS_FOR_PRICE_PERCENT_TO_INCREASE = DAYS_TO_CHANGE;
	public static final int DAYS_TO_PASS_FOR_THREAT_FOR_WAPONS_TO_REDUCE = DAYS_TO_CHANGE;

	public final static int STARTING_BUDGET = 5000;

	public static int getPriceBasedOnStartingBudgetWithPercent(float percentOutOfStartingBudget) {
		return (int) ((percentOutOfStartingBudget / 100) * STARTING_BUDGET);
	}

	public static int getPriceBasedOnStartingBudgetWithPercentAndDaysPassed(float percentOutOfStartingBudget,
			int daysPassed) {
		return getPriceBasedOnStartingBudgetWithPercentAndDaysPassed(percentOutOfStartingBudget, daysPassed,
				DAYS_TO_CHANGE);
	}

	public static int getPriceBasedOnStartingBudgetWithPercentAndDaysPassed(float percentOutOfStartingBudget,
			int daysPassed, int daysChange) {
		int price = getPriceBasedOnStartingBudgetWithPercent(percentOutOfStartingBudget);

		int percentToChange = daysPassed / daysChange;
		int priceToIncrease = (int) ((float) STARTING_BUDGET * ((float) percentToChange / 100));

		return price + priceToIncrease;
	}

	public static int incrementAmountForNumberPicker(int amount) {
		if (amount > 10) {
			return amount / 10;
		} else {
			return 1;
		}
	}

	public void gameOver() {
	}

	public void gameFinishedSuccessFully(int daysPassed) {
	}

}
