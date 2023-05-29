package com.integration.continuos.organizationservice.service;

import com.integration.continuos.organizationservice.exception.ErrorResponse;
import com.integration.continuos.organizationservice.model.Organization;
import com.integration.continuos.organizationservice.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }

    public Organization get(Long id) {
        return organizationRepository.findById(id).orElseThrow(() -> new ErrorResponse("Organization not found"));
    }

}
