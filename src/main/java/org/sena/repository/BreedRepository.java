package org.sena.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.sena.domain.Breed;

import java.util.Optional;

@ApplicationScoped
public class BreedRepository implements PanacheMongoRepositoryBase<Breed, String> {

    private static final Logger LOG = Logger.getLogger(BreedRepository.class);

    public Optional<Breed> getBreedByName(String breedName) {

        LOG.infof("@getBreedByName REPO > Inicia consulta de la raza por el nombre: %s", breedName);

        return find("name = ?1", breedName).firstResultOptional();
    }
}
