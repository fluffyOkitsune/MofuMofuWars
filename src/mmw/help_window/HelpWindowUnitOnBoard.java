package mmw.help_window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import mmw.Game;
import mmw.database.WeaponType;
import mmw.obj.UnitOnBoard;

public class HelpWindowUnitOnBoard implements HelpWindow {
	public final Game GAME;
	public final UnitOnBoard UNIT;

	final int POS_X;
	final int POS_Y;

	// カーソルが当たっているときに表示する説明文
	final String NAME;
	final String TYPE;
	final String WEAPON;
	final String[] WEAPON_LEVEL;

	private int cursol;

	public HelpWindowUnitOnBoard(final Game GAME, final UnitOnBoard UNIT,
			final int X, final int Y) {
		this.GAME = GAME;
		this.UNIT = UNIT;

		this.POS_X = 2 * Game.CHIP_SIZE * X + 25;
		this.POS_Y = 2 * Game.CHIP_SIZE * Y + 25;

		this.NAME = UNIT.UNIT.explain;
		this.TYPE = UNIT.UNIT.type.explain;
		this.WEAPON = UNIT.UNIT.weapon.WEAPON_DATA.explain;

		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < UNIT.UNIT.weaponLevel.length; i++) {
			if (UNIT.UNIT.weaponLevel[i] != '-') {
				temp.add(WeaponType.values()[i].name + " : "
						+ WeaponType.values()[i].explain);
			}
		}
		this.WEAPON_LEVEL = temp.toArray(new String[0]);

		cursol = 0;
	}

	public void run() {
	}

	public void draw(Graphics g) {
		int windowPosX;
		int windowPosY;
		switch (cursol) {
		case 0: // 名前
			g.setColor(Color.RED);
			g.drawRect(POS_X + 7, POS_Y + 3, 70, 20);

			windowPosX = POS_X + 90;
			windowPosY = POS_Y + 10;
			g.setColor(Color.BLACK);
			g.fillRect(windowPosX, windowPosY, 100, 15);
			g.setColor(Color.WHITE);
			g.drawRect(windowPosX, windowPosY, 100, 15);
			g.setFont(new Font(null, Font.PLAIN, 10));
			g.drawString(NAME, windowPosX + 3, windowPosY + 12);
			break;
		case 1: // 兵種名
			g.setColor(Color.RED);
			g.drawRect(POS_X + 7, POS_Y + 28, 70, 15);

			windowPosX = POS_X + 90;
			windowPosY = POS_Y + 30;
			g.setColor(Color.BLACK);
			g.fillRect(windowPosX, windowPosY, 300, 15);
			g.setColor(Color.WHITE);
			g.drawRect(windowPosX, windowPosY, 300, 15);
			g.setFont(new Font(null, Font.PLAIN, 10));
			g.drawString(TYPE, windowPosX + 3, windowPosY + 12);
			break;
		case 2: // 武器1
			g.setColor(Color.RED);
			g.drawRect(POS_X + 8, POS_Y + 48, 31, 20);

			windowPosX = POS_X + 90;
			windowPosY = POS_Y + 50;
			g.setColor(Color.BLACK);
			g.fillRect(windowPosX, windowPosY, 300, 15);
			g.setColor(Color.WHITE);
			g.drawRect(windowPosX, windowPosY, 300, 15);
			g.setFont(new Font(null, Font.PLAIN, 10));
			g.drawString(WEAPON_LEVEL[0], windowPosX + 3, windowPosY + 12);
			break;
		case 3: // 武器1
			g.setColor(Color.RED);
			g.drawRect(POS_X + 38, POS_Y + 48, 31, 20);

			windowPosX = POS_X + 90;
			windowPosY = POS_Y + 50;
			g.setColor(Color.BLACK);
			g.fillRect(windowPosX, windowPosY, 300, 15);
			g.setColor(Color.WHITE);
			g.drawRect(windowPosX, windowPosY, 300, 15);
			g.setFont(new Font(null, Font.PLAIN, 10));
			g.drawString(WEAPON_LEVEL[1], windowPosX + 3, windowPosY + 12);
			break;
		case 4: // 武器1
			g.setColor(Color.RED);
			g.drawRect(POS_X + 68, POS_Y + 48, 31, 20);

			windowPosX = POS_X + 90;
			windowPosY = POS_Y + 50;
			g.setColor(Color.BLACK);
			g.fillRect(windowPosX, windowPosY, 300, 15);
			g.setColor(Color.WHITE);
			g.drawRect(windowPosX, windowPosY, 300, 15);
			g.setFont(new Font(null, Font.PLAIN, 10));
			g.drawString(WEAPON_LEVEL[2], windowPosX + 3, windowPosY + 12);
			break;
		case 5: // 武器の説明
			g.setColor(Color.RED);
			g.drawRect(POS_X + 7, POS_Y + 68, 70, 15);

			windowPosX = POS_X + 90;
			windowPosY = POS_Y + 70;
			g.setColor(Color.BLACK);
			g.fillRect(windowPosX, windowPosY, 300, 15);
			g.setColor(Color.WHITE);
			g.drawRect(windowPosX, windowPosY, 300, 15);
			g.setFont(new Font(null, Font.PLAIN, 10));
			g.drawString(WEAPON, windowPosX + 3, windowPosY + 12);
			break;
		}
	}

	public void keyPressed(KeyEvent e) {
		final int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_RIGHT:
			break;
		case KeyEvent.VK_LEFT:
			break;
		case KeyEvent.VK_DOWN:
			if (cursol < 5) {
				if (cursol == 2 && WEAPON_LEVEL.length < 2)
					cursol = 5;
				else if (cursol == 3 && WEAPON_LEVEL.length < 3)
					cursol = 5;
				else
					cursol++;
			}
			break;
		case KeyEvent.VK_UP:
			if (cursol > 0) {
				if (cursol == 5 && WEAPON_LEVEL.length < 2)
					cursol = 2;
				else if (cursol == 5 && WEAPON_LEVEL.length < 3)
					cursol = 3;
				else
					cursol--;
			}
			break;
		case KeyEvent.VK_H: // ヘルプウィンドウ
			GAME.SM.SB.helpWindow = null;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
