package android.rccar;

public class GlobalSettings {

    private static GlobalSettings INSTANCE = null;
    static boolean status;
    static String name;
    static String address;
    static boolean driveButtons;
    static boolean spinButtons;
    static boolean turnButtons;
    static boolean velocityButton;
    static boolean accTurnButton;
    static boolean accVelocityButton;
    static int buttonsSensitivity;
    static int accSensitivity;

    private GlobalSettings(){
    status = false;
    name = "Unnamed";
    address="null";

    driveButtons=true;

    spinButtons=false;

    turnButtons=true;
    velocityButton=true;

    accTurnButton=true;
    accVelocityButton=true;

    buttonsSensitivity=255;
    accSensitivity=255;
    }

    public static GlobalSettings getInstance(){
        if(INSTANCE ==null){
            INSTANCE= new GlobalSettings();
        }
        return(INSTANCE);
    }

    public static void setStatus(boolean s){
        status=s;
    }
    public static void setName(String s){
        name=s;
    }
    public static void setAddress(String s ) {address=s;}
    public static void setdriveButtons(boolean b){
        driveButtons=b;
    }
    public static void setSpinButtons(boolean b){
        spinButtons=b;
    }
    public static void setturnButtons(boolean b){
        turnButtons=b;
    }
    public static void setvelocityButton(boolean b){
        velocityButton=b;
    }
    public static void setaccTurnButton(boolean b){
        accTurnButton=b;
    }
    public static void setaccVelocityButton(boolean b){
        accVelocityButton=b;
    }
    public static void setbuttonsSensitivity(int i){
        buttonsSensitivity=i;
    }
    public static void setaccSensitivity(int i){
        accSensitivity=i;
    }
}
