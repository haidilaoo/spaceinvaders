package spaceinvaders;

import java.awt.*;        // Use AWT's Layout Manager
import java.awt.event.*;  // Use AWT's Event handlers
import javax.swing.*;     // Use Swing's Containers and Components
import javax.swing.border.EmptyBorder;

import spaceinvaders.Board.GameState;

/** 
 * The Space Invaders Game
 *
 */

@SuppressWarnings("serial")
public class SpaceInvadersMain extends JFrame implements ActionListener {
		
	   static final String TITLE = "Space Invaders";     // title of the game
	   CardLayout cardLayout;
	   JPanel mainPanel;
	   JButton btnStart;
	   // private variables
	   Board board;
	   
	   
	   // Constructor to set up all the UI and game components
	   public SpaceInvadersMain() {
	      Container cp = this.getContentPane();           // JFrame's content-pane
	      cp.setLayout(new BorderLayout()); // in 10x10 GridLayout
	      board = new Board();
	      cp.add(board, BorderLayout.CENTER);
	      
	      	//CARDLAYOUT FOR START SCREEN
	      
	        cardLayout = new CardLayout();
	        mainPanel = new JPanel(cardLayout);
	        JPanel menu = new MenuPanel();

	        
	        
	        //TO ADD START MENU
	      
	        mainPanel.add(menu, "menu");
	        mainPanel.add(board, "game");
            SoundEffect.START.loop();
           //to prevent both BGM and START music from playing frm start
            SoundEffect.BGM.stop();
            cp.add(mainPanel);
            
          //JMENUBAR
	   	     
  	      JMenuBar menubar = new JMenuBar();
  	      
  	      setJMenuBar(menubar);
  	      
  	      //JMENUS
  	      JMenu file = new JMenu("Game Menu");
  	      menubar.add(file);
  	      
  	      JMenuItem newGame = new JMenuItem("New Game");
  	      file.add(newGame);
  	      newGame.addActionListener(new ActionListener() {
  	    	  public void actionPerformed(ActionEvent e) {
  	    		 //reset game
  	    		  board.gameInit();
  	    		  board.gameStart();
  	    	  }
  	      });
  	      
	      //EXIT JMENU
	      JMenuItem exitGame = new JMenuItem("Exit Game");
	      file.add(exitGame);
	      exitGame.addActionListener(new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) {
	    		
	    		  //go to start menu
	    		  cardLayout.show(mainPanel, "menu");
	    		 
	    		  //reset game
	    		  board.gameInit();
	    		  
	    		  	    		 
	    		  //switch BGM to START
	    		  SoundEffect.BGM.stop();
	    		  SoundEffect.START.play();
	    	  }
	      });
    	     
	      //HELP JMENU
	      JMenu help = new JMenu("Help");
	      help.setMnemonic(KeyEvent.VK_H);
	      menubar.add(help);
	      
	      final JMenuItem helpContents = new JMenuItem("Help Contents", KeyEvent.VK_H);
	      help.add(helpContents);
	      helpContents.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            String msg = "Arrow keys to change direction\n"
	                  + "P to pause/resume\n"
	                  + "S to toggle sound on/off\n";
	            JOptionPane.showMessageDialog(helpContents, 
	                  msg, "Instructions", JOptionPane.PLAIN_MESSAGE);
	         }
	      });
          
	      pack();  // Pack the UI components, instead of setSize()
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
	      setTitle(TITLE);
	      setVisible(true);   // show it
	      setLocationRelativeTo(null); // center the application window
	      setResizable(false);
	  
	   }
	   
	   public class MenuPanel extends JPanel {
		   
		   JPanel buttonsPane;
		   public MenuPanel() {

			   setLayout(new GridBagLayout());		   	
            
	            // Start Button
	            btnStart = new JButton("Start");
	            btnStart.setBorderPainted(false);
	            btnStart.setFocusPainted(false);

	            btnStart.setBackground(Color.GREEN);

	            
	            add(btnStart);
	            btnStart.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent evt) {
		            	
		            	// Display Game Panel
		            	cardLayout.show(mainPanel, "game");
		            	board.gameStart();

		            	SoundEffect.BGM.play();
     
		            }
		         });

		    }

		    public Dimension getPreferredSize() {
		        return new Dimension(300, 300);
		    }
		    
		    @Override
		      public void paintComponent(Graphics g) {
		         Graphics2D g2d = (Graphics2D)g;
		         super.paintComponent(g2d);   // paint background

		         setBackground(Color.BLACK);  // may use an image for background
		         Font titleFont = new Font("Verdana", Font.BOLD, 20);
		         g2d.setFont(titleFont);
		  	   FontMetrics titleWidth = this.getFontMetrics(titleFont);
		  	   g2d.setColor(Color.white);
		  	   g2d.drawString(TITLE, (358 - titleWidth.stringWidth(TITLE))/2, 130);
		  	   }

	   }
	  
	   
 
	   // The entry main() method
	   public static void main(String[] args) {
		   // Use the event dispatch thread to build the UI for thread-safety.
	      SwingUtilities.invokeLater(new Runnable() {
	         @Override
	         public void run() {
	        	 
	            new SpaceInvadersMain();
	         }
	      });
	   }

}


