package gw.linker.repository;

import gw.linker.entity.Element;
import gw.linker.entity.Emc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmcRepository   extends JpaRepository<Emc, Long> {

    Emc findByElement1AndElement2(Element element1, Element element2);
}
