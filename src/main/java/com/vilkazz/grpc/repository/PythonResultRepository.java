package com.vilkazz.grpc.repository;

import com.vilkazz.grpc.domain.PythonResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PythonResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PythonResultRepository extends JpaRepository<PythonResult, Long> {

}
