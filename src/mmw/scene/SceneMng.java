package mmw.scene;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import mmw.Game;
import mmw.scene.battle_anim.SceneBattleAnim;
import mmw.scene.board.SceneBoard;

// 大まかな画面の枠組み
public class SceneMng extends Scene {
	public final SceneBoard SB;
	public final SceneBattleAnim SBA;

	public Scene scene;

	public SceneMng(final Game GAME) {
		super(GAME);
		this.SB = new SceneBoard(GAME, 0);
		this.SBA = new SceneBattleAnim(GAME);

		// お試し
		scene = SB;

	}

	@Override
	public void keyPressed(KeyEvent e) {
		scene.keyPressed(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public boolean run() {
		scene.run();
		return true;
	}

	@Override
	public void draw(Graphics g) {
		scene.draw(g);
	}

	@Override
	public void update() {

	}
}
