import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Класс сервер
 */

public class FileServer extends Thread {
    /** Поле порт */
    private final int port;
    /** Поле список файлов на сервере в формате url - путь */
    private final HashMap<String, File> stringFileHashMap;

    /**
     * Конструктор
     * @param port - порт сервера
     * @param stringFileHashMap - список файлов на сервере в формате url - путь
     */
    public FileServer(int port, HashMap<String, File> stringFileHashMap) {
        this.port = port;
        this.stringFileHashMap = stringFileHashMap;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            new TCPFileServer(serverSocket.accept(), stringFileHashMap).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            HashMap<String, File> stringFileHashMap = new HashMap<>();
            stringFileHashMap.put("Video1.mp4", new File("D:\\test\\Server\\Video1.mp4"));
            stringFileHashMap.put("Video2.m4v", new File("D:\\test\\Server\\Video2.m4v"));
            stringFileHashMap.put("vscode.exe", new File("D:\\test\\Server\\vscode.exe"));

            ServerSocket ServerSocket = new ServerSocket(8080);
            Socket socket = ServerSocket.accept();
            System.out.println("Server ready");
            
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Set<String> keys = stringFileHashMap.keySet();
            for ( String key : keys ) {
                out.write(key + ";");
            }
            out.write("\n");
            out.flush();
            out.close();
            ServerSocket.close();

            ArrayList<FileServer> fileServers;
            while (true) {
                fileServers = new ArrayList<>();
                for(int i=0; i<6; i++) {
                    fileServers.add(new FileServer(8080 + i, stringFileHashMap));
                }
                for(FileServer server : fileServers) {
                    server.start();
                }
                for(FileServer server : fileServers) {
                    server.join();
                }
            }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
    }
}