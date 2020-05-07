package net.zdsoft.api.base.enums;

public enum OpenapiParamEnum {
	DATA_MODIFY_TIME {
        @Override
        public String getDescription() {
            return "此时间后发生修改的数据，格式yyyyMMddHHmmssSSS";
        }

        @Override
        public String getName() {
            return "dataModifyTime";
        }
    },
    DATA_SORT_MAX_ID {
        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public String getName() {
            return "dataSortMaxId";
        }
    },
    REALTIME {
    	@Override
        public String getDescription() {
            return "";
        }

        @Override
        public String getName() {
            return "realtime";
        }
    },
    LIMIT {
    	@Override
        public String getDescription() {
            return "每页显示最大记录数，默认是100，最大是1000";
        }

        @Override
        public String getName() {
            return "limit";
        }
    },
    CONTAIN_ADMIN {
    	@Override
        public String getDescription() {
            return "是否包含管理员（默认不包含）";
        }

        @Override
        public String getName() {
            return "containAdmin";
        }
    },
    PAGE {
    	@Override
        public String getDescription() {
            return "页数";
        }

        @Override
        public String getName() {
            return "page";
        }
    },
    SHOW_NULL {
    	@Override
        public String getDescription() {
            return "";
        }

        @Override
        public String getName() {
            return "showNull";
        }
    },
    IS_DELETED {
    	@Override
        public String getDescription() {
            return "是否已经被删除。默认：否";
        }

        @Override
        public String getName() {
            return "isDeleted";
        }
    },
    MAX_LIMIT {
    	@Override
        public String getDescription() {
            return "获取接口最大数量";
        }

        @Override
        public String getName() {
            return "maxLimit";
        }
    };
    public abstract String getDescription();

    public abstract String getName();
}
