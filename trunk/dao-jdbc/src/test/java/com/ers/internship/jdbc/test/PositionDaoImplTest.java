package com.ers.internship.jdbc.test;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.Share;
import com.ers.internship.jdbc.InstrumentDaoImpl;
import com.ers.internship.jdbc.JdbcPersistentStore;
import com.ers.internship.jdbc.PositionDaoImpl;
import com.ers.internship.position.Position;
import org.junit.Ignore;

@Ignore
public class PositionDaoImplTest {
	private static JdbcPersistentStore store;
	private PositionDaoImpl testPositionImpl;
	private InstrumentDaoImpl testInstrumentImpl;
	private String shareId = new String("share");
	private String id = new String("ABCD");

	@BeforeClass
	public static void beforeClass() throws Exception {
		store = new JdbcPersistentStore("jdbc:hsqldb:mem:target/HistorizedDB", "username", "password");
		store.startTransaction();
		store.createDB();
		store.commitTransaction();

	}

	@AfterClass
	public static void afterClass() throws Exception {
		store.startTransaction();
		store.dropDB();
		store.commitTransaction();
		store.close();
	}

	@Before
	public void before() {
		store.startTransaction();
		String shareID = getIdPrimaryKey("IDB_Instrument","instrument_id", shareId);
		deleteEntityInDBTable("IDB_Instrument_STATE", "entity_id", shareID);
		deleteEntityInDBTable("IDB_Instrument", "id", shareID);
		String loadedId = getIdPrimaryKey("IDB_Position","position_id", id);
		deleteEntityInDBTable("IDB_POSITION_STATE", "entity_id", loadedId);

		deleteEntityInDBTable("IDB_POSITION", "id", loadedId);
		store.commitTransaction();
		testPositionImpl = new PositionDaoImpl(store);
		testInstrumentImpl = new InstrumentDaoImpl(store);

	}

	@After
	public void after() {
		store.startTransaction();
		String shareID = getIdPrimaryKey("IDB_Instrument","instrument_id", shareId);
		deleteEntityInDBTable("IDB_Instrument_STATE", "entity_id", shareID);
		deleteEntityInDBTable("IDB_Instrument", "id", shareID);
		String loadedId = getIdPrimaryKey("IDB_Position","position_id", id);
		deleteEntityInDBTable("IDB_POSITION_STATE", "entity_id", loadedId);
		deleteEntityInDBTable("IDB_POSITION", "id", loadedId);
		store.commitTransaction();
		testPositionImpl = null;
		testInstrumentImpl = null;
	}

	@Test
	public void testSavePosition() {
		store.startTransaction();
		Share share = createShare(shareId);
		testInstrumentImpl.save(share);
		assertNotNull("Should be not null share!", share);
		
		Position position = createPosition(id, share);
		testPositionImpl.save(position);
		Calendar date = new GregorianCalendar(2050, 5, 5);
		Position loadedPosition = null;
		loadedPosition = testPositionImpl.loadById(id, date);
		assertNotNull("Should be not null! save method", loadedPosition);
		assertEqualsEntities(position, loadedPosition);
		store.commitTransaction();
	}

