package com.ers.internship.utility;

import com.ers.internship.enums.Frequency;
import com.ers.internship.historization.HistorizedItem;
import com.ers.internship.identificators.FxQuoteId;
import com.ers.internship.identificators.InstrumentPriceQuoteId;
import com.ers.internship.identificators.YieldCurveId;
import com.ers.internship.instruments.CreditRegular;
import com.ers.internship.instruments.Instrument;
import com.ers.internship.instruments.Share;
import com.ers.internship.instruments.TimeDeposit;
import com.ers.internship.market.DayCountConvention;
import com.ers.internship.market.FxQuote;
import com.ers.internship.market.InstrumentPriceQuote;
import com.ers.internship.market.YieldCurve;
import com.ers.internship.portfolio.Portfolio;
import com.ers.internship.position.Position;
import com.ers.internship.transaction.Transaction;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * @author Snejina Yanakieva
 *
 */
public class EntityFactory {

    private static final Logger logger = Logger.getLogger(EntityFactory.class.getName());
    private static final Frequency[] frequencies = Frequency.values();
    private static final Map<Class<? extends Instrument>, Supplier<Instrument>> instrumentSuppliers = new HashMap<>();
    private static List<Supplier<Instrument>> instrumentSuppliersList = null;
    private static long ENTITY_COUNT = 0;
    public static final String[] CURRENCIES = {
        "BGN", "EUR", "USD", "GBP"
    };

    static {

        Supplier<Instrument> creditRegularFactory = new Supplier<Instrument>() {
            @Override
            public Instrument get() {
                return makeValidCreditRegular(null, null, null, null, null, null, null, null, null, null, null);
            }
        };
        Supplier<Instrument> timeDepositFactory = new Supplier<Instrument>() {
            @Override
            public Instrument get() {
                return makeValidTimeDeposit(null, null, null, null, null, null, null, null, null);
            }
        };
        Supplier<Instrument> shareFactory = new Supplier<Instrument>() {
            @Override
            public Instrument get() {
                return makeValidShare(null, null, null, null, null, null);
            }
        };

        instrumentSuppliers.put(CreditRegular.class, creditRegularFactory);
        instrumentSuppliers.put(TimeDeposit.class, timeDepositFactory);
        instrumentSuppliers.put(Share.class, shareFactory);
    }

