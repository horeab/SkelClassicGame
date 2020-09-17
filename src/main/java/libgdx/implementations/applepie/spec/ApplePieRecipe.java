package libgdx.implementations.applepie.spec;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Arrays;
import java.util.List;

public enum ApplePieRecipe {

    APPLE_PIE(Arrays.asList(
            new MutablePair<>(ApplePieIngredient.APPLE, 13)
            ,
            new MutablePair<>(ApplePieIngredient.CINNAMON, 3)
            ,
            new MutablePair<>(ApplePieIngredient.SUGAR, 2)
            ,
            new MutablePair<>(ApplePieIngredient.BUTTER, 1)
            ,
            new MutablePair<>(ApplePieIngredient.PIE_CRUST, 1)
    )
    );

    private List<MutablePair<ApplePieIngredient, Integer>> applePieIngredients;

    ApplePieRecipe(List<MutablePair<ApplePieIngredient, Integer>> applePieIngredients) {
        this.applePieIngredients = applePieIngredients;
    }

    public List<MutablePair<ApplePieIngredient, Integer>> getApplePieIngredients() {
        return applePieIngredients;
    }
}
