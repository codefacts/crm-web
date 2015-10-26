package io.crm.web.service.callreview.repository;

import io.crm.web.ST;
import io.crm.web.util.Page;
import io.vertx.core.json.JsonObject;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by someone on 21/10/2015.
 */
public class BrCheckerDataRepository {

    private EntityManager em;

    public Page<String> searchClusterName(final JsonObject criteria) {
        int page = criteria.getInteger(ST.page, 1);
        int size = criteria.getInteger(ST.size, 100);
        page = page < 1 ? 1 : page;
        size = size < 1 ? 100 : size;

        final String name = criteria.getString(ST.name, "").toUpperCase() + "%";

        String from = "from BrCheckerData b where UPPER(b.CLUSTER_NAME) like ?";
        final Long total = em.createQuery("select count(distinct b.CLUSTER_NAME) " + from, Long.class)
                .setParameter(1, name)
                .getSingleResult();

        page = page * size >= total ?
                total % size == 0 ? (int) (total / size)
                        : (int) ((total / size) + 1) : page;
        page = page <= 0 ? 0 : (page - 1);

        String jpql = "select distinct b.CLUSTER_NAME " + from + " ORDER BY b.CLUSTER_NAME ASC";
        final TypedQuery<String> query = em.createQuery(jpql, String.class);
        query.setParameter(1, name);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return new Page<String>(query.getResultList(), page, size, total);
    }

    public Page<Object[]> searchTSRCode(JsonObject criteria) {
        int page = criteria.getInteger(ST.page, 1);
        int size = criteria.getInteger(ST.size, 100);
        page = page < 1 ? 1 : page;
        size = size < 1 ? 100 : size;

        final String name = criteria.getString(ST.name, "").toUpperCase() + "%";

        String from = "from BrCheckerData b where UPPER(b.TSR_CODE) like ?";
        final Long total = em.createQuery("select count(distinct b.TSR_CODE) " + from, Long.class)
                .setParameter(1, name)
                .getSingleResult();

        page = page * size >= total ?
                total % size == 0 ? (int) (total / size)
                        : (int) ((total / size) + 1) : page;
        page = page <= 0 ? 0 : (page - 1);

        String jpql = "select distinct b.TSR_CODE, b.TSR_NAME " + from + " ORDER BY b.TSR_CODE ASC";
        final TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        query.setParameter(1, name);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return new Page<>(query.getResultList(), page, size, total);
    }

    public Page<Object[]> searchAuditorCode(JsonObject criteria) {
        int page = criteria.getInteger(ST.page, 1);
        int size = criteria.getInteger(ST.size, 100);
        page = page < 1 ? 1 : page;
        size = size < 1 ? 100 : size;

        final String name = criteria.getString(ST.name, "").toUpperCase() + "%";

        String from = "from BrCheckerData b where UPPER(b.AUDITOR_CODE) like ?";
        final Long total = em.createQuery("select count(distinct b.AUDITOR_CODE) " + from, Long.class)
                .setParameter(1, name)
                .getSingleResult();

        page = page * size >= total ?
                total % size == 0 ? (int) (total / size)
                        : (int) ((total / size) + 1) : page;
        page = page <= 0 ? 0 : (page - 1);

        String jpql = "select distinct b.AUDITOR_CODE, b.AUDITOR_NAME " + from + " ORDER BY b.AUDITOR_CODE ASC";
        final TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
        query.setParameter(1, name);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return new Page<>(query.getResultList(), page, size, total);
    }

    public Page<String> searchConsumerName(JsonObject criteria) {
        int page = criteria.getInteger(ST.page, 1);
        int size = criteria.getInteger(ST.size, 100);
        page = page < 1 ? 1 : page;
        size = size < 1 ? 100 : size;

        final String name = criteria.getString(ST.name, "").toUpperCase() + "%";

        String from = "from BrCheckerData b where UPPER(b.CONSUMER_NAME) like ?";
        final Long total = em.createQuery("select count(distinct b.CONSUMER_NAME) " + from, Long.class)
                .setParameter(1, name)
                .getSingleResult();

        page = page * size >= total ?
                total % size == 0 ? (int) (total / size)
                        : (int) ((total / size) + 1) : page;
        page = page <= 0 ? 0 : (page - 1);

        String jpql = "select distinct b.CONSUMER_NAME " + from + " ORDER BY b.CONSUMER_NAME ASC";
        final TypedQuery<String> query = em.createQuery(jpql, String.class);
        query.setParameter(1, name);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return new Page<String>(query.getResultList(), page, size, total);
    }

    public Page<String> searchConsumerMobile(JsonObject criteria) {
        int page = criteria.getInteger(ST.page, 1);
        int size = criteria.getInteger(ST.size, 100);
        page = page < 1 ? 1 : page;
        size = size < 1 ? 100 : size;

        final String name = criteria.getString(ST.name, "").toUpperCase() + "%";

        String from = "from BrCheckerData b where UPPER(b.CONSUMER_MOBILE_NUMBER) like ?";
        final Long total = em.createQuery("select count(distinct b.CONSUMER_MOBILE_NUMBER) " + from, Long.class)
                .setParameter(1, name)
                .getSingleResult();

        page = page * size >= total ?
                total % size == 0 ? (int) (total / size)
                        : (int) ((total / size) + 1) : page;
        page = page <= 0 ? 0 : (page - 1);

        String jpql = "select distinct b.CONSUMER_MOBILE_NUMBER " + from + " ORDER BY b.CONSUMER_MOBILE_NUMBER ASC";
        final TypedQuery<String> query = em.createQuery(jpql, String.class);
        query.setParameter(1, name);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return new Page<>(query.getResultList(), page, size, total);
    }

    public List<String> findAllCallStatuses() {
        return em.createQuery("select distinct b.CALL_STATUS from BrCheckerData b order by b.CALL_STATUS", String.class)
                .getResultList();
    }
}
