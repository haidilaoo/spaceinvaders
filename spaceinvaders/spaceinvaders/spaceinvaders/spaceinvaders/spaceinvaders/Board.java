package spaceinvaders;

import spaceinvaders.SoundEffect.Volume;
import spaceinvaders.sprite.*;
import spaceinvaders.SoundEffect;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*; 
   
public class Board extends JPanel {    // main class for the game
   
   // Define constants for the game
   static final int CANVAS_WIDTH = 358;    // width and height of the game screen
   static final int CANVAS_HEIGHT = 350;
   static final int UPDATES_PER_SEC = 4;    // number of game update per second
   static final long UPDATE_PERIOD_NSEC = 1000000000L / UPDATES_PER_SEC;  // nanoseconds
   
   static final int ALIEN_INIT_X = 150;
   static final int ALIEN_INIT_Y = 5;
   static final int NO_OF_ALIEN_ROW = 3;
   static final int NO_OF_ALIEN_COLUMN = 5;
   static final int NUMBER_OF_ALIENS = NO_OF_ALIEN_ROW * NO_OF_ALIEN_COLUMN;
   static final int ALIEN_HEIGHT = 12;
   static final int ALIEN_WIDTH = 12;
   static final int ALIEN_SPEED = 5;
   
   static final int GO_DOWN = 15;
   static final int GROUND = 290;
   static final int PLAYER_WIDTH = 15;
   static final int PLAYER_HEIGHT = 10;
   static final int BULLET_SPEED = 10;

   
 
   // Enumeration for the states of the game.
   static enum GameState {
      INITIALIZED, PLAYING, PAUSED, GAMEOVER, DESTROYED
   }
   static GameState state;   // current state of the game
   
   // Define instance variables for the game objects
   private Player player;
   private ArrayList<Alien> aliens;
   private Alien alien;
   private Shot shot;
   
   private int direction = -ALIEN_SPEED, deaths = 0;
   private String message, killedmsg;
   private Font headerFont = new Font("Verdana", Font.BOLD, 20);	
   private Font textFont = new Font("Verdana", Font.ITALIC, 14);
//   JButton btnStart; 
   String expImg = "spaceinvaders/sprite/images/explosion.png";
   
   
   
   // Handle for the custom drawing panel
   private GameCanvas canvas;
  
   
   // Constructor to initialize the UI components and game objects
   public Board() {
      // Initialize the game objects
      gameInit();
      System.out.println ("game init " + state);
      // UI components
      canvas = new GameCanvas();
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
      add(canvas);
     
     //FOR KEY BINDING
      Action leftAction;
      leftAction  = new LeftAction();
      InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
      im.put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
      ActionMap am = getActionMap();
      am.put("leftAction", leftAction);
      
      Action rightAction;
      rightAction  = new RightAction();
      im.put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
      am.put("rightAction", rightAction);
      
      Action shootAction;
      shootAction  = new ShootAction();
      im.put(KeyStroke.getKeyStroke("SPACE"), "shootAction");
      am.put("shootAction", shootAction);
      
      Action pauseAction;
      pauseAction  = new PauseAction();
      im.put(KeyStroke.getKeyStroke("P"), "pauseAction");
      am.put("pauseAction", pauseAction);
    
      Action soundAction;
      soundAction  = new SoundAction();
      im.put(KeyStroke.getKeyStroke("S"), "soundAction");
      am.put("soundAction", soundAction);
      
      Action restartAction;
      restartAction  = new RestartAction();
      im.put(KeyStroke.getKeyStroke("R"), "restartAction");
      am.put("restartAction", restartAction);
    
    
      //Start the game
      gameStart();
      SoundEffect.START.stop();
      SoundEffect.BGM.loop();

      
    
   }
   
   // All the game related codes here
   
   // Initialize all the game objects, run only once in the constructor of the main class.
   public void gameInit() {
	   
	   aliens = new ArrayList<>();
	   
	   for (int i = 0; i < NO_OF_ALIEN_ROW; i++) {
		   for (int j = 0; j < NO_OF_ALIEN_COLUMN; j++) {
			   alien = new Alien(ALIEN_INIT_X + 18 * j, ALIEN_INIT_Y + 18 * i);
			   aliens.add(alien);
		   }
	   }
	   
	   player = new Player();
	   shot = new Shot();
	  
       state = GameState.INITIALIZED;
      
   }
   

   private static volatile boolean isStopped=false;
   // To start and re-start the game.
   public void gameStart() { 
      // Create a new thread
	 
      Thread gameThread =  new Thread() {
         // Override run() to provide the running behavior of this thread.
         @Override
         public void run() {
              gameLoop();
        	 
         }
      };
      // Start the thread. start() calls run(), which in turn calls gameLoop().
      gameThread.start();
      System.out.println("thread start " + state);
   }
   
