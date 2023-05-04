import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.NoTypePermission;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.stream.Collectors;

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

    /*public void saveToTXT () {
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
                        line = line.replaceAll(",", ".");
                        String[] s = line.split(" ");
                        double x = Double.parseDouble(s[1]);
                        double y = Double.parseDouble(s[2]);
                        if (s[0].equals("RightClick"))
                            listImage.add(new RightClick((int) x, (int) y, "src/main/resources/assets/RightClick.png"));
                        if (s[0].equals("LeftClick"))
                            listText.add(new LeftClick((int) x, (int) y));
                    }
            );
//            paint();
            updateUI();
        }
        catch(IOException ex) {
            System.err.println(ex);
        }

    }*/

    public void saveToBinary() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("binaryFile.bin"))) {
            oos.writeInt(listImage.size());
            for (Object o : listImage)
                oos.writeObject(o);

            oos.writeInt(listText.size());
            for (Object o : listText)
                oos.writeObject(o);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void loadFromBinary() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("binaryFile.bin"))) {
            int size = ois.readInt();
            for (int i = 0; i < size; i++) {
                Object o = ois.readObject();
                if (o instanceof RightClick)
                    listImage.add((RightClick) o);
            }

            size = ois.readInt();
            for (int i = 0; i < size; i++) {
                Object o = ois.readObject();
                if (o instanceof LeftClick)
                    listText.add((LeftClick) o);
            }
            updateUI();
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println(ex);
        }
    }

    public void saveToSerializable() {
        XStream xstream = new XStream();
        xstream.alias("JLabel", JLabel.class);
        xstream.registerConverter(new JLabelConverter());
        xstream.registerConverter(new ImageConverter());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("serializableFile.xml"))) {
            xstream.toXML(listImage, writer);
            xstream.toXML(listText, writer);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void loadFromSerializable() {
        XStream xstream = new XStream();
        xstream.alias("JLabel", JLabel.class);
        xstream.registerConverter(new JLabelConverter());
        xstream.registerConverter(new ImageConverter());
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.addPermission(NoTypePermission.NONE);
        xstream.allowTypes(new Class[]{Habitat.class, LeftClick.class, RightClick.class, java.util.List.class,
                java.awt.image.BufferedImage.class});
        try (BufferedReader reader = new BufferedReader(new FileReader("serializableFile.xml"))) {
            //listImage = (ArrayList<RightClick>) xstream.fromXML(reader);
            //listText = (ArrayList<LeftClick>) xstream.fromXML(reader);
            String xml = reader.lines().collect(Collectors.joining()); // прочитать весь xml как одну строку
            int index = xml.indexOf("<LeftClick>"); // найти первое вхождение тега <LeftClick>
            while (index != -1) { // если тег найден
                String leftClickXml = xml.substring(index, xml.indexOf("</LeftClick>", index) + 12); // выделить xml для тега
                listText.add((LeftClick) xstream.fromXML(leftClickXml)); // десериализовать список объектов
                // LeftClick из выделенного xml
                index = xml.indexOf("<LeftClick>", index + 1); // искать следующий тег
            }
            index = xml.indexOf("<RightClick>"); // найти первое вхождение тега <RightClick>
            while (index != -1) { // если тег найден
                String rightClickXml = xml.substring(index, xml.indexOf("</RightClick>", index) + 13); // выделить xml для тега
                int indexToReplace = rightClickXml.indexOf("=\"");
                String strToReplace = rightClickXml.substring(indexToReplace + 2, rightClickXml.indexOf("\">"));
                rightClickXml = rightClickXml.replace(strToReplace, "java.awt.image.BufferedImage");
                if (rightClickXml.contains("reference"))
                listImage.add((RightClick) xstream.fromXML(rightClickXml)); // десериализовать список объектов RightClick
                // из выделенного xml
                index = xml.indexOf("<RightClick>", index + 1); // искать следующий тег
            }

            updateUI();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private static class JLabelConverter implements Converter {
        public void marshal(Object obj, HierarchicalStreamWriter writer, MarshallingContext context) {
            JLabel label = (JLabel) obj;
            writer.setValue(label.getText());
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            JLabel label = new JLabel(reader.getValue());
            return label;
        }

        public boolean canConvert(Class clazz) {
            return clazz.equals(JLabel.class);
        }
    }

    private static class ImageConverter implements Converter {
        @Override
        public boolean canConvert(Class clazz) {
            return Image.class.isAssignableFrom(clazz);
        }

        @Override
        public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
            Image image = (Image) object;
            try {
                // Создаем буферизованное изображение из объекта Image
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);
                bufferedImage.getGraphics().drawImage(image, 0, 0, null);
                // Преобразуем его в массив байтов в формате PNG
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                // Записываем массив байтов в XML как текстовый узел
                writer.setValue(Base64.getEncoder().encodeToString(bytes));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            try {
                // Считываем текстовый узел из XML как массив байтов
                byte[] bytes = Base64.getDecoder().decode(reader.getValue());
                // Преобразуем массив байтов в буферизованное изображение
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
                // Создаем объект Image из буферизованного изображения
                return bufferedImage;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void saveToText() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("textFile.txt"))) {
            pw.println(listImage.size());
            for (Object o : listImage)
                pw.println(o.toString());

            pw.println(listText.size());
            for (Object o : listText)
                pw.println(o.toString());
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void loadFromText() {
        try (Scanner scanner = new Scanner(new FileReader("textFile.txt"))) {
            int size = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < size; i++) {
                String line = scanner.nextLine();
                line = line.replaceAll(",", ".");
                String[] s = line.split(" ");
                double x = Double.parseDouble(s[1]);
                double y = Double.parseDouble(s[2]);
                if (s[0].equals("RightClick"))
                    listImage.add(new RightClick((int) x, (int) y, "src/main/resources/assets/RightClick.png"));
            }

            size = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < size; i++) {
                String line = scanner.nextLine();
                line = line.replaceAll(",", ".");
                String[] s = line.split(" ");
                double x = Double.parseDouble(s[1]);
                double y = Double.parseDouble(s[2]);
                if (s[0].equals("LeftClick"))
                    listText.add(new LeftClick((int) x, (int) y));
            }
            updateUI();
        } catch (IOException ex) {
            System.err.println(ex);
        }
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

        if (!listText.isEmpty()) {
            for (int i = 0; i < listText.size(); i++) {
                listText.get(i).paint(g);
            }
        }

        if (!listImage.isEmpty()) {
            for (int i = 0; i < listImage.size(); i++) {
                listImage.get(i).paint(g);
            }
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
