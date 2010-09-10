/*
 * Created on Sep 26, 2004
 */
package name.shabda.spacegame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 * @author shabda
 * This is the major class in this game. It calls and uses all classes to bind and
 * play the game.
 */
public class Space implements Serializable{
	String gameName="SpaceOddECE";//name of the game
	ImageIcon icon=UW.createImageIcon("../images/ship4.gif","icon");//icon which shows in help and top etc
	JFrame frame;
	int chanceRogueComes=20;//determines how often a new enemy is spawned.lower value means heigher chance
	int chanceRogueFires=100;//determines how often a enemy ships will fire.lower value means heigher chance
	int chancePowerUpGenerated=400;//determines how often a powerup is generated.lower value means heigher chance
	int chanceSpecialShipComes=30;
	List levelData;//this list contains integers determining chances of spawning of different type of enemies
	List levelPowerUpData;//List containing which power up be generated in which stage
	final int lives=10;//lives given to hero
	final int stageSize=20;//kill this many enemies to advance to next stage
	int sumOfAllEnemies;	//it is sum of enemies of all levels kept for optimisation
	int sumOfAllPowerUps;//small optimisation
	boolean bossSpawnedInThisStage=false;
	Screen s=new Screen();
	//TODO: Check if arraylist performs better than linkedList
	List rogues=java.util.Collections.synchronizedList(new LinkedList());//list of enemy ships
	List myWeapons=java.util.Collections.synchronizedList( new LinkedList());//list of my weapons
	List enemyWeapons=java.util.Collections.synchronizedList(new LinkedList());//list of enemy weapons
	List bossShip=java.util.Collections.synchronizedList(new LinkedList());//list of special enemyships
	List powerUps=java.util.Collections.synchronizedList(new LinkedList());//powerUps
	List friends=java.util.Collections.synchronizedList(new LinkedList());//reinforcements i have called
	List specialShips=java.util.Collections.synchronizedList(new LinkedList());//special ships i have called
	//int livesLeft=10;
	int stage;//current stage
	int heroDeaths=0;//ships i have lost
	int enemiesKilled=0;//enemies i have killed
	HeroShip hero=new HeroShip(UW.Rand(0, UW.xSize),UW.ySize-20,UW.xSpeed,UW.ySpeed,100,100,100,true,true);
	HeroData heroData=new HeroData();
	JLabel shipsLeftL=new JLabel("Ships left: "+(lives-heroDeaths));
	JLabel moneyL=new JLabel("Money: "+heroData.money);
	JLabel bulletsLeftL=new JLabel("Bullets: "+hero.bullets);
	JLabel minesLeftL=new JLabel("Mines: "+hero.mines);
	JLabel friendsL=new JLabel("Reinforcements: "+hero.friends);
	private MouseListener[] arr;
	private MouseMotionListener[] arr1;
	private MouseWheelListener[] arr2;
	private ActionMap am;
	boolean splashScreenMode=false;
	Timer movement=new Timer(23,new ActionListener() {//move every ship and let enemy ships attack
		public void actionPerformed(ActionEvent e) {
			SwingWorker sw=new SwingWorker() {
				public Object construct() {
					killings();
					moveNAttack();
					return null;
				}
				};
				sw.start();
			}
		}); 
	Timer special=new Timer(80,new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			specialShipsStuff();
			
		}
		
		});
	Timer paint=new Timer(80,new ActionListener() {//repaint the screen
		public void actionPerformed(ActionEvent e) {
			spawn();
			s.artifacts.clear();
			s.artifacts.addAll(rogues);
			s.artifacts.addAll(enemyWeapons);
			s.artifacts.addAll(myWeapons);
			s.artifacts.addAll(powerUps);
			s.artifacts.addAll(bossShip);
			s.artifacts.addAll(friends);
			s.artifacts.addAll(specialShips);
			s.artifacts.add(hero);
			s.repaint();
			}
		/**
		 * 
		 */
		private void spawn() {//create new rogues and power ups
			int i=(int) (Math.random()*chanceRogueComes);
			if(i==0)
			rogues.add(addRogues2());
			i=(int) (Math.random()*chancePowerUpGenerated);
			if(i==0) {
			powerUps.add(addPowerUps());	
			}
		}
		});
	Timer stageUpp=new Timer(100,new ActionListener() {//check if stage is complete
		public void actionPerformed(ActionEvent e) {
			isStageUp();
		}
		});
	public void specialShipsStuff() {
		for(Iterator i=specialShips.iterator();i.hasNext();) {
			Object o=i.next();
			Special s=(Special)o;
			s.doSpecialStuff(this);
			}
		int rand=(int) (Math.random()*chanceSpecialShipComes);//should i add a special ship
		if(rand==0) {
		specialShips.add(addSpecialShip(stage));
		}
		}
	Timer checkGameOver=new Timer(100,new ActionListener() {//check if game is over
		public void actionPerformed(ActionEvent e) {
			if(heroDeaths>lives) {
			pause();
			Object[] options = {"play again",
            "No way!"};
			int n = JOptionPane.showOptionDialog(frame,
					"You have lost all your ships and have lost the game. Would you like to play again",
					"A Silly Question",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					icon,     //custom icon
					options,  //the titles of buttons
					options[0]); //default button title
			if(n==JOptionPane.YES_OPTION) {reSpawnEverything();}
			if(n==JOptionPane.NO_OPTION) {System.exit(0);}

			}
		}
		
		});
	public Space(){//initilise everything
		stage=1;
		levelData=new ArrayList();
		levelPowerUpData=new ArrayList();
		setStage(stage);
		addKeyBindings();
		addMouse();		
		rogues.add(addRogues2());
		rogues.add(addRogues2());
		rogues.add(addRogues2());
		s.artifacts.addAll(rogues);
		s.artifacts.addAll(bossShip);
		s.artifacts.add(hero);
		movement.start();
		paint.start();
		stageUpp.start();
		checkGameOver.start();
		sumOfAllEnemies=getIntFromList(levelData.size());
		sumOfAllPowerUps=getIntFromPowerUpList(levelPowerUpData.size());
		Sounds.backgroundMusic();
		initSplashScreen(UW.createImageIcon("../images/initial-Splash1.gif","splashScreen"));
	}
	public Special addSpecialShip(int stage) {
		Special s;
		if(stage==8) {
		s=new Spawner(UW.Rand(10, UW.xSize),10,UW.xSpeed,(int)UW.ySpeed);
		}
		else if(stage==9) {
		int rand=(int) (Math.random()*2);
		if(rand==0)
			s=new Spawner(UW.Rand(10, UW.xSize),10,UW.xSpeed,(int)UW.ySpeed);
		else s=new SpecialBossShip(UW.Rand(10, UW.xSize),10,UW.xSpeed,(int)UW.ySpeed);
		}
		else s=new Spawner(UW.Rand(10, UW.xSize),10,UW.xSpeed,(int)UW.ySpeed); 
			
		return s;
		}
	public void setStage(int stage) {//update stage
		if(stage==1) {
			//elements containing zero and at end have no effect so we may comment them
			//stage is one
			//enemies-only basic
			//power ups-ships
			levelData.clear();
			levelPowerUpData.clear();
			Integer e1=new Integer(1);//basic enemy
			//Integer e2=new Integer(4);
			//Integer e3=new Integer(4);
			//Integer e4=new Integer(4);
			//Integer e5=new Integer(4);
			//Integer e6=new Integer(4);
			levelData.add(e1);
			//levelData.add(e2);
			//levelData.add(e3);
			//levelData.add(e4);
			//levelData.add(e5);
			//levelData.add(e6);
			Integer f1=new Integer(0);//no bullet powerUp
			Integer f2=new Integer(1);//extra ship powerUp
			//Integer f3=new Integer(4);
			//Integer f4=new Integer(4);
			levelPowerUpData.add(f1);
			levelPowerUpData.add(f2);
			//levelPowerUpData.add(f3);
			//levelPowerUpData.add(f4);
			//ok lets set the music for this stage
			//since this is stage 1 the line below may be commented,as music is already set
			Sounds.setMusic(Sounds.FASTMUSIC);
			}
		else if(stage==2) {
			//enemies:basic & multiShot
			//powerUp:bullets and life
			levelData.clear();
			Integer e1=new Integer(1);//basic
			Integer e2=new Integer(1);//multiShot
			//Integer e3=new Integer(0);
			//Integer e4=new Integer(0);
			levelData.add(e1);
			levelData.add(e2);
			//levelData.add(e3);
			//levelData.add(e4);
			levelPowerUpData.clear();
			Integer f1=new Integer(1);//no bullet powerUp
			Integer f2=new Integer(1);//extra ship powerUp
			//Integer f3=new Integer(4);
			//Integer f4=new Integer(4);
			levelPowerUpData.add(f1);
			levelPowerUpData.add(f2);
			//levelPowerUpData.add(f3);
			//levelPowerUpData.add(f4);
			//ok lets set the music for this stage
			Sounds.setMusic(Sounds.FASTMUSIC2);
			}
		else if(stage==3) {
			//enemies:single shot,multiShot and invisible
			//powerUps:bullets and life
			levelData.clear();
			Integer e1=new Integer(2);//basic
			Integer e2=new Integer(4);//multiShot
			Integer e3=new Integer(2);//miners
			//Integer e4=new Integer(0);
			levelData.add(e1);
			levelData.add(e2);
			levelData.add(e3);
			//levelData.add(e4);
			//no need to to change levelPowerData
//			ok lets set the music for this stage
			Sounds.setMusic(Sounds.FASTMUSIC);
			}
		else if(stage==4) {
			//bonus stage
			//bossShips should not come
			bossSpawnedInThisStage=true;
			//only stupid ships which dont fire
			//only life giving power up and money giving
			levelData.clear();
			Integer e1=new Integer(0);
			Integer e2=new Integer(0);
			Integer e3=new Integer(0);
			Integer e4=new Integer(0);//invisible
			Integer e5=new Integer(1);//stupid ships
			levelData.add(e1);
			levelData.add(e2);
			levelData.add(e3);
			levelData.add(e4);
			levelData.add(e5);
			levelPowerUpData.clear();
			Integer f1=new Integer(0);
			Integer f2=new Integer(1);//ship
			Integer f3=new Integer(1);
			levelPowerUpData.add(f1);
			levelPowerUpData.add(f2);
			levelPowerUpData.add(f3);
			//levelPowerUpData.add(f4);
			//heigher chance of powerUp in tis stage as this is a bonus stage
			chancePowerUpGenerated=100;
//			ok lets set the music for this stage
			Sounds.setMusic(Sounds.SLOWMUSIC);
			}
		else if(stage==5) {
			bossSpawnedInThisStage=false;
			//enemies:many bulleted,mine laying,invisible
			//powerUps:extra ships,extra mines and screen clearers
			levelData.clear();
			Integer e1=new Integer(0);
			Integer e2=new Integer(3);
			Integer e3=new Integer(2);
			Integer e4=new Integer(1);//invisible
			//Integer e5=new Integer(0);//stupid ships
			levelData.add(e1);
			levelData.add(e2);
			levelData.add(e3);
			levelData.add(e4);
			//levelData.add(e4);
			levelPowerUpData.clear();
			Integer f1=new Integer(0);
			Integer f2=new Integer(1);//ship
			Integer f3=new Integer(1);//money
			Integer f4=new Integer(1);//mines
			Integer f5=new Integer(1);//clear
			levelPowerUpData.add(f1);
			levelPowerUpData.add(f2);
			levelPowerUpData.add(f3);
			levelPowerUpData.add(f4);
			levelPowerUpData.add(f5);
			//normal chance of powerUp generation
			chancePowerUpGenerated=200;
//			ok lets set the music for this stage
			Sounds.setMusic(Sounds.FASTMUSIC);
			}
		else if(stage==6) {
			//enemies:many bulleted,mine laying,invisible
			//powerUps:extra ships,extra mines and screen clearers
			levelData.clear();
			Integer e1=new Integer(0);
			Integer e2=new Integer(3);
			Integer e3=new Integer(2);
			Integer e4=new Integer(2);//invisible
			//Integer e5=new Integer(0);//stupid ships
			levelData.add(e1);
			levelData.add(e2);
			levelData.add(e3);
			levelData.add(e4);
			//levelData.add(e4);
			levelPowerUpData.clear();
			Integer f1=new Integer(0);
			Integer f2=new Integer(0);//ship
			Integer f3=new Integer(1);//money
			Integer f4=new Integer(1);//mines
			Integer f5=new Integer(1);//clear
			levelPowerUpData.add(f1);
			levelPowerUpData.add(f2);
			levelPowerUpData.add(f3);
			levelPowerUpData.add(f4);
			levelPowerUpData.add(f5);
//			ok lets set the music for this stage
			Sounds.setMusic(Sounds.FASTMUSIC2);
			//heigher chance of enemies spawning and firing
			chanceRogueComes=(int)(chanceRogueComes/1.2);
			chanceRogueFires=(int)(chanceRogueFires/1.2);
			special.start();
			}
		else if(stage==7) {
			//bonus stage
			//lots of stupid ships
			//powerUps life and money
			bossSpawnedInThisStage=true;
			//only stupid ships which dont fire
			//only life giving power up and money giving
			levelData.clear();
			Integer e1=new Integer(0);
			Integer e2=new Integer(0);
			Integer e3=new Integer(0);
			Integer e4=new Integer(0);//invisible
			Integer e5=new Integer(1);//stupid ships
			levelData.add(e1);
			levelData.add(e2);
			levelData.add(e3);
			levelData.add(e4);
			levelData.add(e5);
			levelPowerUpData.clear();
			Integer f1=new Integer(0);
			Integer f2=new Integer(1);//ship
			Integer f3=new Integer(1);
			levelPowerUpData.add(f1);
			levelPowerUpData.add(f2);
			levelPowerUpData.add(f3);
//			ok lets set the music for this stage
			Sounds.setMusic(Sounds.SLOWMUSIC);
			//levelPowerUpData.add(f4);
			//heigher chance of powerUp in tis stage as this is a bonus stage
			chancePowerUpGenerated=100;
			special.stop();
			specialShips.clear();
			}
		else if(stage==8) {
			//enemies:many bulleted,mine laying,invisible,backWards entry
			//powerUps:there should be no powerUps in this stage
			chancePowerUpGenerated=Integer.MAX_VALUE;//this causes probability of powerUp generation to become very less
			levelData.clear();
			Integer e1=new Integer(0);
			Integer e2=new Integer(2);
			Integer e3=new Integer(2);
			Integer e4=new Integer(2);//invisible
			Integer e5=new Integer(0);//no stupid ships
			Integer e6=new Integer(4);//backwards entry
			levelData.add(e1);
			levelData.add(e2);
			levelData.add(e3);
			levelData.add(e4);
			levelData.add(e5);
			levelData.add(e6);
			levelPowerUpData.clear();
			Integer f1=new Integer(1);
			levelPowerUpData.add(f1);//
//			ok lets set the music for this stage
			Sounds.setMusic(Sounds.FASTMUSIC);
			special.start();
			}
		else  if (stage==9) {
		//just increase the diificulties baby
		chanceRogueComes/=1.2;
		chanceRogueFires/=1.2;
		chanceSpecialShipComes/=1.2;
//		ok lets set the music for this stage
		Sounds.setMusic(Sounds.FASTMUSIC);
		} 
		else if(stage==10) {
			Object[] options = {"Play again",
            "I am done!"};
			int n = JOptionPane.showOptionDialog(frame,
					"Honourable sir! What could we have done without you? The enemy is destroyed,finally. Earth is now safe.",
					"What does honourable sir wish?",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					icon,     //custom icon
					options,  //the titles of buttons
					options[0]); //default button title
			if(n==JOptionPane.YES_OPTION) {reSpawnEverything();}
			if(n==JOptionPane.NO_OPTION) {System.exit(0);}	
		}
		sumOfAllEnemies=getIntFromList(levelData.size());
		sumOfAllPowerUps=getIntFromPowerUpList(levelPowerUpData.size());
		}

	public JPanel createComponents() {//create components
		GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
		JPanel pane=new JPanel();
		pane.setBackground(Color.black);
		pane.setLayout(gridbag);
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=10;
		c.gridheight=5;
		gridbag.setConstraints(s, c);	
		pane.add(s);
		JPanel temp=new JPanel();
		temp.setBackground(Color.BLACK);
		temp.setForeground(Color.black);
		shipsLeftL.setForeground(Color.WHITE);
		moneyL.setForeground(Color.WHITE);
		bulletsLeftL.setForeground(Color.WHITE);
		minesLeftL.setForeground(Color.WHITE);
		friendsL.setForeground(Color.WHITE);
		temp.add(shipsLeftL);
		temp.add(moneyL);
		temp.add(bulletsLeftL);
		temp.add(minesLeftL);
		temp.add(friendsL);
		c.gridx=0;
		c.gridy=5;
		c.gridwidth=10;
		c.gridheight=1;
		gridbag.setConstraints(temp, c);
		pane.add(temp);
		return pane;
	}
	public JMenuBar addMenuBar() {//add a menu
		JMenuBar menuBar =new JMenuBar();
		menuBar.setBackground(Color.black);
		JMenu file=new JMenu("file");
		file.setBackground(Color.black);
		file.setForeground(Color.white);
		JMenuItem newGame=new JMenuItem("new game");
		newGame.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				reSpawnEverything();
			}
			
			});
		JMenuItem save=new JMenuItem("Save Game");
		save.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				saveAs();
				
			}});
		JMenuItem open=new JMenuItem("open");
		open.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Space temp=open();
				setData(temp);
				
			}});
		JMenuItem exit=new JMenuItem("exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}	
			});
		file.add(newGame);
		file.add(save);
		file.add(open);
		file.add(exit);
		JMenu help=new JMenu("help");
		help.setBackground(Color.black);
		help.setForeground(Color.white);
		JMenuItem story=new JMenuItem("story");
		JMenuItem controls=new JMenuItem("controls");
		controls.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				pause();
				String s="<HTML>" +
						gameName+" v 0.9"+
						"<h2>Controls</h2>" +
						"Arrow keys: Accelerate in the direction of arrow" +
						"<br>Enter: Fire"+
						"<br>F1: First Weapon (Single shot)"+
						"<br>F2: Second Weapon (Triple shot)"+
						"<br>F3: Third Weapon (Multiple shot)"+
						"<br>F4: Fourth Weapon (Berserker Mode)"+
						"<br>F5: Fifth Weapon (Mine layers)"+
						"<br>1: Call reinforcements(android ships)"+
						"<br>All these controls work only if you have suffiecient "+
						"<br>ammunition and guns/technology to operate them."+
						"</HTML>";
				JOptionPane.showMessageDialog(null,s, "controls", JOptionPane.INFORMATION_MESSAGE, icon);
			}});
		JMenuItem credits=new JMenuItem("credits");
		credits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause();
				JOptionPane.showMessageDialog(null, gameName+" v 0.9"+"\n Programming by Shabda and Ashwini"+
						"\n Music by Prasun 'dada' Basu"+
						"\n Artwork by Dada, Jaat, Shishir and Shabda","credits",
						JOptionPane.INFORMATION_MESSAGE,icon);
			}
			});
		help.add(story);
		story.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				pause();
				String s="\n The earth has been invaded by horde upon horde of alien ships"+
							"\n Almost all of our ships have been lost in this war."+
							"\n You have been chosen by the HumanFederation for last defence of our homeland."+
							"\n Will you succeed or will you too fail like countless others."+
							"\n Eaths destiny now lies in your hands....................";
				JOptionPane.showMessageDialog(null, gameName+" v 1.1"+s,"The story"
						,JOptionPane.INFORMATION_MESSAGE,icon);
				
			}
			
			});
		help.add(controls);
		help.add(credits);
		menuBar.add(file);
		menuBar.add(help);
		return menuBar;
		}
	/**
	 * this method is called to check if stage should be updated and to do the needful
	 * if stage needs to be updated.This basically calls setStage2(int) to update stage.
	 */
	public void isStageUp() {
		if(enemiesKilled>stageSize) {
		if(!bossSpawnedInThisStage) {
		int val;
		if(stage<3)
			val=stage;
		else val=3;
		for(int i=0;i<val;i++) 
		{
		bossShip.add(addBoss());
		}
		bossSpawnedInThisStage=true;
		chanceRogueComes=50;
		chanceRogueFires=50;
		}
		else if(bossSpawnedInThisStage&&bossShip.isEmpty()) {
		s.setMessage("Stage "+stage+" clear! Press space to play next stage.");
		chanceRogueComes=15;
		chanceRogueFires=100;
		bossSpawnedInThisStage=false;
		splashScreen(getSplashScreenForThisStage());
		stage++;
		myWeapons.clear();
		rogues.clear();
		enemyWeapons.clear();
		specialShips.clear();
		powerUps.clear();
		enemiesKilled=0;
		//setStage(stage);
		s.repaint();
		}
		}
		}
	public ImageIcon getSplashScreenForThisStage() {
		//TODO add stage specific images
		return UW.createImageIcon("../images/splash.gif","splashScreen");
		}
	/**
	 * initial splash screen has no shop.
	 * */
	public void initSplashScreen(ImageIcon ii){
		System.out.print("in pauseplay");
		if(!splashScreenMode) {
		s.setSplashScreen(ii);
		splashScreenMode=true;
		pause();
		arr=s.getMouseListeners();
		arr1=s.getMouseMotionListeners();
		arr2=s.getMouseWheelListeners();
		for(int i=0;i<arr.length;i++) {
			s.removeMouseListener(arr[i]);
			}
		for(int i=0;i<arr1.length;i++) {
			s.removeMouseMotionListener(arr1[i]);
			}
		for(int i=0;i<arr2.length;i++) {
			s.removeMouseWheelListener(arr2[i]);
			}
	/*	for(int i=0;i<arr3.length;i++) {
			s.removeKeyListener(arr3[i]);
			}*/
		s.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("mouse clicked");
				play();
				splashScreenMode=false;
				s.resetSplashScreen();
				s.removeMouseListener(s.getMouseListeners()[0]);
				for(int i=0;i<arr.length;i++) {
					s.addMouseListener(arr[i]);
					}
				for(int i=0;i<arr1.length;i++) {
					s.addMouseMotionListener(arr1[i]);
					}
				for(int i=0;i<arr2.length;i++) {
					s.addMouseWheelListener(arr2[i]);
					}
				/*for(int i=0;i<arr3.length;i++) {
					s.addKeyListener(arr3[i]);
					}*/
				myWeapons.clear();
				enemyWeapons.clear();
				friends.clear();
				rogues.clear();
				bossShip.clear();
				}
			});
			}
		}
	public void splashScreen(ImageIcon ii) {
		//if(!splashScreenMode) {
		pause();
		splashScreenMode=true;
		s.setSplashScreen(ii);
		arr=s.getMouseListeners();
		arr1=s.getMouseMotionListeners();
		arr2=s.getMouseWheelListeners();
		am=s.getActionMap();
		for(int j=0;j<arr.length;j++) {
			s.removeMouseListener(arr[j]);
			}
		for(int j=0;j<arr1.length;j++) {
			s.removeMouseMotionListener(arr1[j]);
			}
		for(int j=0;j<arr1.length;j++) {
			s.removeMouseWheelListener(arr2[j]);
			}
		s.setActionMap(null);
		s.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent me) {
			splashScreenMode=false;
			s.resetSplashScreen();
			setShop();
			s.removeMouseListener(s.getMouseListeners()[0]);
			}	
		});

		//}
		}
	public void updateLabels() {
		shipsLeftL.setText("Ships left: "+(lives-heroDeaths));
		moneyL.setText("Money: "+heroData.money);
		bulletsLeftL.setText("Bullets: "+hero.bullets);
		minesLeftL.setText("Mines: "+hero.mines);
		friendsL.setText("Reinforcements: "+hero.friends);
	}
	public void setShop() {
		final int shipCost=200;
		final int bulletCost=100;
		final int minesCost=100;
		final int gun3Cost=50;
		final int gun4Cost=750;
		final int gun5Cost=750;
		final int techCost=500;
		final int reinforcementCost=400;
		Icon shipIcon=UW.createImageIcon("../images/buttons/ship.gif", "");
		Icon bulletIcon=UW.createImageIcon("../images/buttons/bullets.gif", "");
		Icon minesIcon=UW.createImageIcon("../images/buttons/mines.gif", "");
		Icon gun3Icon=UW.createImageIcon("../images/buttons/zvr3.gif", "");
		Icon gun4Icon=UW.createImageIcon("../images/buttons/zvr4.gif", "");
		Icon gun5Icon=UW.createImageIcon("../images/buttons/minelayers.gif", "");
		Icon techIcon=UW.createImageIcon("../images/buttons/tech.gif", "");
		Icon reinforcementIcon=UW.createImageIcon("../images/buttons/reinforcements.gif", "");
		Icon exitIcon=UW.createImageIcon("../images/buttons/exit.gif", "");
		
		JButton buyShip=new JButton();
		buyShip.setIcon(shipIcon);
		buyShip.setOpaque(false);
		JButton buyBullets=new JButton();
		buyBullets.setIcon(bulletIcon);
		buyBullets.setOpaque(false);
		JButton buyMines=new JButton();
		buyMines.setIcon(minesIcon);
		buyMines.setOpaque(false);
		JButton buyGun3=new JButton();
		buyGun3.setIcon(gun3Icon);
		buyGun3.setOpaque(false);
		JButton buyGun4=new JButton();
		buyGun4.setIcon(gun4Icon);
		buyGun4.setOpaque(false);
		JButton buyGun5=new JButton();
		buyGun5.setIcon(gun5Icon);
		buyGun5.setOpaque(false);
		JButton buyTech=new JButton();
		buyTech.setIcon(techIcon);
		buyTech.setOpaque(false);
		JButton buyReinforcements=new JButton();
		buyReinforcements.setIcon(reinforcementIcon);
		buyReinforcements.setOpaque(false);
		JButton exit=new JButton("");
		exit.setIcon(exitIcon);
		exit.setOpaque(false);
		
		class myMouseAdapter extends MouseAdapter{
			String data;
			public myMouseAdapter(String s) {
				data=s;
				}
			public void mouseEntered(MouseEvent e) {
				s.setShopMessage1(data);
				Object o=e.getSource();
				JComponent jc=(JComponent) o;
				jc.setOpaque(true);
			}

			public void mouseExited(MouseEvent e) {
				s.setShopMessage1("");
				Object o=e.getSource();
				JComponent jc=(JComponent) o;
				jc.setOpaque(false);
			}
			}
		buyShip.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(heroData.money>shipCost) {
				heroDeaths-=1;
				heroData.money-=shipCost;
				s.setShopMessage2("You have bought a ship. Fly it well, lest it meet the fate of your last ship");
				updateLabels();
				}
				else {
					s.setShopMessage2("Sorry! You donot seem to have enough money. The ship costs "+shipCost+". You only have "+heroData.money);
					}
			}});
		buyShip.addMouseListener(new myMouseAdapter("buy an extra ship"));
		buyBullets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(heroData.money>=bulletCost) {
				hero.bullets+=100;
				heroData.money-=bulletCost;
				s.setShopMessage2("You bought 100 bullets. May they find their mark.");
				updateLabels();
				}
				else {
					s.setShopMessage2("Sorry! You donot seem to have enough money. The bullets costs "+bulletCost+". You only have "+heroData.money);
					}
			}});
		buyBullets.addMouseListener(new myMouseAdapter("buy 100 extra bullets"));
		buyMines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(heroData.money>minesCost) {
				hero.mines+=100;
				heroData.money-=minesCost;
				s.setShopMessage2("You bought 10 mines. May the path of your enemies be ever filled with these deadly mines.");
				updateLabels();
				}
				else {
					s.setShopMessage2("Sorry! You donot seem to have enough money. The mines costs "+minesCost+". You only have "+heroData.money);
					}
			}});
		buyMines.addMouseListener(new myMouseAdapter("buy 10 mines"));
		buyGun3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(heroData.money>gun3Cost) {
				if(hero.guns[2]) {
				s.setShopMessage2("You already have this sir!")	;
				}
				else {
				hero.guns[2]=true;
				heroData.guns[2]=true;
				heroData.money-=gun3Cost;
				s.setShopMessage2("You bought the multi-shot gun ZvR3!!");
				updateLabels();
				}
				}
				else {
					s.setShopMessage2("Sorry! You donot seem to have enough money. This gun costs "+gun3Cost+". You only have "+heroData.money);
					}
			}});
		buyGun3.addMouseListener(new myMouseAdapter("Buy the multi-shot gun ZvR3! Fires upto 50 bullets per shot in forward direction."));
		buyGun4.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(heroData.money>gun4Cost) {
				if(hero.guns[3]) {
				s.setShopMessage2("But you already have this sir!")	;
				}
				else {
				hero.guns[3]=true;
				heroData.guns[3]=true;
				heroData.money-=gun4Cost;
				s.setShopMessage2("You bought the multi shot-gun ZvR4");
				updateLabels();
				}
				}
				else {
					s.setShopMessage2("Sorry! You donot seem to have enough money. This gun costs "+gun4Cost+". You only have "+heroData.money);
					}
			}});
		buyGun4.addMouseListener(new myMouseAdapter("Buy the multi-shot gun ZvR4! Fires upto 50 bullets per shot in all direction."));
		buyGun5.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(heroData.money>gun5Cost) {
				if(hero.guns[4]) {
				s.setShopMessage2("But you already have this sir!")	;
				}
				else {
				hero.guns[4]=true;
				heroData.guns[4]=true;
				heroData.money-=gun5Cost;
				s.setShopMessage2("You bought the mine-layers. Now fill the path of your enemies with death.");
				updateLabels();
				}
				}
				else {
					s.setShopMessage2("Sorry! You donot seem to have enough money. The mine layers costs "+gun5Cost+". You only have "+heroData.money);
					}
			}});
		buyGun5.addMouseListener(new myMouseAdapter("Buy the mine-layer MinEX4! And fill your enemies path with death before they come."));
		buyTech.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if(heroData.money>techCost) {
				if(hero.technology) {
				s.setShopMessage2("But you already have this sir!")	;
				}
				else {
				heroData.technology=true;
				hero.technology=true;
				heroData.money-=techCost;
				s.setShopMessage2("You now have the technology to efficiently fire your ZvR guns without bullet spoilage");
				updateLabels();
				}
				}
				else {
					s.setShopMessage2("Sorry! You donot seem to have enough money. This tecnology costs "+techCost+". You only have "+heroData.money);
					}
			}});
		buyTech.addMouseListener(new myMouseAdapter("Buy the technology that will make your ZvR guns much more efficient."));
		buyReinforcements.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(heroData.money>reinforcementCost) {
				hero.friends+=10;	
				heroData.money-=reinforcementCost;
				s.setShopMessage2("You bought 10 android ships to help you.");
				updateLabels();
				}
				else {
					s.setShopMessage2("Sorry! You donot have enough money. The android ships costs "+reinforcementCost+". You only have "+heroData.money);
					}
			}});
		buyReinforcements.addMouseListener(new myMouseAdapter("Buy 10 android ships to defend you and to attack your enemies."));
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStage(stage);
				s.resetShopMode();
				s.setGameMode();
				s.newScene();
				for(int j=0;j<arr.length;j++) {
					s.addMouseListener(arr[j]);
					}
				for(int j=0;j<arr1.length;j++) {
					s.addMouseMotionListener(arr1[j]);
					}
				for(int j=0;j<arr1.length;j++) {
					s.addMouseWheelListener(arr2[j]);
					}
				s.setActionMap(am);
				s.setShopMessage1("");
				s.setShopMessage2("");
				play();
				
			}});
		
		exit.addMouseListener(new myMouseAdapter("To next stage.........."));
		List l=new ArrayList();
		l.add(buyShip);
		l.add(buyBullets);
		l.add(buyMines);
		l.add(buyGun3);
		l.add(buyGun4);
		l.add(buyGun5);
		l.add(buyTech);
		l.add(buyReinforcements);
		l.add(exit);
		s.setShopMode(l);
		//lets set the music
		Sounds.setMusic(Sounds.SHOPMUSIC);
		}
	/**
	 * this method is called with a heroShip and a List containing powerUps.
	 * If the hero is within required distance of the powerUp, the powerUp gives
	 * its power to the hero.
	 */
	public void getPowerUp(HeroShip h,List l) {//list l should contain only powerups or classCastException will occur
		for(Iterator i=l.iterator();i.hasNext();) {
			Artifact a=(Artifact) i.next();
			double safe=h.getSize()+a.getSize();
			Point2D c1=h.getCentre();
			Point2D c2=a.getCentre();
			double dist=c1.distance(c2);
			if(dist<safe) {
				((PowerUp)a).affect(this);
				i.remove();
				}
			}
		}
	/**
	 * this method checks everyThing on the screen which should be killed on collisions and when they move outside the screen
	 * The following things may collide:
	 * 1.enemyShip and myWeapons
	 * 2.enemyShips and myShip
	 * 3.enemyWeapons and myShip
	 * 4.myWeapons and bossShips
	 * 5.myReinforcements and enemyWeapons
	 * plz note that myWeapons and enemyWeapons have no effect when the collide
	 * The following things may be killed by moving outside the screen
	 * 1.enemyShips
	 * 2.MyWeapons
	 * 3.powerUps
	 * the heroShip doesnot die when it moves outside.
	 */
	private void killings() {
		int[] i=killShipsOnCollision(rogues, myWeapons);
		enemiesKilled+=i[0];
		heroData.money+=i[2];
		i=killOnCollision(rogues,hero);
		enemiesKilled+=i[0];
		heroDeaths+=i[1];
		i=killOnCollision(enemyWeapons,hero);
		heroDeaths+=i[1];
		i=killBossOnCollision(bossShip, myWeapons);
		i=killShipsOnCollision(specialShips,myWeapons);
		enemiesKilled+=i[0];
		heroData.money+=i[2];
		i=killOnCollision(specialShips,hero);
		enemiesKilled+=i[0];
		heroDeaths+=i[1];
		killOnCollision(myWeapons, enemyWeapons);
		killOnCollision(friends, enemyWeapons);
		killWhenOutside(rogues);
		killWhenOutside(friends);
		killWhenOutside(enemyWeapons);
		killWhenOutside(myWeapons);
		killWhenOutside(powerUps);
		getPowerUp(hero, powerUps);
		updateLabels();
	}

	private void moveNAttack() {
		for(Iterator i=rogues.iterator();i.hasNext();) {
			Object o=i.next();
			((Moving)o).move();
			RogueShip r=(RogueShip)o;
			int rand=(int) (Math.random()*chanceRogueFires);
			if(rand==0)
				enemyWeapons.addAll(r.fireMultiple());
			}
		for(Iterator j=myWeapons.iterator();j.hasNext();) {
			Object o=j.next();
			((Moving)o).move();
			}
		for(Iterator j=enemyWeapons.iterator();j.hasNext();) {
			Object o=j.next();
			((Moving)o).move();
			}
		for(Iterator i=powerUps.iterator();i.hasNext();) {
			Object o=i.next();
			((Moving)o).move();
			}
		for(Iterator i=friends.iterator();i.hasNext();) {
			Object o=i.next();
			((Moving)o).move();
			HelperShip h=(HelperShip) o;
			int rand=(int) (Math.random()*30);
			if(rand==0)
				myWeapons.addAll(h.fireMultiple());
			h.doStuff(hero);
			}
		for(Iterator i=bossShip.iterator();i.hasNext();) {
			Object o=i.next();
			((Moving)o).move();
			BossShip b=(BossShip)o;
			int rand=(int) (Math.random()*chanceRogueFires);
			if(rand==0)
				enemyWeapons.addAll(b.fireMultiple());
			if(rand==1) {
				rogues.addAll(b.spawnShips());
				}
			}
			
		hero.move(s.getWidth(),s.getHeight());
	}
	
	public int [] hasBeenHit(List l1,HeroShip h) {
		int[] arr=new int[2];
		int killsOnL1=0;
		int killsOnH=0;
		for(Iterator i=l1.iterator();i.hasNext();) {
			Object o=i.next();
			Weapon w=(Weapon)o;
			if(collision(h, w)) {
			h.isHit(w);
			System.out.print("hitpts:"+h.armour);
			i.remove();
			killsOnL1++;}
			}
		arr[0]=killsOnL1;
		arr[1]=killsOnH;
		return arr;
		}
	public boolean collision(Artifact a,Artifact b) {
		boolean col=false;
		double safe=(a.getSize()+b.getSize())/2;
		Point2D c1=a.getCentre();
		Point2D c2=b.getCentre();
		double dist=c1.distance(c2);
		if(dist<safe) {
		col=true;	
		}
		return col;
		}
	public int[] killOnCollision(List l1,List l2) {//takes two lists containing artifacts
		int killsOnL1=0;
		int killsOnL2=0;
		int[] kills=new int[2];
		for(Iterator i=l1.iterator();i.hasNext();) {
			Artifact a1=(Artifact) i.next();
			for(Iterator j=l2.iterator();j.hasNext();) {
				Artifact a2=(Artifact) j.next();
				double safe=(a1.getSize()+a2.getSize())/2;
				Point2D c1=a1.getCentre();
				Point2D c2=a2.getCentre();
				double dist=c1.distance(c2);
				if(dist<safe) {
					i.remove();
					killsOnL1++;
					j.remove();
					killsOnL2++;
					}
				}
			}
		kills[0]=killsOnL1;
		kills[1]=killsOnL2;
		return kills;
		}
	public int[] killShipsOnCollision(List l1,List l2) {//takes two lists containing artifacts
		int valueOfKills=0;
		int killsOnL1=0;
		int killsOnL2=0;
		int[] kills=new int[3];
		for(Iterator i=l1.iterator();i.hasNext();) {
			Artifact a1=(Artifact) i.next();
			Ship ss=(Ship)a1;
			for(Iterator j=l2.iterator();j.hasNext();) {
				Artifact a2=(Artifact) j.next();
				double safe=(a1.getSize()+a2.getSize())/2;
				Point2D c1=a1.getCentre();
				Point2D c2=a2.getCentre();
				double dist=c1.distance(c2);
				if(dist<safe) {
					valueOfKills+=ss.value;
					killsOnL1++;
					i.remove();
					j.remove();
					killsOnL2++;
					}
				}
			}
		kills[0]=killsOnL1;
		kills[1]=killsOnL2;
		kills[2]=valueOfKills;
		return kills;
		}
	public int[] killBossOnCollision(List l1,List l2) {//takes two lists containing artifacts
		//TODO should  boss be killed in one shot?
		int killsOnL1=0;
		int killsOnL2=0;
		//int ran=(int) (Math.random()*5+1);
		int[] kills=new int[2];
		//if(ran==1&&!bossShip.isEmpty()) {
		for(Iterator i=l1.iterator();i.hasNext();) {
			Artifact a1=(Artifact) i.next();
			for(Iterator j=l2.iterator();j.hasNext();) {
				Artifact a2=(Artifact) j.next();
				double safe=(a1.getSize()+a2.getSize())/2;
				Point2D c1=a1.getCentre();
				Point2D c2=a2.getCentre();
				double dist=c1.distance(c2);
				if(dist<safe) {
					i.remove();
					
					killsOnL1++;
					j.remove();
					killsOnL2++;
					}
				}
			}
		//}
		kills[0]=killsOnL1;
		kills[1]=killsOnL2;
		return kills;
		}
	public int[] killOnCollision(List l1,Artifact a) {//takes a lists containing artifacts and a artifact
		//This method should only be called with second argument heroShip
		int killsOnL1=0;
		int killsOnA=0;
		int[] kills=new int[2];
		for(Iterator i=l1.iterator();i.hasNext();) {
			Artifact a1=(Artifact) i.next();
			double safe=(a1.getSize()+a.getSize())/2;
			Point2D c1=a1.getCentre();
			Point2D c2=a.getCentre();
			double dist=c1.distance(c2);
			if(dist<safe) {
				i.remove();
				killsOnL1++;
				moveOutOfScreen(friends);
				heroData.setData(hero);
				hero=new HeroShip(UW.Rand(0, UW.xSize),UW.ySize-20,UW.xSpeed,UW.ySpeed,100,100,100,true,true);
				hero.setData(heroData);
				killsOnA++;
				}
			}
		kills[0]=killsOnL1;
		kills[1]=killsOnA;
		return kills;
		}
	private void moveOutOfScreen(List l) {
		//The list should only cntain defender ships
		for(Iterator i=l.iterator();i.hasNext();) {
			Object o=i.next();
			Defenders d=(Defenders)o;
			d.up=true;
			d.attached=false;
			d.xSpeed=0;
			d.ySpeed=5;
			}
		}
	public int[] hit(List l1,HeroShip a) {//takes a lists containing weapon and a hero
		int killsOnL1=0;
		int killsOnA=0;
		int[] kills=new int[2];
		for(Iterator i=l1.iterator();i.hasNext();) {
			Weapon a1=(Weapon) i.next();
			double safe=(a1.getSize()+a.getSize())/2;
			Point2D c1=a1.getCentre();
			Point2D c2=a.getCentre();
			double dist=c1.distance(c2);
			if(dist<safe) {
				i.remove();
				killsOnL1++;
				a.isHit(a1);
				killsOnA++;
				}
			}
		kills[0]=killsOnL1;
		kills[1]=killsOnA;
		return kills;
		}
	public void killWhenOutside(List l) {
		for(Iterator i=l.iterator();i.hasNext();) {
			Artifact a=((Artifact)i.next());
			if(a.getXPos()>UW.xSize||a.getXPos()<0||a.getYPos()>UW.ySize||a.getYPos()<0) {
				i.remove();
				}
			}
		}
	public void pause() {
		s.setMessage("Game paused. Press space to start");
		movement.stop();
		paint.stop();
		s.pauseMovement();
		stageUpp.stop();
		s.repaint();
		if(stage==6||stage==8||stage==9)//These stages have special ships
			special.stop();
		}
	public void play() {
		s.setMessage("noMessage");
		movement.start();
		paint.start();
		stageUpp.start();
		s.startMovement();
		s.repaint();
		if(stage==6||stage==8||stage==9)//These stages have special ships
			special.start();
		}
	public void pausePlay() {
		if(movement.isRunning()) {
		pause();
		Sounds.stopMusic();
		}
		else  {	
		play();
		Sounds.backgroundMusic();
		}
		}
	public void addKeyBindings() {
		final KeyStroke esc=KeyStroke.getKeyStroke(KeyEvent.VK_UP,0,true);
		final KeyStroke up=KeyStroke.getKeyStroke(KeyEvent.VK_UP,0,true);
		final KeyStroke down=KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0,true);
		final KeyStroke left=KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,true);
		final KeyStroke right=KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,true);
		final KeyStroke	fire=KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,true);
		final KeyStroke weaponOne=KeyStroke.getKeyStroke(KeyEvent.VK_F1,0,true);
		final KeyStroke weaponTwo=KeyStroke.getKeyStroke(KeyEvent.VK_F2,0,true);
		final KeyStroke weaponThree=KeyStroke.getKeyStroke(KeyEvent.VK_F3,0,true);
		final KeyStroke weaponFour=KeyStroke.getKeyStroke(KeyEvent.VK_F4,0,true);
		final KeyStroke weaponFive=KeyStroke.getKeyStroke(KeyEvent.VK_F5,0,true);
		final KeyStroke stop=KeyStroke.getKeyStroke(KeyEvent.VK_F6,0,true);
		final KeyStroke pausePlay=KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0,true);
		//final KeyStroke changeStage=KeyStroke.getKeyStroke(KeyEvent.VK_F7,0,true);
		//final KeyStroke changeLife=KeyStroke.getKeyStroke(KeyEvent.VK_F8,0,true);
		//final KeyStroke changeBullets=KeyStroke.getKeyStroke(KeyEvent.VK_F9,0,true);
		//final KeyStroke changeMoney=KeyStroke.getKeyStroke(KeyEvent.VK_2,0,true);
		final KeyStroke callFriends=KeyStroke.getKeyStroke(KeyEvent.VK_F7,0,true);
		Action upAct=new AbstractAction() {
			public void  actionPerformed(ActionEvent e) {
				hero.accelerate();
				}
			};
		Action downAct=new AbstractAction() {
				public void  actionPerformed(ActionEvent e) {
					hero.reverse();
					}
				};
		Action leftAct=new AbstractAction() {
				public void  actionPerformed(ActionEvent e) {
				hero.speedLeft();
					}
				};
		Action rightAct=new AbstractAction() {
				public void  actionPerformed(ActionEvent e) {
				hero.speedRight();
					}
				};
		Action fireAct=new AbstractAction() {
				public void  actionPerformed(ActionEvent e) {
				List l=hero.fireMultiple();
				myWeapons.addAll(l);
					}
				};
		Action w1Act=new AbstractAction() {
				public void  actionPerformed(ActionEvent e) {
				hero.setCurrentWeapon(1);
					}
				};
		Action w2Act=new AbstractAction() {
				public void  actionPerformed(ActionEvent e) {
				hero.setCurrentWeapon(2);
					}
				};
		Action w3Act=new AbstractAction() {
				public void  actionPerformed(ActionEvent e) {
				if(hero.guns[2])//only if you have this gun
				hero.setCurrentWeapon(3);
					}
				};
		Action w4Act=new AbstractAction() {
				public void  actionPerformed(ActionEvent e) {
				if(hero.guns[3])//only if you have this gun
				hero.setCurrentWeapon(4);
					}
				};
		Action w5Act=new AbstractAction() {
				public void  actionPerformed(ActionEvent e) {
				if(hero.guns[2])//only if you have this gun
				hero.setCurrentWeapon(5);
					}
				};

	Action stopAct=new AbstractAction() {
			public void  actionPerformed(ActionEvent e) {
			hero.xSpeed=0;
			hero.ySpeed=0;
				}
			};		
	  Action ppAct=new AbstractAction() {
			public void  actionPerformed(ActionEvent e) {
			s.setMessage("noMessage");
			pausePlay();
			s.repaint();
				}
			};
	  Action cheatToStage=new AbstractAction() {
	  	public void  actionPerformed(ActionEvent e) {
	  			enemiesKilled=stageSize+1;
	  			bossSpawnedInThisStage=true;
	  			isStageUp();
				}
	  	};
		Action increaseLife=new AbstractAction() {
		  public void  actionPerformed(ActionEvent e) {
		  		heroDeaths-=10;
		  		if(heroDeaths<0)
		  			heroDeaths=0;
				}
		  };
		 Action increaseBullets=new AbstractAction() {
		  	public void  actionPerformed(ActionEvent e) {
		  			hero.bullets+=500;
					}
		  	};
		Action increaseMoney=new AbstractAction() {
			  	public void  actionPerformed(ActionEvent e) {
			  			heroData.money+=1000;
						}
			  	};
		Action callHelp=new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
			friends.addAll(hero.callReinforcements());	
			}};
			s.getInputMap().put(up,
            up);
			s.getActionMap().put(up,upAct);
			s.getInputMap().put(up,up);
			s.getActionMap().put(up,upAct);
			s.getInputMap().put(down,down);
			s.getActionMap().put(down,downAct);
			s.getInputMap().put(left,left);
			s.getActionMap().put(left,leftAct);
			s.getInputMap().put(right,right);
			s.getActionMap().put(right,rightAct);
			s.getInputMap().put(fire,fire);
			s.getActionMap().put(fire,fireAct);
			s.getInputMap().put(weaponOne,weaponOne);
			s.getActionMap().put(weaponOne,w1Act);
			s.getInputMap().put(weaponTwo,weaponTwo);
			s.getActionMap().put(weaponTwo,w2Act);
			s.getInputMap().put(weaponThree,weaponThree);
			s.getActionMap().put(weaponThree,w3Act);
			s.getInputMap().put(weaponFour,weaponFour);
			s.getActionMap().put(weaponFour,w4Act);
			s.getInputMap().put(weaponFive,weaponFive);
			s.getActionMap().put(weaponFive,w5Act);
			s.getInputMap().put(stop,stop);
			s.getActionMap().put(stop,stopAct);
			s.getInputMap().put(pausePlay,pausePlay);
			s.getActionMap().put(pausePlay,ppAct);
			s.getInputMap().put(callFriends,callFriends);
			s.getActionMap().put(callFriends,callHelp);
			/*s.getInputMap().put(changeStage,changeStage);
			s.getActionMap().put(changeStage,cheatToStage);
			s.getInputMap().put(changeLife,changeLife);
			s.getActionMap().put(changeLife,increaseLife);
			s.getInputMap().put(changeBullets,changeBullets);
			s.getActionMap().put(changeBullets,increaseBullets);
			
			s.getInputMap().put(changeMoney,changeMoney);
			s.getActionMap().put(changeMoney,increaseMoney);*/

		}
	public void addMouse() {
		s.addMouseMotionListener(new MouseMotionAdapter() {
			   int x=0,y=0;
			   int tryLeft=0,tryRight=0,tryUp=0,tryDown=0;
			   public void mouseMoved(MouseEvent e) {
     		   if(x>e.getX()){
     		   
     		   tryLeft++;
     		   tryRight=tryUp=tryDown=0;
     		   if(tryLeft>2)
     		   hero.speedLeft();	
     		   }
     		   else if(x<e.getX()) {
     		   tryRight++;
     		  tryLeft=tryUp=tryDown=0;
     		   if(tryRight>2)
     		   hero.speedRight();	
     		   }
     		   if(y>e.getY()){
     		   tryUp++;
     		  tryRight=tryLeft=tryDown=0;
     		   if(tryUp>2)
     		   hero.accelerate();	
     		   }
     		   else if(y<e.getY()){
     		   tryDown++;
     		   tryRight=tryUp=tryLeft=0;
     		   if(tryDown>2)
     		   hero.reverse();	
     		   }
     		   x=e.getX();
     		   y=e.getY();
     		   //s.repaint();
     		   }
			});
		s.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				List l=hero.fireMultiple();
				myWeapons.addAll(l);
				}
			});
		s.addMouseWheelListener(new MouseWheelListener() {

			public void mouseWheelMoved(MouseWheelEvent e) {
				//if(hero.currentWeapon<hero.weapons)
				//	hero.currentWeapon++;
				//else hero.currentWeapon=1;
				hero.nextWeapon();
			}
			});
		}
	public void showMess(String mes) {
		s.setMessage(mes);
		pausePlay();
		}
	public BossShip addBoss() {
		BossShip b=new BossShip(UW.Rand(0, UW.xSize),0,UW.xSpeed,UW.ySpeed,(stage/100));
		return b;
		}
	private int getIntFromList(int stage) {
		int i=0;
		for(int j = 0;j<stage;j++) {
			Integer x=(Integer) levelData.get(j);
			i+=x.intValue();
			}
		return i;
		}
	private int getIntFromPowerUpList(int stage) {
		int i=0;
		for(int j = 0;j<stage;j++) {
			Integer x=(Integer) levelPowerUpData.get(j);
			i+=x.intValue();
			}
		return i;
		}
	/**
	 * This method returns a random enemyShip depending on the values 
	 * in the List LevelData. The relative weights of which rogue is returned depend
	 * on the values in this list. 
	 * */
	public RogueShip addRogues2() {
		int randNum=(int) (Math.random()*sumOfAllEnemies);
		RogueShip r;
		if(randNum<getIntFromList(1)) {
			//r=new StupidShip(UW.Rand(0, UW.xSize),0,UW.xSpeed,(int)UW.ySpeed);
			r=new RogueShip(UW.Rand(0, UW.xSize),0,UW.xSpeed,UW.ySpeed);
			//r=new BackupEnemies(UW.Rand(0, UW.xSize),UW.ySize,UW.xSpeed,-UW.ySpeed);
			}
		else if(randNum<getIntFromList(2)) {
			r=new ManyBulletedEnemy(UW.Rand(0, UW.xSize),0,UW.xSpeed,UW.ySpeed);
			}
		else if(randNum<getIntFromList(3)){
			r=new MinerEnemy(UW.Rand(0, UW.xSize),0,UW.xSpeed,UW.ySpeed);
			}
		else if(randNum<getIntFromList(4)) {
			r=new InvisibleEnemy(UW.Rand(0, UW.xSize),0,UW.xSpeed,UW.ySpeed);
			}
		else if(randNum<getIntFromList(5)) {
			r=new StupidShip(UW.Rand(0, UW.xSize),0,UW.xSpeed,(int)UW.ySpeed);
			}
		else if(randNum<getIntFromList(6)) {
			r=new BackupEnemies(UW.Rand(0, UW.xSize),UW.ySize,UW.xSpeed,(int)UW.ySpeed);
			}
		
		else{r=new RogueShip(UW.Rand(0, UW.xSize),0,UW.xSpeed,UW.ySpeed);
			}
		return r;
		} 
	public PowerUp addPowerUps() {
		int randNum=(int) (Math.random()*sumOfAllPowerUps);
		int randomPos=10+(int) (Math.random()*UW.xSize-10);
		PowerUp p;
		if(randNum<getIntFromPowerUpList(1)) {
			p=new PowerUp(randomPos);
			}
		else if(randNum<getIntFromPowerUpList(2)) {
			p=new LifeUp(randomPos);
			}
		else if(randNum<getIntFromPowerUpList(3)){
			p=new MoneyPowerUp(randomPos);
			}
		else if(randNum<getIntFromPowerUpList(4)) {
			p=new ClearScreenPowerUp(randomPos);
			}
		else if(randNum<getIntFromPowerUpList(5)) {
			p=new MinesUp(randomPos);
			}
		else{p=new PowerUp(randomPos);
			}
		return p;
		} 
	public PowerUp addPowerUps2() {
		int randomPos=10+(int) (Math.random()*UW.xSize-10);
		PowerUp p=new PowerUp(randomPos);
		return p;
		}
	public void reSpawnEverything() {
		rogues.clear();
		myWeapons.clear();
		enemyWeapons.clear();
		bossShip.clear();
		powerUps.clear();
		hero=new HeroShip(UW.Rand(0, UW.xSize),UW.ySize-20,UW.xSpeed,UW.ySpeed,100,100,100,true,true);
		enemiesKilled=0;
		heroDeaths=0;
		stage=1;
		setStage(stage);
		moneyL.setText("enemies killed: "+enemiesKilled);
		shipsLeftL.setText("Ships Lost: "+heroDeaths);
		bulletsLeftL.setText("bullets left: "+hero.bullets);
		minesLeftL.setText("mines left: "+hero.mines);
		s.newScene();
		if(s.shopMode||s.splashScreenMode) {
		s.setGameMode();
		s.resetShopMode();
		for(int j=0;j<arr.length;j++) {
			s.addMouseListener(arr[j]);
			}
		for(int j=0;j<arr1.length;j++) {
			s.addMouseMotionListener(arr1[j]);
			}
		for(int j=0;j<arr1.length;j++) {
			s.addMouseWheelListener(arr2[j]);
			}
		s.setActionMap(am);
		}
		}
	/**
	 * Sets the values of the current object to the passed object. Used to open a saved game.
	 * */
	private void setData(Space data) {
		rogues=data.rogues;
		myWeapons=data.myWeapons;
		enemyWeapons=data.enemyWeapons;
		bossShip=data.bossShip;
		powerUps=data.powerUps;
		hero=data.hero;
		enemiesKilled=data.enemiesKilled;
		heroDeaths=data.heroDeaths;
		stage=data.stage;
		setStage(stage);
		moneyL=data.moneyL;
		shipsLeftL=data.shipsLeftL;
		bulletsLeftL=data.bulletsLeftL;
		minesLeftL=data.minesLeftL;
		s=data.s;
		frame.repaint();
		
	}
	public JLabel addSplashScreen() {
		
		ImageIcon icon=UW.createImageIcon("../images/ship4.gif","icon");
		JLabel l=new JLabel(icon);
		return l;
		}
	private void saveAs() {
		JFileChooser fc=new JFileChooser();
		int ret=fc.showSaveDialog(frame);
		if(ret==JFileChooser.APPROVE_OPTION) {
			File f=fc.getSelectedFile();
			try {
			FileOutputStream out=new FileOutputStream(f);
			ObjectOutputStream obOut=new ObjectOutputStream(out);
			obOut.writeObject(this);
		
			}catch(Exception e) {}
		}
	}
	private Space open() {
		JFileChooser fc=new JFileChooser();
		Space newSpace=null;
		int ret=fc.showOpenDialog(frame);
		if(ret==JFileChooser.APPROVE_OPTION) {
			File f=fc.getSelectedFile();
			try {
			FileInputStream inp=new FileInputStream(f);
			ObjectInputStream objInp=new ObjectInputStream(inp);
			Object obj=objInp.readObject();
			newSpace=(Space)obj;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return newSpace;
	}
	
	public static void main(String[] args) {
		ImageIcon icon=UW.createImageIcon("../images/ship4.gif","icon");
		final Space app=new Space();
		JFrame f=new JFrame(app.gameName+" V 1.1");
		f.setResizable(false);
		//f.setUndecorated(true);
		f.setIconImage(icon.getImage());
		f.getContentPane().add(app.createComponents());
		f.setJMenuBar(app.addMenuBar());
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				}
			});
		/*f.setIgnoreRepaint(true);
		f.setSize(new Dimension(800,600));
		GraphicsEnvironment env = GraphicsEnvironment.
        getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		devices[0].setFullScreenWindow(f);
		f.createBufferStrategy(10);
		final BufferStrategy bs=f.getBufferStrategy();
		final Graphics g=bs.getDrawGraphics();
		g.setColor(Color.RED);
		Timer t=new Timer(100,new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				app.s.paintComponent(g);
				bs.show();
			}
			
			});
		t.start();*/
		f.pack();
		f.setVisible(true);
	}
}
