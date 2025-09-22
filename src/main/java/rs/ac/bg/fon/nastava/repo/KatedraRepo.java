package rs.ac.bg.fon.nastava.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.nastava.model.entity.Katedra;

import java.util.Optional;
import java.util.UUID;

public interface KatedraRepo extends JpaRepository<Katedra, UUID> {
    Optional<Katedra> findBySifra(String sifra);
}
