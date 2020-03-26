package org.primefaces.showcase.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javassist.bytecode.SignatureAttribute.TypeVariable;

@Repository
@Transactional
@Scope("application")
public class EntityDaoImpl<E> implements EntityDao<E> {

	@PersistenceContext(unitName = "persistenceUnit")
	protected EntityManager entityManager;

	protected E instance;

	private Class<E> entityClass;

	@Transactional
	public void persist(E e) throws HibernateException {
		getEntityManager().persist(e);
	}

	@Transactional
	public void merge(E e) throws HibernateException {
		getEntityManager().merge(e);
	}

	@Transactional
	public void remove(Object id) throws Exception {
		getEntityManager().remove((E) getEntityManager().find(getEntityClass(), id));
	}

	@Transactional(readOnly = true)
	public E findById(Object id) throws Exception {
		return (E) getEntityManager().find(getEntityClass(), id);
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<E> findAll() throws Exception {
		return getEntityManager()
				.createQuery("Select t from " + getEntityClass().getSimpleName() + " t")
				.getResultList();
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<E> findByProperty(String prop, Object val) throws Exception {
		return (List<E>) getEntityManager()
				.createQuery("select x from " + getEntityClass().getSimpleName() + " x where x." + prop + " = ?1")
				.setParameter(1, val).getResultList();
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<E> findInRange(int firstResult, int maxResults) throws Exception {
		return getEntityManager().createQuery("Select t from " + getEntityClass().getSimpleName() + " t")
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	@Transactional(readOnly = true)
	public long count() throws Exception {
		return (Long) getEntityManager().createQuery("Select count(t) from " + getEntityClass().getSimpleName() + " t")
				.getSingleResult();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) throws Exception {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public Class<E> getEntityClass() {
		if (entityClass == null) {
			Type type = getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) type;
				if (paramType.getActualTypeArguments().length == 2) {
					if (paramType.getActualTypeArguments()[1] instanceof TypeVariable) {
						throw new IllegalArgumentException("Can't find class using reflection");
					} else {
						entityClass = (Class<E>) paramType.getActualTypeArguments()[1];
					}
				} else {
					entityClass = (Class<E>) paramType.getActualTypeArguments()[0];
				}
			} 
		}
		return entityClass;
	}

	public E getInstance() {
		return instance;
	}

	public void setInstance(E instance) {
		this.instance = instance;
	}

	public Class<E> setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
		return this.entityClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAllOrderBy(String queryName) throws Exception {
		Query q = entityManager.createNamedQuery(queryName);
		List<E> resultList = (List<E>)q.getResultList();
		return resultList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAllByParametro(String query, String parametro) throws Exception {
		Query q = entityManager.createQuery(query, entityClass);
		q.setParameter("nombre", parametro);
		List<E> resultList = (List<E>)q.getResultList();
		return resultList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAllByBooleano(String query, Boolean parametro) throws Exception {
		Query q = entityManager.createQuery(query, entityClass);
		q.setParameter("nombre", parametro);
		List<E> resultList = (List<E>)q.getResultList();
		return resultList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAllByDate(String query, Date fechaDesde, Date fechaHasta) throws Exception {
		Query q = entityManager.createQuery(query, entityClass);
		q.setParameter("fechaDesde", fechaDesde);
		q.setParameter("fechaHasta", fechaHasta);		
		List<E> resultList = (List<E>)q.getResultList();
		return resultList;
	}
}
