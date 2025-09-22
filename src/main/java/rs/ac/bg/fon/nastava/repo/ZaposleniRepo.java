package rs.ac.bg.fon.nastava.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.nastava.model.entity.Zaposleni;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ZaposleniRepo extends JpaRepository<Zaposleni, UUID> {
    Optional<Zaposleni> findByIdAndDeletedAtIsNull(UUID id);
    List<Zaposleni> findByDeletedAtIsNull();
    Optional<Zaposleni> findByJmbgAndDeletedAtIsNull(String jmbg);
    Optional<Zaposleni> findByEmailAndDeletedAtIsNull(String email);
}
