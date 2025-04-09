package org.sena.service;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.sena.domain.Power;
import org.sena.helper.exception.WarriorException;
import org.sena.repository.PowerRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PowerService {

    private static final Logger LOG = Logger.getLogger(PowerService.class);

    @Inject
    PowerRepository powerRepository;

    @CacheResult(cacheName = "power-cache")
    public List<Power> getPowersList() {

        LOG.info("@getPowersList SERV > Inicia consulta para obtener listado de poderes en base de datos");

        List<Power> powerList = powerRepository.listAll();

        LOG.infof("@getPowersList SERV > Finaliza consulta para obtener listado de poderes. Se obtuvo: " +
                "%s registro(s)", powerList.size());

        return powerList;
    }

    @CacheResult(cacheName = "power-cache")
    public Power getPowerById(String powerId) {

        LOG.infof("@getPowerById SERV > Inicia consulta del poder con identificador: %s", powerId);

        Power power = getPowerOptional(powerId);

        LOG.infof("@getPowerById SERV > Finaliza consulta. Poder obtenido: %s", power);

        return power;
    }

    @CacheInvalidateAll(cacheName = "power-cache")
    public void createPower(Power power) {

        LOG.infof("@createPower SERV > Inicia servicio de almacenamiento del poder: %s.", power);

        validatePowerRegistry(power.getName());

        LOG.infof("@createPower SERV > Se agrega identificador al poder con nombre: %s", power.getName());

        power.setIdPower(UUID.randomUUID().toString());
        powerRepository.persist(power);

        LOG.infof("@createPower SERV > El poder fue almacenado correctamente con el ID: %s", power.getIdPower());
    }

    @CacheInvalidateAll(cacheName = "power-cache")
    public void updatePower(Power power) {

        LOG.infof("@updatePower SERV > Inicia actualizacion del poder con la data: %s. Inicia consulta por " +
                "el identificador: %s", power, power.getIdPower());

        Power powerMongo = getPowerOptional(power.getIdPower());

        LOG.infof("@updatePower SERV > Poder obtenido: %s. Inicia actualizacion de la informacion", powerMongo);

        powerMongo.setName(power.getName());
        powerMongo.setDamage(power.getDamage());
        powerMongo.setEffect(power.getEffect());
        powerMongo.setDescription(power.getDescription());
        powerMongo.setPowerEnergyConsumed(power.getPowerEnergyConsumed());

        powerRepository.update(powerMongo);

        LOG.infof("@updatePower SERV > El poder se actualizo correctamente con la informacion: %s", powerMongo);
    }

    @CacheInvalidateAll(cacheName = "power-cache")
    public void deletePowerById(String powerId) {

        LOG.infof("@deletePowerById SERV > Inicia eliminacion del poder con identificador: %s", powerId);

        if (!powerRepository.deleteById(powerId)) {

            LOG.errorf("@deletePowerById SERV > No se encontro el poder con el ID: %s. No se elimino el " +
                    "registro de la base de datos.", powerId);

            throw new WarriorException(Response.Status.NOT_FOUND, "El poder con identificador: " + powerId +
                    ", No esta registrado en base de datos. No se realizó la eliminación");
        }

        LOG.infof("@deletePowerById SERV > El poder con ID: %s fue eliminado correctamente.", powerId);
    }

    private Power getPowerOptional(String powerId) {

        return powerRepository.findByIdOptional(powerId).orElseThrow(() -> {

            LOG.errorf("@getPowerOptional SERV > El poder con ID: %s No se encuentra registrado", powerId);

            return new WarriorException(Response.Status.NOT_FOUND, "El poder con el identificador: " + powerId +
                    ", No se encuentra registrado. No se permite continuar");
        });
    }

    private void validatePowerRegistry(String powerName) {

        LOG.infof("@validatePowerRegistry SERV > Inicia validacion de registro de poder con nombre: %s", powerName);

        powerRepository.getPowerByName(powerName).ifPresent(power -> {

            LOG.errorf("@validatePowerRegistry SERV > El poder con el nombre: %s ya se encuentra registrado. " +
                    "Solicitud invalida", power.getName());

            throw new WarriorException(Response.Status.CONFLICT, "El poder con el nombre: " + power.getName() +
                    ", ya se encuentra registrado en base de datos");
        });

        LOG.infof("@validatePowerRegistry SERV > El poder con el nombre: %s No se encuentra registrado, se " +
                "continua proceso de almacenamiento", powerName);
    }
}
