package Snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;



public class Board extends JPanel implements ActionListener{
	private int dots;
	private Image apple;
	private Image dot;
	private Image head;
	
	private final int ALL_DOTS = 900;
	private final int DOT_SIZE = 10;
	private final int RANDOM_POS = 29;
	
	private int apple_x;
	private int apple_y;
	
	private final int x[] = new int[ALL_DOTS];
	private final int y[] = new int[ALL_DOTS];
	
	private boolean left = false;
	private boolean right = true;
	private boolean up = false;
	private boolean down = false;
	
	private boolean ingame = true;
	
	private Timer timer;
	
	Board(){
		addKeyListener(new TAdapter());
		
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(300,300));
		setFocusable(true);
		
		loadimages();
		initGame();
	}
	public void loadimages() {
		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/apple.png"));
		apple = i1.getImage();
		
		ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("icons/dot.png"));
		dot = i2.getImage();
		
		ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("icons/head.png"));
		head = i3.getImage();
		
	}
	public void initGame() {
		dots = 3;
		for (int i = 0; i < dots; i++) {
			y[i] = 50;
			x[i] = 50 - i*DOT_SIZE;
		}
		
		locateapple();
		
		timer = new Timer(140,this);
		timer.start();
	}
	public void locateapple() {
		int r = (int)(Math.random()*RANDOM_POS);
		apple_x = r*DOT_SIZE;
		
		r = (int)(Math.random()*RANDOM_POS);
		apple_y = r*DOT_SIZE;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if (ingame) {
			g.drawImage(apple, apple_x, apple_y,this);
			for (int i = 0; i < dots; i++) {
				if (i==0) {
					g.drawImage(head, x[i], y[i], this);
				} else {
					g.drawImage(dot, x[i], y[i], this);
				}
			}
			Toolkit.getDefaultToolkit().sync();
		}
		else {
			gameover(g);
		}
		
	}
	private void gameover(Graphics g) {
		String msg = "Game Over";
		String score = ""+(dots-3);
		Font font = new Font("SAN_SERIF",Font.BOLD,15);
		FontMetrics metrices = getFontMetrics(font);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(msg, (300-metrices.stringWidth(msg))/2, 300/2);
		g.drawString(score, (300-metrices.stringWidth(msg))/2+35, 170);
	}
	public void move() {
		for (int i = dots; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		if (left) {
			x[0] = x[0]-DOT_SIZE;
		}
		if (right) {
			x[0] = x[0]+DOT_SIZE;
		}
		if (up) {
			y[0] = y[0]-DOT_SIZE;
		}
		if (down) {
			y[0] = y[0]+DOT_SIZE;
		}
	}
	public void checkApple() {
		if (x[0] == apple_x && y[0] == apple_y) {
			dots++;
			locateapple();
		}
	}
	public void checkcollision() {
		for (int i = dots; i > 0; i--) {
			if ((i>4) && (x[0] == x[i]) && (y[0] == y[i])) {
				ingame = false;
			}
		}
		if (y[0]>=300) {
			ingame = false; 
		}
		if (y[0]<0) {
			ingame = false;
		}
		if (x[0]>=300) {
			ingame = false; 
		}
		if (x[0]<0) {
			ingame = false;
		}
		
		if (!ingame ) {
			timer.stop();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		checkApple();
		checkcollision();
		move();
		repaint();
	}
	public class TAdapter extends KeyAdapter{
		
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_A && (!right)) {
			left = true;
			up = false;
			down = false;
			}
	
			if (key == KeyEvent.VK_D && (!left)) {
			right = true;
			up = false;
			down = false;
			}
			
			
			if (key == KeyEvent.VK_W && (!down)) {
			up = true;
			left = false;
			right = false;
			}
						
			if (key == KeyEvent.VK_S && (!up)) {
			down = true;
			left = false;
			right = false;
			}
			
		}
	}
}
