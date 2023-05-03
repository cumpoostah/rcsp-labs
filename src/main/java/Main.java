import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.*;

public class Main extends JFrame {
    JMenu menu = new JMenu("File");
    JMenu menuSerialization = new JMenu("Serialization");
    JMenu moveQ = new JMenu("Движение картинки по кнопке - Q");
    JMenu moveW = new JMenu("Движение текста по кнопке - W");
    JMenu moveE = new JMenu("Движение объектов по кнопке - E");
    JMenuBar menuBar = new JMenuBar();
    JMenuItem menuItem = new JMenuItem("How to do?");
    JMenuItem menuSaveTXT = new JMenuItem("Save to TXT");
    JMenuItem menuLoadTXT = new JMenuItem("Load to TXT");
    JMenuItem menuSaveBIN = new JMenuItem("Save to BIN");
    JMenuItem menuLoadBIN = new JMenuItem("Load to BIN");

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
        setJMenuBar(menuBar);

        menuItem.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File("src/main/resources/assets/guide.pdf"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        menuSaveTXT.addActionListener(e->{
            habitatPanel.saveToTXT();
        });

        menuLoadTXT.addActionListener(e-> {
            habitatPanel.loadTXT();
        });

        menuSaveBIN.addActionListener(e->{
            System.out.println(3);
        });

        menuLoadBIN.addActionListener(e->{
            System.out.println(4);
        });
    }

    public static void main(String[] args){

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        Main mw = new Main();
    }

}

