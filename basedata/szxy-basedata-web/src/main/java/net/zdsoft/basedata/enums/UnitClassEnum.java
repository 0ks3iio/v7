package net.zdsoft.basedata.enums;

/**
 * @author yeqi
 * @version $Revision: 1.0 $, $Date: 2017-2-28 上午09:47:11 $
 */
public enum UnitClassEnum {
    EDUCATION {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "教育局";
        }
    },
    SCHOOL {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "学校";
        }

        @Override
        public Integer getIntegerValue() {
            return null;
        }
    };
    public abstract int getValue();

    public Integer getIntegerValue(){
        return getValue();
    }

    public abstract String getName();

    public static String getName(int value) {
        for (UnitClassEnum type : UnitClassEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return null;
    }
}
