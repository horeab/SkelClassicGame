package libgdx.implementations.kidlearn.spec;

public class KidLearnGameContext<T extends Enum> {
    public KidLearnGameContext(T level) {
        this.level = level;
    }

    public int score;
    public T level;
}
