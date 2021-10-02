package org.bashirov.distributed.simpleservice.Controller;

import org.bashirov.distributed.simpleservice.Dao.Dao;
import org.bashirov.distributed.simpleservice.Entity.Information;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

@RestController
public class Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

	private Dao dao;

	public Controller(Dao dao) {
		this.dao = dao;
	}

	@PostMapping("/insert")
	public @ResponseBody ResponseEntity<String> insert(@RequestBody String infoJson)
			throws JsonMappingException, JsonProcessingException {
		LOGGER.trace("start");
		ResponseEntity<String> response = null;
		try {
			LOGGER.debug("Got infoJson = " + infoJson.toString());
			Gson gson = new Gson();
			Information info = gson.fromJson(infoJson, Information.class);
			LOGGER.debug("converted infoJson to info = " + info.toString());

			Boolean ifQueuedInfo = dao.insert(info);

			if (ifQueuedInfo) {
				response = ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body("success");
			} else {
				response = ResponseEntity.status(500).contentType(MediaType.TEXT_HTML).body("fail");
			}

		} finally {
			LOGGER.trace("stop");
		}
		return response;
	}

}
