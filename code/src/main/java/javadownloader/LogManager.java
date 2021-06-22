package javadownloader;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Alessio Pellerito
 *
 */

public class LogManager {

	protected static List<String> logText = new ArrayList<>();
	protected static final Logger logger = LoggerFactory.getLogger("JavaDownloader");
	
	public static synchronized void logFileFormat(Node n, String reason) {
		logText.add("<!-- Record wasn't downloaded, because " + reason + " -->");
		logText.add(n.asXML() + "\n");
		
	}

	

}
