package gw.linker.repository;

import gw.linker.entity.Pcb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PcbRepository  extends JpaRepository<Pcb, Long> {
}
