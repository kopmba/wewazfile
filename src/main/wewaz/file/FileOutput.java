package main.com.wazteam;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileOutput {
    private static final Logger LOG = Logger.getLogger(FileOutput.class.getName());

    public String output(String filename) throws IOException {
        String pathFile = fileStorage() + filename ;
        File file = new File(pathFile);
        FileInputStream fis = new FileInputStream(file);
        //Use the buffered reader
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        } in.close();
        // print result
        System.out.println(content.toString());
        return content.toString();
    }

    public boolean isDir(String path) {
        URL url = getClass().getClassLoader().getSystemResource(path);
        String dir = pathDir(url, path);
        File f = new File(dir);
        return f.isDirectory();
    }

    public void remove(String filename) throws URISyntaxException, IOException {
        String pathFile = schema(filename);
        Files.deleteIfExists(Paths.get(new URI(pathFile)));
    }
    public void write(String filename, String data) throws URISyntaxException, IOException {

        String pathFile = schema(filename);
        Files.deleteIfExists(Paths.get(new URI(pathFile)));

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(Paths.get(new URI(pathFile)), CREATE, APPEND))) {
            out.write(data.getBytes(), 0, data.getBytes().length);
        } catch(IOException e) {
            System.err.println(e);
        }
    }

    public void upload(String filename, byte[] data) throws URISyntaxException, IOException {

        String pathFile = schema(filename);
        Files.deleteIfExists(Paths.get(new URI(pathFile)));

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(Paths.get(new URI(pathFile)), CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch(IOException e) {
            System.err.println(e);
        }
    }



    public String fileDirectory() {
        String dirIndexer = "resources/collections/";
        URL url = getClass().getClassLoader().getSystemResource(dirIndexer);
        return pathDir(url, dirIndexer);
    }

    public String fileStorage() {
        String pname = projectName(fileDirectory());
        System.out.println("Le projet : " + pname);
        String storage = storagePath(pname);
        System.out.println("Le store : " + storage);
        return storage;
    }

    public String filepath(String filename) {
        //String dirIndexer = "resources/collections/";
        //URL url = getClass().getClassLoader().getSystemResource(dirIndexer);
        String fpath = fileStorage() + filename;
        return fpath;
    }

    public String pathDir(URL url, String dirIndexer) {
        StringBuffer buffer = new StringBuffer(url.getPath());
        String resIndexer = dirIndexer;
        int resIndex = 0;

        if(buffer.toString().contains("C:")) {
            buffer = new StringBuffer(buffer.substring(1));
        }

        if(buffer.toString().contains(resIndexer)) {
            resIndex = buffer.indexOf(resIndexer) + resIndexer.length();
        }
        String fdir = buffer.substring(0, resIndex) ;

        return fdir;
    }

    public void streamData(byte[] data, String filepath) {
        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(Paths.get(new URI(filepath)), CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch(IOException | URISyntaxException e) {
            System.err.println(e);
        }
    }

    public String projectName(String pathfile) {
        String indexer = "/resources";
        int resIndex = pathfile.indexOf(indexer);
        String newStr = pathfile.substring(0, resIndex);
        StringBuffer bf = new StringBuffer();
        for(int i = newStr.length()- 1; i <= newStr.length()+1; i--) {
            if(newStr.charAt(i) != '/') {
                bf.append(newStr.charAt(i));
            } else {
                return bf.reverse().toString();
            }
        }
        return null;
    }

    public String storagePath(String projectName) {
        String dirIndexer = "resources/collections/";
        String dir = fileDirectory();
        int fromProjectIndex = dir.indexOf(projectName) + projectName.length();
        int resIndex = dir.indexOf(dirIndexer);
        return dir.substring(0, fromProjectIndex) + "/src/" + dir.substring(resIndex);
    }
    public String schema(String filename) {
        String pathFile = fileStorage() + filename ;
        String schema = "file:";
        if(pathFile.contains("C:")) {
            schema = schema + "/";
        }
        pathFile = schema + pathFile;
        return pathFile;
    }

    public static byte[] byteContent(String data) throws IOException {
        StringReader str = new StringReader(data);
        BufferedReader buffer = new BufferedReader(str);
        String inputLine;
        StringBuffer content = new StringBuffer();
        StringBuilder strBuilder = new StringBuilder();

        while((inputLine = buffer.readLine()) != null) {
            if(inputLine != "") {
                content.append(inputLine);
            }
        }
        buffer.close();

        // print result
        System.out.println(content.toString());
        return content.toString().getBytes(StandardCharsets.UTF_8);
    }

}
