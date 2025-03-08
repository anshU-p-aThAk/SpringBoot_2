package org.spring.project.repository;

import org.spring.project.dto.GreetingDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GreetingRepository extends JpaRepository<GreetingDTO, Long> {
}
