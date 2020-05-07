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

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CGUnavailablePeriodPenalty")
public class CGUnavailablePeriodPenaltyHard implements Serializable{

    private CGSectionLecture sectionLecture;
    private CGPeriod period;



    public CGPeriod getPeriod() {
        return period;
    }

    public void setPeriod(CGPeriod period) {
        this.period = period;
    }

    @Override
    public String toString() {
           return sectionLecture.getId() + "@" + period.getPeriodCode();
    }

	public CGSectionLecture getSectionLecture() {
		return sectionLecture;
	}

	public void setSectionLecture(CGSectionLecture sectionLecture) {
		this.sectionLecture = sectionLecture;
	}

}