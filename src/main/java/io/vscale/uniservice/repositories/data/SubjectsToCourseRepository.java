package io.vscale.uniservice.repositories.data;

import io.vscale.uniservice.domain.SubjectsToCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 04.05.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface SubjectsToCourseRepository extends JpaRepository<SubjectsToCourse, Byte>{
    SubjectsToCourse findBySubjectName(String subjectName);
}
