package com.ers.internship.jdbc.test;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ers.internship.aggregation.PositionSnapshot;
import com.ers.internship.instruments.Share;
import com.ers.internship.instruments.TimeDeposit;
import com.ers.internship.jdbc.JdbcPersistentStore;
import com.ers.internship.position.Position;
import com.ers.internship.transaction.Transaction;
import com.ers.internship.utility.EntityFactory;
import org.junit.Ignore;

@Ignore
public class PositionSnapshotLoaderTest {

    private static JdbcPersistentStore store;
    private String longSide = new String("Long side");
    private String shortSide = new String("Short side");
    private String portfolioId = new String("0001");

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

    protected String getIdPrimaryKey(String nameTable, String id) {
        String idPKey = null;
        String selectQuery = "SELECT id FROM " + nameTable + " WHERE position_id=?";
        try (PreparedStatement stmt = store.getConnection().prepareStatement(selectQuery)) {
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

    @Test
    public void testLoadSnapshots() {

        Share share = EntityFactory.makeValidShare("share", new Timestamp(new GregorianCalendar().getTimeInMillis()),
                new Timestamp(new GregorianCalendar().getTimeInMillis()),
                "isin", "market", "BGN");
        Position pos1 = EntityFactory.makeValidPosition("idPos1", new Timestamp(new GregorianCalendar().getTimeInMillis()),
                new Timestamp(new GregorianCalendar().getTimeInMillis()), "pos1", share, longSide, shortSide, portfolioId);
        TimeDeposit td = EntityFactory.makeValidTimeDeposit("td", new Timestamp(new GregorianCalendar().getTimeInMillis()),
                new Timestamp(new GregorianCalendar().getTimeInMillis()),
                "isin", "market", "BGN", 5, 5.5, new GregorianCalendar());
        Position pos2 = EntityFactory.makeValidPosition("idPos2", new Timestamp(new GregorianCalendar().getTimeInMillis()),
                new Timestamp(new GregorianCalendar().getTimeInMillis()), "pos2", td, longSide, shortSide, portfolioId);
        Transaction tr1 = EntityFactory.makeValidTransaction("1", new Timestamp(new GregorianCalendar().getTimeInMillis()),
                new Timestamp(new GregorianCalendar().getTimeInMillis()), "tr1", 20.2, 20.0, "BGN", longSide, shortSide, "idPos1");
        Transaction tr2 = EntityFactory.makeValidTransaction("2", new Timestamp(new GregorianCalendar().getTimeInMillis()),
                new Timestamp(new GregorianCalendar().getTimeInMillis()), "tr2", 1.2, 20.0, "BGN", shortSide, longSide, "idPos1");
        Transaction tr3 = EntityFactory.makeValidTransaction("3", new Timestamp(new GregorianCalendar().getTimeInMillis()),
                new Timestamp(new GregorianCalendar().getTimeInMillis()), "tr3", 50.0, 20.0, "BGN", shortSide, longSide, "idPos2");
        Transaction tr4 = EntityFactory.makeValidTransaction("4", new Timestamp(new GregorianCalendar().getTimeInMillis()),
                new Timestamp(new GregorianCalendar().getTimeInMillis()), "tr3", 60.0, 20.0, "BGN", longSide, shortSide, "idPos2");
        store.startTransaction();
        store.getInstrumentDao().save(share);
        store.getInstrumentDao().save(td);
        store.getPositionDao().save(pos1);
        store.getPositionDao().save(pos2);
        store.getTransactionDao().save(tr1);
        store.getTransactionDao().save(tr2);
        store.getTransactionDao().save(tr3);
        store.getTransactionDao().save(tr4);
        store.commitTransaction();
        store.startTransaction();
        List<PositionSnapshot> snapshots = store.getPositionSnapshotLoader().loadSnapshots(portfolioId, new GregorianCalendar());
        store.commitTransaction();

        assertTrue(snapshots.size() >= 2);
        for (int i = 0; i < snapshots.size(); i++) {
            assertEquals("Should be BGN", snapshots.get(i).getCurrency(), "BGN");
            assertEquals("Should be equals by portfolioId", snapshots.get(i).getPosition().getPortfolioId(), portfolioId);
            assertEquals("Should be equals to long side", snapshots.get(i).getPosition().getLongSide(), longSide);
            assertEquals("Should be equals to short side", snapshots.get(i).getPosition().getShortSide(), shortSide);
            assertEquals("Should be equals to isin", snapshots.get(i).getPosition().getInstrument().getIsin(), "isin");
            assertEquals("Should be equals to market", snapshots.get(i).getPosition().getInstrument().getMarket(), "market");
            assertTrue(snapshots.get(i).getVolume() == 19 || snapshots.get(i).getVolume() == 10);
            assertTrue(snapshots.get(i).getPosition().getName() == "pos1" || snapshots.get(i).getPosition().getName() == "pos2");

        }

    }

}
