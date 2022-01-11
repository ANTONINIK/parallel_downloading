import javax.swing.*;
import java.awt.*;

/**
 * Класс для добавление в клиенское окно прогресс баров
 */

public class ProgressBar extends JComponent
{
    private int HEIGHT = 0;
    private int WIDTH = 0;
    private int MES_X = 0;
    private int MES_Y = 0;

    ProgressBar(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    public void setOriginAndSize(int MES_X, int MES_Y, int WIDTH, int HEIGHT) {
        this.MES_X = MES_X;
        this.MES_Y = MES_Y;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return (new Dimension(WIDTH, HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.pink);
        g.fillRect(MES_X, MES_Y, WIDTH, HEIGHT);
    }

    /**
     * Функция изменения ширины Progress bar
     * @param fileSize - общий размер файла
     * @param received - размер полученных данных
     */
    public void setNewStat(int fileSize, int received) {
        if (fileSize != 0) {
            double percent = (double) received / (double) fileSize;
            setWIDTH((int) (50 + 300 * percent));
        }
    }

    public void setWIDTH(int WIDTH) {
        if(this.WIDTH != WIDTH) {
            this.WIDTH = WIDTH;
            repaint();
        }
    }
}