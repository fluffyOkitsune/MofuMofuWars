package mmw.obj;

import java.awt.Graphics;

import mmw.Game;

public abstract class GameObj {
	// ポインタ
	protected final Game GAME;

	public GameObj() {
		GAME = null;
	}

	public GameObj(Game game) {
		GAME = game;
	}

	/** 実行 */
	public abstract boolean run();

	/** 描画 */
	public abstract void draw(Graphics g);
}
