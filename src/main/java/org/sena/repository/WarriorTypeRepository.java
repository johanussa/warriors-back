package org.sena.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.sena.domain.WarriorType;

import java.util.Optional;

@ApplicationScoped
public class WarriorTypeRepository implements PanacheMongoRepositoryBase<WarriorType, String> {

    private static final Logger LOG = Logger.getLogger(WarriorTypeRepository.class);

    public Optional<WarriorType> getWarriorTypeByName(String warriorTypeName) {

        LOG.infof("@getWarriorTypeByName REPO > Inicia consulta de tipo guerrero por nombre: %s", warriorTypeName);

        return find("name = ?1", warriorTypeName).firstResultOptional();
    }
}
