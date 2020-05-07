package net.zdsoft.career.data.enums;


public enum CarTypeEnum {
	S {
		
		@Override
		public String getName() {
			return "社会型：(S)共同特征：喜欢与人交往、不断结交新的朋友、善言谈、愿意教导别人。关心社会问题、渴望发挥自己的社会作用。寻求广泛的人际关系，比较看重社会义务和社会道德。";
		}

		@Override
		public String getValue() {
			return "S";
		}
		
	},
	
	E {
		
		@Override
		public String getName() {
			return "企业型：(E)共同特征：追求权力、权威和物质财富，具有领导才能。喜欢竞争、敢冒风险、有野心、抱负。为人务实，习惯以利益得失，权利、地位、金钱等来衡量做事的价值，做事有较强的目的性。";
		}

		@Override
		public String getValue() {
			return "E";
		}
		
	},

	C {

		@Override
		public String getName() {
			return "常规型：(C)共同特点：尊重权威和规章制度，喜欢按计划办事，细心、有条理，习惯接受他人的指挥和领导，自己不谋求领导职务。喜欢关注实际和细节情况，通常较为谨慎和保守，缺乏创造性，不喜欢冒险和竞争，富有自我牺牲精神。";
		}

		@Override
		public String getValue() {
			return "C";
		}
		
	},
	
	R {

		@Override
		public String getName() {
			return "实际型：(R)共同特点：愿意使用工具从事操作性工作，动手能力强，做事手脚灵活，动作协调。偏好于具体任务，不善言辞，做事保守，较为谦虚。缺乏社交能力，通常喜欢独立做事。";
		}

		@Override
		public String getValue() {
			return "R";
		}
		
	},
	
	I {

		@Override
		public String getName() {
			return "调研型：(I)共同特点：思想家而非实干家,抽象思维能力强，求知欲强，肯动脑，善思考，不愿动手。喜欢独立的和富有创造性的工作。知识渊博，有学识才能，不善于领导他人。考虑问题理性，做事喜欢精确，喜欢逻辑分析和推理，不断探讨未知的领域。";
		}

		@Override
		public String getValue() {
			return "I";
		}
		
	},
	
	A {

		@Override
		public String getName() {
			return "艺术型：(A)共同特点：有创造力，乐于创造新颖、与众不同的成果，渴望表现自己的个性，实现自身的价值。做事理想化，追求完美，不重实际。具有一定的艺术才能和个性。善于表达、怀旧、心态较为复杂。";
		}

		@Override
		public String getValue() {
			return "A";
		}
		
	};
	
	public abstract String getValue();
	
	public abstract String getName();
	
	public static String getName(String value) {
        for (CarTypeEnum type : CarTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getName();
            }
        }
        return null;
    }
	
	
}
