package libgdx.implementations.resourcewars.spec.logic;

public class GameUtilManager {

    public static final int DAYS_TO_CHANGE = 2;

    public final static int STARTING_BUDGET = 5000;

    public static int getPriceBasedOnStartingBudgetWithPercent(float percentOutOfStartingBudget) {
        return (int) ((percentOutOfStartingBudget / 100) * STARTING_BUDGET);
    }

    public static int getPriceBasedOnStartingBudgetWithPercentAndDaysPassed(float percentOutOfStartingBudget,
                                                                            int daysPassed) {
        return getPriceBasedOnStartingBudgetWithPercentAndDaysPassed(percentOutOfStartingBudget, daysPassed,
                DAYS_TO_CHANGE, STARTING_BUDGET);
    }

    public static int getPriceBasedOnStartingBudgetWithPercentAndDaysPassed(float percentOutOfStartingBudget,
                                                                            int daysPassed, int daysChange, int budget) {
        int price = getPriceBasedOnStartingBudgetWithPercent(percentOutOfStartingBudget);

        int percentToChange = daysPassed / daysChange;
        int priceToIncrease = (int) ((float) budget * ((float) percentToChange / 100));

        return price + priceToIncrease;
    }

}
