package rs.ac.bg.fon.nastava.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.nastava.model.entity.Angazovanje;
import rs.ac.bg.fon.nastava.model.entity.OblikNastave;

import java.util.List;
import java.util.UUID;

public interface AngazovanjeRepo extends JpaRepository<Angazovanje, UUID> {
    List<Angazovanje> findByPredmet_Id(UUID predmetId);

    List<Angazovanje> findByZaposleni_Id(UUID zaposleniId);

    List<Angazovanje> findByZaposleni_IdAndPredmet_IdAndUloga(UUID zaposleniId, UUID predmetId, OblikNastave uloga);
}
