package mmw;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mmw.database.DataBase;
import mmw.scene.SceneMng;

@SuppressWarnings("serial")
public class Game extends JFrame implements KeyListener, Runnable {
	// ウィンドウに関する定数
	public final static int WINDOW_WIDTH = 320;
	public final static int WINDOW_HEIGHT = 240;
	public final static int CHIP_SIZE = 16;

	// キーボードに関する定数
	public final static int KEY_DECIDE = KeyEvent.VK_Z;
	public final static int KEY_CANCEL = KeyEvent.VK_X;
	public final static int KEY_MENU = KeyEvent.VK_C;

	// データベースに関数定数
	public static final int NUM_PARAM = 7;
	public static final int NUM_WEAPON_TYPE = 10;
	public static final int NUM_SKILL = 5;

	// イメージ表示
	private Graphics OFFSCREEN_G;
	private Image OFFSCREEN;
	private JPanel panel;

	// ゲーム開始からの経過フレーム
	public long gametime = 0l;

	// ゲームを動かすオブジェクト
	public final SceneMng SM;
	public final DataBase DATABASE;

	public static void main(String args[]) {
		Game game = new Game();
		Thread thread = new Thread(game);
		thread.start();
	}

	public Game() {
		super("もふもふうぉーず");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = (JPanel) this.getContentPane();
		panel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		this.addKeyListener(this);
		pack();
		setVisible(true);

		// オフスクリーンイメージ
		OFFSCREEN = createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
		OFFSCREEN_G = OFFSCREEN.getGraphics();

		// ゲームのオブジェクト
		DATABASE = new DataBase();
		SM = new SceneMng(this);

	}

	@Override
	public void run() {
		int FPS = 60;
		while (true) {
			SM.run();
			draw(OFFSCREEN_G);
			gametime++;
			try {
				Thread.sleep(1000 / FPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void draw(Graphics g) {
		SM.draw(g);
		// 画面に出力
		paint(panel.getGraphics());
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(OFFSCREEN, 0, 0, this);
	}

	@Override
	public void repaint() {
		// 自動的な再描画を防ぐ
	}

	@Override
	public void keyPressed(KeyEvent e) {
		SM.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
