package io.vscale.uniservice.loaders;

import io.vscale.uniservice.domain.*;
import io.vscale.uniservice.repositories.data.*;
import io.vscale.uniservice.repositories.indexing.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 04.05.2018
 *
 * @author Andrey Romanov
 * @version 1.0
 */
//@Component
@Slf4j
//@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DataLoader implements CommandLineRunner{

    private ConfirmationESRepository confirmationESRepository;
    private CooperatorESRepository cooperatorESRepository;
    private EventESRepository eventESRepository;
    private FileOfServiceESRepository fileOfServiceESRepository;
    private OrganizationESRepository organizationESRepository;
    private ProfileESRepository profileESRepository;
    private StudentESRepository studentESRepository;
    private UserESRepository userESRepository;

    private ElasticsearchTemplate esTemplate;

    private ConfirmationRepository confirmationRepository;
    private CooperatorRepository cooperatorRepository;
    private EventRepository eventRepository;
    private FileOfServiceRepository fileOfServiceRepository;
    private OrganizationRepository organizationRepository;
    private ProfileRepository profileRepository;
    private StudentRepository studentRepository;
    private UserRepository userRepository;

    @Override
    public void run(String... strings){

        log.info("Starting to upload data from PostgreSQL to ElasticSearch");

        log.info("Work with " + Confirmation.class.toString());

        this.esTemplate.deleteIndex(Confirmation.class);
        this.esTemplate.createIndex(Confirmation.class);
        this.esTemplate.putMapping(Confirmation.class);
        this.esTemplate.refresh(Confirmation.class);

        log.info("Starting upload data for "+ Confirmation.class.toString());

        List<Confirmation> confirmations = this.confirmationRepository.findAll();

        confirmations.stream()
                     .filter(confirmation -> this.confirmationESRepository.findOne(confirmation.getId()) == null)
                     .forEach(confirmation -> {
                         log.info("Uploading to ElasticSearch: " + confirmation.toString());
                         this.confirmationESRepository.save(confirmation);
                     });

       /* log.info("Work with " + Cooperator.class.toString());

        this.esTemplate.deleteIndex(Cooperator.class);
        this.esTemplate.createIndex(Cooperator.class);
        this.esTemplate.putMapping(Cooperator.class);
        this.esTemplate.refresh(Cooperator.class);

        log.info("Starting upload data for "+ Cooperator.class.toString());

        List<Cooperator> cooperators = this.cooperatorRepository.findAll();

        List<Cooperator> newCooperators = cooperators.stream()
                                                     .filter(cooperator -> this.cooperatorESRepository.findOne(cooperator.getId()) == null)
                                                     .collect(Collectors.toList());

        this.cooperatorESRepository.save(newCooperators);
*/
        log.info("Work with " + Event.class.toString());

        this.esTemplate.deleteIndex(Event.class);
        this.esTemplate.createIndex(Event.class);
        this.esTemplate.putMapping(Event.class);
        this.esTemplate.refresh(Event.class);

        log.info("Starting upload data for "+ Event.class.toString());

        List<Event> events = this.eventRepository.findAll();

        events.stream()
              .filter(event -> this.eventESRepository.findOne(event.getId()) == null)
              .forEach(event -> {
                  log.info("Uploading to ElasticSearch: " + event.toString());
                  this.eventESRepository.save(event);
              });

        log.info("Work with " + FileOfService.class.toString());

        this.esTemplate.deleteIndex(FileOfService.class);
        this.esTemplate.createIndex(FileOfService.class);
        this.esTemplate.putMapping(FileOfService.class);
        this.esTemplate.refresh(FileOfService.class);

        log.info("Starting upload data for "+ FileOfService.class.toString());

        List<FileOfService> files = this.fileOfServiceRepository.findAll();

        files.stream()
             .filter(file -> this.fileOfServiceESRepository.findOne(file.getId()) == null)
             .forEach(file -> {
                 log.info("Uploading to ElasticSearch: " + file.toString());
                 this.fileOfServiceESRepository.save(file);
             });

        /*log.info("Work with " + Organization.class.toString());

        this.esTemplate.deleteIndex(Organization.class);
        this.esTemplate.createIndex(Organization.class);
        this.esTemplate.putMapping(Organization.class);
        this.esTemplate.refresh(Organization.class);

        log.info("Starting upload data for "+ Organization.class.toString());

        List<Organization> organizations = this.organizationRepository.findAll();

        organizations.stream()
                     .filter(organization -> this.organizationESRepository.findOne(organization.getId()) == null)
                     .forEach(organization -> {
                         log.info("Uploading to ElasticSearch: " + organization.toString());
                         this.organizationESRepository.save(organization);
                     });*/

        /*log.info("Work with " + Profile.class.toString());

        this.esTemplate.deleteIndex(Profile.class);
        this.esTemplate.createIndex(Profile.class);
        this.esTemplate.putMapping(Profile.class);
        this.esTemplate.refresh(Profile.class);

        log.info("Starting upload data for "+ Profile.class.toString());

        List<Profile> profiles = this.profileRepository.findAll();

        profiles.stream()
                .filter(profile -> this.profileESRepository.findOne(profile.getId()) == null)
                .forEach(profile -> {
                    log.info("Uploading to ElasticSearch: " + profile.toString());
                    this.profileESRepository.save(profile);
                });

        log.info("Work with " + Student.class.toString());

        this.esTemplate.deleteIndex(Student.class);
        this.esTemplate.createIndex(Student.class);
        this.esTemplate.putMapping(Student.class);
        this.esTemplate.refresh(Student.class);

        log.info("Starting upload data for "+ Student.class.toString());

        List<Student> students = this.studentRepository.findAll();

        students.stream()
                .filter(student -> this.studentESRepository.findOne(student.getId()) == null)
                .forEach(student -> {
                    log.info("Uploading to ElasticSearch: " + student.toString());
                    this.studentESRepository.save(student);
                });

        log.info("Work with " + User.class.toString());*/

        /*this.esTemplate.deleteIndex(User.class);
        this.esTemplate.createIndex(User.class);
        this.esTemplate.putMapping(User.class);
        this.esTemplate.refresh(User.class);

        log.info("Starting upload data for "+ User.class.toString());

        List<User> users = this.userRepository.findAll();

        users.stream()
             .filter(user -> this.userESRepository.findOne(user.getId()) == null)
             .forEach(user -> {
                 log.info("Uploading to ElasticSearch: " + user.toString());
                 this.userESRepository.save(user);
             });
*/
    }
}
