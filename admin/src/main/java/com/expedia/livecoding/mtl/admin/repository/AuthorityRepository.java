package com.expedia.livecoding.mtl.admin.repository;

import com.expedia.livecoding.mtl.admin.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
