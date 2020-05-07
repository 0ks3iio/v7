package net.zdsoft.familydear.entity;

import net.zdsoft.framework.entity.BaseEntity;

public class FamdearVillage extends BaseEntity<String> {
    @Override
    public String fetchCacheEntitName() {
        return "famdearVillage";
    }
}
