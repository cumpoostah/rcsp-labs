import java.awt.*;
import java.util.Random;

abstract public class ObjectClick implements Drawable, Moveable {
    private double x, y;
    private double velocityX, velocityY;
    private int centerX, centerY;
    int cX, cY;
    int top, left,right, bot;

    private boolean isLeft, ifRigth, isLeftD, isRigthU = false;
    private int height, width;
    private boolean isMoving = true;

    private Color c;

    ObjectClick() {
        this.x = new Random().nextInt(200);
        this.y = new Random().nextInt(200);

        velocityX = -100+new Random().nextInt(200);
        velocityY = -100+new Random().nextInt(200);

        this.height = new Random().nextInt(200);
        this.width = new Random().nextInt(200);
    }

    ObjectClick(int x, int y, int height, int width){
        this.x = x;
        this.y = y;

        velocityX = -100+new Random().nextInt(200);
        velocityY = -100+new Random().nextInt(200);

        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setdX(double dX) {
        this.velocityX = dX;
    }

    public void setdY(double dY) {
        this.velocityY = dY;
    }

    public double getdX() {
        return velocityX;
    }

    public double getdY() {
        return velocityY;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isBelongs(int x, int y){
        if(this.x < x & (this.x + width) > x ){
            if(this.y < y & (this.y + height) > y){
                return true;
            }
        }

        return false;
    }

    @Override
    public void moveX(double t) {
        if(isMoving)
            this.x = x + velocityX * t / 1000.0;
    }

    @Override
    public void moveY(double t) {
        if(isMoving)
            this.y = y + velocityY * t / 1000.0;
    }

    public int getcX() {
        return cX;
    }

    public int getcY() {
        return cY;
    }

    public void setcX(int cX) {
        this.cX = cX;
    }

    public void setcY(int cY) {
        this.cY = cY;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getBot() {
        return bot;
    }

    public Color getC() {
        return c;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setC(Color c) {
        this.c = c;
    }

    public void setIsLeft(boolean left) {
        this.isLeft = left;
    }

    public void setIfRigth(boolean ifRigth) {
        this.ifRigth = ifRigth;
    }

    public void setLeftD(boolean leftD) {
        this.isLeftD = leftD;
    }

    public void setRigthU(boolean rigthU) {
        this.isRigthU = rigthU;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public boolean isIfRigth() {
        return ifRigth;
    }

    public boolean isLeftD() {
        return isLeftD;
    }

    public boolean isRigthU() {
        return isRigthU;
    }
}
