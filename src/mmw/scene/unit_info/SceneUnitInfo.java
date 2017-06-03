package mmw.scene.unit_info;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import mmw.Game;
import mmw.obj.UnitOnBoard;
import mmw.scene.Scene;
import mmw.unit.Unit;

public class SceneUnitInfo extends Scene {
	final UnitOnBoard UNIT;
	int cursol;

	public SceneUnitInfo(final Game GAME, final UnitOnBoard UNIT) {
		super(GAME);
		this.cursol = 0;
		this.UNIT = UNIT;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean run() {
		// TODO Auto-generated method stub
		return true;
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
