package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Textblock;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Textblock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TextblockRepository extends JpaRepository<Textblock, Long> {}
