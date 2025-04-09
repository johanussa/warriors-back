package org.sena.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.sena.domain.Power;

import java.util.Optional;

@ApplicationScoped
public class PowerRepository implements PanacheMongoRepositoryBase<Power, String> {

    private static final Logger LOG = Logger.getLogger(PowerRepository.class);

    public Optional<Power> getPowerByName(String powerName) {

        LOG.infof("@getPowerByName REPO > Inicia consulta del poder por el nombre: %s", powerName);

        return find("name = ?1", powerName).firstResultOptional();
    }
}
