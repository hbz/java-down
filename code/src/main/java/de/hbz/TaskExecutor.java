package de.hbz;

import static de.hbz.Constants.CONTENT_URL;
import static de.hbz.LogManager.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Node;

/**
 * 
 * @author Alessio Pellerito
 *
 */

public class TaskExecutor implements Runnable {
	
	private Node node;
	private XMLParser parser;
	private Document docDcXML;
	private File logFile;
	private Path toStream;
	
	
	public TaskExecutor(Node node, XMLParser parser, Document docDcXML, File logFile, Path toStream) {
		this.node = node;
		this.parser = parser;
		this.docDcXML = docDcXML;
		this.logFile = logFile;
		this.toStream = toStream;
	}
	
	
	
	@Override
	public void run() {
		
		try {
			
			Node nodeFirstIdent = node.selectSingleNode("dc:identifier");
			Boolean hasAttrContentURL = nodeFirstIdent.valueOf("@xsi:type").equals(CONTENT_URL);
			String log = null;
			
			if(hasAttrContentURL) {
				
				URL urlInIdent = new URL(nodeFirstIdent.getText());
				
				File newPath = new File(toStream.toString(), urlInIdent.getPath());

				int dotNumInUrl = (int) newPath.getAbsolutePath().chars().filter(num -> num == '.').count();
				
				switch(dotNumInUrl) {
					case 1:
						parser.replaceURLinDcXML(docDcXML, nodeFirstIdent, newPath.getAbsolutePath());
						FileUtils.copyURLToFile(urlInIdent, newPath);
						logger.info("Record downloaded to: {}", nodeFirstIdent.getText());
						break;
					
					default:
						//logFileFormat(node, "URL is not valid");
						log += "<!-- Record wasn't downloaded, because URL is not valid -->\n";
						log += node.asXML() + "\n";
						logger.warn("Record not downloaded, because URL is not valid");
						break;
				}
		
			}

			if(!hasAttrContentURL) {
				//logFileFormat(node, "ContentURL is missing");
				log += "<!-- Record wasn't downloaded, because ContentUrl is missing -->\n";
				log += node.asXML() + "\n";
				logger.warn("Record not downloaded, because ContentUrl is missing");
			}

			//FileUtils.writeLines(logFile, log);
			FileUtils.writeStringToFile(logFile, log, StandardCharsets.UTF_8);
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	

}