   // Run the game loop here.
   private void gameLoop() {
      // Regenerate the game objects for a new game
	   if (state == GameState.INITIALIZED || state == GameState.GAMEOVER) {  
         state = GameState.PLAYING;
	   }
	   
      // Game loop
      long beginTime, timeTaken, timeLeft;  // in msec
      while (state != GameState.GAMEOVER) {
    	  
         beginTime = System.nanoTime();
         if (state == GameState.PLAYING) {   // not paused
            // Update the state and position of all the game objects,
            // detect collisions and provide responses.
            gameUpdate();
         }
         
         //TO IMPLEMENT PAUSED
         if (state == GameState.PAUSED) {
        	 
        	
    	 }
         
         
         // Refresh the display      
         repaint();
         // Delay timer to provide the necessary delay to meet the target rate
         timeTaken = System.nanoTime() - beginTime;
         timeLeft = (UPDATE_PERIOD_NSEC - timeTaken) / 1000000L;  // in milliseconds
         if (timeLeft < 10) timeLeft = 10;   // set a minimum
         try {
            // Provides the necessary delay and also yields control so that other thread can do work.
            Thread.sleep(timeLeft);
         } catch (InterruptedException ex) { }
      }
      
      
   }
   
   // Update the state and position of all the game objects,
   // detect collisions and provide responses.
   public void gameUpdate() {
	   
	   if (deaths == NUMBER_OF_ALIENS) {

           state = GameState.GAMEOVER;
           message = "Game won!";
           killedmsg = "Killed All Aliens!";
       }
	   
	   killAlien();
	   moveAlien(); 
   }
   
   // Refresh the display. Called back via repaint(), which invoke the paintComponent().
   private void gameDraw(Graphics2D g2d) {
	  
      switch (state) {
         case INITIALIZED:
           
        	 break;
            
         case PLAYING:
         	g2d.setColor(Color.GREEN);
        	g2d.drawLine(0, GROUND, CANVAS_WIDTH, GROUND);
 	    	drawPlayer(g2d);
 	    	drawShot(g2d);
 	     	drawAliens(g2d);
 	     	SoundEffect.START.stop();
	    	
            break;
     
         case PAUSED:
        	  g2d.drawLine(0, GROUND, CANVAS_WIDTH, GROUND);
	       	  drawPlayer(g2d);
	   	      drawShot(g2d);
	   	      drawAliens(g2d);
	       	  g2d.setFont(new Font("Verdana", Font.BOLD, 30));
             g2d.setColor(Color.RED);
             g2d.drawString("PAUSED", 110 , 175);
            break;
         
         case GAMEOVER:
        	System.out.println(message);
        	drawGameOver(g2d);
        	SoundEffect.LOSE.play();
        	System.out.println("Gameover gamestate " + state);
        	deaths =0;
            break;
      }

   }
   
  
    //KEY BINDING

	public class LeftAction extends AbstractAction {
		   @Override
		   public void actionPerformed(ActionEvent e) {
			   player.moveLeft(CANVAS_WIDTH);
	       		repaint(player.getX());
		   }
	   }
	   
	
	public class RightAction extends AbstractAction {
		   @Override
		   public void actionPerformed(ActionEvent e) {
			   player.moveRight(CANVAS_WIDTH);
	           repaint(player.getX());
		   }
	   }
	   
	 
	public class ShootAction extends AbstractAction {
		   @Override
		   public void actionPerformed(ActionEvent e) {
			// Display bullet when SPACE is pressed
	   		if (!shot.isVisible()) {
	   			shot = new Shot(player.getX(),player.getY());
	   			
	   			//SOUND ON SPACE 
	       	    SoundEffect.SHOOT.play(); 
	   		}        		
	   		

		   }
	   }
	   
	
	public class PauseAction extends AbstractAction {
		   @Override
		   public void actionPerformed(ActionEvent e) {
			   if(state == GameState.PLAYING) {
	      		 state = GameState.PAUSED; 
	      	 }
	      	 else if (state == GameState.PAUSED) {
	      		 state = GameState.PLAYING;
	      	 }
	      	
		   }
	   }
	
	public class RestartAction extends AbstractAction {
	   @Override
	   public void actionPerformed(ActionEvent e) {
		  if (state == GameState.GAMEOVER) {
			  gameInit();
			  gameStart();
		  }
      	
	   }
	}
	
	   @SuppressWarnings("serial")
   public class SoundAction extends AbstractAction {
	   @Override
	   public void actionPerformed(ActionEvent e) {
		
   		if (SoundEffect.volume == Volume.LOW) {
   			SoundEffect.volume = Volume.MUTE;
   			SoundEffect.BGM.stop(); 
   			System.out.println("volume " + SoundEffect.volume);
   			
   		}        		
   		else if (SoundEffect.volume == Volume.MUTE){
   			SoundEffect.volume = Volume.LOW;
   			SoundEffect.BGM.play(); 
   			System.out.println("volume " + SoundEffect.volume);
   		}

	   }
   }

   
   // Display/Draw player
   public void drawPlayer(Graphics2D g2d) {
	   if (player.isVisible()) {
		   g2d.drawImage(player.getImage(), player.getX(), player.getY(), this);
	   }
	   
	   if (player.isDying()) {
		   player.die();
		   state = GameState.GAMEOVER;
	   }
   }
   
