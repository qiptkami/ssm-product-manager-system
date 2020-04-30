package com.yiqiandewo.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface IRoleService extends UserDetailsService {

    boolean changePassword(String username, String password, String newPassword);
}
