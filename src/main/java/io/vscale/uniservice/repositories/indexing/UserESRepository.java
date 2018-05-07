package io.vscale.uniservice.repositories.indexing;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import io.vscale.uniservice.domain.User;

/**
 * 08.04.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface UserESRepository extends ElasticsearchRepository<User, Long>{
}
