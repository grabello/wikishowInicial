package com.wikishow.job;

import com.wikishow.dao.TVDBDAOMongo;
import com.wikishow.entity.TVDBData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
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
@Service
public class GetTVShowDataJob {

    public static final String TVDB_API_KEY = "57550B35915D895C";
    public static final String GET_TIME_URL = "http://thetvdb.com/api/Updates.php?type=none";

    @Autowired
    TVDBDAOMongo tvdbdaoMongo;

    public void getTVShowData() throws Exception {

        URL url = new URL(GET_TIME_URL);
        URLConnection connection = url.openConnection();

        Document doc = parseXML(connection.getInputStream());

        TVDBData tvdbData = tvdbdaoMongo.findById();

        if (tvdbData == null) {
            tvdbData = new TVDBData();
            tvdbData.setLastUpdateTime("11111");
            tvdbdaoMongo.addTVDBData(tvdbData);
        } else {
            tvdbData.setLastUpdateTime("22222");
            tvdbdaoMongo.updateTVDBData(tvdbData);
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
