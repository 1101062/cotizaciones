package com.bolsadeideas.springboot.app.models.service;


import com.bolsadeideas.springboot.app.models.dao.IAuthorityDao;
import com.bolsadeideas.springboot.app.models.entity.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Repository("authorityServiceJPA")
public class AuthtorityServiceImpl implements IAuthorityService{

    @Autowired
    private IAuthorityDao authorityDao;

    @Override
    @Transactional(readOnly = true)
    public List<Authority> findAll() {

        return (List<Authority>)authorityDao.findAll();
    }
}
