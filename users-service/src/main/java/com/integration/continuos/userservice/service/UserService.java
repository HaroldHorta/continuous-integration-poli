package com.integration.continuos.userservice.service;

import java.util.List;

import com.integration.continuos.userservice.exception.ErrorResponse;
import com.integration.continuos.userservice.model.User;
import com.integration.continuos.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

	public User get(Long id) {
		return userRepository.findById(id).orElseThrow(()-> new ErrorResponse("User not found"));
	}

	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	public List<User> getBySiteId(Long siteId) {
		return userRepository.findBySiteId(siteId);
	}

	public List<User> getByOrganizationId(Long organizationId) {
		return userRepository.findByOrganizationId(organizationId);
	}
}
