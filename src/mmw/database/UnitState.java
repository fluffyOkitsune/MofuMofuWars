package mmw.database;

/** 状態変化 */
public enum UnitState {
	通常("通常", "健康な状態"), //
	睡眠("睡眠", "ねむねむ 眠ってしまって動けない…"), //
	沈黙("沈黙", "だんまり 魔法と杖が使えない…"), //
	凍結("凍結", "がちがち その場から動けない…"), //
	混乱("混乱", "あれあれ 敵味方関係なく手当たり次第に攻撃してしまう…"), //
	幻惑("幻惑", "ちかちか 攻撃が当たりにくい…"), //
	透明("透明", "すけすけ 敵に気付かれない！"), //
	;
	final String name;
	final String explain;

	UnitState(String s1, String s2) {
		name = s1;
		explain = s2;
	}
}
