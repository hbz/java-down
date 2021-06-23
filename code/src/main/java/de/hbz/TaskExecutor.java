package de.hbz;

import static de.hbz.Constants.CONTENT_URL;
import static de.hbz.Constants.PATH_TO;
import static de.hbz.LogManager.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Node;

/**
 * 
 * @author Alessio Pellerito
 *
 */

public class TaskExecutor implements Runnable {
	
	private Node n;
	private XMLParser parser;
	private Document docDcXML;
	private File logFile;
	
	
	public TaskExecutor(Node n, XMLParser parser, Document docDcXML, File logFile) {
		this.n = n;
		this.parser = parser;
		this.docDcXML = docDcXML;
		this.logFile = logFile;
	}
	
	
	@Override
	public void run() {
		
		try {
			
			Node nodeFirstIdent = n.selectSingleNode("dc:identifier");
			Boolean hasAttrContentURL = nodeFirstIdent.valueOf("@xsi:type").equals(CONTENT_URL);
		
			
			if(hasAttrContentURL) {
				
				URL urlInIdent = new URL(nodeFirstIdent.getText());
				
				File newPath = new File(PATH_TO.toString(), urlInIdent.getPath());

				int dotNumInUrl = (int) newPath.getAbsolutePath().chars().filter(num -> num == '.').count();
				
				switch(dotNumInUrl) {
					case 1:
						parser.replaceURLinDcXML(docDcXML, nodeFirstIdent, newPath.getAbsolutePath());
						FileUtils.copyURLToFile(urlInIdent, newPath);
						//System.out.println("Downloaded: " + nodeFirstIdent.getText());
						logger.info("Record downloaded to: {}", nodeFirstIdent.getText());
						break;
					
					default:
						logFileFormat(n, "URL is not valid");
						//System.out.println("Not Downloaded: URL not valid");
						logger.warn("Record not downloaded, because URL is not valid");
						break;
				}
		
			}

			if(!hasAttrContentURL) {
				logFileFormat(n, "ContentURL is missing");
				//System.out.println("Not Downloaded: ContentUrl is missing");
				logger.warn("Record not downloaded, because ContentUrl is missing");
			}

			FileUtils.writeLines(logFile, logText);
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	

}
