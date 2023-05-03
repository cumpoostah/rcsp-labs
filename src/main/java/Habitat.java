import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Habitat extends JPanel {
    final public static int WIDTH = 800;
    final public static int HEIGHT = 600;
    final private int POSITION_X = 0;
    final private int POSITION_Y = 0;

    int dX, dY;

    private Thread threadImage, threadText;

    public ArrayList<RightClick> listImage = new ArrayList<RightClick>();
    public ArrayList<LeftClick> listText = new ArrayList<LeftClick>();

    private boolean toDraw = true;
    private boolean isRunImage = true;
    private boolean isRunText = true;

    public Habitat() {
        setSize(WIDTH, HEIGHT);
        setLocation(POSITION_Y, POSITION_Y);
        setFocusable(true);
        addMouseListener(new PanelMouseListener());
        addKeyListener(new PanelKeyboardListener());
        setLayout(null);

        threadImage = new Thread(new Runnable() {
            @Override
            public void run() {
                updateImage();
            }
        });

        threadText = new Thread(new Runnable() {
            @Override
            public void run() {
                updateText();
            }
        });
        threadImage.start();
        threadText.start();
    }

    public void saveToTXT () {
        try (OutputStream f = new FileOutputStream("File.txt", false)) {
            try (OutputStreamWriter writer = new OutputStreamWriter(f)) {
                for (Object o : listImage)
                    writer.write(o.toString() + "\n");

                for (Object o : listText)
                    writer.write(o.toString() + "\n");
            }
        }
        catch(IOException ex) {
            System.err.println(ex);
        }
    }

    public void loadTXT () {
        try {
            Files.readAllLines(Paths.get("File.txt")).forEach(line -> {
                        String[] s = line.split("");
                        double x = Double.parseDouble(s[1]);
                        double y = Double.parseDouble(s[2]);
                        if(s[0] == "RightClick")
                            listImage.add(new RightClick((int) x, (int) y, "src/main/resources/assets/RightClick.png"));
                        if(s[1] == "LeftClick")
                            listText.add(new LeftClick((int) x, (int) y));
                    }
            );
//            paint();
            updateUI();
        }
        catch(IOException ex) {
            System.err.println(ex);
        }

    }

    public void saveBIN (){
//        byte[] bytes =
    }

    public void addImage(int x, int y) {
        listImage.add(new RightClick(x, y, "src/main/resources/assets/RightClick.png"));
    }

    public void addText(int x, int y) {
        listText.add(new LeftClick(x, y));
    }

    public void moveText(double t) {
        ObjectClick o;

        for (int i = 0; i < listText.size(); i++) {
            o = listText.get(i);
            o.move(t);
        }
    }

    public void moveImage(double t) {
        ObjectClick o;

        for (int i = 0; i < listImage.size(); i++) {
            o = listImage.get(i);
            o.move(t);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < listText.size(); i++) {
            listText.get(i).paint(g);
        }

        for (int i = 0; i < listImage.size(); i++) {
            listImage.get(i).paint(g);
        }
    }

    public void updateImage() {

        double before, now;
        before = System.currentTimeMillis();

        while (true) {
            now = System.currentTimeMillis();
            if (isRunImage) {
                moveImage((now - before));
            }

            before = now;
            repaint();
        }
    }

    public void updateText() {
        double before, now;
        before = System.currentTimeMillis();

        while (true) {
            now = System.currentTimeMillis();
            if (isRunText) {
                moveText((now - before));
            }
            before = now;
            repaint();
        }
    }

    class PanelMouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            super.mouseClicked(e);

            for (int i = 0; i < listImage.size(); i++) {
                if (listImage.get(i).isBelongs(e.getX(), e.getY())) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        listImage.remove(i);
                    } else if (e.getButton() == MouseEvent.BUTTON2) {
                        if (listImage.get(i).isMoving()) {
                            listImage.get(i).setMoving(false);
                        } else {
                            listImage.get(i).setMoving(true);
                        }
                    }
                    toDraw = false;
                }
            }
            for (int i = 0; i < listText.size(); i++) {
                if (listText.get(i).isBelongs(e.getX(), e.getY())) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        listText.remove(i);
                    } else if (e.getButton() == MouseEvent.BUTTON2) {
                        if (listText.get(i).isMoving()) {
                            listText.get(i).setMoving(false);
                        } else {
                            listText.get(i).setMoving(true);
                        }
                    }
                    toDraw = false;
                }
            }

            if (toDraw) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    addImage(e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    addText(e.getX(), e.getY());
                }
            }
            toDraw = true;

        }
    }

    class PanelKeyboardListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            if (e.getKeyCode() == KeyEvent.VK_Q) {

                if (isRunImage) {
                    isRunImage = false;
                } else {
                    isRunImage = true;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_W) {
                if (isRunText) {
                    isRunText = false;
                } else {
                    isRunText = true;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_E) {
                if (isRunImage) {
                    isRunImage = false;
                } else {
                    isRunImage = true;
                }

                if (isRunText) {
                    isRunText = false;
                } else {
                    isRunText = true;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {

            }
        }
    }
}
