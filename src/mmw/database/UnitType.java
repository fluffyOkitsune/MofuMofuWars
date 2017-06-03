package mmw.database;

/** 兵種 */
public enum UnitType {
	// おきつね
	剣士("剣士", "剣術に優れ、軽い身のこなしで戦う。\n素早さが高い", WeaponType.剣), //
	剣豪("剣豪", "剣術の達人。器用さと素早さに\n極めて優れる上級職", WeaponType.剣), //
	武士("武士", "剣のほか、弓で離れた相手にも\n攻撃できる上級職", WeaponType.剣, WeaponType.弓), //
	夜叉("夜叉", "戦闘の達人。剣と槍で隙の無い\n攻撃を繰り出す上級職", WeaponType.剣, WeaponType.槍), //
	武者("武者", "力まかせに斧で戦う武道者", WeaponType.斧), //
	豪傑("豪傑", "斧の技を極めた剛の者。\n力と必殺に極めて優れる上級職", WeaponType.斧), //
	巫女("巫女", "杖で味方を助け、\n光魔法で敵を裁く神の使い", WeaponType.光魔法, WeaponType.杖), //
	聖女("聖女", "杖と光魔法のほか\n理魔法をも使う上級職", WeaponType.理魔法, WeaponType.光魔法, WeaponType.杖), //
	邪術師("邪術師", "呪いで敵の動きを妨げることを\n得意とする闇の術師", WeaponType.呪い), //
	陰陽師("陰陽師", "呪いのほか、闇魔法も使いこなす上級職", WeaponType.闇魔法, WeaponType.呪い), //
	忍者("忍者", "素早い動きで敵を仕留め、鍵や扉も開けられる", WeaponType.剣), //
	// おおかみ
	グラディエーター("グラディエーター", "剣で戦う戦闘のプロ。能力のバランスがいい", WeaponType.剣), //
	ヴァルキリー("ヴァルキリー", "華麗なる戦場の華。剣と斧を使い分け、戦闘に臨む上級職", WeaponType.剣, WeaponType.斧), //
	ランサー("ランサー", "槍で戦う一般兵。タフで打たれ強い", WeaponType.槍), //
	パラディン("パラディン", "武勇を誇る聖騎士。剣と槍を使い分け、戦闘に臨む上級職", WeaponType.剣, WeaponType.槍), //
	アーチャー("アーチャー", "弓を使って戦う狩人。器用さに優れる", WeaponType.弓), //
	マージ("マージ", "理の魔法を操る魔法使い。物理攻撃に弱い", WeaponType.理魔法), //
	ソーサラー("ソーサラー", "理・光・闇の全ての魔術を操る上級職", WeaponType.理魔法, WeaponType.光魔法,
			WeaponType.闇魔法), //
	シスター("シスター", "杖で味方を助ける。戦いはできない。", WeaponType.杖), //
	モンク("モンク", "光魔法で敵を裁く。魔防が高い", WeaponType.光魔法), //
	プリースト("プリースト", "杖の他、光魔法を操る上級職"), //
	シーフ("シーフ", "鍵や扉を開けられる盗賊", WeaponType.弓), //
	// 人間
	猟師("猟師", "銃を扱う器用さを持つが非力"), //
	;
	final public String name;
	final public String explain;
	final public WeaponType[] equipWeapon;

	UnitType(String name, String explain, WeaponType... equipWeapon) {
		this.name = name;
		this.explain = explain;
		this.equipWeapon = equipWeapon;
	}
}
