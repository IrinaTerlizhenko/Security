package by.bsu.cinemarating.loader;

import javax.servlet.http.Part;
import java.io.*;

/**
 * Created by User on 09.06.2016.
 */
public class PictureLoader {
    public static String loadPicture(Part filePart, int id, String controllerPath, String pathToImgFolder, String defaultPicture) throws IOException {
        final String path = controllerPath + File.separator + ".." + File.separator + pathToImgFolder;
        String finalPath = null;
        String fileName = (filePart != null && filePart.getSize() > 0) ? id + "_" + getFileName(filePart) : defaultPicture;

        if (filePart != null && filePart.getSize() > 0) {
            OutputStream out = null;
            InputStream filecontent = null;
            try {
                out = new FileOutputStream(new File(path + File.separator + fileName));
                filecontent = filePart.getInputStream();
                int read;
                final byte[] bytes = new byte[1024];
                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                finalPath = pathToImgFolder + File.separator + fileName;
            } finally {
                if (out != null) {
                    out.close();
                }
                if (filecontent != null) {
                    filecontent.close();
                }
            }
        }
        return finalPath;
    }

    private static String getFileName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
