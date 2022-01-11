import java.io.*;
import java.net.Socket;

/**
 * Класс для отправки и получению файлов
 */

public class FileTransferProcessor {
    private final Socket socket;
    private InputStream is;
    private FileOutputStream fos;
    private BufferedOutputStream bos;
    private int bufferSize;
    /** Вес скачанного файла */
    private int fileSize = 0;

    public synchronized int getFileSize() {
        return fileSize;
    }

    FileTransferProcessor(Socket client) {
        socket = client;
        is = null;
        fos = null;
        bos = null;
        bufferSize = 0;

    }

    public synchronized void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * Функция для получения файлов
     * @param fileName - путь и имя полученного файла
     * @return возвращает true в случае успеха, иначе false
     */
    public boolean receiveFile(String fileName) {
        try {
            is = socket.getInputStream();
            bufferSize = socket.getReceiveBufferSize();
            fos = new FileOutputStream(fileName);
            bos = new BufferedOutputStream(fos);
            byte[] bytes = new byte[bufferSize];
            int count;
            while ((count = is.read(bytes)) >= 0) {
                bos.write(bytes, 0, count);
                setFileSize(fileSize += count);
            }
            bos.close();
            is.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Функция для отправки файлов
     * @param file - файл который отправляем
     * @return возвращает true в случае успеха, иначе false
     */
    public boolean sendFile(File file) throws RuntimeException {

        FileInputStream fis;
        BufferedInputStream bis;
        BufferedOutputStream out;
        byte[] buffer = new byte[8192];
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            out = new BufferedOutputStream(socket.getOutputStream());
            int count;
            while ((count = bis.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            fis.close();
            bis.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}