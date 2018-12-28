package com.supertek.gates.models.data;

import com.supertek.gates.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface RoleDao extends CrudRepository<Role, Integer> {
}