   // Display/Draw shot
   private void drawShot(Graphics2D g2d) {
	   if (shot.isVisible()) {
		   g2d.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
	   }
   }
   
   // Display/Draw Aliens
   // For-each Loop
   private void drawAliens(Graphics2D g2d) {
 	   for (Alien alien : aliens) {

 		   if (alien.isVisible()) {

 			   g2d.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
 		   }

 		   if (alien.isDying()) {

 			   alien.die();
 		   }
 	   }
   }
   
   // Draw Game Over screen
   private void drawGameOver(Graphics2D g2d) {

	   g2d.setFont(headerFont);
	   FontMetrics headerWidth = this.getFontMetrics(headerFont);
	   g2d.setColor(Color.white);
	   g2d.drawString(message, (CANVAS_WIDTH - headerWidth.stringWidth(message))/2, 130);
	   

	   FontMetrics scoreWidth = this.getFontMetrics(headerFont);
	   if (killedmsg != "Killed All Aliens!") {
		   killedmsg = "You killed " + deaths + " aliens!";
	   }
	   g2d.drawString(killedmsg, (CANVAS_WIDTH - scoreWidth.stringWidth(killedmsg))/2 ,160);
	   
	   
	   
	   g2d.setFont(textFont);
	   FontMetrics textWidth = this.getFontMetrics(textFont);
	   String reset = "Press R to restart";
	   g2d.drawString(reset, (CANVAS_WIDTH - textWidth.stringWidth(reset))/2 ,210);
   }
   
   // Kill an alien 
   private void killAlien() {
	   if (shot.isVisible()) {
		   int shotX = shot.getX();
		   int shotY = shot.getY();
		   
		   for (Alien alien : aliens) {
			   int alienX = alien.getX();
			   int alienY = alien.getY();
			   
			   if (alien.isVisible() && shot.isVisible()) {
				   if (shotX >= (alienX) && shotX <= (alienX + ALIEN_WIDTH) && shotY >= (alienY) && shotY <= (alienY + ALIEN_HEIGHT)) {
					   
					   // Replace alien image with explosion image
					   ImageIcon ii = new ImageIcon(expImg);
					   SoundEffect.EXPLODE.play(); 
					   alien.setImage(ii.getImage());
					   alien.setDying(true);
					   deaths++;
					   
					   // Remove the bullet image
					   shot.die();
				   }
			   }
		   }
		   
		   // Adjusting bullet speed, move the bullet
		   int y = shot.getY();
		   y -= BULLET_SPEED;
		   
		   // Kill the bullet when it is out of screen
		   if (y<0) {
			   shot.die();
		   } else {
			   shot.setY(y);
		   }
	   }
   }
   
   // Move the Aliens
   private void moveAlien() {
	   for (Alien alien : aliens) {
		   int x = alien.getX();
		   
		   // If aliens reach the right end of the Board, move down and change direction to left.
		   if (x >= CANVAS_WIDTH - 15 && direction != -1) {
			   direction = -ALIEN_SPEED;
			   
			   Iterator<Alien> i1 = aliens.iterator();
			   
			   while (i1.hasNext()) {
				   Alien a2 = i1.next();
				   a2.setY(a2.getY() + GO_DOWN);
			   }
		   }
		   
		   // If aliens reach the left end of the Board, move down and change direction to right.
		   if (x <= 0 && direction != 1) {
			   direction = ALIEN_SPEED;
			   
			   Iterator<Alien> i2 = aliens.iterator();
			   
			   while (i2.hasNext()) {
				   Alien a = i2.next();
				   a.setY(a.getY() + GO_DOWN);
			   }
		   }
	   }
	   
	   //Move aliens
	   Iterator<Alien> it = aliens.iterator();

       while (it.hasNext()) {

           Alien alien = it.next();

           if (alien.isVisible()) {

               int y = alien.getY();

               if (y > GROUND - ALIEN_HEIGHT) {
                   state = GameState.GAMEOVER;
                   message = "Invasion!";
               }

               alien.move(direction);
           }
       }
   }
   
   // Retrieve Game State
   public GameState getGameState() {
	   return state;
   }
   
   public void setGameState(GameState state) {
	   this.state = state;
   }

   // Custom drawing panel, written as an inner class.
   class GameCanvas extends JPanel implements KeyListener {
      // Constructor
      public GameCanvas() {
         setFocusable(true);  // so that can receive key-events
         requestFocus();
         addKeyListener(this);
      }
   
      // Override paintComponent to do custom drawing.
      // Called back by repaint().
      @Override
      public void paintComponent(Graphics g) {
         Graphics2D g2d = (Graphics2D)g;
         super.paintComponent(g2d);   // paint background

         setBackground(Color.BLACK);  // may use an image for background
         

         // Draw the game objects
         gameDraw(g2d);
      }
      
       
      // KeyEvent handlers
      @Override
      public void keyPressed(KeyEvent e) {

      }
      
      @Override
      public void keyReleased(KeyEvent e) { }
   
      @Override
      public void keyTyped(KeyEvent e) { }
   }
  
  
}
