package org.sena.service;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.sena.domain.Warrior;
import org.sena.helper.exception.WarriorException;
import org.sena.restClient.ImagesProxy;

import java.util.List;

@ApplicationScoped
public class ImagesService {

    private static final Logger LOG = Logger.getLogger(ImagesService.class);

    @Inject
    @RestClient
    ImagesProxy imagesProxy;

    @CacheResult(cacheName = "warrior-images")
    public List<String> getWarriorsImages() {

        LOG.info("@getWarriorsImages SERV > Inicia servicio de obtencion de imagenes. Inicia servicio rest client " +
                "hacia api-dragon-ball para obtener imagenes");

        int CANT_IMAGES = 60;

        try {
            List<String> images = imagesProxy.getWarriorsImages(CANT_IMAGES)
                    .getItems().stream()
                    .map(Warrior::getImage)
                    .toList();

            LOG.infof("@getWarriorsImages SERV > Finaliza rest client hacia api-dragon-ball se obtuvo: %s " +
                    "imagenes. Finaliza servicio de obtencion de imagenes", images.size());

            return images;

        } catch (Exception ex) {

            LOG.errorf("@getWarriorsImages SERV > Se presento un error al consultar la lista de imagenes");

            throw new WarriorException(Response.Status.INTERNAL_SERVER_ERROR, "Error en servicio rest client hacia " +
                    "api-dragon-ball para consultar las imágenes disponibles");
        }
    }
}
