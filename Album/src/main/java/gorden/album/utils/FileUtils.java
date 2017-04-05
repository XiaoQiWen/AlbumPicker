package gorden.album.utils;

import java.io.File;

import gorden.album.entity.Picture;

public class FileUtils {
    public static boolean fileIsExists(String path) {
        if (path == null || path.trim().length() <= 0) {
            return false;
        }
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isFile(String path) {
        return new File(path).isFile();
    }

    public static boolean isEffective(Picture picture){
        if (!new File(picture.path).isFile())
            return false;
        if (picture.size<=0) return false;
        if (picture.width<=0 ) return false;
        return true;
    }
}
