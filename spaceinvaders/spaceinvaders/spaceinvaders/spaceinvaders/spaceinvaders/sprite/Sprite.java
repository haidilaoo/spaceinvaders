package spaceinvaders.sprite;

import java.awt.Image;

public class Sprite {

	private boolean visible;
    private Image image;
    private boolean dying;
    int x, y, dx;

    public Sprite() {
        visible = true;
    }
    
    public Sprite(int x, int y) {
    	this.x = x;
    	this.y = y;
    }
    
    public Sprite(int x, int y, int dx) {
    	this.x = x;
    	this.y = y;
    	this.dx = dx;
    }

    public void die() {
        visible = false;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public int getDX() {
    	return this.dx;
    }
    
    public void setDX(int dx) {
    	this.dx = dx;
    }
    
    public void move(int dx, int canvas_width, int width) {
    	this.x += dx;
    	
    	if (x <= 2) {
		x = 2;
		}
		 
		if (x >= canvas_width - 1* width) {
	
	        x = canvas_width - 1 * width;
	    }
    }
    
    public void move(int direction) {
		this.x += direction;
	}
    
    public void setDying(boolean dying) {
    	this.dying = dying;
    }
    
    public boolean isDying() {
    	return this.dying;
    }

}
