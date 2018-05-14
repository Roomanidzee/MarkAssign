package io.vscale.uniservice.services.interfaces.cooperator;

import io.vscale.uniservice.domain.Cooperator;
import io.vscale.uniservice.forms.general.CooperatorForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 11.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
public interface CooperatorService {

    List<Cooperator> getAllCooperators();
    Page<Cooperator> findAll(Pageable pageable);
    Page<Cooperator> retrieveAllCooperatorsAsc(Pageable pageable);
    Page<Cooperator> retrieveAllCooperatorsDesc(Pageable pageable);
    Cooperator getCooperatorById(Long id);

    void addCooperator(CooperatorForm cooperatorForm);
    void updateCooperator(CooperatorForm cooperatorForm);
    void deleteCooperator(CooperatorForm cooperatorForm);
    Long getCooperatorsCount();

}
