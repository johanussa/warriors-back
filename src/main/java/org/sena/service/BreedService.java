package org.sena.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.sena.domain.Breed;
import org.sena.helper.exception.WarriorException;
import org.sena.repository.BreedRepository;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BreedService {

    private static final Logger LOG = Logger.getLogger(BreedService.class);

    @Inject
    BreedRepository breedRepository;

    @CacheResult(cacheName = "breed-cache")
    public Breed getBreedById(String idBreed) {

        LOG.infof("@getBreedById SERV > Inicia consulta de la raza con el identificador: %s", idBreed);

        Breed breed = getBreedByIdOptional(idBreed);

        LOG.infof("@getBreedById SERV > Finaliza consultar de raza con id: %s. Raza obtenida: %s", idBreed, breed);

        return breed;
    }

    @CacheResult(cacheName = "breed-cache")
    public List<Breed> getBreedList() {

        LOG.info("@getBreedList SERV > Inicia ejecucion de consulta para obtener el listado de razas en base de datos");

        List<Breed> breeds = breedRepository.listAll();

        LOG.infof("@getBreedList SERV > Consulta finalizada. Se obtuvo: %s elemento(s) en la lista", breeds.size());

        return breeds;
    }

    @CacheInvalidateAll(cacheName = "breed-cache")
    public void createBreed(Breed breed) {

        LOG.infof("@createBreed SERV > Inicia servicio de almacenamiento de raza: %s en base de datos.", breed);

        validateBreedRegistry(breed.getName());

        LOG.infof("@createBreed SERV > Se agrega el identificador a la raza: %s", breed.getName());

        breed.setIdBreed(UUID.randomUUID().toString());
        breedRepository.persist(breed);

        LOG.infof("@createBreed SERV > Raza almacenada correctamente con ID: %s", breed.getIdBreed());
    }

    @CacheInvalidateAll(cacheName = "breed-cache")
    public void updateBreed(Breed breed) {

        LOG.infof("@updateBreed SERV > Inicia servicio para actualizar la raza con la data: %s. Inicia " +
                "consulta de la raza con el identificador: %s", breed, breed.getIdBreed());

        Breed breedMongo = getBreedByIdOptional(breed.getIdBreed());

        LOG.infof("@updateBreed SERV > Raza encontrada: %s. Inicia actualizacion de la informacion", breedMongo);

        breedMongo.setName(breed.getName());
        breedMongo.setDescription(breed.getDescription());
        breedMongo.setResistance(breed.getResistance());

        breedRepository.update(breedMongo);

        LOG.infof("@updateBreed SERV > La raza se actualizo correctamente con la informacion: %s", breedMongo);
    }

    @CacheInvalidateAll(cacheName = "breed-cache")
    public void deleteBreed(String idBreed) {

        LOG.infof("@deleteBreed SERV > Inicia eliminacion de raza con identificador: %s", idBreed);

        if (!breedRepository.deleteById(idBreed)) {

            LOG.errorf("@deleteBreed SERV > No se encontro la raza con identificador: %s. No se elimino el " +
                    "registro de la base de datos.", idBreed);

            throw new WarriorException(Response.Status.NOT_FOUND, "La raza con identificador: " + idBreed + ", No " +
                    "esta registrada en base de datos. No se realizó la eliminación");
        }

        LOG.infof("@deleteBreed SERV > Raza con identificador: %s eliminada correctamente.", idBreed);
    }

    private void validateBreedRegistry(String breedName) {

        LOG.infof("@validateBreedRegistry SERV > Inicia verificacion de existencia raza con nombre: %s", breedName);

        breedRepository.getBreedByName(breedName).ifPresent(breed -> {

            LOG.errorf("@createBreed SERV > La raza con el nombre: %s ya se encuentra registrada", breedName);

            throw new WarriorException(Response.Status.CONFLICT, "La raza con el nombre: " + breedName + ", ya se " +
                    "encuentra registrada en base de datos");
        });

        LOG.infof("@validateBreedRegistry SERV > La raza con el nombre: %s No se encuentra registrada, se " +
                "continua proceso de almacenamiento", breedName);
    }

    private Breed getBreedByIdOptional(String idBreed) {

        return breedRepository.findByIdOptional(idBreed).orElseThrow(() -> {

            LOG.errorf("@getByIdOptional SERV > La raza con identificador: %s No se encuentra registrada", idBreed);

            return new WarriorException(Response.Status.NOT_FOUND, "La raza con el identificador: " + idBreed +
                    ", No se encuentra registrada. No se permite continuar");
        });
    }
}
