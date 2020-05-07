package net.zdsoft.bigdata.taskScheduler;

/**
 * ETL工具枚举
 * 
 * @author feekang
 *
 */
public enum EtlType {
	KETTLE(1), KYLIN(2), SHELL(3), SPARK(4), JSTORM(5), FLINK(6),PYTHON(7),FLINK_STREAM(8),GROUP(9);

	private int value;

	EtlType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public boolean match(int type) {
		return this.getValue() == type;
	}

	public static EtlType parse(int type) {
		switch (type) {
		case 1:
			return KETTLE;
		case 2:
			return KYLIN;
		case 3:
			return SHELL;
		case 4:
			return SPARK;
		case 5:
			return JSTORM;
		case 6:
			return FLINK;
		case 7:
			return PYTHON;
		case 8:
			return PYTHON;
		case 9:
			return GROUP;
		default:
			return null;
		}
	}
}
