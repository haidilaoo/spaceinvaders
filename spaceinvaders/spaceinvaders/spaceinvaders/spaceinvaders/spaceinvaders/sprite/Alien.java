package spaceinvaders.sprite;

import javax.swing.ImageIcon;

public class Alien extends Sprite {
	String alienImg = "spaceinvaders/sprite/images/alien.png";

	
	public Alien(int x, int y) {
		initAlien(x,y);
	}

	private void initAlien(int x, int y) {
		super.setX(x);
		super.setY(y);
		
		ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource(alienImg), "Alien");
		setImage(ii.getImage());
	}	
	
}
