package com.ers.internship.dao;

import java.util.Calendar;
import java.util.List;

import com.ers.internship.transaction.Transaction;
/**
 * 
 * @author Snejina Yanakieva
 *
 */
public interface TransactionDao extends SearchingDao<String, Transaction>{

	List<Transaction> loadPortfolioTransactions(String portfolioId,
			Calendar date);
}
