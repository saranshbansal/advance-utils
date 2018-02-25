package com.advanceutils.util.filehandling;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sbansal
 */

@Service
public class FileOpsHandler {

	private static final Logger logger = LoggerFactory.getLogger(FileOpsHandler.class);

	/**
	 * Read a file from a given path.
	 * 
	 * @param thisPath
	 * @throws IOException
	 */
	public static File readFiles(String thisPath) throws IOException {
		logger.info("Enter method FileOpsHandler.readFiles().");
		File fileFromServer = null;
		final StringBuilder payload = new StringBuilder();
		if (Paths.get(thisPath).toFile().exists()) {
			try (Stream<Path> paths = Files.walk(Paths.get(thisPath))) {
				logger.info("Looking for files...");
				for (Path filePath : paths.toArray(Path[]::new)) {
					if (null != filePath && filePath.toFile().isFile()) {
						fileFromServer = new File(filePath.toString());
						payload.append(fileFromServer.getName());
						logger.debug("File exists in location: " + fileFromServer.getName());
					}
				}
			}
		} else {
			logger.debug(thisPath + " - No such file path exists!!!");
		}
		logger.info("Exit method FileOpsHandler.readFiles().");
		return fileFromServer;
	}

	/**
	 * Store file at a given path
	 * 
	 * @param attachment
	 * @param path
	 * @param persitanceFileName
	 * @return
	 */
	public static String storeFile(MultipartFile attachment, String path, String persitanceFileName) {
		String fileName = null;
		try {
			if (null == attachment) {
				logger.error("FILE IS EMPTY");
			}
			fileName = attachment.getOriginalFilename();
			if (persitanceFileName != null) {
				fileName = persitanceFileName;
			}
			logger.info("Storage File Path: " + path + " | File Name: " + fileName);
			File storageLocation = new File(path, fileName);
			// Create Parent Folder Hierarchy if not exists
			if (!storageLocation.getParentFile().exists()) {
				storageLocation.getParentFile().mkdirs();
				logger.info("creating path === " + storageLocation.getAbsolutePath());
			}
			attachment.transferTo(storageLocation);
		} catch (IOException e) {
			logger.error("FILE_01_02 - Unable to get file from IO stream", e);
		}
		return fileName;
	}

	/**
	 * Convert File to MultipartFile
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static MultipartFile convertFileToMultipart(File file) throws IOException {
		FileInputStream input = new FileInputStream(file);
		logger.info(file.getAbsolutePath());
		return new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
	}

}
