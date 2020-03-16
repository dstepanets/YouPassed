package com.youpassed.service;import com.youpassed.domain.User;import com.youpassed.exception.ValidationException;import org.springframework.data.domain.Page;import org.springframework.data.jpa.repository.Modifying;import org.springframework.data.jpa.repository.Query;import org.springframework.security.core.userdetails.UserDetailsService;import java.util.Optional;public interface UserService extends UserDetailsService {	User register(User user) throws ValidationException;	User update(User currentUser, User userUpdate) throws ValidationException;	User findById(Integer id) throws ValidationException;	Page<User> findAll(int pageIndex, int pageSize);//	Optional<User> login(String email, String password);//	void defaultAllUsersPasswords();//		PaginalList<User> findAll(String strPageNum);//		int getUsersCount();}