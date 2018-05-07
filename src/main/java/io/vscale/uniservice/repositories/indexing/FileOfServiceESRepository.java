package io.vscale.uniservice.repositories.indexing;

import io.vscale.uniservice.domain.FileOfService;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 19.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface FileOfServiceESRepository extends ElasticsearchRepository<FileOfService, Long>{
}
