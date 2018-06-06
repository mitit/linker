package gw.linker.repository;

import gw.linker.entity.LinkSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkSchemaRepository extends JpaRepository<LinkSchema, Long> {
}
