package rs.ac.bg.fon.nastava.model.dto;

import rs.ac.bg.fon.nastava.model.entity.Zaposleni;

import java.time.LocalDateTime;
import java.util.UUID;

public record ZaposleniDto(
        UUID id,
        String ime,
        String prezime,
        String email,
        LocalDateTime datumZaposlenja
) {

    public static ZaposleniDto from(Zaposleni zaposleni) {
        return new ZaposleniDto(
                zaposleni.getId(),
                zaposleni.getIme(),
                zaposleni.getPrezime(),
                zaposleni.getEmail(),
                zaposleni.getDatumZaposlenja()
        );
    }

    public Zaposleni toEntity() {
        Zaposleni zaposleni = new Zaposleni();
        zaposleni.setId(id);
        zaposleni.setIme(ime);
        zaposleni.setPrezime(prezime);
        zaposleni.setEmail(email);
        zaposleni.setDatumZaposlenja(datumZaposlenja);
        return zaposleni;
    }
}