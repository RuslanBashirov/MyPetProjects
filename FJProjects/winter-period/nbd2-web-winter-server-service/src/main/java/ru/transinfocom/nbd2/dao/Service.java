package ru.transinfocom.nbd2.dao;

import java.sql.SQLException;
import java.util.List;

import com.google.common.collect.Multimap;

import ru.transinfocom.nbd2.entity.ChangesHistory;
import ru.transinfocom.nbd2.entity.Dor;
import ru.transinfocom.nbd2.entity.PredIdsAndDates;
import ru.transinfocom.nbd2.entity.PredTreeRow;
import ru.transinfocom.nbd2.entity.SeasonYearsAndDates;
import ru.transinfocom.nbd2.entity.WPUser;

public interface Service {

	public WPUser auth(String login, String passwordHash, String ip) throws SQLException;

	public WPUser refresh(int sessionId, String ip) throws SQLException;

	public WPUser findByUsername(String username) throws SQLException, ClassNotFoundException;

	public Multimap<PredTreeRow, PredTreeRow> getFullDepoTree(int predId) throws SQLException, ClassNotFoundException;

	public List<ChangesHistory> getChangesHistory(int predId, int seasonYear) throws SQLException;

	public Dor getDor(int seasonYear, int dorId, String name) throws SQLException, ClassNotFoundException;
	
	public List<Dor> getCtDor(int seasonYear, int dorId) throws SQLException, ClassNotFoundException;
	
	public String getNameForPredId(int predId) throws SQLException, ClassNotFoundException;

	public List<PredIdsAndDates> getForSeasonYear(int seasonYear) throws SQLException;

	public List<SeasonYearsAndDates> getForPredId(int predId) throws SQLException, ClassNotFoundException;

	public void invalidate(int asutUserId, int predId, int seasonYear) throws SQLException, ClassNotFoundException;

	public void upsert(int asutUserId, int predId, int seasonYear, String startDate, String finishDate)
			throws SQLException, ClassNotFoundException;

	void exit(int sessionId) throws SQLException;
}
