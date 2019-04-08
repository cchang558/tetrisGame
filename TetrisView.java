import java.awt.*;
import javax.swing.*;

public class TetrisView extends JPanel {

	private TetrisModel model;
	//private TetrisController controller;
	private JLabel scoreLabel, lineLabel;
	//private JButton startButton, quitButton;
	private final int width = 320;
	private final int height = 360;
	private int score = 0;
	private int line = 0;
	
	public TetrisView(TetrisModel model) {
		this.model = model;
		//controller = new TetrisController(model, this);
		
		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
		this.setLayout(null);
		
		scoreLabel = new JLabel("Score: " + score);
		scoreLabel.setFont(new Font("Courier", Font.BOLD, 12));
		scoreLabel.setBounds(245, 30, 120, 20);
		
		lineLabel = new JLabel("Line: " + line);
		lineLabel.setFont(new Font("Courier", Font.BOLD, 12));
		lineLabel.setBounds(245, 60, 120, 20);
		
		//startButton = new JButton("Start");
		//startButton.addActionListener(controller);
		
		//quitButton = new JButton("Quit");
		//quitButton.addActionListener(controller);
		
		//startButton.setBounds(245, 110, 120, 20);
		//quitButton.setBounds(245, 160, 120, 20);
	
		this.add(scoreLabel);
		this.add(lineLabel);
		//this.add(startButton);
		//this.add(quitButton);
	}
	
	public JLabel getScoreLabel() {
		return scoreLabel;
	}

	public void setScoreLabel(JLabel scoreLabel) {
		this.scoreLabel = scoreLabel;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(model.isGameOver()) {
			g.setColor(Color.BLACK);
			g.drawString("Game Over", 245, 120);
		}
		model.getBoard().drawBoard(g);
		model.getCurrentTetris().draw(g);
	}
	
	public void setStats(TetrisStats stats) {
		score = stats.score;
		line = stats.line;
		scoreLabel.setText("Score: " + score);
		lineLabel.setText("Line: " + line);
		repaint();
		
	}
}