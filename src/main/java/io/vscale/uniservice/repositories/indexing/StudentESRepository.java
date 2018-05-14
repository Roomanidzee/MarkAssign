package io.vscale.uniservice.repositories.indexing;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import io.vscale.uniservice.domain.Student;

import java.util.List;

/**
 * 08.04.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface StudentESRepository extends ElasticsearchRepository<Student, Long>{
    List<Student> findByProfile_Surname(String surname);
}
