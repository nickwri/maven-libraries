package uk.co.referencepoint.publishtest.http;

/**
 * Created by nick.wright on 15/02/2016.
 */
public class constants {

    // colors available in smartcard-sdk library

    public final static long HTTP_REQUEST_SERVER_CONNECT_TIME_OUT = 10;
    public final static long HTTP_REQUEST_SERVER_WRITE_TIME_OUT = 10;
    public final static long HTTP_REQUEST_SERVER_READ_TIME_OUT = 30;

    // HTTP headers
    public final static String HTTP_HEADER_AUTHORISATION = "Authorization";
    public final static String HTTP_HEADER_API_VERSION = "Api-Version";
    public final static String HTTP_HEADER_API_VERSION_VALUE = "3.0";

    public static final int QR_CODE_REFRESH_FREQUENCY_SECONDS = 30;
    public static final String QR_CODE_VERSION = "2.0";                 // embedded in the qr code string - human readable

    public static final String EXTRA_SKILLSIGHT_DATA = "SkillSightData";
//    public static final String SKILLSIGHT_DATA = "<?xml version=\"1.0\" encoding=\"utf-8\"?><cardholder><quals><qual courseTitle=\"How to blah blah\" AccreditedBy=\"Big Barry\" AchievementDate=\"07/12/2015\" Category=\"Site Induction\" Provider=\"rpl\" CompanyRegNo=\"false\" TrainingProvider=\"Big Barry\" Expiry=\"07/12/2017\" ProviderWebsite=\"http://www.referencepoint.co.uk\" /></quals></cardholder>";

    public static final String EXTRA_CARD_ID = "CardID";
    public static final String EXTRA_CARD_SERIAL_NUMBER = "SerialNumber";
    public static final String EXTRA_SELECTED_TAB = "SelectedTab";

    // Render pack scheme file names
    public static final String RENDER_PACK_FILENAME_EXT = "_renderpack.dat";    // filenames for each pack are stored in the db
    public static final byte[] APPLET_AID_SKILLSIGHT = new byte[] { (byte)0xA0, (byte)0x00, (byte)0x00, (byte)0x03, (byte)0x75, (byte)0x00, (byte)0x05, (byte)0x00, (byte)0x01, (byte)0x01 };

    // =========================
    // CardDataRenderRefreshTask
    public static final String BROADCAST_ACTION_CARD_DATA_REFRESH_TASK_COMPLETED = "BROADCAST_ACTION_CARD_DATA_REFRESH_TASK_COMPLETED";
    public static final String BROADCAST_ACTION_CARD_RENDER_UPDATED = "CARD_RENDER_UPDATED";
    public static final String EXTRA_CARD_RENDER_SERIAL_NUMBER_TO_BE_UPDATED = "CARD_RENDER_SERIAL_NUMBER_TO_BE_UPDATED";    // sent to task to target a card
    public static final String EXTRA_CARD_RENDER_SERIAL_NUMBER_UPDATED = "CARD_RENDER_SERIAL_NUMBER_UPDATED";                // returned in broadcast
    public static final String EXTRA_CARD_REFRESH_SUCCESS = "Success";
    // ========================

    public static final String BROADCAST_ACTION_SERVER_CARD_RECONCILIATION_TASK_COMPLETED = "SERVER_CARD_RECONCILIATION_TASK_COMPLETED";
    public static final String BROADCAST_ACTION_SERVER_CARD_RECONCILIATION_TASK_FCM_COMPLETED = "BROADCAST_ACTION_SERVER_CARD_RECONCILIATION_TASK_FCM_COMPLETED";
    public static final String BROADCAST_ACTION_CHECK_EXPIRED_CANCELLED_CARDS_TASK_COMPLETED = "CHECK_EXPIRED_CANCELLED_CARDS_TASK_COMPLETED";
    public static final String BROADCAST_ACTION_SKILLSIGHT_MEMBER_DATA_REFRESH_TASK_COMPLETED = "BROADCAST_ACTION_SKILLSIGHT_MEMBER_DATA_REFRESH_TASK_COMPLETED";
    public static final String BROADCAST_ACTION_SKILLSIGHT_MEMBER_DATA_HAS_BEEN_REFRESHED = "BROADCAST_ACTION_SKILLSIGHT_MEMBER_DATA_HAS_BEEN_REFRESHED";
    public static final String BROADCAST_ACTION_SWIPE_REFRESH_TASK_COMPLETED = "BROADCAST_ACTION_SWIPE_REFRESH_TASK_COMPLETED";

