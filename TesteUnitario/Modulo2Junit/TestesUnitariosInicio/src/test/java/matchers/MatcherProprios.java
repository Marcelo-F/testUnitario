package matchers;

import java.util.Calendar;

public class MatcherProprios {
    public static  DiaSemanaMatcher caiEm(Integer diaSemana){
        return new DiaSemanaMatcher(diaSemana);
    }

    public static  DiaSemanaMatcher caiNumaSegunda(){
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }
    public static  DataDiferencasDiasMathcer ehHojeComDiferencaDias(Integer qtdDias){
        return new DataDiferencasDiasMathcer(qtdDias);
    }
    public static  DataDiferencasDiasMathcer ehHoje(){
        return new DataDiferencasDiasMathcer(0);
    }
}
