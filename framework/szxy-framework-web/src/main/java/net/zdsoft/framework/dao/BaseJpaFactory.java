package net.zdsoft.framework.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import net.zdsoft.framework.entity.BaseEntity;

public class BaseJpaFactory<R extends JpaRepository<T, I>, T extends BaseEntity<I>, I extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, I> {

	public BaseJpaFactory(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	@SuppressWarnings("rawtypes")
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
		return new CustomRepositoryFactory(em);
	}

	private static class CustomRepositoryFactory<T extends BaseEntity<I>, I extends Serializable> extends JpaRepositoryFactory {

		private final EntityManager em;

		public CustomRepositoryFactory(EntityManager em) {
			super(em);
			this.em = em;
		}

		@SuppressWarnings({ "unused", "unchecked" })
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new BaseJpaRepositoryDaoImpl<T, I>((Class<T>) metadata.getDomainType(), em);
		}

		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return BaseJpaRepositoryDaoImpl.class;
		}
	}

}
