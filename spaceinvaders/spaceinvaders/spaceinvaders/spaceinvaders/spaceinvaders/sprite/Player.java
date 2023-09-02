package spaceinvaders.sprite;

import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Player extends Sprite {
	
	String playerImg = "spaceinvaders/sprite/images/tank.png";
    private final int START_X = 179;
    private final int START_Y = 280;
    
    private ImageIcon ii;
    private int width;

	public Player() {
		
		initPlayer(); 			
	}
	
	private void initPlayer() {

		ii = new ImageIcon(getClass().getClassLoader().getResource(playerImg), "Player");
		
		width = ii.getImage().getWidth(null);
		super.setImage(ii.getImage());
		
		super.setX(START_X);
		super.setY(START_Y);
	}
	
	public void moveLeft(int width) {
		super.move(-2, width, this.width);
	}
	 
	public void moveRight(int width) {
		super.move(2, width, this.width);
	}
	
}
