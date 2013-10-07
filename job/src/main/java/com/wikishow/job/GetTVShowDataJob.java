package com.wikishow.job;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
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
    public static final String GET_MIRROR_URL = "http://thetvdb.com/api/" + TVDB_API_KEY + "/mirrors.xml";
    public static final String GET_SERIES_ZIP = "%zipmirror%/api/" + TVDB_API_KEY + "/series/%seriesid%/all/%language%.zip";
    public static final String GET_SERIES = "http://thetvdb.com/api/GetSeries.php?seriesname=";
    public static final String GET_SERIES_UPDATE = "%xmlmirror%/api/" + TVDB_API_KEY + "/series/%seriesid%/%language%.xml";
    public static final int XML_FILE = 1;
    public static final int BANNER_FILE = 2;
    public static final int ZIP_FILE = 4;
    private static final String ACCESS_KEY = "AKIAJTBPDH4NJDBK7YVQ";
    private static final String SECRET_KEY = "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";
    private static final String PATH_TO_FILE = "/Users/macbookpro/Downloads/wikishow/";
    @Autowired
    CastRepository castRepository;
    @Autowired
    EpisodeRepository episodeRepository;
    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    TVShowRepository tvShowRepository;
    @Autowired
    NewTVShowRepository newTVShowRepository;
    @Autowired
    BannersRepository bannersRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    NewImageRepository newImageRepository;
    private String xmlMirror = null;
    private String bannerMirror = null;
    private String zipMirror = null;

    @Scheduled(cron = "*/30 * * * * *")
    public void getTVShowData() throws IOException {

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
                        xmlMirror = url;
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

        List<NewTVShow> newTVShowList = newTVShowRepository.listAllNewTVShow();

        for (NewTVShow newTvShow : newTVShowList) {
            System.out.println("Processing " + newTvShow.getName());
            URL newShow = new URL(GET_SERIES + newTvShow.getName().replaceAll(" ", "%20"));
            URLConnection updateConnection = newShow.openConnection();
            Document newShowDocument = parseXML((updateConnection.getInputStream()));
            NodeList newShowsNodeList = newShowDocument.getElementsByTagName("Series");

            if (newShowsNodeList.getLength() > 10) {
                System.out.println("To many shows with the same name. Adding them to newTVShow List");
                for (int i = 0; i < newShowsNodeList.getLength(); i++) {
                    Node tvShowNode = newShowsNodeList.item(i);
                    NodeList tvShowAttributes = tvShowNode.getChildNodes();
                    for (int j = 0; j < tvShowAttributes.getLength(); j++) {
                        Node node = tvShowAttributes.item(j);
                        if (node.getNodeName().equals("seriesid")) {
                            String url = GET_SERIES_UPDATE.replace("%xmlmirror%", xmlMirror). //
                                    replace("%seriesid%", node.getTextContent()) //
                                    .replace("%language%", "en");
                            addSerie(node.getTextContent(), url);
                        }
                    }
                }
                newTVShowRepository.deleteNewTVShow(newTvShow);
                break;
            }
            for (int i = 0; i < newShowsNodeList.getLength(); i++) {
                System.out.println("Found " + newShowsNodeList.getLength() + " tvShow=" + newTvShow.getName());
                Node tvShowNode = newShowsNodeList.item(i);
                NodeList tvShowAttributes = tvShowNode.getChildNodes();
                for (int j = 0; j < tvShowAttributes.getLength(); j++) {
                    Node node = tvShowAttributes.item(j);
                    if (node.getNodeName().equals("seriesid")) {
                        String url = GET_SERIES_ZIP.replace("%zipmirror%", zipMirror). //
                                replace("%seriesid%", node.getTextContent()). //
                                replace("%language%", "en");
                        processSeries(node.getTextContent(), url, "en");
                        url = GET_SERIES_ZIP.replace("%zipmirror%", zipMirror). //
                                replace("%seriesid%", node.getTextContent()). //
                                replace("%language%", "pt");
                        processSeries(node.getTextContent(), url, "pt");
                    }
                }
            }
            if (newShowsNodeList.getLength() >= 0) {
                newTVShowRepository.deleteNewTVShow(newTvShow);
            }
        }

    }

    public Document parseXML(InputStream stream) {
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
                        while (!xml.delete()) ;
                    }

                    if (xml.getName().equals("actors.xml")) {
                        Document actors = parseXML(new FileInputStream(xml));
                        saveActorsXML(actors, id);
                        while (!xml.delete()) ;
                    }

                    if (xml.getName().equals("banners.xml")) {
                        Document banners = parseXML(new FileInputStream(xml));
                        saveBannersXML(banners, id, null, null, null);
                        while (!xml.delete()) ;
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
        TvShow tvShowEntity;
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
                System.out.println("tvShowName=" + tvShowName);
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

        if (tvShowEntity == null) {
            tvShowEntity = new TvShow();
            tvShowEntity.setId(id);
            tvShowEntity.setTvShowName(tvShowName);
            tvShowEntity.setEnded(isEnded);
            tvShowEntity.setFirstAired(firstAired);
            tvShowEntity.setNetwork(network);
            if (language.equals("pt")) {
                tvShowEntity.setOverview_pt(overview);
                tvShowEntity.setOverview_en("Overview not set!");
            } else {
                tvShowEntity.setOverview_en(overview);
                tvShowEntity.setOverview_pt("Sinopse ainda não disponível!");
            }
            tvShowEntity.setCast(findCast(actors, "Actor"));
            tvShowRepository.addTVShowData(tvShowEntity);
        } else {
            Map<String, Object> updateMap = new HashMap<String, Object>();

            if (tvShowEntity.getNetwork() == null || !tvShowEntity.getNetwork().equals(network)) {
                updateMap.put("network", network);
            }

            if (tvShowEntity.getFirstAired() == null || !tvShowEntity.getFirstAired().equals(firstAired)) {
                updateMap.put("firstAired", firstAired);
            }

            if (language.equals("pt")) {
                if (tvShowEntity.getOverview_pt() == null || !tvShowEntity.getOverview_pt().equals(overview)) {
                    updateMap.put("overview_pt", overview);
                }
            } else {
                if (tvShowEntity.getOverview_en() == null || !tvShowEntity.getOverview_en().equals(overview)) {
                    updateMap.put("overview_en", overview);
                }
            }

            if (tvShowEntity.getCast() == null || !checkCastList(tvShowEntity.getCast(), findCast(actors, "Actor"))) {
                updateMap.put("cast", findCast(actors, "Actor"));
            }

            if (tvShowEntity.getEnded() == null || tvShowEntity.getEnded() != isEnded) {
                updateMap.put("isEnded", isEnded);
            }

            tvShowRepository.updateTVShow("id", id, updateMap);

        }
    }

    public List<CastAndCrew> findCast(String[] names, String type) {
        List<CastAndCrew> actors = new ArrayList<CastAndCrew>();
        if (names == null || names.length == 0) {
            return null;
        }

        for (String name : names) {
            if (name.equals("") || name.equals(" ")) {
                break;
            }
            CastAndCrew actor = castRepository.findByName(name);
            if (actor == null) {
                actor = new CastAndCrew();
                actor.setName(name);
                actor.setType(type);
                castRepository.addCastData(actor);
            } else {
                if (!actor.getType().contains(type)) {
                    String newType = actor.getType() + "|" + type;
                    actor.setType(newType);
                    Map<String, Object> updateMap = new HashMap<String, Object>();
                    updateMap.put("type", newType);
                    castRepository.updateCast("name", name, updateMap);
                }
            }
            actors.add(actor);
        }
        return actors;
    }

    public void saveBannersXML(Document bannersDocument, String seriesId, String episodeId, String seasonNumber, String path) throws IOException {
        AmazonS3Client s3 = new AmazonS3Client(
                new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
        if (!s3.doesBucketExist("2ndscreentvshow")) {
            s3.createBucket("2ndscreentvshow");
        }

        String type = null;
        String type2;
        String id = null;
        Banners banners = null;

        File imageFile;
        if (episodeId != null) {
            if (path == null || path.isEmpty()) {
                return;
            }
            StringBuffer s3Key = new StringBuffer();
            s3Key.append(seriesId).append("/");
            s3Key.append("episodes/").append(seasonNumber).append("/");
            s3Key.append(path.substring(path.lastIndexOf("/") + 1));

            S3Object s3object = null;
            try {
                s3.getObject(new GetObjectRequest(
                        "2ndscreentvshow", s3Key.toString()));
            } catch (Exception e) {
                System.err.println("Image does not exist.");
            }
            if (s3object == null) {
                imageFile = downloadImage(path);
                if (imageFile != null) {
                    try {
                        s3.putObject("2ndscreentvshow", s3Key.toString(), imageFile);
                        imageFile.delete();
                    } catch (Exception e) {
                        System.err.println("Problem to upload image " + s3Key.toString());
                    }
                }
            }

            return;
        }

        NodeList bannersNodeList = bannersDocument.getElementsByTagName("Banner");

        for (int i = 0; i < bannersNodeList.getLength(); i++) {
            Node bannerNode = bannersNodeList.item(i);
            NodeList attributes = bannerNode.getChildNodes();
            boolean download = true;
            for (int j = 0; j < attributes.getLength(); j++) {

                Node item = attributes.item(j);
                if (item.getNodeName().equals("id")) {
                    id = item.getTextContent();
                    banners = bannersRepository.findById(id);
                    if (banners != null) {
                        System.out.println("Found banner id = " + id);
                        download = false;
                        break;
                    }
                } else if (item.getNodeName().equals("BannerPath")) {
                    path = item.getTextContent();
                } else if (item.getNodeName().equals("BannerType")) {
                    if (!item.getTextContent().equals("series") &&
                            !item.getTextContent().equals("season") &&
                            !item.getTextContent().equals("poster")) {
                        download = false;
                        break;
                    }
                    type = item.getTextContent();
                    System.out.println("BannerType=" + type);
                } else if (item.getNodeName().equals("BannerType2")) {
                    if (!item.getTextContent().equals("graphical") &&
                            !item.getTextContent().equals("seasonwide") &&
                            !item.getTextContent().equals("680x1000")) {
                        download = false;
                        break;
                    }
                    type2 = item.getTextContent();
                } else if (item.getNodeName().equals("Language")) {
                    if (!item.getTextContent().equals("pt") && !item.getTextContent().equals("en")) {
                        download = false;
                        break;
                    }
                }
            }

            if (download) {

                banners = new Banners();
                banners.setId(id);
                banners.setSeriesId(seriesId);
                bannersRepository.addBanner(banners);
                imageFile = downloadImage(path);
                if (imageFile != null) {
                    StringBuffer s3Key = new StringBuffer();
                    s3Key.append(seriesId).append("/");
                    if (type.equals("season")) {
                        s3Key.append("seasons/");
                        seasonNumber = path.substring(path.indexOf("-") + 1);
                        if (seasonNumber.indexOf("-") > -1) {
                            seasonNumber = seasonNumber.substring(0, seasonNumber.indexOf("-"));
                        } else {
                            seasonNumber = seasonNumber.substring(0, seasonNumber.indexOf("."));
                        }
                        s3Key.append(seasonNumber).append("/");
                    } else if (type.equals("poster")) {
                        s3Key.append("poster").append("/");
                    } else {
                        s3Key.append("series/");
                    }

                    s3Key.append(path.substring(path.indexOf("/") + 1));
                    try {
                        s3.putObject("2ndscreentvshow", s3Key.toString(), imageFile);
                        imageFile.delete();
                    } catch (Exception e) {
                        System.err.println("Problem to upload image " + s3Key.toString());
                    }

                }
            }

        }
    }

    public File downloadImage(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            return null;
        }

        URL image = new URL(bannerMirror + "/banners/" + path);
        URLConnection connection = image.openConnection();
        InputStream in = connection.getInputStream();
        FileOutputStream out = new FileOutputStream(PATH_TO_FILE + path.substring(path.lastIndexOf("/")));
        byte[] buf = new byte[1024];
        int n = in.read(buf);
        while (n >= 0) {
            out.write(buf, 0, n);
            n = in.read(buf);
        }
        out.flush();
        out.close();

        File imageFile = new File(PATH_TO_FILE + path.substring(path.lastIndexOf("/")));
        return imageFile;
    }

    public void saveActorsXML(Document actor, String seriesId) throws IOException {
        NodeList actorsList = actor.getElementsByTagName("Actor");

        for (int i = 0; i < actorsList.getLength(); i++) {
            Node actorNode = actorsList.item(i);
            NodeList attributes = actorNode.getChildNodes();
            String id = null;
            String name = null;
            String role = null;
            String imageURL = null;
            for (int j = 0; j < attributes.getLength(); j++) {
                Node item = attributes.item(j);

                if (item.getNodeName().equals("id")) {
                    id = item.getTextContent();
                } else if (item.getNodeName().equals("Name")) {
                    name = item.getTextContent();
                    System.out.println("Actors name=" + name);
                } else if (item.getNodeName().equals("Role")) {
                    role = item.getTextContent();
                    System.out.println("Actors role=" + role);
                } else if (item.getNodeName().equals("Image")) {
                    imageURL = item.getTextContent();
                }
            }

            CastAndCrew actorEntity = castRepository.findByName(name);
            Role roleEntity = roleRepository.findByRoleAndSeriesId(role, seriesId);
            File imageFile;

            if (imageURL != null && !imageURL.isEmpty()) {
                AmazonS3Client s3 = new AmazonS3Client(
                        new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
                if (!s3.doesBucketExist("2ndscreentvshow")) {
                    s3.createBucket("2ndscreentvshow");
                }
                StringBuffer s3Key = new StringBuffer();
                s3Key.append("actors").append("/");
                s3Key.append(imageURL.substring(imageURL.lastIndexOf("/") + 1));

                S3Object s3object = null;

                try {
                    s3.getObject(new GetObjectRequest(
                            "2ndscreentvshow", s3Key.toString()));
                } catch (Exception e) {
                    System.out.println("Image does not exist.");
                }
                if (s3object == null) {
                    imageFile = downloadImage(imageURL);
                    if (imageFile != null) {
                        try {
                            s3.putObject("2ndscreentvshow", s3Key.toString(), imageFile);
                            imageFile.delete();
                        } catch (Exception e) {
                            System.err.println("Problem to upload image " + s3Key.toString());
                        }
                    }
                }
            }

            if (roleEntity == null) {
                roleEntity = new Role();
                roleEntity.setRole(role);
                roleEntity.setTvShowId(seriesId);
                roleRepository.addRole(roleEntity);
                roleEntity = roleRepository.findByRoleAndSeriesId(role, seriesId);
            }

            if (actorEntity == null) {
                actorEntity = new CastAndCrew();
                actorEntity.setType("Actor");
                actorEntity.setName(name);
                actorEntity.setId(id);
                List<Role> roleList = new ArrayList<Role>();
                roleList.add(roleEntity);
                actorEntity.setRole(roleList);
                castRepository.addCastData(actorEntity);
            } else {
                Map<String, Object> updateMap = new HashMap<String, Object>();
                List<Role> roleList = actorEntity.getRole();

                if (actorEntity.getRole() != null && !hasRole(roleList, roleEntity)) {
                    roleList.add(roleEntity);
                    actorEntity.setRole(roleList);
                    updateMap.put("role", roleList);
                } else if (actorEntity.getRole() == null) {
                    roleList = new ArrayList<Role>();
                    roleList.add(roleEntity);
                    actorEntity.setRole(roleList);
                    updateMap.put("role", roleList);
                }

                if (actorEntity.getType() != null && !actorEntity.getType().contains("Actor")) {
                    String type = actorEntity.getType() + "|Actor";
                    actorEntity.setType(type);
                    updateMap.put("type", type);
                } else if (actorEntity.getType() == null) {
                    String type = "Actor";
                    actorEntity.setType(type);
                    updateMap.put("type", type);
                }
                castRepository.updateCast("name", name, updateMap);
            }
        }
    }

    public boolean hasRole(List<Role> roleList, Role roleEntity) {
        for (Role role : roleList) {
            if (role.getId().equals(roleEntity.getId())) {
                return true;
            }
        }
        return false;
    }

    private void saveEpisodesXML(Document episodes, String language) throws IOException {
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
            String[] guestStars = null;
            String[] directors = null;
            String[] writers = null;
            Date firstAired = null;
            String filename = null;

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
                    guestStars = item.getTextContent().split("\\|");
                } else if (item.getNodeName().equals("Overview")) {
                    overview = item.getTextContent();
                } else if (item.getNodeName().equals("Writer")) {
                    writers = item.getTextContent().split("\\|");
                } else if (item.getNodeName().equals("seasonid")) {
                    seasonId = item.getTextContent();
                } else if (item.getNodeName().equals("seriesid")) {
                    seriesId = item.getTextContent();
                } else if (item.getNodeName().equals("filename")) {
                    filename = item.getTextContent();
                }
            }

            System.out.println("S" + seasonNumber + "E" + episodeNumber + " - EpisodeName=" + name);

            TvShow tvShowEntity = tvShowRepository.findById(seriesId);
            Season seasonEntity = seasonRepository.findById(seasonId);
            Episode episodeEntity = episodeRepository.findById(id);

            if (filename != null && !filename.isEmpty()) {
                saveBannersXML(null, seriesId, id, String.valueOf(seasonNumber), filename);
            }

            if (seasonEntity == null) {
                seasonEntity = new Season();
                seasonEntity.setId(seasonId);
                seasonEntity.setDateFirst(firstAired);
                seasonEntity.setDateLast(firstAired);
                seasonEntity.setSeasonNumber(seasonNumber);
                seasonRepository.addSeasonData(seasonEntity);
                List<Season> seasons = tvShowEntity.getSeasons();
                if (seasons != null) {
                    seasons.add(seasonEntity);
                } else {
                    seasons = new ArrayList<Season>();
                    seasons.add(seasonEntity);
                }
                Map<String, Object> updateMap = new HashMap<String, Object>();
                updateMap.put("seasons", seasons);
                tvShowRepository.updateTVShow("id", seriesId, updateMap);
            }

            if (episodeEntity == null) {
                episodeEntity = new Episode();
                episodeEntity.setId(id);
                episodeEntity.setEpisodeNumber(episodeNumber);
                episodeEntity.setSeasonNumber(seasonNumber);
                episodeEntity.setFirstAired(firstAired);
                episodeEntity.setDirectors(findCast(directors, "Director"));
                episodeEntity.setGuestStars(findCast(guestStars, "Actor"));
                episodeEntity.setWriters(findCast(writers, "Writer"));
                if (language.equals("pt")) {
                    episodeEntity.setName_pt(name);
                    episodeEntity.setOverview_pt(overview);
                    episodeEntity.setOverview_en("Overview not defined");
                } else {
                    episodeEntity.setName_en(name);
                    episodeEntity.setOverview_en(overview);
                    episodeEntity.setOverview_pt("Overview não definido");
                }
                episodeRepository.addEpisodeData(episodeEntity);
                List<Episode> episodeList = seasonEntity.getEpisodes();
                Map<String, Object> updateMap = new HashMap<String, Object>();
                if (episodeList != null) {
                    episodeList.add(episodeEntity);
                    if (firstAired != null) {
                        if (seasonEntity.getDateFirst() == null || !seasonEntity.getDateFirst().before(firstAired)) {
                            updateMap.put("dateFirst", firstAired);
                        } else if (seasonEntity.getDateLast() == null || !seasonEntity.getDateLast().after(firstAired)) {
                            updateMap.put("dateLast", firstAired);
                        }
                    }
                } else {
                    episodeList = new ArrayList<Episode>();
                    episodeList.add(episodeEntity);
                }

                updateMap.put("episodes", episodeList);
                seasonRepository.updateSeason("id", seasonId, updateMap);
            } else {
                Map<String, Object> updateMap = new HashMap<String, Object>();

                if (episodeEntity.getEpisodeNumber() == null || !episodeEntity.getEpisodeNumber().equals(episodeNumber)) {
                    updateMap.put("episodeNumber", episodeNumber);
                }

                if (episodeEntity.getSeasonNumber() == null || !episodeEntity.getSeasonNumber().equals(seasonNumber)) {
                    updateMap.put("seasonNumber", seasonNumber);
                }

                if (episodeEntity.getFirstAired() == null || !episodeEntity.getFirstAired().equals(firstAired)) {
                    updateMap.put("firstAired", firstAired);
                }

                if (!checkCastList(episodeEntity.getDirectors(), findCast(directors, "Director"))) {
                    updateMap.put("directors", findCast(directors, "Director"));
                }

                if (!checkCastList(episodeEntity.getGuestStars(), findCast(guestStars, "Actor"))) {
                    updateMap.put("guestStars", findCast(guestStars, "Actor"));
                }

                if (!checkCastList(episodeEntity.getWriters(), findCast(writers, "Writer"))) {
                    updateMap.put("writers", findCast(guestStars, "Writer"));
                }

                if (language.equals("pt")) {
                    if (episodeEntity.getOverview_pt() == null || !episodeEntity.getOverview_pt().equals(overview)) {
                        updateMap.put("overview_pt", overview);
                    }
                    if (episodeEntity.getName_pt() == null || !episodeEntity.getName_pt().equals(name)) {
                        updateMap.put("name_pt", name);
                    }
                } else {
                    if (episodeEntity.getOverview_en() == null || !episodeEntity.getOverview_en().equals(overview)) {
                        updateMap.put("overview_en", overview);
                    }
                    if (episodeEntity.getName_en() == null || !episodeEntity.getName_en().equals(name)) {
                        updateMap.put("name_en", name);
                    }
                }
                episodeRepository.updateEpisode("id", episodeEntity.getId(), updateMap);
            }

        }

    }

    public boolean checkCastList(List<CastAndCrew> firstList, List<CastAndCrew> secondList) {
        int secondInt = 0;
        if (firstList == null) {
            if (secondList == null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (secondList == null) {
                return false;
            }
        }

        for (CastAndCrew first : firstList) {
            for (CastAndCrew second : secondList) {
                if (first.getName().equals(second.getName())) {
                    secondInt++;
                    break;
                }
            }
        }

        if (secondInt == secondList.size()) {
            return true;
        }

        return false;
    }

    private void downloadZipFile(String id, String url, String language) throws IOException {
        URL downloadUrl = new URL(url);
        URLConnection connection = downloadUrl.openConnection();
        InputStream in = connection.getInputStream();
        FileOutputStream out = new FileOutputStream(PATH_TO_FILE + id + "_" + language + ".zip");
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
            File folder = new File(PATH_TO_FILE + id + "_" + language);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(PATH_TO_FILE + id + "_" + language + ".zip"));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(PATH_TO_FILE + id + "_" + language + File.separator + fileName);

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
            File zip = new File(PATH_TO_FILE + id + "_" + language + ".zip");
            if (zip.isFile()) {
                while (!zip.delete()) ;
            }

            return folder;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void addSerie(String id, String url) throws IOException {
        URL updateUrl = new URL(url);
        URLConnection updateConnection = updateUrl.openConnection();
        Document update;

        try {
            update = parseXML(updateConnection.getInputStream());
        } catch (FileNotFoundException e) {
            System.err.println("Not possible to get file for tvshow id=" + id);
            return;
        }

        if (update == null) {
            return;
        }

        NodeList tvShowData = update.getElementsByTagName("Series");

        Node showData = tvShowData.item(0);
        NodeList attributes = showData.getChildNodes();
        NewTVShow newTVShow = new NewTVShow();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            if (item.getNodeName().equals("SeriesName")) {
                if (!item.getTextContent().isEmpty()) {
                    List<NewTVShow> list = newTVShowRepository.findByName(item.getTextContent());
                    if (list == null || list.isEmpty()) {
                        System.out.println("Adding " + item.getTextContent() + " to newTVShow");
                        newTVShow.setName(item.getTextContent());
                        newTVShowRepository.addNewTVShowData(newTVShow);
                    }
                    break;
                }
            }
        }

    }

}
