/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;

@XStreamAlias("CGTeacher")
public class CGTeacher  extends AbstractPersistable{
	private static final long serialVersionUID = 1L;

	private String code;//id
	private String teacherName;//id
	// 教师互斥课时数
	private int mutexNum;
	
	private String weekTimeArrange;//周课时分布，1-均衡，2-集中
	private String dayTimeArrange;//每天课时分配，1-不限，2-同半天，3-同半天连续，4-同半天间隔
    
    public CGTeacher() {
	}
    public CGTeacher(String teacherCode) {
    	this.code = teacherCode;
    }
   

	public CGTeacher(String code, int mutexNum, String weekTimeArrange, String dayTimeArrange) {
		super();
		this.code = code;
		this.mutexNum = mutexNum;
		if(weekTimeArrange == null) {
			weekTimeArrange = "1";
		}
		this.weekTimeArrange = weekTimeArrange;
		if(dayTimeArrange == null) {
			dayTimeArrange = "1";
		}
		this.dayTimeArrange = dayTimeArrange;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }

	public int getMutexNum() {
		return mutexNum;
	}

	public void setMutexNum(int mutxNum) {
		this.mutexNum = mutxNum;
	}

	public String getWeekTimeArrange() {
		return weekTimeArrange;
	}

	public void setWeekTimeArrange(String weekTimeArrange) {
		this.weekTimeArrange = weekTimeArrange;
	}

	public String getDayTimeArrange() {
		return dayTimeArrange;
	}

	public void setDayTimeArrange(String dayTimeArrange) {
		this.dayTimeArrange = dayTimeArrange;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
}
