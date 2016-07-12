package mmw;

/** マップの地形 */
public enum Terrain {
	PLAINS("平地", true, 0b111, 1, 0, 0, 0),
	GRASS("芝生", true, 0b111, 1, 0, -10, 0),
	GROVE("林", true, 0b111, 2, 10, -10, 0),
	FOREST("森", true, 0b100, 1, 0, 0, 0),// 飛行のみ
	BOG("沼", true, 0b110, 2, -20, -20, 0),
	MOUNTAIN("山", true, 0b111, 2, -10, -10, 0),
	
	SEA("海", true, 0b100, 1, 0, 0, 0),
	SHOAL("浅瀬", true, 0b110, 1, -10, -10, 0),
	BEACH("砂浜", true, 0b111, 1, -10, -10, 0),
	
	// 建物
	FRROR("床", true, 0b111, 2, 0, 0, 0),
	WALL("壁", false, 0b000, 0, 0, 0, 0),
	LOW_WALL("低い壁", true, 0b100, 1, 0, 0, 0),// 飛行のみ
	CARPET("絨毯", true, 0b111, 1, 0, -10, 0),
	GATE("門", true, 0b111, 2, 10, -10, 0),
	PILLER("柱", true, 0b111, 2, 10, -10, 0),
	RUIN("廃墟", true, 0b111, 1, 0, -10, 0),

	// ダメージ床
	BARRIER("結界", true, 0b000, 0, 0, 0, 20),
	GEYSER("間欠泉", true, 0b111, 0, 0, 0, -20),
	VENOM("毒沼", true, 0b111, 0, -20, -20, -20),
	;

	/** 表示される地形の名前 */
	String name;
	/** 待機できるか */
	boolean canWait;
	/** 移動できるか */
	byte canMove;
	/** 移動コスト */
	byte cost;
	/** 地形効果(防御) */
	byte guard;
	/** 地形効果(回避) */
	byte dodge;
	/** 回復値(割合) */
	byte cure;

	Terrain(String s, boolean b, int i0, int i1, int i2, int i3, int i4) {
		name = s;
		canWait = b;
		canMove = (byte) i0;
		cost = (byte) i1;
		guard = (byte) i2;
		dodge = (byte) i3;
		cure = (byte) i4;
	}
}
