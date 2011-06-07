package com.wrapper.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlFeedPuller {

	static String feedUrl = "http://50.19.255.176/chister/go/getnewsxml.doit";
	static HashMap<String, String> siteMap = new HashMap<String, String>();
	
	public static String getUrl(String siteName) {
		return siteMap.get(siteName);
	}
	
	public static List<String> pullMobileSites() {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(feedUrl);
		ArrayList<String> urls = new ArrayList<String>();
		try {
			HttpResponse response = client.execute(get);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				InputStream stream = response.getEntity().getContent();
				
				DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
				builderFactory.setNamespaceAware(true);
				DocumentBuilder builder = builderFactory.newDocumentBuilder();
				Document document = builder.parse(stream);

				
				NodeList nodes = document.getElementsByTagName("url");

				for (int i=0; i<nodes.getLength(); i++) {
					Node node = nodes.item(i);
					String[] site = node.getTextContent().split(",");
					
					urls.add(site[0]);
					siteMap.put(site[0], site[1]);
				} 
			}
			
		} catch (ClientProtocolException e) {
			urls.add("1 "+e.getMessage());
		} catch (IOException e) {
			urls.add("2 "+e.getMessage());
		} catch (ParserConfigurationException e) {
			urls.add("4 "+e.getMessage());
		} catch (SAXException e) {
			urls.add("5 "+e.getMessage());
		}   
		
		return urls;
	}
}