    public static final String BROADCAST_ACTION_CARD_STATE_CHANGED = "CARD_STATE_CHANGED";

    public static final String BROADCAST_ACTION_DISPLAY_CARD_CARD_DATA_REFRESH_COMPLETED = "BROADCAST_ACTION_DISPLAY_CARD_CARD_DATA_REFRESH_COMPLETED";   // used by display card tabs

    // refresh render pack service
    //public static final String BROADCAST_ACTION_RENDER_PACK_REFRESH_TASK_COMPLETED = "BROADCAST_ACTION_RENDER_PACK_REFRESH_TASK_COMPLETED";
//    public static final String EXTRA_REFRESH_RENDER_PACK_ID = "EXTRA_REFRESH_RENDER_PACK_ID";
//    public static final String EXTRA_REFRESH_RENDER_PACK_RESULT = "EXTRA_REFRESH_RENDER_PACK_RESULT";


    public static final String EXTENDED_SERVER_CARD_RECONCILIATION_TASK_ID = "SERVER_CARD_RECONCILIATION_TASK_ID";
    public static final String EXTENDED_SERVER_CARD_RECONCILIATION_FAILED_NO_AUTH = "SERVER_CARD_RECONCILIATION_FAILED_NO_AUTH";    // allows UI to activate Login activity
    public static final String EXTENDED_CARD_STATE_CHANGED_SERIAL_NUMBER = "CARD_STATE_HAS_CHANGED";


    public static final String EXTRA_LOAD_CARDS_GLOBAL_SCHEME_ID = "EXTRA_LOAD_CARDS_GLOBAL_SCHEME_ID";
    public static final String EXTRA_SCHEME_VERIFICATION_VALUES_JSON = "EXTRA_SCHEME_VERIFICATION_VALUES_JSON";

    public static final String EXTRA_PASSWORD_RESET_INITIALISE_RESPONSE_JSON = "EXTRA_PASSWORD_RESET_INITIALISE_RESPONSE_JSON";
    public static final String EXTRA_MOBILE_NUMBER_INPUT_MODE = "EXTRA_MOBILE_NUMBER_INPUT_MODE";

    public static final String EXTRA_DISPLAY_CARD_REFRESH_SUCCESS = "Success";

    public static final String TERMS_AND_CONDITIONS_URL_LINK = "https://vircarda.com/privacy/termsAndConditions.html";


    // C A R O U S E L   S I Z I N G
    // ========================================
    // SIZE and MARGIN settings are to be used in conjunction with each other (changing one may affect the other)
    // Determines the size of the card in view i.e. make the card bigger (1 is full size card i.e. no left and right cards in view)
    public static final double CAROUSEL_MAIN_CARD_SIZE = 0.75;      // 0.75 shows us just the tip of the cards left and right. 0.5 would show us half the left and right cards
    // use this to adjust how much of the left and right cards are in view
    public static final double CAROUSEL_MAIN_CARD_SIZE_MARGIN_WIDTH = 1.1;

    // The carousel is not implemented as being infinite i.e. its not a carousel!
    // These 2 params define how far left and right the user can swipe before they hit the end of the pager
    // LOOPS is multiplied by the number of cards held to get the count (will be min of 2 so count will never be less that 2000).
    // Start position should be somewhere in the middle i.e. 1000.
    // This means that the user can swipe left 1000 times before they hit the end of the pager. They will also be able to swipe right at least 1000 times
    // depending on how many cards are loaded.
    // This is not ideal as a new card is loaded each time they swipe - see https://stackoverflow.com/questions/7766630/changing-viewpager-to-enable-infinite-page-scrolling
    // todo - make the view pager more efficient to re-use images
    public static final int CAROUSEL_PAGING_LOOPS = 1000;       // LOOPS x cards_loaded = carousel_card_count
    public static final int CAROUSEL_PAGING_START_POS = 1000;
    // ========================================

