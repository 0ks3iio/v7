package net.zdsoft.syncdata.service.impl;

import java.io.Serializable;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.entity.BaseEntity;

public abstract class SymAbstractServiceImpl<T extends BaseEntity<K>, K extends Serializable>
		extends BaseServiceImpl<T, K> {
}
