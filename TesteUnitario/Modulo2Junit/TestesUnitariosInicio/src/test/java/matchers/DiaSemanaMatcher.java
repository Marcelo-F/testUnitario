package matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {

    private Integer diaSemana;

    public DiaSemanaMatcher(Integer diaSemana){
        this.diaSemana = diaSemana;
    }

   @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.verificarDiaSemana(data, diaSemana);
    }

    @Override
    public void describeTo(Description description) {
        Calendar calendar = new Date();
        calendar.set(Calendar.DAY_OF_WEEK, diaSemana);
        String dataExtenso = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt","br"));
        description.appendText(dataExtenso);
    }
}
