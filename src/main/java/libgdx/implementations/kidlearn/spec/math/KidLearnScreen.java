package libgdx.implementations.kidlearn.spec.math;

public class KidLearnScreen {

    public Object[] params;
    public ScreenType screenType;

    public KidLearnScreen(ScreenType screenType, Object... params) {
        this.params = params;
        this.screenType = screenType;
    }

    public enum ScreenType {
        CHOOSE_LEVEL,
        LEVEL,
        GAME
    }
}
