package mmw.scene;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import mmw.Game;

import java.awt.event.KeyListener;

public abstract class Scene implements KeyListener {
	// ポインタ
	protected final Game GAME;
	// このレンダーのイメージ
	protected final BufferedImage LAYER;
	protected final Graphics LAYER_G;

	public Scene(Game game) {
		GAME = game;
		LAYER = new BufferedImage(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		LAYER_G = LAYER.getGraphics();
	}

	/** 実行 */
	public abstract boolean run();

	/** 描画内容の更新 */
	public abstract void update();

	/** 描画 */
	public void draw(Graphics g) {
		g.drawImage(LAYER, 0, 0, GAME);
	};

}
