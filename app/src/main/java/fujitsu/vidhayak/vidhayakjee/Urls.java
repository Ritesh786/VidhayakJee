package fujitsu.vidhayak.vidhayakjee;

/**
 * Created by Fujitsu on 26/07/2017.
 */

public class Urls {

    public static String serverAddresscore="http://minews.in/core/";
    public static String serverAddresslumen="http://minews.in/lumen/public/";
    //  public static String server_url="http://aceresults.info/api";
    //public static String serverAddress="http://10.0.0.14/plspray/";
    // public static String server_url="http://10.0.0.14/plspray";
    public static String register=serverAddresscore+"user_reg.php";
    public static String login=serverAddresscore+"request_sms.php";
    public static String verifyotp=serverAddresscore+"verify_otp.php";
    public static String pendingrequest=serverAddresslumen+"post/user/pending/";
    public static String pendingstory=serverAddresslumen+"story/user/pending/";
//    public static String incompleterequest=serverAddresslumen+"update_profileDetails";
    public static String completedrequest=serverAddresslumen+"post/user/completed/";

//    public static String Approvedstory=serverAddress+"update_profileDetails";
      public static String completedstory=serverAddresslumen+"story/user/completed/";
     public static String vidhayakJeeNews=serverAddresslumen+"story/completed";

    public static String uploadrequest=serverAddresslumen+"post/create";
    public static String uploadstory=serverAddresslumen+"story/create";


}
