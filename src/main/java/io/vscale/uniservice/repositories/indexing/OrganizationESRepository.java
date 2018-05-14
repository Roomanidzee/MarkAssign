package io.vscale.uniservice.repositories.indexing;

import io.vscale.uniservice.domain.Organization;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 19.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface OrganizationESRepository extends ElasticsearchRepository<Organization, Long>{
    List<Organization> findByTitle(String title);
}
