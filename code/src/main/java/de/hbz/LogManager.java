package de.hbz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Alessio Pellerito
 *
 */

public class LogManager {

	protected List<String> logText = new ArrayList<>();
	protected static final Logger logger = LoggerFactory.getLogger("JavaDownloader");
	

	public synchronized void logFileFormat(Node n, String reason) {
		logText.add("<!-- Record wasn't downloaded, because " + reason + " -->");
		logText.add(n.asXML() + "\n");		
	}
	
	public synchronized void print(File file) throws IOException {
		FileUtils.writeLines(file, logText, true);
	}	

}
