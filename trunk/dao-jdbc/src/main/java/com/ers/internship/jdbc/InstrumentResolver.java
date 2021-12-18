package com.ers.internship.jdbc;

import java.util.HashMap;
import java.util.Map;

import com.ers.internship.instrumentdbconnector.InstrumentDBConnector;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.instruments.Share;
import com.ers.internship.instruments.TimeDeposit;

/**
 *@author Snejina Yanakieva
 */
public class InstrumentResolver {

	private static final Map<String, InstrumentDBConnector> instrumentTableConnection = new HashMap<>();

	static {
		instrumentTableConnection.put(CreditRegular.class.getSimpleName(), new CreditRegularDaoImpl());
		instrumentTableConnection.put(Share.class.getSimpleName(), new ShareDaoImpl());
		instrumentTableConnection.put(TimeDeposit.class.getSimpleName(), new TimeDepositDaoImpl());
	}

	public InstrumentDBConnector getDao(String instrumentClass) {
	

		return instrumentTableConnection.get(instrumentClass);

	}
}
