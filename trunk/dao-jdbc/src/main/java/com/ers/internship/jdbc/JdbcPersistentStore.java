package com.ers.internship.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hsqldb.jdbc.JDBCDataSource;

import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.dao.FxQuoteDao;
import com.ers.internship.dao.InstrumentPriceQuoteDao;
import com.ers.internship.dao.SearchingDao;
import com.ers.internship.dao.TransactionDao;
import com.ers.internship.dao.YieldCurveDao;
import com.ers.internship.jdbc.PortfolioDaoImpl;
import com.ers.internship.persistentstore.PersistentStore;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;
import com.ers.internship.snapshotloader.SnapshotLoader;

/**
 *
 * @author Snejina Yanakieva
 *
 * 
 */
public class JdbcPersistentStore implements PersistentStore {
	private PortfolioDaoImpl portfolioDaoImpl;
	private TransactionDaoImpl transactionDaoImpl;
	private YieldCurveDaoImpl yieldCurveDaoImpl;
	private PositionDaoImpl positionDaoImpl;
	private InstrumentDaoImpl instrumentDaoImpl;
	private FxQuoteDaoImpl fxQuoteDaoImpl;
	private InstrumentPriceQuoteDaoImpl priceQuoteDaoImpl;
	private PositionSnapshotLoader positionSnapshot;
	private Connection connection;
	private JDBCDataSource dataSource;
	private String userName;
	private String pass;

	/**
	 * Constructs persistent store by url, user and pass
	 * 
	 * @param url
	 *            the database url
	 * @param user
	 *            username of user
	 * @param pass
	 *            the password of the user
	 */
	public JdbcPersistentStore(String url, String user, String pass) {
		this.dataSource = new JDBCDataSource();
		this.dataSource.setDatabase(url);
		this.userName = user;
		this.pass = pass;
	}

	/**
	 * Constructs persistent store by user and pass
	 * 
	 * @param ds
	 *            DataSource which must have url of database
	 * @param user
	 *            username of user
	 * @param pass
	 *            the password of user
	 */
	public JdbcPersistentStore(JDBCDataSource ds, String user, String pass) {
		this.dataSource = ds;
		this.userName = user;
		this.pass = pass;
	}

	@Override
	public void close() throws Exception {
		this.connection.close();
	}

	public Connection getConnection() {
		return this.connection;
	}

