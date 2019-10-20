package libgdx.campaign;

import libgdx.utils.EnumUtils;

import java.util.*;

public class QuestionConfig {

    private List<String> l = new ArrayList<>();
    private List<String> c = new ArrayList<>();
    private int a;

    public QuestionConfig(int amountOfQuestions) {
        this(new ArrayList<QuestionDifficulty>(Arrays.asList(EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum()))),
                new ArrayList<QuestionCategory>(Arrays.asList(EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum()))),
                amountOfQuestions, -1);
    }

    public QuestionConfig(QuestionCategory questionCategory) {
        this(Collections.singletonList(questionCategory));
    }

    public QuestionConfig(QuestionDifficulty questionDifficulty) {
        this(Collections.singletonList(questionDifficulty), new ArrayList<QuestionCategory>(Arrays.asList(EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum()))));
    }

    public QuestionConfig(QuestionCategory questionCategory, int amountOfQuestions) {
        this(Collections.singletonList(questionCategory), amountOfQuestions);
    }

    public QuestionConfig(QuestionDifficulty questionDifficulty, int amountOfQuestions) {
        this(Collections.singletonList(questionDifficulty), new ArrayList<QuestionCategory>(Arrays.asList(EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum()))), amountOfQuestions, -1);
    }

    public QuestionConfig(QuestionDifficulty QuestionDifficulty, QuestionCategory questionCategory) {
        this(Collections.singletonList(QuestionDifficulty), Collections.singletonList(questionCategory));
    }

    public QuestionConfig(QuestionDifficulty QuestionDifficulty, QuestionCategory questionCategory, int amount) {
        this(Collections.singletonList(QuestionDifficulty), Collections.singletonList(questionCategory), amount, -1);
    }

    public void setA(int a) {
        this.a = a;
    }

    public QuestionConfig(List<QuestionCategory> questionCategory) {
        this(questionCategory, 1);
    }

    public QuestionConfig(List<QuestionCategory> questionCategory, int amountOfQuestions) {
        this(new ArrayList<QuestionDifficulty>(Arrays.asList(EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum()))), questionCategory, amountOfQuestions, -1);
    }

    public QuestionConfig(List<QuestionDifficulty> questionDifficulty, List<QuestionCategory> questionCategory) {
        this(questionDifficulty, questionCategory, 1, -1);
    }

    public QuestionConfig(QuestionDifficulty QuestionDifficulty, List<QuestionCategory> questionCategory, int amount) {
        this(Collections.singletonList(QuestionDifficulty), questionCategory, amount, -1);
    }

    private QuestionConfig(List<QuestionDifficulty> questionDifficulty, List<QuestionCategory> questionCategory, int amount, int forUniqueConstructor) {
        for (QuestionDifficulty item : questionDifficulty) {
            this.l.add(item.name());
        }
        for (QuestionCategory item : questionCategory) {
            this.c.add(item.name());
        }
        this.a = amount;
    }

    public QuestionConfig(List<String> l, List<String> c, int a) {
        this.l = l;
        this.c = c;
        this.a = a;
    }

    public List<String> getQuestionDifficultyStringList() {
        return l;
    }

    public List<String> getQuestionCategoryStringList() {
        return c;
    }

    public int getAmount() {
        return a;
    }

    public RandomCategoryAndDifficulty getRandomCategoryAndDifficulty() {
        QuestionDifficulty randomQuestionDifficulty = getRandomQuestionDifficulty();
        List<QuestionCategory> categories = new QuestionConfigFileHandler().getQuestionCategoriesForDifficulty(randomQuestionDifficulty);
        Collections.shuffle(categories);
        for (QuestionCategory category : new ArrayList<>(categories)) {
            if (!c.contains(category.name())) {
                categories.remove(category);
            }
        }
        return new RandomCategoryAndDifficulty(categories.get(0), randomQuestionDifficulty);
    }

    private QuestionDifficulty getRandomQuestionDifficulty() {
        ArrayList<String> list = new ArrayList<String>(l);
        Collections.shuffle(list);
        return (QuestionDifficulty) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum(), list.get(0));
    }

    private QuestionCategory getRandomQuestionCategory() {
        ArrayList<String> list = new ArrayList<String>(c);
        Collections.shuffle(list);
        return (QuestionCategory) EnumUtils.getEnumValue(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum(), list.get(0));
    }

    public int getAmountOfQuestions() {
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionConfig that = (QuestionConfig) o;
        return a == that.a &&
                l.equals(that.l) &&
                c.equals(that.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, c, a);
    }
}
