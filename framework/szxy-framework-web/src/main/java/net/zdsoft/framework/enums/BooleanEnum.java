/* 
 * @(#)YesNoEnum.java    Created on 2017年3月2日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.framework.enums;

public enum BooleanEnum {
	TRUE {
		@Override
		public int intValue() {
			return 1;
		}

		@Override
		public String stringValue() {
			return "true";
		}

		@Override
		public boolean booleanValue() {
			return true;
		}
	},
	FALSE {
		@Override
		public int intValue() {
			return 0;
		}

		@Override
		public String stringValue() {
			return "false";
		}

		@Override
		public boolean booleanValue() {
			return false;
		}
	};

	public abstract int intValue();
	
	public abstract String stringValue();
	
	public abstract boolean booleanValue();
}
