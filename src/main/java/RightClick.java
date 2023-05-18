import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class RightClick extends ObjectClick implements Serializable {
    transient Image i;

    RightClick() {
        super(new Random().nextInt(), new Random().nextInt(), new Random().nextInt(), new Random().nextInt());

        i = new ImageIcon("src/main/resources/RightClick.png").getImage();

        setHeight(i.getHeight(null));
        setWidth(i.getWidth(null));

        setX(getX()-getWidth()/2);
        setY(getY()-getHeight()/2);
    }

    RightClick(int x, int y, int height, int width){
        super(new Random().nextInt(), new Random().nextInt(), new Random().nextInt(), new Random().nextInt());
        this.setMoving(true);
    }

    RightClick(int x, int y, String filename) {

        super(x, y, 0, 0);
        i = new ImageIcon(filename).getImage();

        setHeight(i.getHeight(null));
        setWidth(i.getWidth(null));

        setX(getX()-getWidth()/2);
        setY(getY()-getHeight()/2);

        System.out.println("Добавление картинки по координатам:");
        System.out.println("Ширина: " + getX());
        System.out.println("Высота: " + getY());
        System.out.println("Объект добавлен!" + "\n");
    }

    public void paint(Graphics g){
        g.drawImage(i, (int) getX(), (int) getY(), null);
    }

    public void setImage(Image i){
        this.i = i;
    }

    public void move (double t) {
        if (getX()+getWidth() < Habitat.WIDTH && getX() > 0) {
            moveX(t);
        } else {

            if(getX()+getWidth()>= Habitat.WIDTH)
                setX(Habitat.WIDTH -1 - getWidth());
            if(getX() <= 0)
                setX(0 +1);

            setdX(getdX()*-1);

        }
        if(getY()+getHeight() < Habitat.HEIGHT & getY() > 0) {
            moveY(t);

        } else {
            if(getY()+getHeight() >= Habitat.HEIGHT)
                setY(Habitat.HEIGHT -1 - getHeight());
            if(getY() <= 0)
                setY(0 +1);
            setdY(getdY()*-1);
        }
    }


    @Override
    public String toString() {
        return String.format("RightClick %f %f", getX(), getY());
    }

}

