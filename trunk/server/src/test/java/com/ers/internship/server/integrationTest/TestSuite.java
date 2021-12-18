package com.ers.internship.server.integrationTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ers.internship.server.tests.*;

@RunWith(Suite.class)

	@Suite.SuiteClasses({
		 FxQuotePerformanceTest.class,
		 InstrumentPriceQuotePerformanceTest.class,
		 PortfolioPerformanceTest.class,
		 PresentValueCalculatorPerformanceTest.class,
		 TransactionPerformanceTest.class,
		 YieldCurvePerformanceTest.class,
		IntegrationFxQuoteTest.class,
		IntegrationTimeDepositTest.class,
		IntegrationCreditRegularTest.class,
		IntegrationInstrumentPriceQuoteTest.class,
		IntegrationPortfolioTest.class,
		IntegrationPositionTest.class,
		IntegrationShareTest.class,
		IntegrationYieldCurveTest.class,
	})

public class TestSuite {  

	}