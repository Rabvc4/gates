package com.supertek.gates.models.data;

import com.supertek.gates.models.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface EmployeeDao extends CrudRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e WHERE CONCAT( e.firstName, ' ', e.lastName ) LIKE CONCAT('%',:name,'%')")
    List<Employee> findByNameLike(String name);

    @Query("SELECT e FROM Employee e WHERE CONCAT( e.firstName, ' ', e.lastName ) LIKE CONCAT('%',:name,'%') AND NOT id = :id")
    List<Employee> findByNameLike(String name, int id);
}
