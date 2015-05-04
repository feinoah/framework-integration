package com.chttl.jee.dao.spi;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.chttl.jee.dao.util.DaoUtil;

@SuppressWarnings({"unchecked","unused","rawtypes"})
public class JpaQuery implements Query{

	private Query query ;
	private String queryString ;
	private DaoUtil util = new DaoUtil() ;
	
	public JpaQuery(Query query,String queryString){
		this.query = query ;
		this.queryString = queryString ;
	}

	public List getResultList() {
		return util.resultList(query.getResultList());
	}

	public Object getSingleResult() {
		return util.result(query.getSingleResult());
	}

	public int executeUpdate() {
		return query.executeUpdate();
	}

	public Query setMaxResults(int maxResult) {
		return query.setMaxResults(maxResult);
	}

	public int getMaxResults() {
		return query.getMaxResults();
	}

	public Query setFirstResult(int startPosition) {
		return query.setFirstResult(startPosition);
	}

	public int getFirstResult() {
		return query.getFirstResult();
	}

	public Query setHint(String hintName, Object value) {
		return query.setHint(hintName, value);
	}

	public Map<String, Object> getHints() {
		return query.getHints();
	}

	public <T> Query setParameter(Parameter<T> param, T value) {
		return query.setParameter(param, value);
	}

	public Query setParameter(Parameter<Calendar> param, Calendar value,
			TemporalType temporalType) {
		return query.setParameter(param, value, temporalType);
	}

	public Query setParameter(Parameter<Date> param, Date value,
			TemporalType temporalType) {
		return query.setParameter(param, value, temporalType);
	}

	public Query setParameter(String name, Object value) {
		return query.setParameter(name, value);
	}

	public Query setParameter(String name, Calendar value,
			TemporalType temporalType) {
		return query.setParameter(name, value, temporalType);
	}

	public Query setParameter(String name, Date value, TemporalType temporalType) {
		return query.setParameter(name, value, temporalType);
	}

	public Query setParameter(int position, Object value) {
		return query.setParameter(position, value);
	}

	public Query setParameter(int position, Calendar value,
			TemporalType temporalType) {
		return query.setParameter(position, value, temporalType);
	}

	public Query setParameter(int position, Date value,
			TemporalType temporalType) {
		return query.setParameter(position, value, temporalType);
	}

	public Set<Parameter<?>> getParameters() {
		return query.getParameters();
	}

	public Parameter<?> getParameter(String name) {
		return query.getParameter(name);
	}

	public <T> Parameter<T> getParameter(String name, Class<T> type) {
		return query.getParameter(name, type);
	}

	public Parameter<?> getParameter(int position) {
		return query.getParameter(position);
	}

	public <T> Parameter<T> getParameter(int position, Class<T> type) {
		return query.getParameter(position, type);
	}

	public boolean isBound(Parameter<?> param) {
		return query.isBound(param);
	}

	public <T> T getParameterValue(Parameter<T> param) {
		return query.getParameterValue(param);
	}

	public Object getParameterValue(String name) {
		return query.getParameterValue(name);
	}

	public Object getParameterValue(int position) {
		return query.getParameterValue(position);
	}

	public Query setFlushMode(FlushModeType flushMode) {
		return query.setFlushMode(flushMode);
	}

	public FlushModeType getFlushMode() {
		return query.getFlushMode();
	}

	public Query setLockMode(LockModeType lockMode) {
		return query.setLockMode(lockMode);
	}

	public LockModeType getLockMode() {
		return query.getLockMode();
	}

	public <T> T unwrap(Class<T> cls) {
		return query.unwrap(cls);
	}
	
}