package lilith.config;

/**
 * Config class holds all constants used throughout the Lilith application.
 * This includes links, file paths, and command keywords.
 */
public class Config {

    /**
     * Cheer link URL.
     * Only Youtube link is allowed for security.
     */
    public static final String CHEER_LINK =
            "https://www.youtube.com/watch?v=FAmojODvK64&list=RDFAmojODvK64&start_radio=1";

    /**
     * Path to store task data.
     */
    public static final String DATA_PATH = "./LilithData/lilith.txt";

    /**
     * Command keywords.
     */
    public static final String CMD_BYE = "bye";
    public static final String CMD_YES = "yes";
    public static final String CMD_NO = "no";
    public static final String CMD_CHEER = "cheer";
    public static final String CMD_LIST = "list";
    public static final String CMD_FIND = "find ";
    public static final String CMD_TODO = "todo ";
    public static final String CMD_DEADLINE = "deadline ";
    public static final String CMD_EVENT = "event ";
    public static final String CMD_MARK = "mark ";
    public static final String CMD_UNMARK = "unmark ";
    public static final String CMD_DELETE = "delete ";
    public static final String CMD_DEL = "del ";
    public static final String CMD_REMOVE = "remove ";
    public static final String CMD_EMPTY_ALL = "/emptyall";
    public static final String CMD_UPDATE = "update ";
    public static final String ERROR_PREFIX = "/ERROR:";

    // Private constructor to prevent instantiation
    private Config() {}
}
