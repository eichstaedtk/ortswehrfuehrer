package de.eichstaedt.ortswehrfuehrer.adapter.web;

import de.eichstaedt.ortswehrfuehrer.application.AnmeldungApplicationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request-Filter zur Sicherung des Startseiten-Einstiegspunkts und Weiterleitung unauthentifizierter Anfragen auf die Anmeldeseite.
 */
@Provider
public class AnmeldungFilter implements ContainerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AnmeldungFilter.class);

    @Inject
    AnmeldungApplicationService anmeldungService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if (path != null) {
            path = path.startsWith("/") ? path.substring(1) : path;
        }

        if (isGeschuetzterPfad(path)) {
            Cookie sitzungCookie = requestContext.getCookies().get(AnmeldungController.SITZUNG_COOKIE_NAME);
            String sitzungsId = (sitzungCookie != null) ? sitzungCookie.getValue() : null;

            if (sitzungsId == null || !anmeldungService.istAngemeldet(sitzungsId)) {
                log.debug("Zugriff auf Startseite ohne gültige Sitzung abgewiesen. Weiterleitung zu /anmeldung.");
                requestContext.abortWith(Response.seeOther(URI.create("/anmeldung")).build());
            }
        }
    }

    private boolean isGeschuetzterPfad(String path) {
        return path == null || path.isEmpty();
    }
}
