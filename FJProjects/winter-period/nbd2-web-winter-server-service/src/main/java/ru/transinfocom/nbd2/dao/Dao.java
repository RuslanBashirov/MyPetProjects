package ru.transinfocom.nbd2.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import ru.transinfocom.dao.TiDaoUtil;
import ru.transinfocom.dbconnection.TiDbConnectionFactory;
import ru.transinfocom.nbd2.entity.ChangesHistory;
import ru.transinfocom.nbd2.entity.Dor;
import ru.transinfocom.nbd2.entity.PredIdsAndDates;
import ru.transinfocom.nbd2.entity.PredTreeRow;
import ru.transinfocom.nbd2.entity.SeasonYearsAndDates;
import ru.transinfocom.nbd2.entity.WPUser;

public class Dao implements Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(Dao.class);

	// @Value("${jwt.sessionTime}")
	private long sessionTime = 600000; // milliseconds

	public static Connection getMainConnection() throws SQLException {
		Connection con = TiDbConnectionFactory.getInstance().getNbd2Connection();
		con.setAutoCommit(false);
		return con;
	}

	@Override
	public void exit(int sessionId) throws SQLException {
		LOGGER.trace("start");
		CallableStatement st = null;
		Connection con = null;
		try {
			con = getMainConnection();
			st = con.prepareCall("{call winter_period.exit(?)}");
			TiDaoUtil.setInt(st, 1, sessionId);
			st.execute();

			LOGGER.debug("logouted for sessionId = {}", sessionId);
		} finally {
			TiDaoUtil.close(st);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public WPUser auth(String userName, String passwordHash, String ip) throws SQLException {
		LOGGER.trace("start");
		CallableStatement st = null;
		ResultSet rs = null;
		WPUser wpUser = null;
		Connection con = null;
		try {
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.auth(?, ?, ?, ?)}");
			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setString(st, 2, userName);
			TiDaoUtil.setString(st, 3, passwordHash);
			TiDaoUtil.setString(st, 4, ip);
			TiDaoUtil.setInt(st, 5, (int) (sessionTime / 60000)); // recount to minutes
			st.execute();
			con.commit();
			rs = (ResultSet) st.getObject(1);

			if (rs != null && rs.next()) {
				wpUser = new WPUser();
				wpUser.setSessionId(rs.getInt("new_generated_session_id"));
				wpUser.setFirstName(rs.getString("first_name"));
				wpUser.setLastName(rs.getString("last_name"));
				wpUser.setPatrName(rs.getString("patr_name"));
				wpUser.setUserName(rs.getString("user_name"));
				wpUser.setAsutUserId(rs.getInt("asut_user_id"));
				int predId = rs.getInt("pred_id");
				wpUser.setPredId(predId);
				wpUser.setPredName(rs.getString("sname2"));

				String functionCode = rs.getString("function_code");
				int grId = rs.getInt("gr_id");
				int vdId = rs.getInt("vd_id");
				if (predId == 13006 || (grId == 10 && vdId == 520)) {
					if (functionCode != null && functionCode.equals("nbd2-web-winter-edit")) {
						wpUser.setRole("ROLE_DIR_EDIT");
					} else {
						wpUser.setRole("ROLE_DIR");
					}
				} else {
					if (functionCode != null && functionCode.equals("nbd2-web-winter-edit")) {
						wpUser.setRole("ROLE_DEPO_EDIT");
					} else {
						wpUser.setRole("ROLE_DEPO");
					}
				}
			}

			if (wpUser != null) {
				LOGGER.debug("Authenticated with token where session_id = {}", wpUser.getSessionId());
			}
			return wpUser;
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public WPUser refresh(int sessionId, String ip) throws SQLException {
		LOGGER.trace("start");
		CallableStatement st = null;
		ResultSet rs = null;
		WPUser wpUser = null;
		Connection con = null;
		try {
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.refresh(?, ?, ?)}");
			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setInt(st, 2, sessionId);
			TiDaoUtil.setString(st, 3, ip);
			TiDaoUtil.setInt(st, 4, (int) (sessionTime / 60000)); // recount to minutes
			st.execute();
			con.commit();
			rs = (ResultSet) st.getObject(1);

			if (rs != null && rs.next()) {
				wpUser = new WPUser();
				wpUser.setSessionId(rs.getInt("new_generated_session_id"));
				wpUser.setFirstName(rs.getString("first_name"));
				wpUser.setLastName(rs.getString("last_name"));
				wpUser.setPatrName(rs.getString("patr_name"));
				wpUser.setUserName(rs.getString("user_name"));
				wpUser.setAsutUserId(rs.getInt("asut_user_id"));
				wpUser.setPredId(rs.getInt("pred_id"));
				wpUser.setPredName(rs.getString("sname2"));
			}

			LOGGER.debug("Refreshed token where old session_id = {}", sessionId);
			return wpUser;
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public WPUser findByUsername(String userName) throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		WPUser wpUser = null;
		CallableStatement st = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getMainConnection();
			wpUser = new WPUser();
			st = con.prepareCall("{? = call asut2.get_by_user_name(?)}");

			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setString(st, 2, userName);
			st.execute();
			rs = (ResultSet) st.getObject(1);

			if (rs.next()) {
				wpUser.setFirstName(rs.getString("first_name"));
				wpUser.setLastName(rs.getString("last_name"));
				wpUser.setPatrName(rs.getString("patr_name"));
				wpUser.setUserName(rs.getString("user_name"));
				wpUser.setAsutUserId(rs.getInt("asut_user_id"));
				wpUser.setPredId(rs.getInt("pred_id"));
				wpUser.setPasswordHash(rs.getString("password_hash"));
			}

			return wpUser;
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public Multimap<PredTreeRow, PredTreeRow> getFullDepoTree(int inputPredId)
			throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		Connection con = null;
		CallableStatement st = null;
		ResultSet rs = null;
		Multimap<PredTreeRow, PredTreeRow> dorPredMap = HashMultimap.create();
		List<PredTreeRow> dors = new ArrayList<PredTreeRow>();
		List<PredTreeRow> preds = new ArrayList<PredTreeRow>();
		try {
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.get_full_depo_tree(?)}");
			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setInt(st, 2, inputPredId);
			st.execute();
			rs = (ResultSet) st.getObject(1);

			while (rs.next()) {
				int predId = rs.getInt("pred_id");
				int parentPredId = rs.getInt("parent_pred_id");
				PredTreeRow predTreeRow = new PredTreeRow();
				predTreeRow.setPredId(predId);
				predTreeRow.setParentPredId(parentPredId);
				predTreeRow.setName(rs.getString("name"));
				predTreeRow.setDorKod(rs.getInt("dor_kod"));

				if (predId != 13006) {
					if (parentPredId == 13006) {
						dors.add(predTreeRow);
					} else {
						preds.add(predTreeRow);
					}
				}
			}

			for (PredTreeRow dor : dors) {
				for (PredTreeRow pred : preds) {
					if (pred.getParentPredId() == dor.getPredId()) {
						dorPredMap.put(dor, pred);
					}
				}
			}

			return dorPredMap;
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public List<ChangesHistory> getChangesHistory(int predId, int seasonYear) throws SQLException {
		LOGGER.trace("start");
		CallableStatement st = null;
		ResultSet rs = null;
		List<ChangesHistory> changesHistoryList = new ArrayList<ChangesHistory>();
		Connection con = null;
		try {
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.get_changes_history(?, ?)}");
			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setInt(st, 2, predId);
			TiDaoUtil.setInt(st, 3, seasonYear);
			st.execute();
			rs = (ResultSet) st.getObject(1);

			while (rs.next()) {
				ChangesHistory changesHistory = new ChangesHistory();
				changesHistory.setPredId(predId);
				changesHistory.setSeasonYear(seasonYear);
				changesHistory.setName(rs.getString("sname2"));
				changesHistory.setFirstName(rs.getString("first_name"));
				changesHistory.setLastName(rs.getString("last_name"));
				changesHistory.setPatrName(rs.getString("patr_name"));
				changesHistory.setStartDate(rs.getString("start_date"));
				changesHistory.setFinishDate(rs.getString("finish_date"));
				changesHistory.setLastUpdate(rs.getString("last_update"));
				changesHistoryList.add(changesHistory);
			}
			return changesHistoryList;
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public Dor getDor(int seasonYear, int dorId, String name) throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		Dor dor = new Dor();
		Connection con = null;
		CallableStatement st = null;
		ResultSet rs = null;
		SeasonYearsAndDates seasonYearsAndDates = null;
		try {
			int startDateCnt = 0; // counters to count percentage
			int finishDateCnt = 0;
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.get_dor(?, ?)}");
			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setInt(st, 2, dorId);
			TiDaoUtil.setInt(st, 3, seasonYear);
			st.execute();
			rs = (ResultSet) st.getObject(1);

			List<SeasonYearsAndDates> listSeasonYearsAndDates = new ArrayList<>();
			while (rs.next()) {
				seasonYearsAndDates = new SeasonYearsAndDates();
				int predId = rs.getInt("pred_id");
				int parentPredId = rs.getInt("parent_pred_id");
				if ((predId != 13006) && (parentPredId != 13006)) {
					seasonYearsAndDates.setPredId(predId);
					seasonYearsAndDates.setName(rs.getString("name"));
					seasonYearsAndDates.setSeasonYear(rs.getInt("season_year"));
					seasonYearsAndDates.setStartDate(rs.getString("start_date"));
					seasonYearsAndDates.setFinishDate(rs.getString("finish_date"));
					seasonYearsAndDates.setFirstName(rs.getString("first_name"));
					seasonYearsAndDates.setLastName(rs.getString("last_name"));
					seasonYearsAndDates.setPatrName(rs.getString("patr_name"));
					seasonYearsAndDates.setLastUpdate(rs.getString("last_update"));

					if (seasonYearsAndDates.getStartDate() != null) {
						startDateCnt++;
					}
					if (seasonYearsAndDates.getFinishDate() != null) {
						finishDateCnt++;
					}
					listSeasonYearsAndDates.add(seasonYearsAndDates);
				}
			}

			dor.setPreds(listSeasonYearsAndDates);
			dor.setName(name);
			dor.setPredId(dorId);
			int predsCnt = listSeasonYearsAndDates.size() == 0 ? 1 : listSeasonYearsAndDates.size();
			dor.setPerStartDate((startDateCnt * 100) / predsCnt);
			dor.setPerFinishDate((finishDateCnt * 100) / predsCnt);

			return dor;
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public List<Dor> getCtDor(int seasonYear, int dorId) throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		Connection con = null;
		CallableStatement st = null;
		ResultSet rs = null;
		List<Dor> listDor = new ArrayList<Dor>();
		Multimap<Integer, SeasonYearsAndDates> parentIdAndDatesMultimap = LinkedListMultimap.create();

		try {
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.get_dor(?, ?)}");
			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setInt(st, 2, dorId);
			TiDaoUtil.setInt(st, 3, seasonYear);
			st.execute();
			rs = (ResultSet) st.getObject(1);

			while (rs.next()) {
				int predId = rs.getInt("pred_id");
				int parentPredId = rs.getInt("parent_pred_id");
				if (predId != 13006) {
					// if CT then create Dor object and add it to Dor objects list
					if (parentPredId == 13006) {
						Dor dor = new Dor();
						dor.setName(rs.getString("name"));
						dor.setPredId(predId);
						dor.setPreds(new ArrayList<SeasonYearsAndDates>());
						listDor.add(dor);
					}
					// if not CT then it is depo, create seasonYearsAndDates and add to another list
					else {
						SeasonYearsAndDates seasonYearsAndDates = new SeasonYearsAndDates();
						seasonYearsAndDates.setPredId(predId);
						seasonYearsAndDates.setName(rs.getString("name"));
						seasonYearsAndDates.setSeasonYear(rs.getInt("season_year"));
						seasonYearsAndDates.setStartDate(rs.getString("start_date"));
						seasonYearsAndDates.setFinishDate(rs.getString("finish_date"));
						seasonYearsAndDates.setFirstName(rs.getString("first_name"));
						seasonYearsAndDates.setLastName(rs.getString("last_name"));
						seasonYearsAndDates.setPatrName(rs.getString("patr_name"));
						seasonYearsAndDates.setLastUpdate(rs.getString("last_update"));
						parentIdAndDatesMultimap.put(parentPredId, seasonYearsAndDates);
					}
				}
			}

			// process listDor and listSeasonYearsAndDates to get fulfilled List<Dor>
			for (Dor dor : listDor) {
				int predId = dor.getPredId();
				List<SeasonYearsAndDates> dates = new ArrayList<SeasonYearsAndDates>(
						parentIdAndDatesMultimap.get(predId));
				dor.setPreds(dates);

				// calculate percentage
				int startDateCnt = 0;
				int finishDateCnt = 0;
				for (SeasonYearsAndDates date : dates) {
					if (date.getStartDate() != null) {
						startDateCnt++;
					}
					if (date.getFinishDate() != null) {
						finishDateCnt++;
					}
				}
				int predsCnt = dates.size() == 0 ? 1 : dates.size();
				dor.setPerStartDate((startDateCnt * 100) / predsCnt);
				dor.setPerFinishDate((finishDateCnt * 100) / predsCnt);
			}

			return listDor;
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public String getNameForPredId(int predId) throws SQLException, ClassNotFoundException {
		Connection con = null;
		CallableStatement st = null;
		ResultSet rs = null;
		String name = null;
		try {
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.get_name_for_pred_id(?)}");
			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setInt(st, 2, predId);
			st.execute();
			rs = (ResultSet) st.getObject(1);

			if (rs.next()) {
				name = rs.getString("name");
			}
			return name;
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public List<PredIdsAndDates> getForSeasonYear(int seasonYear) throws SQLException {
		LOGGER.trace("start");
		Connection con = null;
		CallableStatement st = null;
		ResultSet rs = null;
		List<PredIdsAndDates> listOfIdsAndDates = new ArrayList<PredIdsAndDates>();
		try {
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.get_for_season_year(?)}");
			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setInt(st, 2, seasonYear);
			st.execute();
			rs = (ResultSet) st.getObject(1);

			while (rs.next()) {
				PredIdsAndDates idsAndDates = new PredIdsAndDates();
				idsAndDates.setPredId(rs.getInt("pred_id"));
				idsAndDates.setStartDate(rs.getString("start_date"));
				idsAndDates.setFinishDate(rs.getString("finish_date"));
				listOfIdsAndDates.add(idsAndDates);
				LOGGER.debug("Got pred_id = {} with valid start_date = {} and finish_date = {}",
						idsAndDates.getPredId(), idsAndDates.getStartDate(), idsAndDates.getFinishDate());
			}

			return listOfIdsAndDates;
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public List<SeasonYearsAndDates> getForPredId(int predId) throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		Connection con = null;
		CallableStatement st = null;
		ResultSet rs = null;
		List<SeasonYearsAndDates> listOfSeasonYearsAndDates = new ArrayList<SeasonYearsAndDates>();
		try {
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.get_for_pred_id(?)}");

			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setInt(st, 2, predId);
			st.execute();
			rs = (ResultSet) st.getObject(1);

			while (rs.next()) {
				SeasonYearsAndDates yearsAndDates = new SeasonYearsAndDates();
				yearsAndDates.setPredId(predId);
				yearsAndDates.setName(rs.getString("sname2"));
				yearsAndDates.setSeasonYear(rs.getInt("season_year"));
				yearsAndDates.setStartDate(rs.getString("start_date"));
				yearsAndDates.setFinishDate(rs.getString("finish_date"));
				yearsAndDates.setFirstName(rs.getString("first_name"));
				yearsAndDates.setLastName(rs.getString("last_name"));
				yearsAndDates.setPatrName(rs.getString("patr_name"));
				yearsAndDates.setLastUpdate(rs.getString("last_update"));
				listOfSeasonYearsAndDates.add(yearsAndDates);
				LOGGER.debug("Got season_year = {} with valid start_date = {} and finish_date = {}",
						yearsAndDates.getSeasonYear(), yearsAndDates.getStartDate(), yearsAndDates.getFinishDate());
			}

			Collections.sort(listOfSeasonYearsAndDates);
			return listOfSeasonYearsAndDates;
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	@Override
	public void invalidate(int asutUserId, int predId, int seasonYear) throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		try {
			int winterPeriodId = getWinterPeriodId(predId, seasonYear);
			if (winterPeriodId > 0) {
				updateT(asutUserId, predId, winterPeriodId);
			}
		} finally {
			LOGGER.trace("stop");
		}
	}

	@Override
	public void upsert(int asutUserId, int predId, int seasonYear, String startDate, String finishDate)
			throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		SeasonYearsAndDates rowWithValidDates = null;
		try {
			rowWithValidDates = getRow(predId, seasonYear);
			if (rowWithValidDates != null) {
				updateT(asutUserId, predId, getWinterPeriodId(predId, seasonYear));
			}

			insertRow(asutUserId, predId, seasonYear, startDate, finishDate);
		} finally {
			LOGGER.trace("stop");
		}
	}

	private int getWinterPeriodId(int predId, int seasonYear) throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		CallableStatement st = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.get_winter_period_id(?, ?)}");
			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setInt(st, 2, predId);
			TiDaoUtil.setInt(st, 3, seasonYear);
			st.execute();
			rs = (ResultSet) st.getObject(1);
			if (rs.next()) {
				int winterPeriodId = rs.getInt("winter_period_id");

				LOGGER.debug("Found row for pred_id = {} and season_year = {}: winter_period_id is = {}", predId,
						seasonYear, winterPeriodId);
				return winterPeriodId;
			} else {
				LOGGER.debug("Havent found row for pred_id = {} and season_year = {}", predId, seasonYear);
				return -1;
			}
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	private SeasonYearsAndDates getRow(int predId, int seasonYear) throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		CallableStatement st = null;
		ResultSet rs = null;
		Connection con = null;
		SeasonYearsAndDates rowWithValidDates = null;
		try {
			con = getMainConnection();
			st = con.prepareCall("{? = call winter_period.get_row(?, ?)}");
			st.registerOutParameter(1, Types.OTHER);
			TiDaoUtil.setInt(st, 2, predId);
			TiDaoUtil.setInt(st, 3, seasonYear);
			st.execute();
			rs = (ResultSet) st.getObject(1);
			if (rs.next()) {
				rowWithValidDates = new SeasonYearsAndDates();
				rowWithValidDates.setSeasonYear(rs.getInt("season_year"));
				rowWithValidDates.setStartDate(rs.getString("start_date"));
				rowWithValidDates.setFinishDate(rs.getString("finish_date"));
				rowWithValidDates.setFirstName(rs.getString("first_name"));
				rowWithValidDates.setLastName(rs.getString("last_name"));
				rowWithValidDates.setPatrName(rs.getString("patr_name"));
				rowWithValidDates.setLastUpdate(rs.getString("last_update"));

				LOGGER.debug("returned PredIdAndDates where pred_id = {} and seasonYear = {}", predId, seasonYear);
				return rowWithValidDates;
			} else {
				LOGGER.debug("Havent found row for pred_id = {} and season_year = {}", predId, seasonYear);
				return rowWithValidDates;
			}
		} finally {
			TiDaoUtil.close(st, rs);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	private void updateT(int asutUserId, int predId, int winterPeriodId) throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		CallableStatement st = null;
		Connection con = null;
		try {
			con = getMainConnection();
			st = con.prepareCall("{call winter_period.update_t(?, ?, ?)}");
			TiDaoUtil.setInt(st, 1, asutUserId);
			TiDaoUtil.setInt(st, 2, predId);
			TiDaoUtil.setInt(st, 3, winterPeriodId);
			st.execute();
			con.commit();

			LOGGER.debug("Updated row's t value in winter_period table where winter_period_id = {}", winterPeriodId);
		} finally {
			TiDaoUtil.close(st);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}

	private void insertRow(int asutUserId, int predId, int seasonYear, String startDate, String finishDate)
			throws SQLException, ClassNotFoundException {
		LOGGER.trace("start");
		CallableStatement st = null;
		Connection con = null;
		try {

			con = getMainConnection();
			st = con.prepareCall("{call winter_period.insert_row(?, ?, ?, ?, ?)}");
			TiDaoUtil.setInt(st, 1, asutUserId);
			TiDaoUtil.setInt(st, 2, predId);
			TiDaoUtil.setInt(st, 3, seasonYear);
			TiDaoUtil.setString(st, 4, startDate);
			TiDaoUtil.setString(st, 5, finishDate);
			st.execute();
			con.commit();

			LOGGER.debug("Inserted row in winter_period table where pred_id = {} and season_year = {}", predId,
					seasonYear);
		} finally {
			TiDaoUtil.close(st);
			TiDaoUtil.close(con);
			LOGGER.trace("stop");
		}
	}
}