import java.awt.*;
import javax.swing.*;

public class TetrisMain {

	public static void main(String[] args) {
		JFrame window = new JFrame("Tetris");

		TetrisModel model = new TetrisModel();
		TetrisView view = new TetrisView(model);
		new TetrisController(model, view);

		window.setContentPane(view);
		window.pack();
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation((screensize.width - window.getWidth()) / 2, (screensize.height - window.getHeight()) / 2);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);  
		window.setVisible(true);
		view.setFocusable(true);
		view.requestFocusInWindow();

		while(true) {
			view.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) { }
		}
	}
}