	@Test
	public void testDeleteString() {
		store.startTransaction();
		Share share = createShare(shareId);
		testInstrumentImpl.save(share);
		Position position = createPosition(id, share);
		testPositionImpl.save(position);
		Calendar date = new GregorianCalendar(2050, 5, 5);
		Position loadedPosition = null;
		loadedPosition = testPositionImpl.loadById(id, date);
		assertNotNull("Should be not null! save method", loadedPosition);
		assertEqualsEntities(position, loadedPosition);

		date = new GregorianCalendar();
		testPositionImpl.delete(id);
		Position loadedPositionAfterUpdate = null;
		loadedPositionAfterUpdate = testPositionImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedPositionAfterUpdate);
		assertEqualsEntities(position, loadedPositionAfterUpdate);
		store.commitTransaction();
	}

	@Test
	public void testLoadByIdStringCalendar() {
		store.startTransaction();
		Share share = createShare(shareId);
		testInstrumentImpl.save(share);
		Position position = createPosition(id, share);
		testPositionImpl.save(position);
		Calendar date = new GregorianCalendar(2050, 5, 5);
		Position loadedPosition = null;
		loadedPosition = testPositionImpl.loadById(id, date);
		assertNotNull("Should be not null! save method", loadedPosition);
		assertEqualsEntities(position, loadedPosition);
		store.commitTransaction();
	}

	@Test
	public void testSearchByName() {
		store.startTransaction();
		Share share = createShare(shareId);
		testInstrumentImpl.save(share);
		Position position = createPosition(id, share);
		testPositionImpl.save(position);
		Calendar date = new GregorianCalendar(2050, 5, 5);
		Position loadedPosition = null;
		loadedPosition = testPositionImpl.loadById(id, date);
		assertNotNull("Should be not null! save method", loadedPosition);
		assertEqualsEntities(position, loadedPosition);
		
		Position position2 = createPosition(id, share);
		testPositionImpl.save(position2);
		Position loadedPosition2 = null;
		loadedPosition2 = testPositionImpl.loadById(id, date);
		assertNotNull("Should be not null! save method", loadedPosition2);
		assertEqualsEntities(position2, loadedPosition2);
		
		List<Position> listPositions = new LinkedList<>();
		listPositions = testPositionImpl.searchByName("alabala");
		assertTrue(listPositions.size() >= 2);
		store.commitTransaction();
		
	}
	@Test
	public void testHistorization() {
		store.startTransaction();
		System.out.println("historization method");
		// first position
		Share share = createShare(shareId);
		testInstrumentImpl.save(share);
		Position position = createPosition(id, share);
		testPositionImpl.save(position);
		Position loadedPosition = null;
		Calendar date = new GregorianCalendar();
		// second position
		Share share2 = createShare("share2");
		testInstrumentImpl.save(share2);
		Position position2 = createPosition(id, share2);
		testPositionImpl.save(position2);
		Calendar date2 = new GregorianCalendar(2050, 5, 5);
		//
		loadedPosition = testPositionImpl.loadById(id, date);
		assertNotNull("Should be not null", loadedPosition);
		assertEqualsEntities(position, loadedPosition);
		Position loadedPosition2 = testPositionImpl.loadById(id, date2);
		assertNotNull("Should be not null", loadedPosition2);
		assertNotSame("Should be not same", loadedPosition2.getInstrument().getID(), loadedPosition.getInstrument().getID());
		store.commitTransaction();
	}
	protected Position createPosition(String posID, Instrument instrument) {
		Position position = new Position(posID);
		position.setName("alabala");
		position.setLongSide("Long side");
		position.setShortSide("Short side");
		position.setPortfolioId("0001");
		position.setInstrument(instrument);
		return position;
	}
	protected Share createShare(String id){
		Share share = new Share(id);
		share.setIsin("isin");
		share.setMarket("market");
		share.setCurrency("BGN");
		return share;
	}
	protected void deleteEntityInDBTable(String nameTable, String columnName, String id) {
		String deleteQuery = "DELETE FROM " + nameTable + " WHERE " + columnName + "=?";
		try (PreparedStatement stmt = store.getConnection().prepareStatement(deleteQuery);){
			stmt.setString(1, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			fail("Fail delete");
		}
	}

	protected String getIdPrimaryKey(String nameTable,String columnName, String id) {
		String idPKey = null;
		String selectQuery = "SELECT id FROM " + nameTable + " WHERE " + columnName + "=?";
		try (PreparedStatement stmt = store.getConnection().prepareStatement(selectQuery);){
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				idPKey = rs.getString("id");
			}
			stmt.close();
		} catch (SQLException e) {
			fail("Fail select");
		}
		return idPKey;
	}

	protected void assertEqualsEntities(Position pos1, Position pos2) {
		assertEquals("Not equals by id the entities!", pos1.getID(), pos2.getID());
		assertEquals("Not equals by name the entities!", pos1.getName(), pos2.getName());
		assertEquals("Not equals by long side the entities!", pos1.getLongSide(), pos2.getLongSide());
		assertEquals("Not equals by short side the entities!", pos1.getShortSide(), pos2.getShortSide());
		assertEquals("Not equals by portfolio id the entities!", pos1.getPortfolioId(), pos2.getPortfolioId());
		Instrument instrument1 = pos1.getInstrument();
		Instrument instrumnet2 = pos2.getInstrument();
		assertEquals("Not equals instruments by currency the entities!", instrument1.getCurrency(),instrumnet2.getCurrency());
		assertEquals("Not equals instruments by isin the entities!", instrument1.getIsin(), instrumnet2.getIsin());
		assertEquals("Not equals instruments by market the entities!",instrument1.getMarket(), instrumnet2.getMarket());
		assertEquals("Not equals instruments by id the entities!",instrument1.getID(), instrumnet2.getID());
	}
	
}
