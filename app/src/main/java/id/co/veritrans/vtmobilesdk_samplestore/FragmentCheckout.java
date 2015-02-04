package id.co.veritrans.vtmobilesdk_samplestore;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.veritrans.vtmobilesdk_samplestore.adapter.CheckoutAdapter;
import id.co.veritrans.vtmobilesdk_samplestore.model.VTCheckoutModel;
import id.co.veritrans.vtmobilesdk_samplestore.model.VTItem;

/**
 * Created by muhammadanis on 2/4/15.
 */
public class FragmentCheckout extends Fragment implements View.OnClickListener {
    public static String FragmentCheckoutTag = "fragmentcheckouttag";

    AlertDialog dialog3ds;
    ProgressDialog chargeDialog;
    int totalPrice;

    public FragmentCheckout(){
        //required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);
        ListView l = (ListView) v.findViewById(R.id.list_bought);
        List<VTCheckoutModel> checkouts = new ArrayList<>();
        HashMap<VTItem,Integer> cart = ((MainActivity)getActivity()).getCart();
        totalPrice = 0;
        for(VTItem item : cart.keySet()){
            checkouts.add(new VTCheckoutModel(item,cart.get(item)));
            totalPrice+=item.getPrice() * cart.get(item);
        }
        CheckoutAdapter adapter = new CheckoutAdapter(getActivity(),checkouts);

        return v;

    }

    @Override
    public void onClick(View view) {

    }
}
