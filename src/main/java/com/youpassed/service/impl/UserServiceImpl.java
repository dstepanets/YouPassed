package com.youpassed.service.impl;

import com.youpassed.domain.PaginationUtility;
import com.youpassed.domain.User;
import com.youpassed.entity.Role;
import com.youpassed.entity.UserEntity;
import com.youpassed.exception.UserNotFoundException;
import com.youpassed.exception.ValidationException;
import com.youpassed.mapper.UserMapper;
import com.youpassed.repository.UserRepository;
import com.youpassed.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserServiceImpl implements UserService {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private UserMapper userMapper;

/*	@Override
	public void defaultAllUsersPasswords() {
		List<UserEntity> userList = userRepository.findAll();
		final String encodedPass = passwordEncoder.encode("pass");
		for (UserEntity ue : userList) {
			System.out.println(ue);
			ue.setPassword(encodedPass);
			System.out.println(encodedPass);
			userRepository.save(ue);
		}
	}*/

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		defaultAllUsersPasswords();

		final Optional<UserEntity> userEntity = userRepository.findByEmail(username);
		return userEntity
				.map(userMapper::mapEntityToDomain)
				.orElseThrow(() -> new UsernameNotFoundException("User with email '" + username + "' is not found."));
	}

/*	@Override
	public Optional<User> login(String email, String password) {
		final Optional<UserEntity> userEntity = userRepository.findByEmail(email);
		if (userEntity.isPresent()) {
			String encryptedPassword = passwordEncoder.encode(password);
			if (encryptedPassword.equals(userEntity.get().getPassword()))
				return Optional.of(userMapper.mapEntityToDomain(userEntity.get()));
		}
		return Optional.empty();
	}*/

	@Transactional
	@Override
	public User register(User user) throws ValidationException {
		if (!user.getPassword().equals(user.getPassword2())) {
			throw new ValidationException("Passwords don't match");
		}
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new ValidationException("User with this email was registered already");
		}
		final String encryptedPass = passwordEncoder.encode(user.getPassword());

		UserEntity newUserEntity = UserEntity.builder()
				.email(user.getEmail())
				.password(encryptedPass)
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.role(Role.valueOf(user.getRole().name()))
				.build();

		UserEntity userEntity = userRepository.save(newUserEntity);
//		як варіант, id повертати?
		return userMapper.mapEntityToDomain(userEntity);
	}

	@Override
	public User findById(Integer id) throws UserNotFoundException {
		return userRepository.findById(id)
				.map(userMapper::mapEntityToDomainFetchLists)
				.orElseThrow(() -> new UserNotFoundException("User with id [" + id + "] was not found"));
	}

	@Override
	public Page<User> findAll(int pageIndex, int pageSize) {
		pageIndex = PaginationUtility.limitPageIndex(userRepository.count(), pageIndex, pageSize);

		return userRepository.findAll(PageRequest.of(pageIndex, pageSize))
				.map(userMapper::mapEntityToDomain);
	}

	@Override
	@Transactional
	public User updateProfile(User currentUser, User userUpdate) throws ValidationException, UserNotFoundException {
		if (!userUpdate.getEmail().equals(currentUser.getEmail()) &&
				userRepository.findByEmail(userUpdate.getEmail()).isPresent()) {
			throw new ValidationException("User with this email was registered already");
		}

		String encryptedPass = currentUser.getPassword();
		if (!userUpdate.getPassword().isEmpty() && !userUpdate.getPassword2().isEmpty()) {
			if (!userUpdate.getPassword().equals(userUpdate.getPassword2())) {
				throw new ValidationException("Passwords don't match");
			}
			encryptedPass = passwordEncoder.encode(userUpdate.getPassword());
		}
		userUpdate.setPassword(encryptedPass);

		userUpdate.setId(currentUser.getId());
		userUpdate.setRole(currentUser.getRole());

		userRepository.save(userMapper.mapDomainToEntity(userUpdate));
		return userUpdate;
	}

	@Override
	@Transactional
	public User saveStudentWithLists(User student) {
		UserEntity userEntity = userRepository.save(userMapper.mapDomainToEntityWithLists(student));
		return userMapper.mapEntityToDomain(userEntity);
	}

}
