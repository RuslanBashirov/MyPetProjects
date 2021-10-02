package org.bashirov.distributed;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bashirov.distributed.controller.AddInformationController;

public class Main {

	public static void main(String[] args) {
		try {
			while (true) {
				AddInformationController controller = new AddInformationController();
				String lastFilename1 = Utils.getLastFilename(1);
				String lastFilename2 = Utils.getLastFilename(2);

				if (lastFilename1 != null) {
					List<String> nextFilenames1 = Utils.getNextFilenames(lastFilename1, 1);
					int counter = 0;
					System.out.println("files 1 folder size: " + nextFilenames1.size());
					for (String filename : nextFilenames1) {
						String infoJson = Utils.getInformationFromFile(filename);
						Utils.refreshLastFilename(filename, 1);
						System.out.println(
								"Service-1, #" + counter++ + ": " + infoJson + ": " + controller.start(infoJson));
					}
				}
				
				if (lastFilename1 != null) {
					List<String> nextFilenames2 = Utils.getNextFilenames(lastFilename2, 2);
					int counter = 0;
					System.out.println("files 2 folder size: " + nextFilenames2.size());
					for (String filename : nextFilenames2) {
						String infoJson = Utils.getInformationFromFile(filename);
						Utils.refreshLastFilename(filename, 2);
						System.out.println(
								"Service-2, #" + counter++ + ": " + infoJson + ": " + controller.start(infoJson));
					}
				}
				TimeUnit.MILLISECONDS.sleep(5000);
			}
		} catch (Throwable e) {
			System.out.println("main function exc: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
