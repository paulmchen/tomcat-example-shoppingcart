package com.service;

import com.model.Authorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import com.model.Customer;
import com.model.User;
import com.model.Authorities;
import com.dao.CustomerDao;

/**
 * Created by bill on 2/6/18.
 */
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired CustomerDao customerDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerDao.getCustomerByemailId(username);
        if(customer == null){
            throw new UsernameNotFoundException("not found");
        }
        User user = customer.getUsers();
        Authorities authorities = customerDao.getAuthoritiesByemailId(user.getEmailId());
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority(authorities.getRoleName()));

        System.err.println("username is " + username + ", " + authorities.getRoleName());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authList);
    }

}