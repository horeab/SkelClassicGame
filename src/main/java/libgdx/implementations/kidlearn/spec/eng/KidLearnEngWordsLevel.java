package libgdx.implementations.kidlearn.spec.eng;

public enum KidLearnEngWordsLevel  {

    L0(5),
    L1(5),
    L2(5),
    L3(5),
    ;

    public int nrOfCorrectUnknownWords;

    KidLearnEngWordsLevel(int nrOfCorrectUnknownWords) {
        this.nrOfCorrectUnknownWords = nrOfCorrectUnknownWords;
    }
}
