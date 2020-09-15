package libgdx.implementations.applepie.spec;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Arrays;
import java.util.List;

public enum ApplePieRecipe {

    APPLE_PIE(Arrays.asList(
            new MutablePair<>(ApplePieIngredient.APPLE, 1f),
            new MutablePair<>(ApplePieIngredient.BUTTER, 3f)));

    private List<MutablePair<ApplePieIngredient, Float>> applePieIngredients;

    ApplePieRecipe(List<MutablePair<ApplePieIngredient, Float>> applePieIngredients) {
        this.applePieIngredients = applePieIngredients;
    }
}
