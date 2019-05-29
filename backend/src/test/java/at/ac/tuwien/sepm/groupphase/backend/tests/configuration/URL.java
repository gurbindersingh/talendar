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
    public static final String TRAINER = "/api/v1/talendar/trainers";
    public static final String EVENT = "/api/v1/talendar/events";
    public static final String HOLIDAY = "/api/v1/talendar/holiday";
    public static final String HOLIDAYS = "/api/v1/talendar/holidays";
}