    public static final String DATE_FORMAT_UI = "dd/MM/yyyy";
    public static final String SKILLSIGHT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String SHOW_CARD_DATE_FORMAT_UI = "dd MMM yyyy";
    public static final String DATETIME_FORMAT_UI = "dd-MM-yyyy HH:mm:ss";
    public static final String DATETIME_FORMAT_UI_MONTH_NAME = "dd-MMM-yy HH:mm:ss";


    // R E G E X

    public static final String REG_EX_REG_NAME = "^[a-zA-Z ,.'-]{2,30}$";
    public static final String REG_EX_REG_EMAIL = "^[\\w-\\.+]+@([\\w-]+\\.)+[\\w-]{2,63}$";
    public static final String REG_EX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,100}$";
    public static final String REG_EX_REG_MOBILE = "^(077|078|079)\\s?\\d{2}\\s?\\d{6}$";
    public static final String REG_EX_REG_SECURITY_ANSWER = "^.{1,50}$";
    public static final String REG_EX_REG_REGISTRATION_NUMBER = "^[A-Za-z]{1,}-[0-9]{1,}$";
    public static final String REG_EX_RESET_PASSWORD_PIN = "^[0-9]{1,}$";
    public static final String REG_EX_SIM_PHONE_NUMBER = "^\\+?[0-9]{6,19}$";       // optional + followed by 6-19 numbers
    public static final String REG_EX_SIM_PHONE_COUNTRY_CODE = "^\\+?[0-9]{1,3}$";   // optional + followed by 1-3 numbers
    public static final String REG_EX_MOBILE_INPUT_COUNTRY_CODE = "^\\d{1,3}$";       // any numbers between 1-3 in length
    public static final String REG_EX_MOBILE_INPUT_PHONE_NUMBER = "^\\d{8,14}$";   // any numbers between 8-14 in length

    public static final String EXTRA_SWIPE_REFRESH_INTENT_TASK_NAME = "SwipeRefreshTaskExtra_TaskName";
    public static final String EXTRA_SWIPE_REFRESH_INTENT_TASK_VALUE_GET_SWIPES = "GetSwipes";
    public static final String EXTRA_SWIPE_REFRESH_INTENT_TASK_VALUE_SAVE_SWIPES = "SaveSwipes";
    public static final String EXTRA_SWIPE_REFRESH_INTENT_TASK_SAVE_SWIPES_JSON_RESPONSE = "JSONResponse";

    public static final String EXTRA_SKILLSIGHT_INTENT_SERVICE_TASK_NAME = "SkillSightIntentService_TaskName";
    public static final String EXTRA_SKILLSIGHT_INTENT_SERVICE_TASK_VALUE_GET_DATA = "GetData";
    public static final String EXTRA_SKILLSIGHT_INTENT_SERVICE_TASK_VALUE_SAVE_DATA = "SaveData";
    public static final String EXTRA_SKILLSIGHT_INTENT_SERVICE_TASK_SAVE_DATA_JSON_RESPONSE = "JSONResponse";

    public static final String EXTRA_SKILLSIGHT_INTENT_SERVICE_REFRESH_RESULT = "EXTRA_SKILLSIGHT_INTENT_SERVICE_REFRESH_RESULT";
    public static final String EXTRA_SKILLSIGHT_INTENT_SERVICE_REFRESH_RESULT_FAIL_MSG = "EXTRA_SKILLSIGHT_INTENT_SERVICE_REFRESH_RESULT_FAIL_MSG";
    public static final String EXTRA_SKILLSIGHT_INTENT_SERVICE_FORCE_REFRESH = "EXTRA_SKILLSIGHT_INTENT_SERVICE_FORCE_REFRESH";
    public static final String EXTRA_SKILLSIGHT_INTENT_SERVICE_NOTI_REFRESH = "EXTRA_SKILLSIGHT_INTENT_SERVICE_NOTI_REFRESH";

    public static final String EXTRA_MESSAGE_VIEWER_TITLE = "EXTRA_MESSAGE_VIEWER_TITLE";
        public static final String EXTRA_MESSAGE_VIEWER_MESSAGE = "EXTRA_MESSAGE_VIEWER_MESSAGE";
    public static final String TASK_ID_FCM_CARD_RECON = "TASK_ID_FCM_CARD_RECON";

    // Other settings
    // dashboard_content.xml - see Height property on card_carousel_viewpager
    //
    // ========================================
}

