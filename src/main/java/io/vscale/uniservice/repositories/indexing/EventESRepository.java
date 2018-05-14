package io.vscale.uniservice.repositories.indexing;

import io.vscale.uniservice.domain.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 19.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface EventESRepository extends ElasticsearchRepository<Event, Long>{
    List<Event> findByName(String title);
}
