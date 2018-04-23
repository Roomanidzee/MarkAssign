package io.vscale.uniservice.services.implementations.schedule;

import io.vscale.uniservice.domain.Schedule;
import io.vscale.uniservice.repositories.data.ScheduleRepository;
import io.vscale.uniservice.services.interfaces.schedule.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 19.03.2018
 *
 * @author Dias Arkharov
 * @version 1.0
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule findById(Long id) {
        return scheduleRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        scheduleRepository.delete(id);
    }

    @Override
    public void delete(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }

    @Override
    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }
}
