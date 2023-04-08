package az.cmammad.lilo.repository;

import az.cmammad.lilo.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("AuthorityRepository")
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Set<Authority> findByName(String authority);
}