    private static boolean contains(String[] array, String item) {
        for (String str : array) {
            if (item.equals(str)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("rawtypes")
    private static void setValidity(HistorizedItem entity, Timestamp validFrom, Timestamp validTo) {

        if (validFrom == null) {
            validFrom = new Timestamp((long) (Math.random() * Long.MAX_VALUE));
        }

        if (validTo == null) {
            long validFromTime = validFrom.getTime();
            validTo = new Timestamp(validFromTime + (long) (Math.random() * (Long.MAX_VALUE - validFromTime)));
        }

        entity.setValidFrom(validFrom);
        entity.setValidTo(validTo);
    }

    private static Calendar getRandomDate(Calendar min, Calendar max) {
        min = min != null ? min : DayCountConvention.getCalendarInstance(1970, 1, 1);
        max = max != null ? max : DayCountConvention.getCalendarInstance(2029, 12, 31);

        Calendar result = Calendar.getInstance();

        // Get a random long value between min and max
        long resultInMillis = min.getTimeInMillis()
                + (long) (Math.random() * (max.getTimeInMillis() - min.getTimeInMillis()));

        result.setTimeInMillis(resultInMillis);
        return result;
    }

    private static Calendar getRandomDate() {
        return getRandomDate(null, null);
    }

    private static String getRandomCurrency(String... forbidden) {

        int index = (int) (Math.random() * CURRENCIES.length);

        while (contains(forbidden, CURRENCIES[index])) {
            index = (index + 1) % CURRENCIES.length;
        }

        return CURRENCIES[index];
    }

    private static double getRandomInterestRate() {
        return Math.random() * 20;
    }

    private static void setupInstrument(Instrument instrument, String id, Timestamp validFrom,
            Timestamp validTo, String isin, String market, String currency) {

        id = id != null ? id : "Instrument" + ENTITY_COUNT;
        currency = currency != null ? currency : getRandomCurrency();
        isin = isin != null ? isin : "Isin of " + id;
        market = market != null ? market : "Market of " + id;

        instrument.setID(id);
        setValidity(instrument, validFrom, validTo);
        instrument.setIsin(isin);
        instrument.setMarket(market);
        instrument.setCurrency(currency);
    }

    public static Instrument makeValidInstrument() {
        if (instrumentSuppliersList == null) {
            instrumentSuppliersList = new ArrayList<>(instrumentSuppliers.size());
            instrumentSuppliersList.addAll(instrumentSuppliers.values());
        }

        int index = (int) (Math.random() * instrumentSuppliersList.size());
        Instrument result = instrumentSuppliersList.get(index).get();
        return result;
    }

    public static Portfolio makeValidPortfolio(String id, Timestamp validFrom, Timestamp validTo, String name,
            String currency) {
        ENTITY_COUNT += 1;

        Portfolio result = new Portfolio();

        setValidity(result, validFrom, validTo);
        result.setID(id == null ? "TestPortfolio" + ENTITY_COUNT : id);
        result.setName(name == null ? "Name of TestPortfolio" + ENTITY_COUNT : name);
        result.setCurrency(currency != null ? currency : getRandomCurrency());

        return result;
    }

    public static Position makeValidPosition(String id, Timestamp validFrom,
            Timestamp validTo, String name, Instrument instrument, String longSide, String shortSide, String portfolioId) {
        ENTITY_COUNT += 1;

        Position result = new Position();

        if (instrument == null) {
            instrument = makeValidInstrument();
        }

        setValidity(result, validFrom, validTo);
        setValidity(instrument, result.getValidFrom(), result.getValidTo());
        result.setID(id != null ? id : "TestPosition" + ENTITY_COUNT);
        result.setName(name != null ? name : "NameOfTestPosition" + ENTITY_COUNT);
        result.setLongSide(longSide != null ? longSide : "LongSideAtTestPosition" + ENTITY_COUNT);
        result.setShortSide(shortSide != null ? shortSide : "ShortSideAtTestPosition" + ENTITY_COUNT);
        result.setInstrument(instrument);
        result.setPortfolioId(portfolioId != null ? portfolioId : "PortfolioIdOfTestPosition" + ENTITY_COUNT);

        return result;
    }

    public static FxQuote makeValidFxQuote(String baseCurrency, String targetCurrency, Calendar atDate,
            Timestamp validFrom, Timestamp validTo, Double value) {
        ENTITY_COUNT += 1;

        if (baseCurrency == null) {
            if (targetCurrency == null) {
                baseCurrency = getRandomCurrency();
            } else {
                baseCurrency = getRandomCurrency(targetCurrency);
            }
        }

        if (targetCurrency == null) {
            targetCurrency = getRandomCurrency(baseCurrency);
        }

        // If no value has been specified, randomize one between 0.5 and 5.5
        value = value != null ? value : (0.1d + Math.random()) * 5;
        atDate = atDate != null ? atDate : getRandomDate();

        FxQuote fxQuote = new FxQuote(new FxQuoteId(baseCurrency, targetCurrency, atDate));
        fxQuote.setValue(value);
        setValidity(fxQuote, validFrom, validTo);

        return fxQuote;
    }

    public static InstrumentPriceQuote makeValidInstrumentPriceQuote(String instrumentId, Calendar atDate,
            Timestamp validFrom, Timestamp validTo, Double price, String currency) {
        ENTITY_COUNT += 1;

        instrumentId = instrumentId != null ? instrumentId : "InstrumentPriceQuote" + ENTITY_COUNT;
        price = price != null ? price : Math.random() + 1;
        currency = currency != null ? currency : getRandomCurrency();
        atDate = atDate != null ? atDate : getRandomDate();

        InstrumentPriceQuoteId id = new InstrumentPriceQuoteId(instrumentId, atDate);
        InstrumentPriceQuote instrumentPriceQuote = new InstrumentPriceQuote(id);

        setValidity(instrumentPriceQuote, validFrom, validTo);
        instrumentPriceQuote.setInstrumentPrice(price);
        instrumentPriceQuote.setCurrency(currency);

        return instrumentPriceQuote;
    }

    public static YieldCurve makeValidYieldCurve(String currency, Calendar date, Timestamp validFrom,
            Timestamp validTo, Double zy3m, Double zy6m, Double zy1y, Double zy2y, Double zy5y,
            Double zy10y, Double zy30y) {
        ENTITY_COUNT += 1;

        date = date != null ? date : getRandomDate();
        currency = currency != null ? currency : getRandomCurrency();
        zy3m = zy3m != null ? zy3m : getRandomInterestRate();
        zy6m = zy6m != null ? zy6m : getRandomInterestRate();
        zy1y = zy1y != null ? zy1y : getRandomInterestRate();
        zy2y = zy2y != null ? zy2y : getRandomInterestRate();
        zy5y = zy5y != null ? zy5y : getRandomInterestRate();
        zy10y = zy10y != null ? zy10y : getRandomInterestRate();
        zy30y = zy30y != null ? zy30y : getRandomInterestRate();

        YieldCurveId id = new YieldCurveId(currency, date);
        YieldCurve yieldCurve = new YieldCurve(id);

        setValidity(yieldCurve, validFrom, validTo);
        yieldCurve.setZeroYieldThreeMonths(zy3m);
        yieldCurve.setZeroYieldSixMonths(zy6m);
        yieldCurve.setZeroYieldOneYear(zy1y);
        yieldCurve.setZeroYieldTwoYears(zy2y);
        yieldCurve.setZeroYieldFiveYears(zy5y);
        yieldCurve.setZeroYieldTenYears(zy10y);
        yieldCurve.setZeroYieldThirtyYears(zy30y);

        return yieldCurve;
    }

    public static Transaction makeValidTransaction(String id, Timestamp validFrom, Timestamp validTo,
            String name, Double volume, Double paidAmount, String currency, String sender, String receiver,
            String positionId) {

        ENTITY_COUNT += 1;

        id = id != null ? id : "Transaction" + ENTITY_COUNT;
        name = name != null ? name : "NameOfTransaction" + id;
        volume = volume != null ? volume : Math.random(); //TODO
        paidAmount = paidAmount != null ? paidAmount : Math.random(); // TODO
        sender = sender != null ? sender : "Sender at " + id;
        receiver = receiver != null ? receiver : "Receiver at " + id;
        currency = currency != null ? currency : getRandomCurrency();
        positionId = positionId != null ? positionId : "PositionIdOf" + id;

        Transaction transaction = new Transaction(id);

        setValidity(transaction, validFrom, validTo);
        transaction.setName(name);
        transaction.setVolume(volume);
        transaction.setPaidAmount(paidAmount);
        transaction.setCurrency(currency);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setPositionId(positionId);

        return transaction;
    }

    public static CreditRegular makeValidCreditRegular(String id, Timestamp validFrom,
            Timestamp validTo, String isin, String market, String currency, Integer tenorMonths,
            Double interestRate, Frequency frequency, Calendar issue, Calendar maturity) {

        ENTITY_COUNT += 1;

        tenorMonths = tenorMonths != null ? tenorMonths : (int) (Math.random() * 4 + 1) * 3;
        interestRate = interestRate != null ? interestRate : Math.random() * 10 + 3;
        issue = issue != null ? issue : getRandomDate();
        maturity = maturity != null ? maturity : getRandomDate(issue, null);
        // Get a random NOT ZERO entry from the frequencies array if frequency is null
        if (frequency == null) {
            do {
                int index = (int) (Math.random() * frequencies.length);
                frequency = frequencies[index];
            } while (frequency == Frequency.ZERO);
        }

        CreditRegular instrument = new CreditRegular();
        setupInstrument(instrument, id, validFrom, validTo, isin, market, currency);

        instrument.setTenorMonths(tenorMonths);
        instrument.setInterestRate(interestRate);
        instrument.setFrequency(frequency);
        instrument.setIssue(issue);
        instrument.setMaturity(maturity);

        return instrument;
    }

    public static TimeDeposit makeValidTimeDeposit(String id, Timestamp validFrom,
            Timestamp validTo, String isin, String market, String currency,
            Integer tenorMonths, Double interestRate, Calendar issue) {

        ENTITY_COUNT += 1;

        tenorMonths = tenorMonths != null ? tenorMonths : (int) (Math.random() * 4 + 1) * 3;
        interestRate = interestRate != null ? interestRate : Math.random() * 10 + 3;
        issue = issue != null ? issue : getRandomDate();

        TimeDeposit instrument = new TimeDeposit();
        setupInstrument(instrument, id, validFrom, validTo, isin, market, currency);

        instrument.setTenorMonths(tenorMonths);
        instrument.setInterestRate(interestRate);
        instrument.setIssue(issue);

        return instrument;
    }

    public static Share makeValidShare(String id, Timestamp validFrom,
            Timestamp validTo, String isin, String market, String currency) {

        ENTITY_COUNT += 1;

        Share instrument = new Share();
        setupInstrument(instrument, id, validFrom, validTo, isin, market, currency);
        return instrument;
    }

    public static YieldCurve makeValidYieldCurve(YieldCurveId id) {
        return makeValidYieldCurve(id.getCurrency(), id.getAtDate(), null, null, null,
                null, null, null, null, null, null);
    }

    public static CreditRegular makeValidCreditRegular(String id) {
        return makeValidCreditRegular(id, null, null, null, null, null, null, null, null, null, null);
    }

    public static TimeDeposit makeValidTimeDeposit(String id) {
        return makeValidTimeDeposit(id, null, null, null, null, null, null, null, null);
    }

    public static Share makeValidShare(String id) {
        return makeValidShare(id, null, null, null, null, null);
    }

    public static Transaction makeValidTransaction(String id) {
        return makeValidTransaction(id, null, null, null, null, null, null, null, null, null);
    }

    public static InstrumentPriceQuote makeValidInstrumentPriceQuote(InstrumentPriceQuoteId id) {
        return makeValidInstrumentPriceQuote(id.getInstrumentId(), id.getAtDate(), null, null, null, null);
    }

    public static FxQuote makeValidFxQuote(FxQuoteId id) {
        return makeValidFxQuote(id.getFromCurrency(), id.getToCurrency(), id.getAtDate(), null, null, null);
    }

    public static Position makeValidPosition(String id) {
        return makeValidPosition(id, null, null, null, null, null, null, null);
    }

    public static Portfolio makeValidPortfolio(String id) {
        return makeValidPortfolio(id, null, null, null, null);
    }

    private EntityFactory() {
    }

}
