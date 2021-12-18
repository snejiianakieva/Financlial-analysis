package com.ers.internship.calculation.instrument;

import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.Share;
import com.ers.internship.instruments.TimeDeposit;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class contains a registry of instrument calculators mapped to the class of their respective instruments
 *
 * @author Snejina Yanakieva
 */
public class InstrumentCalculatorResolver {

    private static final Logger logger = Logger.getLogger(InstrumentCalculatorResolver.class.getName());

    private static final Map<Class<? extends Instrument>, InstrumentCalculator> cashFlowBuilders = new HashMap<>();

    static {
        cashFlowBuilders.put(TimeDeposit.class, new TimeDepositCalculatorImpl());
        cashFlowBuilders.put(CreditRegular.class, new CreditRegularCalculatorImpl());
        cashFlowBuilders.put(Share.class, new AssetCalculatorImpl());
    }

    /**
     * Resolves and returns an instance of the correct instrument calculator for the class of the input instrument 
     * 
     * @param instrumentClass the class of the instrument to be calculated
     * @return the correct instrument calculator for the input class
     */
    public static InstrumentCalculator getInstrumentCalculator(Class<? extends Instrument> instrumentClass) {
        return cashFlowBuilders.get(instrumentClass);
    }

    private InstrumentCalculatorResolver() {
    }
}
