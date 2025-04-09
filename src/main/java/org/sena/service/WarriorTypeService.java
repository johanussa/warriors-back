package org.sena.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.sena.domain.WarriorType;
import org.sena.helper.exception.WarriorException;
import org.sena.repository.WarriorTypeRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class WarriorTypeService {

    private static final Logger LOG = Logger.getLogger(WarriorTypeService.class);

    @Inject
    WarriorTypeRepository warriorTypeRepository;

    @CacheResult(cacheName = "warrior-type-cache")
    public WarriorType getWarriorTypeById(String warriorTypeId) {

        LOG.infof("@getWarriorTypeById SERV > Inicia consulta del tipo de guerrero con ID: %s", warriorTypeId);

        WarriorType warriorType = getWarriorTypeOptional(warriorTypeId);

        LOG.infof("@getWarriorTypeById SERV > Finaliza consulta. Tipo de guerrero obtenido: %s", warriorType);

        return warriorType;
    }

    @CacheResult(cacheName = "warrior-type-cache")
    public List<WarriorType> getListTypesWarrior() {

        LOG.info("@getListTypesWarrior SERV > Inicia consulta para obtener listado de tipos de guerrero en DB");

        List<WarriorType> warriorTypes = warriorTypeRepository.listAll();

        LOG.infof("@getListTypesWarrior SERV > Finaliza consulta para obtener listado de tipos de guerrero. " +
                "Se obtuvo: %s registro(s) desde la base de datos", warriorTypes.size());

        return warriorTypes;
    }

    @CacheInvalidateAll(cacheName = "warrior-type-cache")
    public void createWarriorType(WarriorType warriorType) {

        LOG.infof("@createWarriorType SERV > Inicia servicio almacenamiento del tipo guerrero: %s.", warriorType);

        validateWarriorTypeRegistry(warriorType.getName());

        LOG.infof("@createWarriorType SERV > Se agrega identificador al tipo guerrero: %s", warriorType.getName());

        warriorType.setIdWarriorType(UUID.randomUUID().toString());
        warriorTypeRepository.persist(warriorType);

        LOG.infof("@createWarriorType SERV > El tipo de guerrero fue almacenado correctamente con el " +
                "identificador: %s", warriorType.getIdWarriorType());
    }

    @CacheInvalidateAll(cacheName = "warrior-type-cache")
    public void updateWarriorType(WarriorType warriorType) {

        LOG.infof("@updateWarriorType SERV > Inicia actualizacion del tipo de guerrero con la data: %s. " +
                "Inicia consulta por el identificador: %s", warriorType, warriorType.getIdWarriorType());

        WarriorType warriorTypeMongo = getWarriorTypeOptional(warriorType.getIdWarriorType());

        LOG.infof("@updateWarriorType SERV > Tipo de guerrero obtenido: %s. Inicia actualizacion de la " +
                "informacion", warriorTypeMongo);

        warriorTypeMongo.setName(warriorType.getName());
        warriorTypeMongo.setDescription(warriorType.getDescription());

        warriorTypeRepository.update(warriorTypeMongo);

        LOG.infof("@updateWarriorType SERV > EL tipo de guerrero se actualizo correctamente con la " +
                "informacion: %s", warriorTypeMongo);
    }

    @CacheInvalidateAll(cacheName = "warrior-type-cache")
    public void deleteWarriorType(String warriorTypeId) {

        LOG.infof("@deleteWarriorType SERV > Inicia eliminacion del tipo de guerrero con ID: %s", warriorTypeId);

        if (!warriorTypeRepository.deleteById(warriorTypeId)) {

            LOG.errorf("@deleteWarriorType SERV > No se encontro el tipo de guerrero con el ID: %s. No se " +
                    "elimino el registro de la base de datos.", warriorTypeId);

            throw new WarriorException(Response.Status.NOT_FOUND, "El tipo de guerrero con identificador: " +
                    warriorTypeId + ", No esta registrado en base de datos. No se realizó la eliminación");
        }

        LOG.infof("@deleteWarriorType SERV > Tipo de guerrero con ID: %s eliminado correctamente.", warriorTypeId);
    }

    private WarriorType getWarriorTypeOptional(String warriorTypeId) {

        return warriorTypeRepository.findByIdOptional(warriorTypeId).orElseThrow(() -> {

            LOG.errorf("@getWarriorTpeOptional SERV > El tipo de guerrero con identificador: %s No se " +
                    "encuentra registrado en base de datos", warriorTypeId);

            return new WarriorException(Response.Status.NOT_FOUND, "El tipo de guerrero con el identificador: " +
                    warriorTypeId + ", No se encuentra registrado. No se permite continuar");
        });
    }

    private void validateWarriorTypeRegistry(String warriorTypeName) {

        LOG.infof("@validateWarriorTypeRegistry SERV > Inicia validacion de existencia del tipo de guerrero " +
                "con el nombre: %s", warriorTypeName);

        warriorTypeRepository.getWarriorTypeByName(warriorTypeName).ifPresent(warriorType -> {

            LOG.errorf("@validateWarriorTypeRegistry SERV > El tipo de guerrero con el nombre: %s ya se " +
                    "encuentra registrado. Solicitud invalidada", warriorType.getName());

            throw new WarriorException(Response.Status.CONFLICT, "El tipo de guerrero con el nombre: " + warriorType
                    .getName() + ", ya se encuentra registrada en base de datos");
        });

        LOG.infof("@validateWarriorTypeRegistry SERV > El tipo de guerrero con el nombre: %s No se encuentra " +
                "registrado, se continua proceso de almacenamiento", warriorTypeName);
    }
}
