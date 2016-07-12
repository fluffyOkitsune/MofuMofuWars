package mmw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends JFrame implements KeyListener, Runnable {
	public final static int WINDOW_WIDTH = 640;
	public final static int WINDOW_HEIGHT = 480;
	public final static int CHIP_SIZE = 16;
	// ポインタもどき
	public static Game game;

	public Graphics g;
	public ImageObserver io;
	public Image offscreen;

	/** プレイ中のステージ番号 */
	int stageNo;

	/** ゲーム開始からの経過フレーム */
	static long gametime = 0;

	/** ゲームで使用する画像 */
	static Image[] imgField;

	ObjectMng om;
	StageMng sm;

	Point cursol;

	public static void main(String args[]) {
		Thread thread = new Thread(new Game());
		thread.start();
	}

	public Game() {
		game = this;
	}

	@Override
	public void run() {
		init();
		while (true) {
			update();
			draw();
			gametime++;
		}
	}

	void init() {
		// ウィンドウの設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("もふもふうぉーず");
		setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		setVisible(true);

		this.addKeyListener(this);

		// 画像の表示の設定
		imgField = new Image[1];
		try {
			imgField[0] = ImageIO.read(new File("src/Stage0.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		offscreen = this.createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
		g = offscreen.getGraphics();

		om = new ObjectMng();
		sm = new StageMng();

		stageNo = 0;

		restart();

	}

	/** リスタート時のリセット */
	void restart() {
		cursol = new Point(0, 0);
	}

	/** ゲーム計算 */
	void update() {
	};

	/** ゲーム描画 */
	void draw() {
		sm.draw(stageNo);

		paint(this.getGraphics());
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(offscreen, 0, 0, this);
	}

	@Override
	public void repaint() {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_DOWN:
			if (cursol.y + 1 < sm.stage[stageNo].mapCol)
				cursol.y++;
			break;
		case KeyEvent.VK_UP:
			if (cursol.y > 0)
				cursol.y--;
			break;
		case KeyEvent.VK_RIGHT:
			if (cursol.x + 1 < sm.stage[stageNo].mapRow)
				cursol.x++;
			break;
		case KeyEvent.VK_LEFT:
			if (cursol.x > 0)
				cursol.x--;
			break;
		case KeyEvent.VK_Z:
			break;
		case KeyEvent.VK_X:
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

}
