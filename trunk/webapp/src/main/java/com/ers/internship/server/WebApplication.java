package com.ers.internship.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ers.internship.jdbc.JdbcPersistentStore;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.rest.calculation.CalculationRestServiceImpl;
import com.ers.internship.rest.fxquote.FxQuoteRestServiceImpl;
import com.ers.internship.rest.instrument.InstrumentRestServiceImpl;
import com.ers.internship.rest.instrumentpricequote.InstrumentPriceQuoteRestServiceImpl;
import com.ers.internship.rest.portfolio.PortfolioRestServiceImpl;
import com.ers.internship.rest.position.PositionRestServiceImpl;
import com.ers.internship.jetty.server.JettyServer;
import com.ers.internship.rest.transaction.TransactionRestServiceImpl;
import com.ers.internship.rest.yieldcurve.YieldCurveRestServiceImpl;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Snejina Yanakieva
 *
 */
public class WebApplication {

    private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);

    private static final String DB_TYPE = "dbType";
    private static final String DB_NAME = "dbName";
    private static final String DB_USER = "dbUser";
    private static final String DB_PASS = "dbPass";
    private static final String APP_URL = "AppURL";

    /**
     * Prints the commands list to the standard output
     */
    public static void printHelp() {
        System.out.println("Commands list :");
        System.out.println("start - starts the web server");
        System.out.println("stop - stops the web server");
        System.out.println("exit - stops the web server and exits");
        System.out.println("help - prints this help");
    }

    public static void main(String[] args) {
        WebApplication app = new WebApplication();

        try (Scanner input = new Scanner(System.in);) {
            while (true) {
                if (input.hasNextLine()) {
                    String command = input.nextLine();

                    switch (command) {
                        case "start":
                            app.startServer();
                            break;
                        case "stop":
                            app.stopServer();
                            break;
                        case "help":
                            printHelp();
                            break;
                        case "exit":
                            app.stopServer();
                            return;
                    }
                }
            }
        }
    }

    private JettyServer restServer;

    public WebApplication() {
        Properties properties = new Properties();
        try (InputStream inputSteam = getClass().getClassLoader().getResourceAsStream("settings.properties")) {
            properties.load(inputSteam);
        } catch (IOException | NullPointerException e) {
            logger.error("Cannot load input stream", e);
            System.exit(1);
        }

        PropertyManager pm = new PropertyManager(properties, DB_NAME, DB_TYPE, DB_USER, DB_PASS, APP_URL);

        String url = pm.getProperty(APP_URL);
        if (pm.getValidity() == false || validateUrl(url) == false) {
            System.exit(1);
        }

        restServer = new JettyServer(url);
        registerServices(createPersistentStore(pm));
    }

    private void registerServices(PersistentStore persistentStore) {
        restServer.registerService(new CalculationRestServiceImpl(persistentStore));
        restServer.registerService(new FxQuoteRestServiceImpl(persistentStore));
        restServer.registerService(new InstrumentPriceQuoteRestServiceImpl(persistentStore));
        restServer.registerService(new InstrumentRestServiceImpl(persistentStore));
        restServer.registerService(new PortfolioRestServiceImpl(persistentStore));
        restServer.registerService(new PositionRestServiceImpl(persistentStore));
        restServer.registerService(new TransactionRestServiceImpl(persistentStore));
        restServer.registerService(new YieldCurveRestServiceImpl(persistentStore));
    }

    private PersistentStore createPersistentStore(PropertyManager pm) {
        String dbUrl = String.format("jdbc:hsqldb:%s:%s", pm.getProperty(DB_TYPE), pm.getProperty(DB_NAME));
        PersistentStore persistentStore = new JdbcPersistentStore(dbUrl, pm.getProperty(DB_USER), pm.getProperty(DB_PASS));
        persistentStore.startTransaction();
        persistentStore.createDB();
        persistentStore.rollbackTransaction();
        return persistentStore;
    }

    private boolean validateUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            logger.error("URL property is invalid");
            return false;
        }
    }

    public void startServer() {
        restServer.start();
    }

    public void stopServer() {
        restServer.stop();
    }
}
