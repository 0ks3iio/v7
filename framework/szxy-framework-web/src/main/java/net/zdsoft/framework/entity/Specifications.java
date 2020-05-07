package net.zdsoft.framework.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class Specifications<T> {

	private List<String> eqNames = new ArrayList<String>();
	private List<Object> eqValues = new ArrayList<Object>();
	private List<Class<?>> eqClasses = new ArrayList<Class<?>>();
	private List<String> neqNames = new ArrayList<String>();
	private List<Object> neqValues = new ArrayList<Object>();
	private List<Class<?>> neqClasses = new ArrayList<Class<?>>();
	private List<String> orNames = new ArrayList<String>();
	private List<Object> orValues = new ArrayList<Object>();
	private List<Class<?>> orClasses = new ArrayList<Class<?>>();
	private List<String> orderNames = new ArrayList<String>();
	private List<Class<?>> orderClasses = new ArrayList<Class<?>>();
	private List<String> inNames = new ArrayList<String>();
	private List<Object[]> inValues = new ArrayList<Object[]>();
	private List<Class<?>> inClasses = new ArrayList<Class<?>>();

	public Specifications<T> addEq(String name, Object value, Class<?> clazz) {
		eqNames.add(name);
		eqValues.add(value);
		eqClasses.add(clazz == null ? value == null ? String.class : value.getClass() : clazz);
		return this;
	}
	
	public Specifications<T> addIn(String name, Object[] values, Class<?> clazz) {
		inNames.add(name);
		inValues.add(values);
		inClasses.add(clazz == null ? ArrayUtils.isEmpty(values) ? String.class : values[0].getClass() : clazz);
		return this;
	}

	public Specifications<T> addEq(String name, Object value) {
		return addEq(name, value, null);
	}

	public Specifications<T> addOr(String name, Object value, Class<?> clazz) {
		orNames.add(name);
		orValues.add(value);
		orClasses.add(clazz == null ? value == null ? String.class : value.getClass() : clazz);
		return this;
	}

	public Specifications<T> addOrder(String name, Class<?> clazz) {
		orderNames.add(name);
		orderClasses.add(clazz);
		return this;
	}

	public Specifications<T> addOrderDesc(String name, Class<?> clazz) {
		orderNames.add("_" + name);
		orderClasses.add(clazz);
		return this;
	}

	public Specifications<T> addOr(String name, Object value) {
		return addOr(name, value, null);
	}

	public Specifications<T> addNeq(String name, Object value, Class<?> clazz) {
		neqNames.add(name);
		neqValues.add(value);
		neqClasses.add(clazz == null ? value == null ? String.class : value.getClass() : clazz);
		return this;
	}

	public Specifications<T> addNeq(String name, Object value) {
		return addNeq(name, value, null);
	}

	public Specification<T> getSpecification(boolean autoFillIsDeleted) {
		if (autoFillIsDeleted) {
			eqNames.add("isDeleted");
			eqValues.add(0);
			eqClasses.add(Integer.class);
		}
		Specification<T> s = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				List<Predicate> predicatesOr = new ArrayList<Predicate>();
				
//				cb.createQuery().select(root.get("studentName"));
				
				for (int index = 0; index < eqNames.size(); index++) {
					String name = eqNames.get(index);
					predicates.add(cb.equal(root.get(name).as(eqClasses.get(index)), eqValues.get(index)));
				}
				if (CollectionUtils.isNotEmpty(predicates))
					cq.where(cb.and(predicates.toArray(new Predicate[0])));
				predicates.clear();
				
				for (int index = 0; index < inNames.size(); index++) {
					String name = inNames.get(index);
					predicates.add(root.get(name).as(inClasses.get(index)).in(inValues));
				}
				if (CollectionUtils.isNotEmpty(predicates))
					cq.where(cb.and(predicates.toArray(new Predicate[0])));
				predicates.clear();
				
				for (int index = 0; index < neqNames.size(); index++) {
					String name = neqNames.get(index);
					predicates.add(cb.notEqual(root.get(name).as(neqClasses.get(index)), neqValues.get(index)));
				}
				if (CollectionUtils.isNotEmpty(predicates))
					cq.where(cb.and(predicates.toArray(new Predicate[0])));
				predicates.clear();
				
				for (int index = 0; index < orNames.size(); index++) {
					String name = orNames.get(index);
					predicatesOr.add(cb.equal(root.get(name).as(orClasses.get(index)), orValues.get(index)));
				}
				if (CollectionUtils.isNotEmpty(predicatesOr))
					cq.where(cb.or(predicatesOr.toArray(new Predicate[0])));
				predicatesOr.clear();

				for (int i = 0; i < orderNames.size(); i++) {
					String orderName = orderNames.get(i);
					if (StringUtils.startsWith(orderName, "_")) {
						orderName = StringUtils.substring(orderName, 1);
						cq.orderBy(cb.desc(root.get(orderName).as(orderClasses.get(i))));
					} else {
						cq.orderBy(cb.asc(root.get(orderName).as(Integer.class)));
					}
				}
				return cq.getRestriction();
			}
		};
		return s;
	}

	public Specification<T> getSpecification() {
		return getSpecification(false);
	}

}
