package com.integration.continuos.siteservice.service;

import com.integration.continuos.siteservice.exception.ErrorResponse;
import com.integration.continuos.siteservice.model.Site;
import com.integration.continuos.siteservice.repository.SiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

	private final SiteRepository siteRepository;

	public SiteService(SiteRepository siteRepository) {
		this.siteRepository = siteRepository;
	}

	public Site save(Site site) {
		return siteRepository.save(site);
	}

	public List<Site> getAll() {
		return siteRepository.findAll();
	}

	public Site get(Long id) {
		return siteRepository.findById(id).orElseThrow(()-> new ErrorResponse("Site not found"));
	}

	public List<Site> getByOrganizationId(Long organizationId) {
		return siteRepository.findByOrganizationId(organizationId);
	}

}
