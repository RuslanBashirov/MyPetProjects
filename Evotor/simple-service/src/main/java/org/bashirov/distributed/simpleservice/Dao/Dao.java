package org.bashirov.distributed.simpleservice.Dao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.bashirov.distributed.simpleservice.Utils;
import org.bashirov.distributed.simpleservice.Entity.Information;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class Dao {

	private static final Logger LOGGER = LoggerFactory.getLogger(Dao.class); 

	public boolean insert(Information info) {
		LOGGER.debug("insert into " + info.toString());
		// write to two local queues 
		String filename = LocalDateTime.now().format(Utils.formatter) + "_" + UUID.randomUUID().toString() + ".txt";
		Path path1 = Paths.get(Utils.recordsFolder1 + filename);
		Path path2 = Paths.get(Utils.recordsFolder2 + filename);
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String infoJson = gson.toJson(info);
			Files.writeString(path1, infoJson, StandardCharsets.UTF_8);
			Files.writeString(path2, infoJson, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOGGER.debug("io exc"); 
			return false;
		}
		return true;
	}
}
