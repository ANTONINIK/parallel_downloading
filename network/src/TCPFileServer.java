import java.io.*;
import java.net.Socket;
import java.util.HashMap;

/**
 * Класс для отправки файла сервером
 */

public class TCPFileServer extends Thread {

    private final HashMap<String, File> stringFileHashMap;
    private final Socket socket;
    private final int numberOfServer;

    private static final boolean[] fileIsSplited = new boolean[6];
    private static int countOfServers = 0;
    private static String url = null;

    public TCPFileServer(Socket socket, HashMap<String, File> stringFileHashMap) {
        this.stringFileHashMap = stringFileHashMap;
        this.socket = socket;
        numberOfServer = countOfServers;
        countOfServers++;
        fileIsSplited[numberOfServer] = false;
    }

    @Override
    public void run() {
        File fileSend = null;
        try {
            if (numberOfServer == 0) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                url = in.readLine();
                File targetFile = stringFileHashMap.get(url);

                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write(targetFile.length() + "\n");
                out.flush();
                SplitFiles.split(targetFile.getAbsolutePath(), fileIsSplited);
            }

            while (!fileIsSplited[numberOfServer] || url == null) { Thread.sleep(100);}

            fileSend = new File(stringFileHashMap.get(url).getAbsolutePath() + "." + numberOfServer + ".sp");
            if (new FileTransferProcessor(socket).sendFile(fileSend))
                System.out.println("The file has been sent: " + fileSend);

        } catch (IOException | InterruptedException error) {
            System.out.println(error.getMessage());
        } finally {
            if (fileSend != null) fileSend.delete();
            countOfServers--;
        }
    }
}
