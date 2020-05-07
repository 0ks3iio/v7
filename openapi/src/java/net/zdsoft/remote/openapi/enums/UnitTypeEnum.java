/* 
 * @(#)UnitTypeEnum.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.enums;

/**
 * @author wulinhao
 * @version $Revision: 1.0 $, $Date: 2017-2-28 上午09:47:11 $
 */
public enum UnitTypeEnum {
	UNIT_EDU_TOP{
		@Override
		public int getValue() {
			return 1;
		}

		@Override
		public String getName() {
			return "顶级教育局";
		}
	},
	UNIT_EDU_SUB{
		@Override
		public int getValue() {
			return 2;
		}

		@Override
		public String getName() {
			return "下属教育局";
		}
	},
	UNIT_SCHOOL_ASP{
		@Override
		public int getValue() {
			return 3;
		}

		@Override
		public String getName() {
			return "托管中小学";
		}
	},
	UNIT_SCHOOL_COLLEGE{
		@Override
		public int getValue() {
			return 4;
		}

		@Override
		public String getName() {
			return "托管大中专学校";
		}
	},
	UNIT_SCHOOL_KINDERGARTEN{
		@Override
		public int getValue() {
			return 5;
		}

		@Override
		public String getName() {
			return "托管幼儿园";
		}
	},
	UNIT_SCHOOL_EISS{
		@Override
		public int getValue() {
			return 6;
		}

		@Override
		public String getName() {
			return "EISS中小学";
		}
	},
	UNIT_SCHOOL_EISV{
		@Override
		public int getValue() {
			return 7;
		}

		@Override
		public String getName() {
			return "EISV大中专学校";
		}
	},
	UNIT_NOTEDU_NOTSCH{
		@Override
		public int getValue() {
			return 8;
		}

		@Override
		public String getName() {
			return "非教育局单位";
		}
	},
	UNIT_HIGH_SCHOOL{
		@Override
		public int getValue() {
			return 9;
		}

		@Override
		public String getName() {
			return "高职校";
		}
	};
//    EDUCATION {
//        @Override
//        public int getValue() {
//            return 1;
//        }
//
//        @Override
//        public String getName() {
//            return "教育局";
//        }
//    },
//    SCHOOL {
//        @Override
//        public int getValue() {
//            return 2;
//        }
//
//        @Override
//        public String getName() {
//            return "学校";
//        }
//    };
    public abstract int getValue();

    public abstract String getName();

    public static String getName(int value) {
        for (UnitTypeEnum type : UnitTypeEnum.values()) {
            if (type.getValue() == value) {
                return type.getName();
            }
        }
        return null;
    }
}
