package com.wikishow.job;

import com.wikishow.dao.TVDBDAOMongo;
import com.wikishow.entity.TVDBData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 9/18/13
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
@EnableScheduling
public class GetTVShowDataJob {

    public static final String TVDB_API_KEY = "57550B35915D895C";
    public static final String GET_TIME_URL = "http://thetvdb.com/api/Updates.php?type=none";
    @Autowired
    TVDBDAOMongo tvdbdaoMongo;

    @Scheduled(cron = "*/10 * * * * *")
    public void getTVShowData() throws Exception {

        URL url = new URL(GET_TIME_URL);
        URLConnection connection = url.openConnection();

        Document doc = parseXML(connection.getInputStream());
        System.out.println("******************************************************");

        TVDBData tvdbData = tvdbdaoMongo.findById();
        NodeList descNodes = doc.getElementsByTagName("Time");

        for (int i = 0; i < descNodes.getLength(); i++) {
            System.out.println(descNodes.item(i).getTextContent());
        }


        if (tvdbData == null) {
            tvdbData = new TVDBData();
            tvdbData.setLastUpdateTime("11111");
            tvdbdaoMongo.addTVDBData(tvdbData);
        } else {
            tvdbData.setLastUpdateTime("22222");
            tvdbdaoMongo.updateTVDBData("_id", tvdbData.getId(), "lastUpdateTime", tvdbData.getLastUpdateTime());
        }
    }

    private Document parseXML(InputStream stream)
            throws Exception {
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
        try {
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
            doc = objDocumentBuilder.parse(stream);
        } catch (Exception ex) {
            throw ex;
        }

        return doc;
    }

}
