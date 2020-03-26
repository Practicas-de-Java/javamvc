package org.primefaces.showcase.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

@Repository
public interface EntityDao<E> {
	
	void persist(E e) throws Exception;
	
	void merge(E e) throws Exception;

	void remove(Object id) throws Exception;
	
	E findById(Object id) throws Exception;
	
	List<E> findAll() throws Exception;

	List<E> findAllOrderBy(String queryName) throws Exception;

	List<E> findByProperty(String prop, Object val) throws Exception;
	
	List<E> findInRange(int firstResult, int maxResults) throws Exception;
	
	long count() throws Exception;
	
	public Class<E> setEntityClass(Class<E> entityClass);

	public Class<E> getEntityClass();

	List<E> findAllByParametro(String query, String parametro) throws Exception;

	List<E> findAllByBooleano(String query, Boolean parametro) throws Exception;

	List<E> findAllByDate(String query, Date fechaDesde, Date fechaHasta) throws Exception;

}
