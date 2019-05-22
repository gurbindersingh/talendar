package at.ac.tuwien.sepm.groupphase.backend.tests.configuration;

/**
 * DEFINE ALL ENDPOINTS CURREMTLY SUPPORET BY THE APPLICATION
 *
 * As one integration test may depend on several other endpoints, each address is defined in one
 * place.
 */
public class URL {


    /**
     * Existing Api Urls
     */
    public static final String BASE = "http://localhost:";
    public static final String TRAINER = "/api/talendar/trainers";
    public static final String EVENT = "/api/talendar/events";
    public static final String HOLIDAY = "/api/talendar/holiday";
}
