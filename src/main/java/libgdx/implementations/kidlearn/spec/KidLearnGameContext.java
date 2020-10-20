package libgdx.implementations.kidlearn.spec;

public class KidLearnGameContext<T extends Enum & KidLearnLevel> {
    public KidLearnGameContext(T level) {
        this.level = level;
    }

    public int score;
    public T level;
}
