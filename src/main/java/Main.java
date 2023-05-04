import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.util.stream.Collectors;

public class Main extends JFrame {
    JMenu menu = new JMenu("File");
    JMenu menuSerialization = new JMenu("Serialization");
    JMenu moveQ = new JMenu("Движение/остановка картинки по кнопке - Q");
    JMenu moveW = new JMenu("Движение текста по кнопке - W");
    JMenu moveE = new JMenu("Движение объектов по кнопке - E");
    JMenuBar menuBar = new JMenuBar();
    JMenuItem menuItem = new JMenuItem("Manual");
    JMenuItem menuSaveTXT = new JMenuItem("Save to TXT");
    JMenuItem menuLoadTXT = new JMenuItem("Load from TXT");
    JMenuItem menuSaveBIN = new JMenuItem("Save to BIN");
    JMenuItem menuLoadBIN = new JMenuItem("Load from BIN");
    JMenuItem menuSaveXML = new JMenuItem("Save to XML");
    JMenuItem menuLoadXML = new JMenuItem("Load from XML");

    public Main(){
        setTitle("Лабораторная работа №1");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        setSize(900, 600);
        setLocation(400, 400);
        Habitat habitatPanel = new Habitat();
        add(habitatPanel);
        setVisible(true);

        moveQ.setEnabled(false);
        moveW.setEnabled(false);
        moveE.setEnabled(false);

        add(menuBar);
        menuBar.add(menu);
        menuBar.add(menuSerialization);
        menuBar.add(moveQ);
        menuBar.add(moveW);
        menuBar.add(moveE);
        menu.add(menuItem);
        menuSerialization.add(menuSaveTXT);
        menuSerialization.add(menuLoadTXT);
        menuSerialization.addSeparator();
        menuSerialization.add(menuSaveBIN);
        menuSerialization.add(menuLoadBIN);
        menuSerialization.addSeparator();
        menuSerialization.add(menuSaveXML);
        menuSerialization.add(menuLoadXML);
        setJMenuBar(menuBar);

        menuItem.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File("src/main/resources/assets/guide.pdf"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        menuSaveTXT.addActionListener(e->{
            habitatPanel.saveToText();
        });

        menuLoadTXT.addActionListener(e-> {
            habitatPanel.loadFromText();
        });

        menuSaveBIN.addActionListener(e->{
            habitatPanel.saveToBinary();
        });

        menuLoadBIN.addActionListener(e->{
            habitatPanel.loadFromBinary();
        });

        menuSaveXML.addActionListener(e->{
            habitatPanel.saveToSerializable();
        });

        menuLoadXML.addActionListener(e->{
            habitatPanel.loadFromSerializable();
        });
    }

    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader("serializableFile.xml"));
        String xml = reader.lines().collect(Collectors.joining());
        int index = xml.indexOf("<RightClick>");
        String rightClickXml = xml.substring(index, xml.indexOf("</RightClick>", index) + 13); // выделить xml для тега
        int indexToReplace = rightClickXml.indexOf("=\"");
        String strToReplace;
        if (indexToReplace != -1) {
            strToReplace = rightClickXml.substring(indexToReplace + 2, rightClickXml.indexOf("\">"));
        } else {
            if (rightClickXml.contains("reference")) {

            }
        }
        rightClickXml = rightClickXml.replace(strToReplace, "java.awt.image.BufferedImage");
        System.out.println(rightClickXml);
    }

}

