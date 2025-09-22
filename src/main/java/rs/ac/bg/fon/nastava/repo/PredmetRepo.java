package rs.ac.bg.fon.nastava.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.nastava.model.entity.Predmet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PredmetRepo extends JpaRepository<Predmet, UUID> {
    Optional<Predmet> findByIdAndDeletedAtIsNull(UUID id);
    List<Predmet> findByDeletedAtIsNull();
    Optional<Predmet> findBySifraAndDeletedAtIsNull(String sifra);
    Optional<Predmet> findByNazivAndDeletedAtIsNull(String naziv);
}
