package ultil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

public class check_connection {
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = cm.getAllNetworks();
        for (Network ni : networks) {
            NetworkInfo networkInfo = cm.getNetworkInfo(ni);
            if (networkInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (networkInfo.isConnected())
                    haveConnectedWifi = true;
            if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (networkInfo.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static  void ShowToast_Short(Context context, String m)
    {
        Toast.makeText(context,m, Toast.LENGTH_SHORT).show();
    }
}
