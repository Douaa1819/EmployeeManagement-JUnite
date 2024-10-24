package org.employeemanagement.service;

import org.employeemanagement.entities.Application;
import org.employeemanagement.repository.interfaces.ApplicationRepository;
import org.employeemanagement.service.interfaces.ApplicationService;
import java.util.List;

public class ApplicationServiceImpl implements ApplicationService {
    private ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Application save(Application application) {
       return applicationRepository.save(application);
    }
    @Override
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }
    @Override
    public List<String> findAllSkills() {
        return applicationRepository.findAllSkills();
    }
    @Override
    public Application findById(Long id) {
        return applicationRepository.findById(id);
    }

    @Override
    public List<Application> findDistinctByCompetencies(String competency) {
        return applicationRepository.findDistinctByCompetencies(competency);
    }
}
