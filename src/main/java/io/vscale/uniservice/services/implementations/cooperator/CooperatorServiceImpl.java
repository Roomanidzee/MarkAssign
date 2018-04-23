package io.vscale.uniservice.services.implementations.cooperator;

import io.vscale.uniservice.domain.Cooperator;
import io.vscale.uniservice.domain.Profile;
import io.vscale.uniservice.forms.general.CooperatorForm;
import io.vscale.uniservice.repositories.data.CooperatorRepository;
import io.vscale.uniservice.repositories.data.ProfileRepository;
import io.vscale.uniservice.repositories.indexing.CooperatorESRepository;
import io.vscale.uniservice.services.interfaces.cooperator.CooperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * 11.03.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
@Service
public class CooperatorServiceImpl implements CooperatorService{

    private final ProfileRepository profileRepository;
    private final CooperatorRepository cooperatorRepository;
    private final CooperatorESRepository cooperatorESRepository;

    @Autowired
    public CooperatorServiceImpl(ProfileRepository profileRepository, CooperatorRepository cooperatorRepository,
                                 CooperatorESRepository cooperatorESRepository) {
        this.profileRepository = profileRepository;
        this.cooperatorRepository = cooperatorRepository;
        this.cooperatorESRepository = cooperatorESRepository;
    }

    @Override
    public List<Cooperator> getAllCooperators() {
        return this.cooperatorRepository.findAll();
    }

    @Override
    public Page<Cooperator> findAll(Pageable pageable) {
        return this.cooperatorRepository.findAll(pageable);
    }

    @Override
    public Page<Cooperator> retrieveAllCooperatorsAsc(Pageable pageable) {

        Long number;

        if(pageable.getPageNumber() == 1){
            number = (long)0;
        }else{
            number = (long) (pageable.getPageNumber() + 3);
        }

        List<Cooperator> cooperators = this.cooperatorRepository.findAllByOrderBySurnameAsc(number);

        return new PageImpl<>(cooperators, pageable, cooperators.size());

    }

    @Override
    public Page<Cooperator> retrieveAllCooperatorsDesc(Pageable pageable) {

        Long number;

        if(pageable.getPageNumber() == 1){
            number = (long)0;
        }else{
            number = (long) (pageable.getPageNumber() + 3);
        }

        List<Cooperator> cooperators = this.cooperatorRepository.findAllByOrderBySurnameDesc(number);

        return new PageImpl<>(cooperators, pageable, cooperators.size());

    }

    @Override
    public void addCooperator(CooperatorForm cooperatorForm) {

        Profile profile = this.profileRepository.findOne(cooperatorForm.getProfileId());

        Cooperator cooperator = Cooperator.builder()
                                          .profile(profile)
                                          .recordOfService(cooperatorForm.getRecordOfService())
                                          .appointment(cooperatorForm.getAppointment())
                                          .cooperatorFiles(new HashSet<>())
                                          .build();

        this.cooperatorRepository.save(cooperator);
        this.cooperatorESRepository.save(cooperator);

    }

    @Override
    public void updateCooperator(CooperatorForm cooperatorForm) {

        Cooperator cooperator = this.cooperatorRepository.findOne(cooperatorForm.getId());
        Profile profile = this.profileRepository.findOne(cooperatorForm.getProfileId());

        cooperator.setProfile(profile);
        cooperator.setAppointment(cooperatorForm.getAppointment());
        cooperator.setRecordOfService(cooperatorForm.getRecordOfService());

        this.cooperatorRepository.save(cooperator);
        this.cooperatorESRepository.save(cooperator);

    }

    @Override
    public void deleteCooperator(CooperatorForm cooperatorForm) {

        this.cooperatorRepository.delete(cooperatorForm.getId());
        this.cooperatorESRepository.delete(String.valueOf(cooperatorForm.getId()));

    }
}
