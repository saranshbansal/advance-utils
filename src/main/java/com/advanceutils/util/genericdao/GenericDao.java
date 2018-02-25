package com.advanceutils.util.genericdao;

import java.util.List;
import java.util.Map;

/**
 * @author sbansal
 *
 * @param <T>
 */
public interface GenericDao<T> {

    /**
     * 
     * @param id
     *            id
     * @return T T
     */
    T find(Object id) throws DatabaseException;
    
    /**
     * 
     * @param Map
     *            predicateMap
     * @return T T
     */
    T find(Map<String, Object> predicateMap) throws DatabaseException;
    
    /**
     * 
     * @return List<T>
     */
    List<T> findAll() throws DatabaseException;

    /**
     * 
     * @param t
     *            t
     * @return T T
     */
    T create(T t) throws DatabaseException;

    /**
     * 
     * @param id
     *            id
     */
    void delete(Object id) throws DatabaseException;

    /**
     * 
     * @param t
     *            t
     * @return T T
     */
    T update(T t) throws DatabaseException;

    /**
     * 
     * @param predicateMap
     *            predicateMap
     * @return List<T> List<T>
     */
    List<T> findAll(Map<String, Object> predicateMap) throws DatabaseException;

    /**
     * 
     * @param id
     *            id
     * @return T T
     */
    T getReference(Object id);

    List<T> findAll(Map<String, Object> predicateMap, String orderBy, Boolean asc) throws DatabaseException;

    List<T> findAllByIds(List<String> id, String idColumnName) throws DatabaseException;

}
