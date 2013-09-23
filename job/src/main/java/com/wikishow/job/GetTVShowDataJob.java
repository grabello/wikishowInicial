package com.wikishow.job;

import com.wikishow.entity.*;
import com.wikishow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    public static final String GET_MIRROR_URL = "http://thetvdb.com/api/" + TVDB_API_KEY + "/mirrors.xml";
    public static final String GET_UPDATE_URL = "http://thetvdb.com/api/Updates.php?type=all&time=";
    public static final String GET_SERIES_ZIP = "%zipmirror%/api/" + TVDB_API_KEY + "/series/%seriesid%/all/%language%.zip";
    public static final int XML_FILE = 1;
    public static final int BANNER_FILE = 2;
    public static final int ZIP_FILE = 4;
    @Autowired
    TVDBRepository tvDBRepository;
    @Autowired
    CastRepository castRepository;
    @Autowired
    EpisodeRepository episodeRepository;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    TVShowRepository tvShowRepository;
    private String XMLMirror = null;
    private String bannerMirror = null;
    private String zipMirror = null;
    private String timeUpdate = null;

    @Scheduled(cron = "*/10 * * * * *")
    public void getTVShowData() throws Exception {

        URL mirrorUrl = new URL(GET_MIRROR_URL);
        URLConnection mirrorConnection = mirrorUrl.openConnection();
        Document mirror = parseXML(mirrorConnection.getInputStream());

        NodeList mirrorNodeList = mirror.getElementsByTagName("Mirror");

        // Mascaras definidas pela API. 1 é mirror com XML, 2 é mirror com banner e 4 é mirror com zip
        for (int i = 0; i < mirrorNodeList.getLength(); i++) {
            Node mirrorNode = mirrorNodeList.item(i);
            NodeList mirrorChild = mirrorNode.getChildNodes();
            String url = null;
            for (int j = 0; j < mirrorChild.getLength(); j++) {

                if (mirrorChild.item(j).getNodeName().equals("mirrorpath")) {
                    url = mirrorChild.item(j).getTextContent();
                }
                if (mirrorChild.item(j).getNodeName().equals("typemask")) {
                    int maskValue = Integer.valueOf(mirrorChild.item(j).getTextContent());
                    if ((maskValue & XML_FILE) == XML_FILE) {
                        XMLMirror = url;
                    }

                    if ((maskValue & BANNER_FILE) == BANNER_FILE) {
                        bannerMirror = url;
                    }

                    if ((maskValue & ZIP_FILE) == ZIP_FILE) {
                        zipMirror = url;
                    }
                }
            }

        }

        TVDBData tvdbData = tvDBRepository.findById();

//        URL updateUrl = new URL(GET_UPDATE_URL + tvdbData.getLastUpdateTime());
        URL updateUrl = new URL(GET_UPDATE_URL + 1);
        URLConnection updateConnection = updateUrl.openConnection();

        Document update = parseXML(updateConnection.getInputStream());

        NodeList descNodes = update.getElementsByTagName("Time");

        for (int i = 0; i < descNodes.getLength(); i++) {
            timeUpdate = descNodes.item(i).getTextContent();
        }

        NodeList seriesNode = update.getElementsByTagName("Series");
        System.out.println("Tamanho do update = " + seriesNode.getLength());
        for (int i = 0; i < seriesNode.getLength(); i++) {
            String url = GET_SERIES_ZIP.replace("%zipmirror%", zipMirror). //
                    replace("%seriesid%", seriesNode.item(i).getTextContent()). //
                    replace("%language%", "pt");
            processSeries(seriesNode.item(i).getTextContent(), url, "pt");
        }

        if (tvdbData == null) {
            tvdbData = new TVDBData();
            tvdbData.setLastUpdateTime(timeUpdate);
            tvdbData.setMirror(XMLMirror);
            tvDBRepository.addTVDBData(tvdbData);
        } else {
            tvdbData.setLastUpdateTime(timeUpdate);
            tvdbData.setMirror(XMLMirror);
            tvDBRepository.updateTVDBData("_id", tvdbData.getId(), "lastUpdateTime", tvdbData.getLastUpdateTime());
        }
    }

    private Document parseXML(InputStream stream) {
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
        objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
            doc = objDocumentBuilder.parse(stream);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return doc;
    }

    private void processSeries(String id, String url, String language) {
        try {
            downloadZipFile(id, url, language);
            File folder = unzipFile(id, language);
            if (folder != null) {
                File[] files = folder.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File xml = files[i];
                    if (xml.getName().equals(language + ".xml")) {
                        Document tvShow = parseXML(new FileInputStream(xml));
                        saveSeriesXML(tvShow, language);
                        saveEpisodesXML(tvShow, language);
                        xml.delete();
                    }

                    if (xml.getName().equals("actors.xml")) {
                        Document actors = parseXML(new FileInputStream(xml));
                        System.out.println("São os atores");
                        saveActorsXML(actors);
                        xml.delete();
                    }

                    if (xml.getName().equals("banners.xml")) {
                        System.out.println("São os banners");
                        xml.delete();
                    }
                }
                folder.delete();
            }

        } catch (IOException e) {
            System.err.println("m=processSeries,id=" + id + "url=" + url + "language=" + language);
        }

    }

    private void saveSeriesXML(Document tvShow, String language) {

        NodeList tvShowData = tvShow.getElementsByTagName("Series");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        TVShowEntity tvShowEntity;
        String id = null;
        String tvShowName = null;
        String network = null;
        Date firstAired = null;
        String overview = null;
        Boolean isEnded = null;
        String[] actors = null;
        Node showData = tvShowData.item(0);
        NodeList attributes = showData.getChildNodes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            if (item.getNodeName().equals("id")) {
                id = item.getTextContent();
            }

            if (item.getNodeName().equals("SeriesName")) {
                tvShowName = item.getTextContent();
            } else if (item.getNodeName().equals("Network")) {
                network = item.getTextContent();
            } else if (item.getNodeName().equals("FirstAired")) {
                if (item.getTextContent() != null && !item.getTextContent().isEmpty()) {
                    try {
                        firstAired = formatter.parse(item.getTextContent());
                    } catch (ParseException e) {
                        System.err.println("Nao foi possivel fazer o parse da data.");
                    }
                }
            } else if (item.getNodeName().equals("Overview")) {
                overview = item.getTextContent();
            } else if (item.getNodeName().equals("Status")) {
                isEnded = item.getTextContent().equals("Ended");
            } else if (item.getNodeName().equals("Actors")) {
                actors = item.getTextContent().split("\\|");
            }
        }

        tvShowEntity = tvShowRepository.findById(id);
        boolean isNew = false;

        if (tvShowEntity == null) {
            tvShowEntity = new TVShowEntity();
            isNew = true;

        }
        tvShowEntity.setId(id);
        tvShowEntity.setTvShowName(tvShowName);
        tvShowEntity.setEnded(isEnded);
        tvShowEntity.setFirstAired(firstAired);
        tvShowEntity.setNetwork(network);
        if (language.equals("pt")) {
            tvShowEntity.setOverview_pt(overview);
        } else {
            tvShowEntity.setOverview_en(overview);
        }
        tvShowEntity.setCast(findCast(actors, "Actor"));

        if (isNew) {
            tvShowRepository.addTVShowData(tvShowEntity);
        } else {
            //tvShowRepository.updateTVShow();
        }
    }

    private List<CastEntity> findCast(String[] names, String type) {
        List<CastEntity> actors = new ArrayList<CastEntity>();
        for (String name : names) {
            CastEntity actor = castRepository.findByName(name);
            if (actor == null) {
                actor = new CastEntity();
                actor.setName(name);
                actor.setType(type);
                castRepository.addCastData(actor);
            } else {
                if (!actor.getType().contains(type)) {
                    String newType = actor.getType() + "|" + type;
                    actor.setType(newType);
                    Map<String, String> updateMap = new HashMap<String, String>();
                    updateMap.put("type", newType);
                    castRepository.updateCast("name", name, updateMap);
                }
            }
            actors.add(actor);
        }
        return actors;
    }

    private void saveBannersXML(File bannersXML) {

    }

    private void saveActorsXML(Document actor) {
        NodeList actorsList = actor.getElementsByTagName("Actor");

        for (int i = 0; i < actorsList.getLength(); i++) {
            Node actorNode = actorsList.item(i);
            NodeList attributes = actorNode.getChildNodes();
            String id = null;
            String name = null;
            String role = null;
            for (int j = 0; j < attributes.getLength(); j++) {
                Node item = attributes.item(j);

                if (item.getNodeName().equals("id")) {
                    id = item.getTextContent();
                } else if (item.getNodeName().equals("Name")) {
                    name = item.getTextContent();
                } else if (item.getNodeName().equals("Role")) {
                    role = item.getTextContent();
                }
            }

            CastEntity actorEntity = castRepository.findByName(name);
            if (actorEntity == null) {
                actorEntity = new CastEntity();
                actorEntity.setType("Actor");
                actorEntity.setName(name);
                actorEntity.setId(id);
                actorEntity.setRole(role);
                castRepository.addCastData(actorEntity);
            } else {
                Map<String, String> updateMap = new HashMap<String, String>();
                if (!actorEntity.getRole().contains(role)) {
                    role = actorEntity.getRole() + "|" + role;
                    actorEntity.setRole(role);
                    updateMap.put("role", role);
                }

                if (!actorEntity.getType().contains("Actor")) {
                    String type = actorEntity.getType() + "|Actor";
                    actorEntity.setType(type);
                    updateMap.put("type", type);
                }
                castRepository.updateCast("name", name, updateMap);
            }
        }
    }

    private void saveEpisodesXML(Document episodes, String language) {
        NodeList episodeNodeList = episodes.getElementsByTagName("Episode");

        for (int i = 0; i < episodeNodeList.getLength(); i++) {
            Node episodeNode = episodeNodeList.item(i);
            NodeList episodeAttributes = episodeNode.getChildNodes();
            String id = null;
            String name = null;
            Integer episodeNumber = null;
            Integer seasonNumber = null;
            String seasonId = null;
            String seriesId = null;
            String overview = null;
            String[] guestStarts = null;
            String[] directors = null;
            String[] writers = null;
            Date firstAired = null;

            for (int j = 0; j < episodeAttributes.getLength(); j++) {
                Node item = episodeAttributes.item(j);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                if (item.getNodeName().equals("id")) {
                    id = item.getTextContent();
                } else if (item.getNodeName().equals("EpisodeName")) {
                    name = item.getTextContent();
                } else if (item.getNodeName().equals("SeasonNumber")) {
                    seasonNumber = Integer.valueOf(item.getTextContent());
                } else if (item.getNodeName().equals("Director")) {
                    directors = item.getTextContent().split("\\|");
                } else if (item.getNodeName().equals("EpisodeNumber")) {
                    episodeNumber = Integer.valueOf(item.getTextContent());
                } else if (item.getNodeName().equals("FirstAired")) {
                    if (item.getTextContent() != null && !item.getTextContent().isEmpty()) {
                        try {
                            firstAired = formatter.parse(item.getTextContent());
                        } catch (ParseException e) {
                            System.err.println("Nao foi possivel fazer o parse da data.");
                        }
                    }
                } else if (item.getNodeName().equals("GuestStars")) {
                    guestStarts = item.getTextContent().split("\\|");
                } else if (item.getNodeName().equals("Overview")) {

                } else if (item.getNodeName().equals("Writer")) {
                    writers = item.getTextContent().split("\\|");
                } else if (item.getNodeName().equals("seasonid")) {
                    seasonId = item.getTextContent();
                } else if (item.getNodeName().equals("seriesid")) {
                    seriesId = item.getTextContent();
                }
            }

            TVShowEntity tvShowEntity = tvShowRepository.findById(seriesId);
            SeasonEntity seasonEntity = seasonRepository.findById(seasonId);
            EpisodeEntity episodeEntity = episodeRepository.findById(id);

            if (tvShowEntity == null) {
                String url = GET_SERIES_ZIP.replace("%zipmirror%", zipMirror). //
                        replace("%seriesid%", seriesId). //
                        replace("%language%", "pt");
                processSeries(seriesId, url, "pt");
                tvShowEntity = tvShowRepository.findById(seriesId);
            }

            if (seasonEntity == null) {
                seasonEntity = new SeasonEntity();
                seasonEntity.setId(seasonId);
                seasonEntity.setDateFirst(firstAired);
                seasonEntity.setDateLast(firstAired);
                seasonEntity.setSeasonNumber(seasonNumber);
                seasonRepository.addSeasonData(seasonEntity);
                List<SeasonEntity> seasons = tvShowEntity.getSeasons();
                if (seasons != null) {
                    seasons.add(seasonEntity);
                } else {
                    seasons = new ArrayList<SeasonEntity>();
                    seasons.add(seasonEntity);
                }
                Map<String, Object> updateMap = new HashMap<String, Object>();
                updateMap.put("seasons", seasons);
                tvShowRepository.updateTVShow("id", seriesId, updateMap);
            }

            if (episodeEntity == null) {
                episodeEntity = new EpisodeEntity();
                episodeEntity.setId(id);
                episodeEntity.setName(name);
                episodeEntity.setEpisodeNumber(episodeNumber);
                episodeEntity.setSeasonNumber(seasonNumber);
                episodeEntity.setFirstAired(firstAired);
                episodeEntity.setDirectors(findCast(directors, "Director"));
                episodeEntity.setGuestStars(findCast(guestStarts, "Actor"));
                episodeEntity.setWriters(findCast(writers, "Writer"));
                if (language.equals("pt")) {
                    episodeEntity.setOverview_pt(overview);
                } else {
                    episodeEntity.setOverview_en(overview);
                }
                List<EpisodeEntity> episodeList = seasonEntity.getEpisodes();
                Map<String, Object> updateMap = new HashMap<String, Object>();
                if (episodeList != null) {
                    episodeList.add(episodeEntity);
                    if (!seasonEntity.getDateFirst().before(firstAired)) {
                        updateMap.put("dateFirst", firstAired);
                    } else if (!seasonEntity.getDateLast().after(firstAired)) {
                        updateMap.put("dateLast", firstAired);
                    }
                } else {
                    episodeList = new ArrayList<EpisodeEntity>();
                    episodeList.add(episodeEntity);
                }

                updateMap.put("episodes", episodeList);
                seasonRepository.updateSeason("id", seasonId, updateMap);
            } else {
                Map<String, Object> updateMap = new HashMap<String, Object>();
                if (!episodeEntity.getName().equals(name)) {
                    updateMap.put("name", name);
                }

                if (!episodeEntity.getEpisodeNumber().equals(episodeNumber)) {
                    updateMap.put("episodeNumber", episodeNumber);
                }

                if (!episodeEntity.getSeasonNumber().equals(seasonNumber)) {
                    updateMap.put("seasonNumber", seasonNumber);
                }

                if (!episodeEntity.getFirstAired().equals(firstAired)) {
                    updateMap.put("firstAired", firstAired);
                }

                if (!checkCastList(episodeEntity.getDirectors(), findCast(directors, "Director"))) {
                    updateMap.put("directors", findCast(directors, "Director"));
                }

                if (!checkCastList(episodeEntity.getGuestStars(), findCast(guestStarts, "Actor"))) {
                    updateMap.put("guestStars", findCast(directors, "Actor"));
                }

                if (!checkCastList(episodeEntity.getWriters(), findCast(writers, "Writer"))) {
                    updateMap.put("writers", findCast(directors, "Writer"));
                }

                if (language.equals("pt")) {
                    if (!episodeEntity.getOverview_pt().equals(overview)) {
                        updateMap.put("overview_pt", overview);
                    }
                } else {
                    if (!episodeEntity.getOverview_en().equals(overview)) {
                        updateMap.put("overview_en", overview);
                    }
                }

            }

        }

    }

    private boolean checkCastList(List<CastEntity> firstList, List<CastEntity> secondList) {
        int firstInt = 0, secondInt = 0;
        for (CastEntity first : firstList) {
            firstInt++;
            for (CastEntity second : secondList) {
                secondInt++;
                if (first.getName().equals(second.getName())) {
                    secondInt = 0;
                    break;
                }
            }

            if (secondInt >= secondList.size()) {
                return false;
            }
        }
        return true;
    }

    private void downloadZipFile(String id, String url, String language) throws IOException {
        URL downloadUrl = new URL(url);
        URLConnection connection = downloadUrl.openConnection();
        InputStream in = connection.getInputStream();
        FileOutputStream out = new FileOutputStream(id + "_" + language + ".zip");
        byte[] buf = new byte[1024];
        int n = in.read(buf);
        while (n >= 0) {
            out.write(buf, 0, n);
            n = in.read(buf);
        }
        out.flush();
        out.close();
    }

    /**
     * Retorna a pasta criada
     *
     * @param id
     * @param language
     * @return
     */
    private File unzipFile(String id, String language) {
        byte[] buffer = new byte[1024];
        List<File> files = new ArrayList<File>();

        try {

            //create output directory is not exists
            File folder = new File(id + "_" + language);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(id + "_" + language + ".zip"));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(id + "_" + language + File.separator + fileName);

                files.add(newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            //Delete zip file
            File zip = new File(id + "_" + language + ".zip");
            if (zip.isFile()) {
                while (!zip.delete()) ;
            }

            return folder;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
