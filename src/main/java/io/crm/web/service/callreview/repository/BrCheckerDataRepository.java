package io.crm.web.service.callreview.repository;

import io.crm.web.service.callreview.entity.BrCheckerData;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by someone on 21/10/2015.
 */
public interface BrCheckerDataRepository extends PagingAndSortingRepository<BrCheckerData, Long>, BrCheckerDataRepositoryCustom {
}
