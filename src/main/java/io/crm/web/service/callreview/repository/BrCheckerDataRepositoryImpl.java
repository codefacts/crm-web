package io.crm.web.service.callreview.repository;

import io.crm.web.ST;
import io.crm.web.service.callreview.entity.BrCheckerData;
import io.crm.web.service.callreview.model.BrCheckerModel;
import io.vertx.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by someone on 21/10/2015.
 */
public class BrCheckerDataRepositoryImpl implements BrCheckerDataRepositoryCustom {
    @Autowired
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

        return new PageImpl<String>(query.getResultList(), new PageRequest(page, size), total);
    }
}
