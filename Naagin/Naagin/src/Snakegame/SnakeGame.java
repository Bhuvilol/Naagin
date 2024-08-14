package Snakegame;

import javax.swing.JFrame;

public class SnakeGame extends JFrame{
	SnakeGame(){
		super("Naagin");
		add(new Board());
		pack();
	
		setResizable(false);
		setLocationRelativeTo(null);
		
	}

	public static void main(String[] args) {
		new SnakeGame().setVisible(true);;
	}

}