	@Override
	public void rollbackTransaction() {

		try {
			this.connection.rollback();
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

	@Override
	public void startTransaction() {
		try {
			this.connection = this.dataSource.getConnection(this.userName, this.pass);
			this.connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void commitTransaction() {
		try {
			this.connection.commit();
			this.connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dropDB() {
		String drop_table = new String();
		try {
			drop_table = "DROP TABLE IDB_PORTFOLIO IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_PORTFOLIO_STATE IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_YIELDCURVE IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_YIELDCURVE_STATE IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_FxQuote IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_FxQuote_STATE IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_InstrumentPriceQuote IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_InstrumentPriceQuote_STATE IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_Transaction IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_Transaction_STATE IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_Instrument IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_Instrument_STATE IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_Position IF EXISTS CASCADE";
			this.executeStatement(drop_table);
			drop_table = "DROP TABLE IDB_Position_STATE IF EXISTS CASCADE";
			this.executeStatement(drop_table);
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}	
	
	private void createPortfolioTable() {
		try {
			String create_table = "CREATE TABLE IF NOT EXISTS IDB_PORTFOLIO (id VARCHAR(50) PRIMARY KEY, portfolio_id VARCHAR(50))";
			this.executeStatement(create_table);
			create_table = "CREATE TABLE  IF NOT EXISTS IDB_PORTFOLIO_STATE (snapshot_id VARCHAR(50) PRIMARY KEY,"
					+ "entity_id VARCHAR(50) FOREIGN KEY REFERENCES IDB_PORTFOLIO (id), valid_from TIMESTAMP,"
					+ "valid_to TIMESTAMP, name VARCHAR(50), currency VARCHAR(3))";
			this.executeStatement(create_table);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	private void createYieldCurveTable() {
		try {
			String create_table = "CREATE TABLE  IF NOT EXISTS IDB_YIELDCURVE(id VARCHAR(50) PRIMARY KEY, currency VARCHAR(3),"
					+ "at_date TIMESTAMP)";
			this.executeStatement(create_table);
			create_table = "CREATE TABLE  IF NOT EXISTS IDB_YIELDCURVE_STATE (snapshot_id VARCHAR(100) PRIMARY KEY,"
					+ "entity_id VARCHAR(50) FOREIGN KEY REFERENCES IDB_YIELDCURVE (id), valid_from TIMESTAMP,"
					+ "valid_to TIMESTAMP, three_months DOUBLE, six_months DOUBLE, one_year DOUBLE,"
					+ "two_years DOUBLE, five_years DOUBLE, ten_years DOUBLE, thirty_years DOUBLE)";
			this.executeStatement(create_table);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void createFxQuoteTable() {
		try {
			String create_table = "CREATE TABLE IF NOT EXISTS IDB_FxQuote(id VARCHAR(50) PRIMARY KEY, from_currency VARCHAR(3),"
					+ "to_currency VARCHAR(3), at_date TIMESTAMP)";
			this.executeStatement(create_table);
			create_table = "CREATE TABLE IF NOT EXISTS IDB_FxQuote_STATE(snapshot_id VARCHAR(100) PRIMARY KEY,"
					+ "entity_id VARCHAR(50) FOREIGN KEY REFERENCES IDB_FxQuote (id), valid_from TIMESTAMP,"
					+ "valid_to TIMESTAMP, value DOUBLE)";
			this.executeStatement(create_table);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void createTransactionTable() {
		try {
			String create_table = "CREATE TABLE IF NOT EXISTS IDB_Transaction(id VARCHAR(50) PRIMARY KEY, transaction_id VARCHAR(50))";
			this.executeStatement(create_table);
			create_table = "CREATE TABLE  IF NOT EXISTS IDB_Transaction_STATE (snapshot_id VARCHAR(100) PRIMARY KEY,"
					+ "entity_id VARCHAR(50) FOREIGN KEY REFERENCES IDB_Transaction (id), valid_from TIMESTAMP,"
					+ "valid_to TIMESTAMP, name VARCHAR(50), volume DOUBLE, paidAmount DOUBLE,"
					+ "currency VARCHAR(3), sender VARCHAR(50), receiver VARCHAR(50), positionId VARCHAR(50))";
			this.executeStatement(create_table);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void createInstrumentPriceQuoteTable() {
		try {
			String create_table = "CREATE TABLE IF NOT EXISTS IDB_InstrumentPriceQuote (id VARCHAR(100) PRIMARY KEY,"
					+ "instrument_id VARCHAR(50), at_date TIMESTAMP)";
			this.executeStatement(create_table);
			create_table = "CREATE TABLE IF NOT EXISTS IDB_InstrumentPriceQuote_STATE(snapshot_id VARCHAR(100) PRIMARY KEY,"
					+ "entity_id VARCHAR(50) FOREIGN KEY REFERENCES IDB_InstrumentPriceQuote(id),"
					+ "valid_from TIMESTAMP, valid_to TIMESTAMP, instrument_price Double," + "currency VARCHAR(3))";
			this.executeStatement(create_table);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void createInstrumentTable() {
		try {
			String create_table = "CREATE TABLE IF NOT EXISTS IDB_Instrument(id VARCHAR(100) PRIMARY KEY, instrument_id VARCHAR(50))";
			this.executeStatement(create_table);
			create_table = "CREATE TABLE  IF NOT EXISTS IDB_Instrument_STATE(snapshot_id VARCHAR(100) PRIMARY KEY,"
					+ "entity_id VARCHAR(50) FOREIGN KEY REFERENCES IDB_Instrument(id),"
					+ "valid_from TIMESTAMP, valid_to TIMESTAMP,"
					+ "instrument_type VARCHAR(15), isin VARCHAR(50), currency VARCHAR(3),market VARCHAR(50),"
					+ "tenorMonths INTEGER, interestRate DOUBLE, issue TIMESTAMP, frequency VARCHAR(15),"
					+ "maturity TIMESTAMP)";
			this.executeStatement(create_table);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void createPositionTable() {
		try {
			String create_table = "CREATE TABLE IF NOT EXISTS IDB_Position(id VARCHAR(100) PRIMARY KEY, position_id VARCHAR(50))";
			this.executeStatement(create_table);
			create_table = "CREATE TABLE IF NOT EXISTS IDB_Position_STATE(snapshot_id VARCHAR(100) PRIMARY KEY,"
					+ "entity_id VARCHAR(50) FOREIGN KEY REFERENCES IDB_Position(id),"
					+ "valid_from TIMESTAMP, valid_to TIMESTAMP,"
					+ "name VARCHAR(50), longSide VARCHAR(50), shortSide VARCHAR(50), portfolioId VARCHAR(50),"
					+ "instrument VARCHAR(50) NOT NULL)";
			this.executeStatement(create_table);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void createDB() {
		createPortfolioTable();
		createYieldCurveTable();
		createFxQuoteTable();
		createTransactionTable();
		createInstrumentPriceQuoteTable();
		createInstrumentTable();
		createPositionTable();
	}

	private void executeStatement(String query) throws SQLException {
		try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
			stmt.executeUpdate();
			stmt.close();
		}
	}

	@Override
	public SearchingDao<String, Portfolio> getPortfolioDao() {
		if (this.portfolioDaoImpl == null) {
			this.portfolioDaoImpl = new PortfolioDaoImpl(this);
		}
		return this.portfolioDaoImpl;
	}

	@Override
	public TransactionDao getTransactionDao() {
		if (this.transactionDaoImpl == null) {
			this.transactionDaoImpl = new TransactionDaoImpl(this);
		}
		return this.transactionDaoImpl;
	}

	@Override
	public SearchingDao<String, Position> getPositionDao() {
		if (this.positionDaoImpl == null) {
			this.positionDaoImpl = new PositionDaoImpl(this);
		}
		return this.positionDaoImpl;
	}

	@Override
	public FxQuoteDao getFxQuoteDao() {
		if (this.fxQuoteDaoImpl == null) {
			this.fxQuoteDaoImpl = new FxQuoteDaoImpl(this);
		}
		return this.fxQuoteDaoImpl;
	}

	@Override
	public InstrumentPriceQuoteDao getInstrumentPriceQuoteDao() {
		if (this.priceQuoteDaoImpl == null) {
			this.priceQuoteDaoImpl = new InstrumentPriceQuoteDaoImpl(this);
		}
		return this.priceQuoteDaoImpl;
	}

	@Override
	public SearchingDao<String, Instrument> getInstrumentDao() {
		if (this.instrumentDaoImpl == null) {
			this.instrumentDaoImpl = new InstrumentDaoImpl(this);
		}
		return this.instrumentDaoImpl;
	}

	@Override
	public YieldCurveDao getYieldCurveDao() {
		if (this.yieldCurveDaoImpl == null) {
			this.yieldCurveDaoImpl = new YieldCurveDaoImpl(this);
		}
		return this.yieldCurveDaoImpl;
	}

	@Override
	public SnapshotLoader<PositionSnapshot> getPositionSnapshotLoader() {
		if (this.positionSnapshot == null) {
			this.positionSnapshot = new PositionSnapshotLoader(this);
		}
		return this.positionSnapshot;
	}
}
