package spaceinvaders.sprite;

import javax.swing.ImageIcon;

public class Shot extends Sprite {
	String bulletImg = "spaceinvaders/sprite/images/shot.png";
	static final int H_SPACE = 6;
	static final int V_SPACE = 1;
	
	public Shot() { }
	
	public Shot(int x, int y) {
		initShot(x, y);
	}

	public void initShot(int x, int y) {
		ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource(bulletImg), "Bullet");

		setImage(ii.getImage());
		
		setX(x + H_SPACE);
		setY(y - V_SPACE);
	}

}
