import java.io.*;
import java.net.Socket;

/**
 * Класс для получения файла клиентом
 */

public class TCPClient extends Thread {

    private final Socket socket;
    private final String url;
    private final String savePath;
    private final int numberOfClient;
    private final FileTransferProcessor fileTransferProcessor;

    private Boolean FinishFlag = false;

    private static int countOfClients = 0;
    private static int fileSize = 0;

    public static int getFileSize() {
        return fileSize;
    }
    public FileTransferProcessor getFileTransferProcessor() {
        return fileTransferProcessor;
    }

    public boolean isFinished() {
        return !this.isAlive() && FinishFlag;
    }

    public TCPClient(Socket socket, String url, String savePath) {
        this.socket = socket;
        this.url = url;
        this.savePath = savePath;
        numberOfClient = countOfClients++;
        fileTransferProcessor = new FileTransferProcessor(socket);
    }

    @Override
    public void run() {
        try {
            if (numberOfClient == 0) {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write(url + "\n");
                out.flush();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                fileSize = Integer.parseInt(in.readLine()) / 6;

                System.out.println("Package size: " + fileSize);
            }

            String filePath = savePath + "\\" + url + "." + numberOfClient + ".sp";

            if (fileTransferProcessor.receiveFile(filePath)) {
                System.out.println("File received: " + filePath);
            }
        }  catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        finally {
            FinishFlag = true;
            --countOfClients;
        }
    }
}
