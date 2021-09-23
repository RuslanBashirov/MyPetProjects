package ru.transinfocom.nbd2.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ru.transinfocom.nbd2.config.jwt.JwtProvider;
import ru.transinfocom.nbd2.dao.Dao;
import ru.transinfocom.nbd2.dao.Service;
import ru.transinfocom.nbd2.entity.ChangesHistory;
import ru.transinfocom.nbd2.entity.Dor;
import ru.transinfocom.nbd2.entity.DorsWithTotal;
import ru.transinfocom.nbd2.entity.PredIdsAndDates;
import ru.transinfocom.nbd2.entity.SeasonYearsAndDates;
import ru.transinfocom.nbd2.entity.Total;
import ru.transinfocom.nbd2.entity.requests.GetForPredIdRequest;
import ru.transinfocom.nbd2.entity.requests.GetForSeasonYearRequest;
import ru.transinfocom.nbd2.entity.requests.InvalidateRequest;
import ru.transinfocom.nbd2.entity.requests.UpsertRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

	private Service dao;

	@Autowired
	public JwtProvider jwtProvider;

	public Controller() throws SQLException, ClassNotFoundException {
		this.dao = new Dao();
	}

	@GetMapping("/getChangesHistory")
	@ResponseBody
	public List<ChangesHistory> getChangesHistory(HttpServletRequest request, @RequestParam int seasonYear,
			@RequestParam int predId) {
		LOGGER.trace("start");
		List<ChangesHistory> changesHistoryList = null;
		try {
			changesHistoryList = dao.getChangesHistory(predId, seasonYear);
			return changesHistoryList;
		} catch (SQLException sql) {
			LOGGER.error("SQLException while closing connection", sql);
		} catch (Throwable t) {
			LOGGER.error("Unknown exception", t);
		} finally {
			LOGGER.trace("stop");
		}
		return changesHistoryList;
	}

	@GetMapping("/getDorsWithTotal")
	@ResponseBody
	public DorsWithTotal getDorsWithTotal(HttpServletRequest request, @RequestParam int seasonYear) {
		LOGGER.trace("start");
		DorsWithTotal dorsWithTotal = null;
		try {
			String token = JwtProvider.getTokenFromRequest(request);
			int predId = jwtProvider.getPredIdFromToken(token);
			dorsWithTotal = processGetDorsWithTotal(predId, seasonYear);
			return dorsWithTotal;
		} catch (SQLException sql) {
			LOGGER.error("SQLException while closing connection", sql);
		} catch (Throwable t) {
			LOGGER.error("Unknown exception", t);
		} finally {
			LOGGER.trace("stop");
		}
		return dorsWithTotal;
	}

	@PostMapping("/getForSeasonYear")
	@ResponseBody
	public List<PredIdsAndDates> getForSeasonYear(@RequestBody GetForSeasonYearRequest getForSeasonYearRequest) {
		LOGGER.trace("start");
		List<PredIdsAndDates> dates = null;
		try {
			dates = dao.getForSeasonYear(getForSeasonYearRequest.getSeasonYear());
			return dates;
		} catch (SQLException sql) {
			LOGGER.error("SQLException while closing connection", sql);
		} catch (Throwable t) {
			LOGGER.error("Unknown exception", t);
		} finally {
			LOGGER.trace("stop");
		}
		return dates;
	}

	@PostMapping("/getForPredId")
	@ResponseBody
	public List<SeasonYearsAndDates> getForPredId(@RequestBody GetForPredIdRequest getForPredIdRequest) {
		LOGGER.trace("start");
		List<SeasonYearsAndDates> dates = null;
		try {
			dates = dao.getForPredId(getForPredIdRequest.getPredId());
			return dates;
		} catch (SQLException sql) {
			LOGGER.error("SQLException while closing connection", sql);
		} catch (Throwable t) {
			LOGGER.error("Unknown exception", t);
		} finally {
			LOGGER.trace("stop");
		}
		return dates;
	}

	@PostMapping("/invalidate")
	@ResponseBody
	public void invalidate(HttpServletRequest request, @RequestBody InvalidateRequest invalidateRequest) {
		LOGGER.trace("start");
		try {
			String token = JwtProvider.getTokenFromRequest(request);
			int asutUserId = jwtProvider.getAsutUserIdFromToken(token);
			dao.invalidate(asutUserId, invalidateRequest.getPredId(), invalidateRequest.getSeasonYear());
		} catch (SQLException sql) {
			LOGGER.error("SQLException while closing connection", sql);
		} catch (Throwable t) {
			LOGGER.error("Unknown exception", t);
		} finally {
			LOGGER.trace("stop");
		}
	}

	@PostMapping("/upsert")
	@ResponseBody
	public void upsert(HttpServletRequest request, @RequestBody UpsertRequest upsertRequest) throws Exception {
		LOGGER.trace("start");
		try {
			int predId = upsertRequest.getPredId();
			int seasonYear = upsertRequest.getSeasonYear();
			String startDate = upsertRequest.getStartDate();
			String finishDate = upsertRequest.getFinishDate();

			if ((startDate == null) && (finishDate == null)) {
				LOGGER.error("startDate and finishDate are null values");
				throw new Exception("startDate and finishDate are null values");
			}

			String token = JwtProvider.getTokenFromRequest(request);
			int asutUserId = jwtProvider.getAsutUserIdFromToken(token);
			dao.upsert(asutUserId, predId, seasonYear, startDate, finishDate);
		} catch (SQLException sql) {
			LOGGER.error("SQLException while closing connection", sql);
		} catch (Throwable t) {
			LOGGER.error("Unknown exception", t);
		} finally {
			LOGGER.trace("stop");
		}
	}

	private DorsWithTotal processGetDorsWithTotal(int inputDorId, int seasonYear) throws SQLException {
		LOGGER.trace("start");
		DorsWithTotal dorsWithTotal = new DorsWithTotal();
		List<Dor> listDors = null;
		try {
			String inputDorName = dao.getNameForPredId(inputDorId);

			// navigate over dao's getDor(..) or getCtDor(..) methods
			if (inputDorId == 13006) {
				listDors = dao.getCtDor(seasonYear, inputDorId);
			} else {
				listDors = new ArrayList<Dor>();
				listDors.add(dao.getDor(seasonYear, inputDorId, inputDorName));
			}

			// calculate Total
			Total total = new Total();
			total.setName(new String("Итого по " + inputDorName));

			int sumPerStart = 0;
			int sumPerFinish = 0;
			for (Dor currDor : listDors) {
				sumPerStart += currDor.getPerStartDate();
				sumPerFinish += currDor.getPerFinishDate();

			}

			if (listDors.size() != 0) {
				total.setPerStartDate(sumPerStart / listDors.size());
				total.setPerFinishDate(sumPerFinish / listDors.size());
			} else {
				total.setPerStartDate(0);
				total.setPerFinishDate(0);
			}

			dorsWithTotal.setDors(listDors);
			dorsWithTotal.setTotal(total);

			return dorsWithTotal;
		} catch (SQLException sql) {
			LOGGER.error("SQLException", sql);
		} catch (Throwable t) {
			LOGGER.error("Unknown exception", t);
		} finally {
			LOGGER.trace("stop");
		}
		return dorsWithTotal;
	}
}
