package cn.com.twoke.game.spider_solitaire.enums;

import java.util.HashMap;
import java.util.Map;

public enum PokerNoEnum {
	POKER_A(0),
	POKER_2(1),
	POKER_3(2),
	POKER_4(3),
	POKER_5(4),
	POKER_6(5),
	POKER_7(6),
	POKER_8(7),
	POKER_9(8),
	POKER_10(9),
	POKER_J(10),
	POKER_Q(11),
	POKER_K(12),
	POKER_JOKER(13);
	private final int id;
	
	public static final Map<Integer, PokerNoEnum> PO_MAP ;
	
	static {
		PO_MAP = new HashMap<Integer, PokerNoEnum>();
		for (PokerNoEnum no : values()) {
			PO_MAP.put(no.id, no);
		}
	}

	private PokerNoEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public static PokerNoEnum of(Integer id) {
		return PO_MAP.get(id);
	}
	
}
