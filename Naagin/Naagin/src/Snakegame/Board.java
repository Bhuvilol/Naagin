package Snakegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {
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

	// Restart button
	private JButton restartButton;

	private JButton exitButton;

	Board() {
		addKeyListener(new TAdapter());
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(300, 300));
		setFocusable(true);

		loadImages();
		initGame();

		// Initialize and add restart button
		restartButton = new JButton("Restart");
		restartButton.setBounds(100, 190, 100, 30);
		restartButton.setVisible(false);
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
		});
		setLayout(null);
		add(restartButton);

		exitButton = new JButton("Exit");
		exitButton.setBounds(100, 230, 100, 30);
		exitButton.setVisible(false);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitGame();
			}
		});
		setLayout(null);
		add(exitButton);

	}

	public void loadImages() {
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
			x[i] = 50 - i * DOT_SIZE;
		}

		locateApple();

		timer = new Timer(140, this);
		timer.start();
	}

	public void locateApple() {
		int r = (int)(Math.random() * RANDOM_POS);
		apple_x = r * DOT_SIZE;

		r = (int)(Math.random() * RANDOM_POS);
		apple_y = r * DOT_SIZE;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (ingame) {
			g.drawImage(apple, apple_x, apple_y, this);
			for (int i = 0; i < dots; i++) {
				if (i == 0) {
					g.drawImage(head, x[i], y[i], this);
				} else {
					g.drawImage(dot, x[i], y[i], this);
				}
			}
			Toolkit.getDefaultToolkit().sync();
		} else {
			gameOver(g);
		}
	}

	private void gameOver(Graphics g) {
		String msg = "Game Over";
		String score = "" + (dots - 3);
		Font font = new Font("SAN_SERIF", Font.BOLD, 15);
		FontMetrics metrics = getFontMetrics(font);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(msg, (300 - metrics.stringWidth(msg)) / 2, 300 / 2);
		g.drawString(score, (300 - metrics.stringWidth(msg)) / 2 + 35, 170);

		restartButton.setVisible(true);
		exitButton.setVisible(true);
	}

	private void restartGame() {
		restartButton.setVisible(false);
		exitButton.setVisible(false);

		ingame = true;
		initGame();
		repaint();
	}

	private void exitGame(){
		System.exit(0);
	}

	public void move() {
		for (int i = dots; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		if (left) {
			x[0] = x[0] - DOT_SIZE;
		}
		if (right) {
			x[0] = x[0] + DOT_SIZE;
		}
		if (up) {
			y[0] = y[0] - DOT_SIZE;
		}
		if (down) {
			y[0] = y[0] + DOT_SIZE;
		}
	}

	public void checkApple() {
		if (x[0] == apple_x && y[0] == apple_y) {
			dots++;
			locateApple();
		}
	}

	public void checkCollision() {
		for (int i = dots; i > 0; i--) {
			if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
				ingame = false;
			}
		}
		if (y[0] >= 300 || y[0] < 0 || x[0] >= 300 || x[0] < 0) {
			ingame = false;
		}

		if (!ingame) {
			timer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		checkApple();
		checkCollision();
		move();
		repaint();
	}

	public class TAdapter extends KeyAdapter {
		@Override
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