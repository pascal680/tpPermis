package me.pascal.tpPermis.repositories;

import me.pascal.tpPermis.model.Citoyen;
import me.pascal.tpPermis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitoyenRepository extends JpaRepository<Citoyen, Integer> {

    public Citoyen findCitoyenByEmailIgnoreCase(String email);

    public Citoyen findCitoyenByEmailIgnoreCaseAndPassword(String email, String pwd);

    public Citoyen save(Citoyen citoyen);

    public Citoyen findById(int id);

    public Citoyen findByNumAssMal(String numAssMal);

    public void deleteByEmail(String email);






}
