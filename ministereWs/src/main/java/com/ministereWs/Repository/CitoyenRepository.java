package com.ministereWs.Repository;

import com.ministereWs.model.Citoyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitoyenRepository extends JpaRepository<Citoyen, Integer> {

    public Citoyen findByNumAssMal(String numAssMal);

}
