package vn.ntduycs.javaintern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ntduycs.javaintern.models.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByName(String name);

    Optional<Tag> findByName(String name);

    List<Tag> findByNameIn(Set<String> name);

}
