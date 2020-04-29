package com.yiqiandewo.service.impl;

import com.yiqiandewo.dao.IRoleDao;
import com.yiqiandewo.domain.Role;
import com.yiqiandewo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IRoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Role role = roleDao.findByUsername(username);
        User user = new User(role.getUsername(), "{noop}"+role.getPassword(), getAuthority(role));
        return user;
    }

    public List<SimpleGrantedAuthority> getAuthority(Role role) {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(role.getType()));

        return list;
    }

}
