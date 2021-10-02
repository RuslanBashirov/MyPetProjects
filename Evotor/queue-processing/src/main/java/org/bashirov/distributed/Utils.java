package org.bashirov.distributed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bashirov.distributed.api.Service1Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Utils {

	public static final String SERVICE_URL = "PASTE URL"; //http://localhost:8080/

	public static String recordsFolder = "C:\\Users\\bashi\\OneDrive\\Desktop\\MyPetProjects\\Evotor\\simple-service\\records";
	public static String recordsFolder1 = "C:\\Users\\bashi\\OneDrive\\Desktop\\MyPetProjects\\Evotor\\simple-service\\records\\service-1\\";
	public static String recordsFolder2 = "C:\\Users\\bashi\\OneDrive\\Desktop\\MyPetProjects\\Evotor\\simple-service\\records\\service-2\\";
	public static String lastFilenameFolderService1 = "C:\\Users\\bashi\\OneDrive\\Desktop\\MyPetProjects\\Evotor\\simple-service\\records"
			+ "\\last-processed-service-1.txt";
	public static String lastFilenameFolderService2 = "C:\\Users\\bashi\\OneDrive\\Desktop\\MyPetProjects\\Evotor\\simple-service\\records"
			+ "\\last-processed-service-2.txt";

	// in file 3rd line looks like 2007-12-03T10:15:30
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-ddhh:mm:ss");

	public static Gson getGson() {
		return new GsonBuilder().setLenient().create();
	}

	public static Retrofit getRetrofit(Gson gson) {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(SERVICE_URL)
				.addConverterFactory(ScalarsConverterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(gson)).build();
		return retrofit;
	}

	public static Service1Api getService1Api(Retrofit retrofit) {
		Service1Api studentAPI = retrofit.create(Service1Api.class);
		return studentAPI;
	}

	public static String getInformationFromFile(String lastFilename) {
		String infoJson = null;
		try (BufferedReader br = new BufferedReader(new FileReader(lastFilename))) {
			infoJson = br.readLine();
		} catch (IOException io) {
			io.printStackTrace();
		}
		return infoJson;
	}

	public static String getLastFilename(int serviceId) {
		String filename = null;
		BufferedReader br = null;
		try {
			if (serviceId == 1) {
				br = new BufferedReader(new FileReader(Utils.lastFilenameFolderService1));
			} else if (serviceId == 2) {
				br = new BufferedReader(new FileReader(Utils.lastFilenameFolderService2));
			}
			if (br != null) {
				filename = br.readLine();
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
		return filename;
	}

	public static List<String> getNextFilenames(String lastFilename, int serviceId) {
		File folder = new File(Utils.recordsFolder1);
		File[] listOfFilesAndFolders = folder.listFiles(); // XXX may be not sorted
		List<String> listOfFiles = new ArrayList<String>();

		if (serviceId == 1) {
			boolean ifLastFileBefore = false;
			for (int i = 0; i < listOfFilesAndFolders.length; i++) {
				if (listOfFilesAndFolders[i].isFile()) {
					if (!ifLastFileBefore) {
						if (lastFilename.equals(Utils.recordsFolder1 + listOfFilesAndFolders[i].getName())) {
							ifLastFileBefore = true;
						}
					} else {
						listOfFiles.add(Utils.recordsFolder1 + listOfFilesAndFolders[i].getName());
					}
				}
			}
		} else {
			boolean ifLastFileBefore = false;
			for (int i = 0; i < listOfFilesAndFolders.length; i++) {
				if (listOfFilesAndFolders[i].isFile()) {
					if (!ifLastFileBefore) {
						if (lastFilename.equals(Utils.recordsFolder2 + listOfFilesAndFolders[i].getName())) {
							ifLastFileBefore = true;
						}
					} else {
						listOfFiles.add(Utils.recordsFolder2 + listOfFilesAndFolders[i].getName());
					}
				}
			}
		}

		return listOfFiles;
	}

	public static void refreshLastFilenameWhenSoleRecord(int serviceId) {
		try {
			File folder = null;
			if (serviceId == 1) {
				folder = new File(Utils.recordsFolder1);
			}
			else if (serviceId == 2) {
				folder = new File(Utils.recordsFolder2);
			}
			File[] listOfFilesAndFolders = folder.listFiles();// XXX remove folders from listOfFilesAndFolders
			if (listOfFilesAndFolders.length == 1) {
				Path path = Paths.get(listOfFilesAndFolders[0].getName());
				Files.writeString(path, listOfFilesAndFolders[0].getName(), StandardCharsets.UTF_8);
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public static void refreshLastFilename(String filename, int serviceId) {
		Path path = null;
		try {
			if (serviceId == 1) {
				path = Paths.get(Utils.lastFilenameFolderService1);
			} else if (serviceId == 2) {
				path = Paths.get(Utils.lastFilenameFolderService2);
			}
			Files.writeString(path, filename, StandardCharsets.UTF_8);
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

}
