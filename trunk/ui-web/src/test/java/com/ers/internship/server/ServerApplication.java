/**
 * 
 */
package com.ers.internship.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ers.internship.jdbc.JdbcPersistentStore;
import com.ers.internship.rest.calculation.CalculationRestServiceImpl;
import com.ers.internship.rest.fxquote.FxQuoteRestServiceImpl;
import com.ers.internship.rest.instrument.InstrumentRestServiceImpl;
import com.ers.internship.rest.instrumentpricequote.InstrumentPriceQuoteRestServiceImpl;
import com.ers.internship.rest.portfolio.PortfolioRestServiceImpl;
import com.ers.internship.rest.position.PositionRestServiceImpl;
import com.ers.internship.rest.server.RestServer;
import com.ers.internship.rest.transaction.TransactionRestServiceImpl;
import com.ers.internship.rest.yieldcurve.YieldCurveRestServiceImpl;

/**
 * @author Snejina Yanakieva
 *
 */

public class ServerApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);
	
	private static final String DB_URL = "dbURL";
	private static final String DB_USER = "dbUser";
	private static final String DB_PASS = "dbPass";
	private static final String APP_URL = "AppURL";
	
	public static void main(String[] args) {
		ServerApplication server = new ServerApplication();
		server.startServer();
//		server.stopServer();	
	}
	
	private JdbcPersistentStore persistentStore;
	private RestServer restServer; 
	
	public ServerApplication () {
		
		Properties property = new Properties();
		
		try (InputStream inputSteam = getClass().getClassLoader().getResourceAsStream("settings.properties")) {
			
			property.load(inputSteam);
		
			if (persistentStore == null) {
				persistentStore = new JdbcPersistentStore(property.getProperty(DB_URL), 
														  property.getProperty(DB_USER), 
														  property.getProperty(DB_PASS)
														  );
			}
			
			if (restServer == null) {
				restServer = new RestServer(property.getProperty(APP_URL));
			}
			
			registerServices();
			
		} catch (IOException|NullPointerException e) {
			logger.error("Cannot load input steam", e);
			System.exit(1);
		} 
	}
	
	public void registerServices () {
		restServer.registerService(new CalculationRestServiceImpl(persistentStore));
		restServer.registerService(new FxQuoteRestServiceImpl(persistentStore));
		restServer.registerService(new InstrumentPriceQuoteRestServiceImpl(persistentStore));
		restServer.registerService(new InstrumentRestServiceImpl(persistentStore));
		restServer.registerService(new PortfolioRestServiceImpl(persistentStore));
		restServer.registerService(new PositionRestServiceImpl(persistentStore));
		restServer.registerService(new TransactionRestServiceImpl(persistentStore));
		restServer.registerService(new YieldCurveRestServiceImpl(persistentStore));
	}
	
	public void startServer() {
		restServer.start();
	}
	
	public void stopServer() {
		restServer.stop();
	}
}