package com.advanceutils.util.genericdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sbansal
 *
 * @param <T>
 */
public abstract class GenericDaoImpl<T> implements GenericDao<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "eptPersistenceUnit")
    private EntityManager em;

    private final Class<T> type;

    public GenericDaoImpl(Class<T> clazz) {
        this.type = clazz;
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public T find(final Object id) throws DatabaseException {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(type);
            Root<T> root = cq.from(type);
            logger.debug("ID :: " + id);
            cq.where(cb.equal(root.get("id"), id));
            CriteriaQuery<T> all = cq.select(root);
            TypedQuery<T> allQuery = getEntityManager().createQuery(all);
            T result = allQuery.getSingleResult();
            return result;
        } catch (QueryTimeoutException qto) {
            throw new DatabaseException(ErrorCode.QUERY_TIMEOUT_ERROR_CODE, qto.getMessage(), qto);
        } catch (PersistenceException pex) {
            throw new DatabaseException(ErrorCode.COMMON_DB_ERROR_CODE, pex.getMessage(), pex);
        }
    }

    @Override
    public List<T> findAll() throws DatabaseException {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(type);
            Root<T> root = cq.from(type);
            CriteriaQuery<T> all = cq.select(root);
            TypedQuery<T> allQuery = getEntityManager().createQuery(all);
            List<T> result = allQuery.getResultList();
            return result;
        } catch (QueryTimeoutException qto) {
            throw new DatabaseException(ErrorCode.QUERY_TIMEOUT_ERROR_CODE, qto);
        } catch (PersistenceException pex) {
            throw new DatabaseException(ErrorCode.COMMON_DB_ERROR_CODE, pex);
        }
    }

    @Override
    public List<T> findAll(Map<String, Object> predicateMap) throws DatabaseException {
    	return this.findAll(predicateMap, null, null);
    }

    @Override
    public List<T> findAll(Map<String, Object> predicateMap, String orderBy, Boolean asc) throws DatabaseException {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(type);
            Root<T> root = cq.from(type);
            if (predicateMap != null) {
                final List<Predicate> predicates = new ArrayList<>();
                for (final String key : predicateMap.keySet()) {
                	if(StringUtils.isNotBlank(key)) {
						final Object value = predicateMap.get(key);
						if (value != null && !(value instanceof List)) {
							predicates.add(cb.equal(root.get(key), value));
						} else if(value != null && value instanceof List) {
							predicates.add(cb.isTrue(root.get(key).in(value)));
						} else if(value == null) {
							predicates.add(cb.isNull(root.get(key)));
						}
					}
                }
                cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
            }
            if (orderBy != null && asc != null) {
                cq.orderBy(asc ? cb.asc(root.get(orderBy)) : cb.desc(root.get(orderBy)));
            }
            CriteriaQuery<T> all = cq.select(root);
            TypedQuery<T> allQuery = getEntityManager().createQuery(all);
            List<T> result = allQuery.getResultList();
            return result;
        } catch (QueryTimeoutException qto) {
            throw new DatabaseException(ErrorCode.QUERY_TIMEOUT_ERROR_CODE, qto);
        } catch (PersistenceException pex) {
            throw new DatabaseException(ErrorCode.COMMON_DB_ERROR_CODE, pex);
        }
    }
    
    /**
     * Generic query for all domains using IN conditions
     * 
     * @author sbansal
     */
    @Override
    public List<T> findAllByIds(final List<String> primaryKeyIds, String idColumnName) throws DatabaseException {
    	List<T> results = null;
    	try {
    		String query = "Select tbl from " + type.getName() + " tbl where tbl." +  idColumnName + " IN :primaryKeyIds";
    		TypedQuery<T> typedQuery = getEntityManager().createQuery(query, type);
    		typedQuery.setParameter("primaryKeyIds", primaryKeyIds);
    		results = typedQuery.getResultList();
    	} catch (NoResultException nre) {
			logger.warn("No rows found as per the search criteria.");
		} catch (QueryTimeoutException qto) {
    		throw new DatabaseException(ErrorCode.QUERY_TIMEOUT_ERROR_CODE, qto);
    	} catch (PersistenceException pex) {
    		throw new DatabaseException(ErrorCode.COMMON_DB_ERROR_CODE, pex);
    	}
		return results;
    }
    
    @Override
    public T find(Map<String, Object> predicateMap) throws DatabaseException {
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(type);
            Root<T> root = cq.from(type);
            if (predicateMap != null) {
                final List<Predicate> predicates = new ArrayList<>();
                for (final String key : predicateMap.keySet()) {
                    final Object value = predicateMap.get(key);
                    if ((key != null) && (value != null)) {
                        predicates.add(cb.equal(root.get(key), value));
                    } else if((key != null)) {
                    	predicates.add(cb.isNull(root.get(key)));
                    }
                }
                cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
            }

            CriteriaQuery<T> all = cq.select(root);
            TypedQuery<T> allQuery = getEntityManager().createQuery(all);
            T result = allQuery.getSingleResult();
            return result;
        } catch (QueryTimeoutException qto) {
            throw new DatabaseException(ErrorCode.QUERY_TIMEOUT_ERROR_CODE, qto);
        } catch (PersistenceException pex) {
            throw new DatabaseException(ErrorCode.COMMON_DB_ERROR_CODE, pex);
        }
    }

    @Override
    public T getReference(final Object id) {
        return this.em.getReference(type, id);

    }
    @Override
    public T create(final T t) throws DatabaseException {
        try {
            this.em.persist(t);
            return t;
        } catch (QueryTimeoutException qto) {
            throw new DatabaseException(ErrorCode.QUERY_TIMEOUT_ERROR_CODE, qto);
        } catch (PersistenceException pex) {
            throw new DatabaseException(ErrorCode.COMMON_DB_ERROR_CODE, pex);
        }
    }

    @Override
    public void delete(final Object id) throws DatabaseException {
        try {
            this.em.remove(this.em.getReference(type, id));
        } catch (QueryTimeoutException qto) {
            throw new DatabaseException(ErrorCode.QUERY_TIMEOUT_ERROR_CODE, qto);
        } catch (PersistenceException pex) {
            throw new DatabaseException(ErrorCode.COMMON_DB_ERROR_CODE, pex);
        }
    }

    @Override
    public T update(final T t) throws DatabaseException {
        try {
            return this.em.merge(t);
        } catch (QueryTimeoutException qto) {
            throw new DatabaseException(ErrorCode.QUERY_TIMEOUT_ERROR_CODE, qto);
        } catch (PersistenceException pex) {
            throw new DatabaseException(ErrorCode.COMMON_DB_ERROR_CODE, pex);
        }
    }

}
