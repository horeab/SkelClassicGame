package libgdx.implementations.iqtest.spec;

import java.util.List;

public interface IqGameQuestion {


    int getQuestionNr();

    int getAnwser();

    List<IqGameQuestion> getEnumAllValues();
}
