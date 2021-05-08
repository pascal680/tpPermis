package me.pascal.tpPermis.repositories;

import me.pascal.tpPermis.model.Permis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PermisRepository extends JpaRepository<Permis, Integer> {

    List<Permis> findByDateExpirationPermis(LocalDate date);

    List<Permis> findByDateExpirationPermisBefore(LocalDate date);

    List<Permis> findByTypePermisIgnoreCase(String typePermis);

    Permis getOne(Integer idPermis);

    public List<Permis> findAll();


}
