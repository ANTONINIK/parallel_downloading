import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для загрузки конфигурационного файла
 */

public class Preloader {
    public Preloader(String FileName, Properties props) throws IOException, Exception {
        getConfigFile(FileName, props);
        Preloader.class.getResource("config.ini");
    }
    private static void getConfigFile(String FileName, Properties props) throws
            FileNotFoundException, IOException, Exception {
        InputStream fs;
        try {
            File f = new File(FileName);
            if(f.exists()) {
                fs = new FileInputStream(f);
            } else {
                fs = Preloader.class.getResourceAsStream(FileName);
            }
            props.load(fs);
            System.out.println("Configuration loaded");
            fs.close();
        } catch (FileNotFoundException e) {
            System.err.println("Configuration file not found. Error - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("The configuration file is not readable. Error - " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Error loading the configuration file. Error - " +e.getMessage());
        }
    }
}