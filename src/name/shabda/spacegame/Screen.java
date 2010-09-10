/*
 * Created on Sep 26, 2004
 */
package name.shabda.spacegame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author shabda
 * Screen repsents the game screen of the game-basically a night sky.
 * It draws the stars randomly. Everything else which needs to be drawn is added to its
 * Artifacts list.This is its basic operation.
 * It also has two other modes:
 * 1.SplashScreen mode-it shows a image.
 * 2.ShopMode-A image and some buttons using which we can shop.
 * on the screen everything should be drawable and everything should be an artifact
 */
public class Screen extends JPanel implements Serializable{
	private static final Hashtable map = new Hashtable();
    static {
        map.put(TextAttribute.SIZE, new Float(18.0));
    } 
	List artifacts=new ArrayList();
	List justSomeDots=new ArrayList();
	List planets=new ArrayList();
	int chancePlanetSpawns=200;
	String message="noMessage";
	
	AttributedString splashMessage=new AttributedString(" ",map);
	final static BasicStroke wideStroke = new BasicStroke(8.0f);

	
	String[] shopMessages;
	boolean splashScreenMode=false;
	boolean shopMode=false;
	Image img;
	Font font;
	JPanel shopButtons=new JPanel();//for shop buttons
	Timer t=new Timer(80,new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int randNum=(int) (Math.random()*1.2);
			for(int j=0;j<randNum;j++) {
				int randXPos=(int) (Math.random()*UW.xSize);
				Dots d=new Dots(randXPos,0,UW.passiveSpeed);
				justSomeDots.add(d);
			}
			for(Iterator i=justSomeDots.iterator();i.hasNext();) {
				Object o=i.next();
				Moving m=(Moving)o;
				m.move();
				}
			for(Iterator i=planets.iterator();i.hasNext();) {
				Object o=i.next();
				Moving m=(Moving)o;
				m.move();
				}
			int i=(int) (Math.random()*chancePlanetSpawns);
			if(i==0)
			planets.add(addPlanets());
			killWhenOutside(justSomeDots);
			killWhenOutside(planets);
			killWhenOutside(artifacts);
		}
		});
	/**
	 * @param text
	 * @param icon
	 * @param horizontalAlignment
	 */
	public Screen() {
		super();
		shopMessages=new String[2];
		shopMessages[0]="";
		shopMessages[1]="";
		font = new Font("SansSerif", Font.PLAIN, 20);
		setPreferredSize(new Dimension(UW.xSize,UW.ySize));
		setLayout(new GraphPaperLayout(new Dimension(15,15)));
		shopButtons.setOpaque(false);
		t.start();
		for(int i=0;i<100;i++) {
			double randXPos= (Math.random()*UW.xSize);
			double randYPos= (Math.random()*UW.ySize);
			Dots d=new Dots(randXPos,randYPos);
			justSomeDots.add(d);
			}
		for(int i=0;i<2;i++) {
			double randXPos= (Math.random()*UW.xSize);
			double randYPos= (Math.random()*UW.ySize);
			double randSize= (Math.random()*30)+30;
			Planets p=new Planets((int)randXPos,(int)randYPos,(int)randSize);
			planets.add(p);
			}
		//paragraph=splashMessage.getIterator();
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void paintComponent(Graphics g) {
	if(splashScreenMode) {
			//if(img!=null)
			g.drawImage(img, getX(), getY(), getWidth(), getHeight(), this);
		  Graphics2D graphics2D = (Graphics2D) g;
		 // graphics2D.setStroke(wideStroke);
		  AttributedCharacterIterator paragraph=splashMessage.getIterator();
		  LineBreakMeasurer lineMeasurer=lineMeasurer = new LineBreakMeasurer(paragraph, 
	            new FontRenderContext(null, false, false));
		  int paragraphStart = paragraph.getBeginIndex();
		  int paragraphEnd = paragraph.getEndIndex();
		  
		  
	      // Set formatting width to width of Component.
		Dimension size = getSize();
	      float formatWidth = (float) size.width;

	      float drawPosY = 50;

	      lineMeasurer.setPosition(paragraphStart);

	      // Get lines from lineMeasurer until the entire
	      // paragraph has been displayed.
	      while (lineMeasurer.getPosition() < paragraphEnd) {

	          // Retrieve next layout.
	          TextLayout layout = lineMeasurer.nextLayout(formatWidth);
	          // Move y-coordinate by the ascent of the layout.
	          drawPosY += layout.getAscent();

	          // Compute pen x position.  If the paragraph is 
	          // right-to-left, we want to align the TextLayouts
	          // to the right edge of the panel.
	          float drawPosX;
	          if (layout.isLeftToRight()) {
	              drawPosX = 0;
	          }
	          else {
	              drawPosX = formatWidth - layout.getAdvance();
	          }

	          // Draw the TextLayout at (drawPosX, drawPosY).
	          layout.draw(graphics2D, drawPosX, drawPosY);

	          // Move y-coordinate in preparation for next layout.
	          drawPosY += layout.getDescent() + layout.getLeading();
	      }
		}
	else if(shopMode) {
		setOpaque(true);
		g.drawImage(img,0,0,getWidth(),getHeight(),null);
		g.setColor(Color.red);
		g.setFont(font);
		
		g.drawString(shopMessages[0], 30, 50);
		g.drawString(shopMessages[1], 30, 450);
		}
	else {
	g.setColor(Color.black);
	g.fillRect(getX(), getY(), getWidth(), getHeight());
	for(Iterator i=justSomeDots.iterator();i.hasNext();) {
		((Drawable)i.next()).draw(g);	
	}
	for(Iterator i=planets.iterator();i.hasNext();) {
		((Drawable)i.next()).draw(g);	
	}
	for(Iterator i=artifacts.iterator();i.hasNext();) {
		((Drawable)i.next()).draw(g);	
	}
	if(message.equals("noMessage"));
	else {
	g.setColor(Color.WHITE);
	g.setFont(font);
	g.drawString(message,UW.xSize/4,UW.ySize/3 );
	}
	}
	}
	public void setSplashScreen(ImageIcon ii) {
		img=ii.getImage();
		splashScreenMode=true;
		shopMode=false;
		repaint();
		
		}
	public void resetSplashScreen(){
		splashScreenMode=false;
		repaint();
		}
	public void newScene() {
		planets.clear();
		justSomeDots.clear();
		for(int i=0;i<100;i++) {
			double randXPos= (Math.random()*UW.xSize);
			double randYPos= (Math.random()*UW.ySize);
			Dots d=new Dots(randXPos,randYPos);
			justSomeDots.add(d);
			}
		for(int i=0;i<2;i++) {
			double randXPos= (Math.random()*UW.xSize);
			double randYPos= (Math.random()*UW.ySize);
			double randSize= (Math.random()*30)+30;
			Planets p=new Planets((int)randXPos,(int)randYPos,(int)randSize);
			planets.add(p);
			}
		}
	/**
	 * in shop mode we need to show the wares.So all the buttons from the list are 
	 * added to the screen. The list is cleared.
	 * */
	public void setShopMode( List l) {
		//setOpaque(false);
		splashScreenMode=false;
		shopMode=true;
		ImageIcon ic=UW.createImageIcon("../images/shop.gif", null);
		img=ic.getImage();
		//buttons.addAll(l);
		for(Iterator i=l.iterator();i.hasNext();) {
			Object o=i.next();
			JButton b=(JButton)o;
			shopButtons.add(b);
			}
		add(shopButtons,new Rectangle(2,2,11,11));
		repaint();
		validate();
		}
	public void resetShopMode() {
		shopMode=false;
		removeAll();
		shopButtons.removeAll();
		//System.out.print(shopButtons.)
		repaint();
		}
	public void setGameMode() {
		splashScreenMode=false;
		shopMode=false;
		removeAll();
		}
	public void killWhenOutside(List l) {
		for(Iterator i=l.iterator();i.hasNext();) {
			Artifact a=((Artifact)i.next());
			if(a.getXPos()>UW.xSize||a.getXPos()<0||a.getYPos()>UW.ySize||a.getYPos()<0) {
				i.remove();
				}
			}
		}
	public void setShopMessage1(String s) {
		shopMessages[0]=s;
		repaint();
		}
	public void setShopMessage2(String s) {
		shopMessages[1]=s;
		repaint();
		}
	public void pauseMovement() {
		t.stop();
		for(Iterator i=planets.iterator();i.hasNext();) {
		Planets p=(Planets) i.next();
		p.t.stop();
		}
		}
	public void startMovement() {
		t.start();
		for(Iterator i=planets.iterator();i.hasNext();) {
		Planets p=(Planets) i.next();
		p.t.start();
		}
		}
	public SpaceScape addPlanets() {
		int randomPos=10+(int) (Math.random()*UW.xSize-10);
		int randomSize=(int) (Math.random()*35+25);
		SpaceScape s=new Planets(randomPos, -randomSize,randomSize);
		s.setYPos(1);
		return s;
		}
	public static void main(String[] args) {
		JFrame f=new JFrame();
		FlowLayout layO = new FlowLayout();
		f.getContentPane().setLayout(layO);
		Dimension d = new Dimension(UW.xSize,UW.ySize);
		final Screen l = new Screen();
		List ll=new ArrayList();
		JButton b=new JButton("a button");
		JButton b2=new JButton("another button");
		ll.add(b);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				l.resetShopMode();	
			}
		});
		
		ll.add(b2);
		//l.setShopMode(ll);
		/*l.setSplashScreen(null);
		l.splashMessage=new AttributedString("asdfJFrame f=new JFrame();\r\n" + 
				"		FlowLayout layO = new FlowLayout();\r\n" + 
				"		f.getContentPane().setLayout(layO);\r\n" + 
				"		Dimension d = new Dimension(UW.xSize,UW.ySize);\r\n" + 
				"		final Screen l = new Screen();\r\n" + 
				"		List ll=new ArrayList();\r\n" + 
				"		JButton b=new JButton(\"a button\");\r\n" + 
				"		JButton b2=new JButton(\"another button\");\r\n" + 
				"		ll.add(b);\r\n" + 
				"		b.addActionListener(new ActionListener() {\r\n" + 
				"			public void actionPerformed(ActionEvent e) {\r\n" + 
				"				l.resetShopMode();	\r\n" + 
				"			}\r\n" + 
				"		});",l.map);*/
		f.getContentPane().add(l);
		f.getContentPane().setSize(d);
		f.pack();
		f.setVisible(true);
	}
}
