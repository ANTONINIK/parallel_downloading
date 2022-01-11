import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Класс - клиентское окна программы
 */

public class ClientWindow extends JFrame {
    /** Поле адресс сервера */
    private String IP_ADDRESS = "localhost";
    /** Поле порт 0 */
    private int PORT0 = 0;
    /** Поле порт 1 */
    private int PORT1 = 0;
    /** Поле порт 2 */
    private int PORT2 = 0;
    /** Поле порт 3 */
    private int PORT3 = 0;
    /** Поле порт 4 */
    private int PORT4 = 0;
    /** Поле порт 5 */
    private int PORT5 = 0;
    /** Поле список TCP соединений */
    private final ArrayList<TCPClient> tcpClients = new ArrayList<>();
    /** Поле ввод URL */
    private final JTextField urlField = new JTextField("Video1.mp4", 30);
    /** Поле ввод пути сохранения скачанного файда  */
    private final JTextField savePathField = new JTextField("C:\\Users\\anton\\OneDrive\\Documents\\AATEST\\Client", 22);
    /** Текст с различной информацией */
    private final JLabel statusBar = new JLabel("Before starting work, download configuration file");
    /** Массив доступных URL */
    private String[] urls;

    public ClientWindow() {
        super("Download");
        this.setSize(450, 450);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        statusBar.setFont(new Font("Arial", Font.BOLD, 13));

        menuBar.add(statusBar);

        JPanel jTitles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jTitles.setBackground(new Color(207, 207, 255));
        jTitles.setPreferredSize(new Dimension(100, 300));

        JLabel urlTitle = new JLabel("URL: ");
        urlTitle.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel savePathTitle = new JLabel("Save Path: ");
        savePathTitle.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel progressTitle = new JLabel("Progress: ");
        progressTitle.setFont(new Font("Arial", Font.BOLD, 15));

        JButton savePathButton = new JButton("Browse");
        savePathButton.addActionListener(e -> {
            final JFileChooser saveAs = new JFileChooser();
            saveAs.setApproveButtonText("Select the file save path");
            saveAs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = saveAs.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this,
                        saveAs.getSelectedFile());
            }
            savePathField.setText(saveAs.getSelectedFile().getAbsolutePath());
        });

        JButton fileButton = new JButton("File");

        JButton connectToServer = new JButton("Connect to server");

        urlField.setFont(new Font("Arial", Font.BOLD, 13));
        savePathField.setFont(new Font("Arial", Font.BOLD, 13));
        savePathField.setEnabled(false);

        final ProgressBar prBar0= new ProgressBar(20, 20);
        prBar0.setOriginAndSize(0, 0, 0, 0);
        final ProgressBar prBar1= new ProgressBar(20, 20);
        prBar1.setOriginAndSize(0, 0, 0, 0);
        final ProgressBar prBar2= new ProgressBar(20, 20);
        prBar2.setOriginAndSize(0, 0, 0, 0);
        final ProgressBar prBar3= new ProgressBar(20, 20);
        prBar3.setOriginAndSize(0, 0, 0, 0);
        final ProgressBar prBar4= new ProgressBar(20, 20);
        prBar4.setOriginAndSize(0, 0, 0, 0);
        final ProgressBar prBar5= new ProgressBar(20, 20);
        prBar5.setOriginAndSize(0, 0, 0, 0);

        jTitles.add(urlTitle);
        jTitles.add(urlField);
        jTitles.add(savePathTitle);
        jTitles.add(savePathField);
        jTitles.add(savePathButton);
        jTitles.add(fileButton);
        jTitles.add(connectToServer);


        JPanel jProgressPanel = new JPanel();
        jProgressPanel.setLayout(new BoxLayout(jProgressPanel, BoxLayout.Y_AXIS));
        jProgressPanel.add(progressTitle);
        jProgressPanel.add(prBar0);
        jProgressPanel.add(prBar1);
        jProgressPanel.add(prBar2);
        jProgressPanel.add(prBar3);
        jProgressPanel.add(prBar4);
        jProgressPanel.add(prBar5);

        JPanel jContentPanel = new JPanel();
        jContentPanel.setLayout(new GridLayout(2,1));
        jContentPanel.add(jTitles);
        jContentPanel.add(jProgressPanel);

        JButton readyButton = new JButton("Ready");
        readyButton.setBackground(new Color(207, 207, 255));
        readyButton.setFont(new Font("Arial", Font.BOLD, 15));
        readyButton.setEnabled(false);

        readyButton.addActionListener(e -> {
            try {
                boolean flagStart = false;
                if (!(urlField.getText().isEmpty() || savePathField.getText().isEmpty())) {
                    for(String url : urls) {
                        if(urlField.getText().equals(url)) flagStart = true;
                        else statusBar.setText("Incorrect URL");
                    }
                    if(flagStart) {

                        prBar0.setOriginAndSize(0, 0, 0, 20);
                        prBar1.setOriginAndSize(0, 0, 0, 20);
                        prBar2.setOriginAndSize(0, 0, 0, 20);
                        prBar3.setOriginAndSize(0, 0, 0, 20);
                        prBar4.setOriginAndSize(0, 0, 0, 20);
                        prBar5.setOriginAndSize(0, 0, 0, 20);
                        statusBar.setText("Downloading...");
                        this.revalidate();
                        readyButton.setEnabled(false);

                        TCPClient tcpClient0 = new TCPClient(new Socket(IP_ADDRESS, PORT0), urlField.getText(), savePathField.getText());
                        TCPClient tcpClient1 = new TCPClient(new Socket(IP_ADDRESS, PORT1), urlField.getText(), savePathField.getText());
                        TCPClient tcpClient2 = new TCPClient(new Socket(IP_ADDRESS, PORT2), urlField.getText(), savePathField.getText());
                        TCPClient tcpClient3 = new TCPClient(new Socket(IP_ADDRESS, PORT3), urlField.getText(), savePathField.getText());
                        TCPClient tcpClient4 = new TCPClient(new Socket(IP_ADDRESS, PORT4), urlField.getText(), savePathField.getText());
                        TCPClient tcpClient5 = new TCPClient(new Socket(IP_ADDRESS, PORT5), urlField.getText(), savePathField.getText());

                        tcpClients.add(tcpClient0);
                        tcpClients.add(tcpClient1);
                        tcpClients.add(tcpClient2);
                        tcpClients.add(tcpClient3);
                        tcpClients.add(tcpClient4);
                        tcpClients.add(tcpClient5);

                        for(TCPClient tcpClient : tcpClients) {
                            tcpClient.start();
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    prBar0.setNewStat(TCPClient.getFileSize(), tcpClient0.getFileTransferProcessor().getFileSize());
                                    prBar1.setNewStat(TCPClient.getFileSize(), tcpClient1.getFileTransferProcessor().getFileSize());
                                    prBar2.setNewStat(TCPClient.getFileSize(), tcpClient2.getFileTransferProcessor().getFileSize());
                                    prBar3.setNewStat(TCPClient.getFileSize(), tcpClient3.getFileTransferProcessor().getFileSize());
                                    prBar4.setNewStat(TCPClient.getFileSize(), tcpClient4.getFileTransferProcessor().getFileSize());
                                    prBar5.setNewStat(TCPClient.getFileSize(), tcpClient5.getFileTransferProcessor().getFileSize());
                                    ClientWindow.this.revalidate();

                                    if ((tcpClients.size() == 6) &&
                                            tcpClients.get(0).isFinished() &&
                                            tcpClients.get(1).isFinished() &&
                                            tcpClients.get(2).isFinished() &&
                                            tcpClients.get(3).isFinished() &&
                                            tcpClients.get(4).isFinished() &&
                                            tcpClients.get(5).isFinished()) {
                                        prBar0.setWIDTH(350);
                                        prBar1.setWIDTH(350);
                                        prBar2.setWIDTH(350);
                                        prBar3.setWIDTH(350);
                                        prBar4.setWIDTH(350);
                                        prBar5.setWIDTH(350);
                                        tcpClients.clear();
                                        break;
                                    }
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException interruptedException) {
                                        interruptedException.printStackTrace();
                                    }
                                }
                                if (!SplitFiles.join(savePathField.getText() + "\\" + urlField.getText())) {
                                    throw new RuntimeException("Files are corrupted");
                                } else {
                                    prBar0.setWIDTH(450);
                                    prBar1.setWIDTH(450);
                                    prBar2.setWIDTH(450);
                                    prBar3.setWIDTH(450);
                                    prBar4.setWIDTH(450);
                                    prBar5.setWIDTH(450);
                                    statusBar.setText("Successful");
                                    JOptionPane.showMessageDialog(ClientWindow.this, "File has been built");
                                    readyButton.setEnabled(true);
                                }
                            }
                        }).start();
                    }
                } else throw new RuntimeException("The fields are not filled in");
            } catch (Exception error) {
                statusBar.setText(error.getMessage());
                readyButton.setEnabled(true);
                this.repaint();
            }
        });
        fileButton.addActionListener(e -> {
            try {
                final JFileChooser configurationFile = new JFileChooser();
                configurationFile.setApproveButtonText("Select the file configuration path");
                configurationFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "ini files only", "ini");
                configurationFile.setFileFilter(filter);
                int result = configurationFile.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(this,
                            configurationFile.getSelectedFile());
                    Properties prop = new Properties();
                    new Preloader(configurationFile.getSelectedFile().getAbsolutePath(), prop);
                    IP_ADDRESS = prop.getProperty("IP_ADDRESS");
                    PORT0 = Integer.parseInt(prop.getProperty("PORT0"));
                    PORT1 = Integer.parseInt(prop.getProperty("PORT1"));
                    PORT2 = Integer.parseInt(prop.getProperty("PORT2"));
                    PORT3 = Integer.parseInt(prop.getProperty("PORT3"));
                    PORT4 = Integer.parseInt(prop.getProperty("PORT4"));
                    PORT5 = Integer.parseInt(prop.getProperty("PORT5"));
                    statusBar.setText("File configuration uploaded");
                }
            } catch (Exception error) {
                error.printStackTrace();
                statusBar.setText("Wrong file configuration");
            }
        });
        connectToServer.addActionListener(e -> {
            try {
                if(statusBar.getText().equals("File configuration uploaded")) {
                    Socket socket = new Socket(IP_ADDRESS, PORT0);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    urls = in.readLine().split(";");
                    in.close();
                    statusBar.setText("Connection ready");
                    readyButton.setEnabled(true);
                } else statusBar.setText("File configuration not uploaded");
            } catch (IOException error) {
                error.printStackTrace();
                statusBar.setText(error.getMessage());
            }
        });

        this.add(jContentPanel, BorderLayout.CENTER);
        this.add(readyButton, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(ClientWindow::new);
    }
}