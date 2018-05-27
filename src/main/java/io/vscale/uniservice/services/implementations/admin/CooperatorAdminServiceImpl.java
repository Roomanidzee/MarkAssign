package io.vscale.uniservice.services.implementations.admin;

import io.vscale.uniservice.domain.Cooperator;
import io.vscale.uniservice.domain.Profile;
import io.vscale.uniservice.forms.rest.CooperatorRESTForm;
import io.vscale.uniservice.repositories.data.CooperatorRepository;
import io.vscale.uniservice.repositories.data.ProfileRepository;
import io.vscale.uniservice.repositories.indexing.CooperatorESRepository;
import io.vscale.uniservice.services.interfaces.admin.CooperatorAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 17.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Service
public class CooperatorAdminServiceImpl implements CooperatorAdminService{

    private final ProfileRepository profileRepository;
    private final CooperatorRepository cooperatorRepository;
    private final CooperatorESRepository cooperatorESRepository;

    @Autowired
    public CooperatorAdminServiceImpl(ProfileRepository profileRepository, CooperatorRepository cooperatorRepository,
                                      CooperatorESRepository cooperatorESRepository) {
        this.profileRepository = profileRepository;
        this.cooperatorRepository = cooperatorRepository;
        this.cooperatorESRepository = cooperatorESRepository;
    }

    @Override
    public void makeRESTCooperator(CooperatorRESTForm cooperatorRESTForm) {

        Profile profile = this.profileRepository.findOne(cooperatorRESTForm.getProfileId());

        Cooperator cooperator = Cooperator.builder()
                                          .profile(profile)
                                          .recordOfService(cooperatorRESTForm.getRecordOfService())
                                          .appointment(cooperatorRESTForm.getAppointment())
                                          .build();

        this.cooperatorRepository.save(cooperator);
        this.cooperatorESRepository.save(cooperator);

    }

    @Override
    public Page<Cooperator> searchBySurname(String surname, Pageable pageable) {

        List<Cooperator> result = this.cooperatorESRepository.findByProfile_Surname(surname);

        return new PageImpl<>(result, pageable, result.size());
    }
}
