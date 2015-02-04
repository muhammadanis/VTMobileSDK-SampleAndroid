package id.co.veritrans.vtmobilesdk_samplestore;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.veritrans.vtmobilesdk_samplestore.adapter.CheckoutAdapter;
import id.co.veritrans.vtmobilesdk_samplestore.model.VTCheckoutModel;
import id.co.veritrans.vtmobilesdk_samplestore.model.VTItem;
import id.co.veritrans.vtmobilesdk_samplestore.view.CustomWebView;
import veritrans.co.id.mobile.sdk.VTMobile;
import veritrans.co.id.mobile.sdk.entity.VTCardDetails;
import veritrans.co.id.mobile.sdk.entity.VTProduct;
import veritrans.co.id.mobile.sdk.entity.VTTokenData;
import veritrans.co.id.mobile.sdk.helper.VTConstants;
import veritrans.co.id.mobile.sdk.helper.VTLogger;
import veritrans.co.id.mobile.sdk.interfaces.IActionCallback;
import veritrans.co.id.mobile.sdk.request.VTChargeRequest;
import veritrans.co.id.mobile.sdk.request.VTTokenRequest;
import veritrans.co.id.mobile.sdk.response.VTChargeResponse;
import veritrans.co.id.mobile.sdk.response.VTTokenResponse;
import veritrans.co.id.mobile.sdk.vtexceptions.VTMobileException;

/**
 * Created by muhammadanis on 2/4/15.
 */
public class FragmentCheckout extends Fragment implements View.OnClickListener {

    private VTLogger logger = new VTLogger(FragmentCheckout.class);

    public static String FragmentCheckoutTag = "fragmentcheckouttag";

    AlertDialog dialog3ds;
    ProgressDialog chargeDialog;
    int totalPrice;
    List<VTCheckoutModel> checkouts;

    public FragmentCheckout(){
        //required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);
        ListView l = (ListView) v.findViewById(R.id.list_bought);
        checkouts = new ArrayList<>();
        HashMap<VTItem,Integer> cart = ((MainActivity)getActivity()).getCart();
        totalPrice = 0;
        for(VTItem item : cart.keySet()){
            checkouts.add(new VTCheckoutModel(item,cart.get(item)));
            totalPrice+=item.getPrice() * cart.get(item);
        }
        CheckoutAdapter adapter = new CheckoutAdapter(getActivity(),checkouts);
        l.setAdapter(adapter);

        TextView txtTotal = (TextView) v.findViewById(R.id.grandTotal);
        txtTotal.setText(Integer.toString(totalPrice));

        //set button click listener
        ImageButton ccButton = (ImageButton) v.findViewById(R.id.ccButton);
        ccButton.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ccButton:{
                VTCardDetails cardDetails = CardFactory(true);
                VTTokenRequest request = new VTTokenRequest();
                request.setCardDetails(cardDetails);

                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),"","Loading, Please Wait...",true);
                VTMobile.getToken(new IActionCallback<VTTokenResponse, VTMobileException>() {
                    @Override
                    public void onSuccess(VTTokenResponse vtTokenResponse) {
                        progressDialog.dismiss();
                        if(vtTokenResponse.getRedirect_url() != null){
                            //using 3d secure
                            //show to user with webview
                            logger.Log("TokenId: "+vtTokenResponse.getToken_id(), VTLogger.LogLevel.DEBUG);
                            CustomWebView webView = new CustomWebView(getActivity());
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    switch (motionEvent.getAction()) {
                                        case MotionEvent.ACTION_DOWN:
                                        case MotionEvent.ACTION_UP:
                                            if (!view.hasFocus()) {
                                                view.requestFocus();
                                            }
                                            break;
                                    }
                                    return false;
                                }
                            });
                            webView.setWebChromeClient(new WebChromeClient());
                            webView.setWebViewClient(new VTWebViewClient(vtTokenResponse.getToken_id(), "dichi@alfaridi.info", checkouts));

                            webView.loadUrl(vtTokenResponse.getRedirect_url());
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                            dialog3ds = alertBuilder.create();

                            dialog3ds.setTitle("3D Secure Veritrans");
                            dialog3ds.setView(webView);

                            webView.requestFocus(View.FOCUS_DOWN);
                            alertBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                            dialog3ds.show();

                        }
                    }

                    @Override
                    public void onError(VTMobileException e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, request);
            }
            break;
        }
    }

    private VTCardDetails CardFactory(boolean secure){
        VTCardDetails cardDetails = new VTCardDetails();
        cardDetails.setCard_number("4811111111111114");
        cardDetails.setCard_cvv("123");
        cardDetails.setCard_exp_month(1);
        cardDetails.setCard_exp_year(2020);
        cardDetails.setSecure(secure);
        cardDetails.setGross_amount(totalPrice+"");
        return cardDetails;
    }

    private class VTWebViewClient extends WebViewClient {

        private String tokenId;
        private List<VTCheckoutModel> checkouts;
        private String email;

        public VTWebViewClient(String tokenId, String email, List<VTCheckoutModel> checkouts) {
            this.tokenId = tokenId;
            this.email = email;
            this.checkouts = checkouts;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return  true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view,url);
            if(url.startsWith(VTConstants.getTokenEndPoint() + "/callback")){
                //charge client
                VTChargeRequest chargeRequest = new VTChargeRequest();
                //create tokendata
                VTTokenData tokenData = new VTTokenData();
                tokenData.setTokenId(tokenId);
                //create item data
                List<VTProduct> products = new ArrayList<>();
                for(VTCheckoutModel checkoutModel : checkouts){
                    VTProduct product = new VTProduct();
                    product.setId(checkoutModel.getItem().getId());
                    product.setPrice(checkoutModel.getItem().getPrice());
                    product.setQuantity(checkoutModel.getQuantity());
                    product.setName(checkoutModel.getItem().getName());
                    products.add(product);
                }

                chargeRequest.setEmail(email);
                chargeRequest.setItems(products);
                chargeRequest.setPaymentType("credit_card");
                chargeRequest.setTokenData(tokenData);
                logger.Log("Start call charging", VTLogger.LogLevel.DEBUG);
                VTMobile.charge(new IActionCallback<VTChargeResponse, VTMobileException>() {
                    @Override
                    public void onSuccess(VTChargeResponse vtChargeResponse) {
                        if(dialog3ds != null && dialog3ds.isShowing()){
                            dialog3ds.dismiss();
                        }
                        if(chargeDialog != null && chargeDialog.isShowing()){
                            chargeDialog.dismiss();
                        }
                        Toast.makeText(getActivity(),"Success to Charge with Transaction Id: "+vtChargeResponse.getTransaction_id(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(VTMobileException e) {
                        e.printStackTrace();
                        if(dialog3ds != null && dialog3ds.isShowing()){
                            dialog3ds.dismiss();
                        }
                        if(chargeDialog != null && chargeDialog.isShowing()){
                            chargeDialog.dismiss();
                        }
                        Toast.makeText(getActivity(), "Failed to Charge with Exception: "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }, chargeRequest);

                dialog3ds.dismiss();
                chargeDialog = ProgressDialog.show(getActivity(), "", "Please Wait, Charging Client...", true);

            }else if(url.startsWith(VTConstants.getTokenEndPoint() + "/redirect") || url.contains("3dsecure")){
                /* Do Nothing */
            }else{
                if(dialog3ds != null){
                    dialog3ds.dismiss();
                }
            }
        }
    }
}
