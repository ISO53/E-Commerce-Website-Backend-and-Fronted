package com.perfume.perfume.repository;

import com.perfume.perfume.model.MakeUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ISO53
 */
@Repository
public interface MakeUpRepository extends JpaRepository<MakeUp, Integer>{
}
