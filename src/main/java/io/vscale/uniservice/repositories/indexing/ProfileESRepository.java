package io.vscale.uniservice.repositories.indexing;

import io.vscale.uniservice.domain.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 08.04.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface ProfileESRepository extends ElasticsearchRepository<Profile, Long>{
}
