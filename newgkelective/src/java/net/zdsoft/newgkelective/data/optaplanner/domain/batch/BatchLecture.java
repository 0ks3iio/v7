package net.zdsoft.newgkelective.data.optaplanner.domain.batch;

import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;

@XStreamAlias("BatchLecture")
@PlanningEntity
public class BatchLecture extends AbstractPersistable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BatchEntity batchEntity; 
	
	private BatchPeriod period;
	

	public BatchEntity getBatchEntity() {
		return batchEntity;
	}

	public void setBatchEntity(BatchEntity batchEntity) {
		this.batchEntity = batchEntity;
	}

	@PlanningVariable(valueRangeProviderRefs="batchPeriodProvider")
	public BatchPeriod getPeriod() {
		return period;
	}

	@ValueRangeProvider(id="batchPeriodProvider")
	public List<BatchPeriod> getPeriodRange(){
		return batchEntity.getPeriodList();
	}
	
	public void setPeriod(BatchPeriod period) {
		this.period = period;
	}
}
