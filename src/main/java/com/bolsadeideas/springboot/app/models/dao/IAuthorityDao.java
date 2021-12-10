package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.Authority;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IAuthorityDao extends PagingAndSortingRepository<Authority, Long> {
}
