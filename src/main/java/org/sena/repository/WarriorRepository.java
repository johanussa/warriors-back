package org.sena.repository;

import com.mongodb.client.model.UnwindOptions;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.conversions.Bson;
import org.jboss.logging.Logger;
import org.sena.domain.Warrior;

import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

@ApplicationScoped
public class WarriorRepository implements PanacheMongoRepositoryBase<Warrior, String> {

    private static final Logger LOG = Logger.getLogger(WarriorRepository.class);

    public Optional<Warrior> getWarriorByName(String warriorName) {

        LOG.infof("@getWarriorByName REPO > Inicia consulta del guerrero con el nombre: %s", warriorName);

        return find("name = ?1", warriorName).firstResultOptional();
    }

    public List<Warrior> getWarriorList() {

        LOG.info("@getWarriorList REPO > Inicia obtencion del listado de guerreros registrados en base de datos");

        return findAll().list();
    }

    public Optional<Warrior> getWarriorById(String warriorId) {

        LOG.infof("@getWarriorById REPO > Inicia consulta del guerrero con identificador: %s", warriorId);

        List<Bson> pipeline = List.of(
                match(eq("_id", warriorId)),
                lookup("Breed", "breedId", "_id", "breed"),
                lookup("WarriorType", "warriorTypeId", "_id", "warriorType"),
                lookup("Power", "powersId", "_id", "powers"),
                unwind("$breed", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                unwind("$warriorType", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                project(fields(include("_id", "name", "image", "health", "energy", "breed", "warriorType", "powers")))
        );

        Warrior warrior = mongoCollection().aggregate(pipeline).first();

        return Optional.ofNullable(warrior);
    }
}
