package id.co.veritrans.vtmobilesdk_samplestore;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.HashMap;

import id.co.veritrans.vtmobilesdk_samplestore.model.VTItem;
import veritrans.co.id.mobile.sdk.helper.VTMobileConfig;


public class MainActivity extends Activity {

    private HashMap<VTItem, Integer> cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set config data
        VTMobileConfig.IsProduction = false;
        VTMobileConfig.ClientKey = "VT-client-SimkwEjR3_fKj73D";

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //hide status bar of android
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        cart = new HashMap<>();

        Fragment fr = new FragmentHome();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();



        ft.replace(R.id.frame_container, fr, FragmentHome.FragmentHomeTag);

        ft.commit();
    }


    public void updatePrice() {
        int totalPrice = 0;
        for(VTItem item : cart.keySet()){
            int quantity = cart.get(item);
            int itemPrice = item.getPrice() * quantity;
            totalPrice+=itemPrice;
        }
        TextView txtGross = (TextView) findViewById(R.id.gross_amount);
        txtGross.setText(Integer.toString(totalPrice));
        //update checkout button
        FragmentHome fragmentHome = (FragmentHome) getFragmentManager().findFragmentByTag(FragmentHome.FragmentHomeTag);
        if(fragmentHome.isVisible()){
            fragmentHome.updateCheckoutVisibility(totalPrice == 0? false : true);
        }
    }


    public HashMap<VTItem, Integer> getCart() {
        return cart;
    }
}
