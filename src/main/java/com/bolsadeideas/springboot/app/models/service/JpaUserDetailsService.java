package com.bolsadeideas.springboot.app.models.service;

import com.bolsadeideas.springboot.app.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.app.models.entity.Authority;
import com.bolsadeideas.springboot.app.models.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioDao usuarioDao;

    private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = usuarioDao.findByUsername(username);

        if(user == null){
            logger.error("Error login: no existe el usuario '" + username + "'");
            throw new UsernameNotFoundException("Username " + username + " no existe en el sistema!");
        }

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for(Authority authority : user.getRoles()){
            logger.info("Role: ".concat(authority.getAuthority()));
            authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }

        if(authorities.isEmpty()){
            logger.error("Error login: usuario '" + username + "' no tiene roles asignados!");
            throw new UsernameNotFoundException("Error login: usuario '" + username + "' no tiene roles asignados!");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                        user.getPassword(),
                        user.getEnabled()==1?true:false,
                        true,
                        true,
                        true, authorities);
    }
}
