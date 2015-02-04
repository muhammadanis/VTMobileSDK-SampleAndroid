package id.co.veritrans.vtmobilesdk_samplestore;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import id.co.veritrans.vtmobilesdk_samplestore.adapter.ShoppingItemAdapter;
import id.co.veritrans.vtmobilesdk_samplestore.model.VTItem;
import veritrans.co.id.mobile.sdk.VTMobile;
import veritrans.co.id.mobile.sdk.entity.VTProduct;
import veritrans.co.id.mobile.sdk.helper.VTLogger;
import veritrans.co.id.mobile.sdk.interfaces.IActionCallback;
import veritrans.co.id.mobile.sdk.response.VTGetProductResponse;
import veritrans.co.id.mobile.sdk.vtexceptions.VTMobileException;

/**
 * Created by muhammadanis on 2/3/15.
 */
public class FragmentHome extends Fragment implements View.OnClickListener{
    public static String FragmentHomeTag = "HomeFragment";

    public static FragmentHome newInstance(){
        return new FragmentHome();
    }

    static VTLogger logger = new VTLogger(FragmentHome.class);

    ProgressDialog progressDialog;

    public FragmentHome(){
        //required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_home, container,false);

        final ListView l = (ListView) v.findViewById(R.id.list_nearby);
        v.findViewById(R.id.checkout).setOnClickListener(this);

        List<VTItem> items = new ArrayList<>();
        ShoppingItemAdapter adapter = new ShoppingItemAdapter(getActivity(),items);
        l.setAdapter(adapter);
        VTMobile.getAllProducts(new IActionCallback<VTGetProductResponse, VTMobileException>() {
            @Override
            public void onSuccess(VTGetProductResponse vtGetProductResponse) {
                ShoppingItemAdapter adapter = (ShoppingItemAdapter) l.getAdapter();
                for(VTProduct product : vtGetProductResponse.getProducts()){
                    VTItem vtItem = new VTItem(product.getId(),R.drawable.motor1,product.getName(),product.getPrice());
                    adapter.add(vtItem);
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onError(VTMobileException e) {
                progressDialog.dismiss();
                e.printStackTrace();
                logger.Log("Error: "+e.getMessage(), VTLogger.LogLevel.DEBUG);
            }
        });

        progressDialog = ProgressDialog.show(getActivity(),"Loading..","Please Wait...");
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.checkout:
                Fragment fr = new FragmentCheckout();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(FragmentHomeTag);
                ft.add(R.id.frame_container, fr, FragmentCheckout.FragmentCheckoutTag);
                ft.commit();
                break;
            default:
                break;
        }
    }

    public void updateCheckoutVisibility(boolean visible) {
        getView().findViewById(R.id.checkout).setVisibility(visible? View.VISIBLE : View.INVISIBLE);
    }
}
