import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class LeftClick extends ObjectClick {
    String l;
    JLabel jl = new JLabel();
    private int dXY;

    LeftClick(int x, int y){
        super(x,y,0, 0);
        jl.setText("Строка текста");

        setHeight((int)jl.getPreferredSize().getHeight());
        setWidth((int)jl.getPreferredSize().getWidth());

        setX(getX()-getWidth()/2);
        setY(getY()-getHeight()/2);

        System.out.println("Добавление строки текста по координатам:");
        System.out.println("Ширина: " + getX());
        System.out.println("Высота: " + getY());
        System.out.println("Строка текста добавлена!" + "\n");

        top = new Random().nextInt(25);
        left = new Random().nextInt(25);
        right = new Random().nextInt(25);
        bot = new Random().nextInt(25);
    }

    public void paint(Graphics g) {

        g.drawString(jl.getText(), (int)getX(), (int)(getY()+getHeight()));
    }

    public void moveXLeft(double t) {
        setX(getX()+getdXY()* t / 100);
    }

    public void moveXRigth(double t) {
        setX(getX()-getdXY()* t / 100);
    }

    public void moveYUp(double t) {
        setY(getY()-getdXY()* t / 100);
    }

    public void moveYDown(double t) {
        setY(getY()+getdXY()* t / 100);
    }


    private int getdXY() {
        return dXY;
    }


    @Override
    public void move(double t) {
        if (getX()+getWidth()< Habitat.WIDTH && getX() > 0) {
            moveX(t);
        } else {
            if (getX()+getWidth() >= Habitat.WIDTH) setX(Habitat.WIDTH -1 - getWidth());
            if (getX() <= 0) setX(0 + 1);

            setdX(getdX()*-1);
        }

        if(getY()+getHeight()< Habitat.HEIGHT & getY() > 0) {
            moveY(t);

        } else {
            if(getY()+getHeight()>= Habitat.HEIGHT) setY(Habitat.HEIGHT -1 - getHeight());
            if(getY() <= 0) setY(0 + 1);
            setdY(getdY()*-1);
        }
    }

    @Override
    public String toString() {
        return String.format("LeftClick %f %f", getX(), getY());
    }
}

