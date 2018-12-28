package com.supertek.gates.models.data;

import com.supertek.gates.models.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface DepartmentDao extends CrudRepository<Department, Integer> {
}
