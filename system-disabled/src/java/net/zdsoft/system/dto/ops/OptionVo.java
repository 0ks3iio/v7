package net.zdsoft.system.dto.ops;

/**
 * deploy 参数vo
 * @author ke_shen@126.com
 * @since 2017/12/22 上午9:50
 */
public class OptionVo {

	private String code;
	private String name;
	private String value;
	private String description;
	private int ini;
	private int valueType;

	public OptionVo() {

	}

	public OptionVo(Builder builder) {
		setCode(builder.code);
		setName(builder.name);
		setValue(builder.value);
		setDescription(builder.description);
		setIni(builder.ini);
		setValueType(builder.valueType);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIni() {
		return ini;
	}

	public void setIni(int ini) {
		this.ini = ini;
	}

	public int getValueType() {
		return valueType;
	}

	public void setValueType(int valueType) {
		this.valueType = valueType;
	}

	public static final class Builder {
		private String code;
		private String name;
		private String value;
		private String description;
		private int ini;
		private int valueType;

		private Builder() {
		}

		public Builder code(String val) {
			code = val;
			return this;
		}

		public Builder name(String val) {
			name = val;
			return this;
		}

		public Builder value(String val) {
			value = val;
			return this;
		}

		public Builder description(String val) {
			description = val;
			return this;
		}

		public OptionVo build() {
			return new OptionVo(this);
		}

		public Builder setIni(int ini) {
			this.ini = ini;
			return this;
		}

		public Builder setValueType(int valueType) {
			this.valueType = valueType;
			return this;
		}
	}
}
