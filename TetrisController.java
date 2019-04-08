import java.awt.event.*;

public class TetrisController implements ActionListener, KeyListener, StatsObserver, FocusListener {

	private TetrisModel model;
	private TetrisView view;
	
	public TetrisController(TetrisModel model, TetrisView view) {
		this.model = model;
		this.view = view;
		model.registerStatsObserver(this);
		view.addKeyListener(this);
	}

	public void statsChanged() {
		TetrisStats stats = model.getStats();
		if(!stats.isGameOver) {
			view.setStats(model.getStats());
			view.repaint();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_LEFT) {
			if(model.canMoveLeft()) {
				model.getCurrentTetris().moveLeft();
			}
		} else if(keyCode == KeyEvent.VK_RIGHT) {
			if(model.canMoveRight()) {
				model.getCurrentTetris().moveRight();
			}
		} else if(keyCode == KeyEvent.VK_DOWN) {
			if(model.canMoveDown()) {
				model.getCurrentTetris().moveDown();
			}
		} else if(keyCode == KeyEvent.VK_UP) {
			if(model.canRotate()) {
				model.getCurrentTetris().rotate();
			}
		}
		view.repaint();
	}
	
	public void keyTyped(KeyEvent e) { }
	public void keyReleased(KeyEvent e) { }

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command.equals("Start")) {
			
		} else if(command.equals("Quit")) {
			System.exit(0);
		}
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		view.repaint();
	}

	@Override
	public void focusLost(FocusEvent e) {
		view.repaint();
	}
}