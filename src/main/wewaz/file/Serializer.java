package main.com.wazteam;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URISyntaxException;

public class Serializer {
    public static void serialize(String collection, Object data) throws IOException, URISyntaxException, InterruptedException {
        FileOutput frw = new FileOutput();
        String fpath = frw.filepath(collection);
        File f = new File(fpath);
        if(!f.exists()) {
            String path = "";
            if(fpath.contains("C:")) {
                path = "file:/" + fpath;
            } else {
                path = "file:" + fpath;
            }
            frw.write(collection, "");
        }
        FileOutputStream fos = new FileOutputStream(new File(fpath));
        XMLEncoder encode = new XMLEncoder(fos);
        encode.writeObject(data);
        encode.close();
    }

    public static Object deserialize(byte[] content) {
        Object data = null;
        if(content.length > 0) {
            InputStream is = new ByteArrayInputStream(content);
            XMLDecoder decode = new XMLDecoder(is);
            data = decode.readObject();
        }
        return data;

    }
    public static Object deserialize(String collection) throws FileNotFoundException {
        FileOutput frw = new FileOutput();
        String fpath = frw.filepath(collection);
        InputStream is = is = new FileInputStream(new File(fpath));
        XMLDecoder decode = new XMLDecoder(is);
        Object data = decode.readObject();
        return data;
    }
}
