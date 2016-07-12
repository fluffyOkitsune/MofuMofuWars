package mmw;

/** �}�b�v�̒n�` */
public enum Terrain {
	PLAINS("���n", true, 0b111, 1, 0, 0, 0),
	GRASS("�Ő�", true, 0b111, 1, 0, -10, 0),
	GROVE("��", true, 0b111, 2, 10, -10, 0),
	FOREST("�X", true, 0b100, 1, 0, 0, 0),// ��s�̂�
	BOG("��", true, 0b110, 2, -20, -20, 0),
	MOUNTAIN("�R", true, 0b111, 2, -10, -10, 0),
	
	SEA("�C", true, 0b100, 1, 0, 0, 0),
	SHOAL("��", true, 0b110, 1, -10, -10, 0),
	BEACH("���l", true, 0b111, 1, -10, -10, 0),
	
	// ����
	FRROR("��", true, 0b111, 2, 0, 0, 0),
	WALL("��", false, 0b000, 0, 0, 0, 0),
	LOW_WALL("�Ⴂ��", true, 0b100, 1, 0, 0, 0),// ��s�̂�
	CARPET("�O�~", true, 0b111, 1, 0, -10, 0),
	GATE("��", true, 0b111, 2, 10, -10, 0),
	PILLER("��", true, 0b111, 2, 10, -10, 0),
	RUIN("�p��", true, 0b111, 1, 0, -10, 0),

	// �_���[�W��
	BARRIER("���E", true, 0b000, 0, 0, 0, 20),
	GEYSER("�Ԍ���", true, 0b111, 0, 0, 0, -20),
	VENOM("�ŏ�", true, 0b111, 0, -20, -20, -20),
	;

	/** �\�������n�`�̖��O */
	String name;
	/** �ҋ@�ł��邩 */
	boolean canWait;
	/** �ړ��ł��邩 */
	byte canMove;
	/** �ړ��R�X�g */
	byte cost;
	/** �n�`����(�h��) */
	byte guard;
	/** �n�`����(���) */
	byte dodge;
	/** �񕜒l(����) */
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
