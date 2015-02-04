package id.co.veritrans.vtmobilesdk_samplestore.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.veritrans.vtmobilesdk_samplestore.R;
import id.co.veritrans.vtmobilesdk_samplestore.model.VTCheckoutModel;

/**
 * Created by muhammadanis on 2/4/15.
 */
public class CheckoutAdapter extends ArrayAdapter<VTCheckoutModel>{

    private List<VTCheckoutModel> checkouts;
    private Context context;

    public CheckoutAdapter(Context context, List<VTCheckoutModel> checkouts) {
        super(context, R.layout.checkout_adapter,checkouts);
        this.checkouts = new ArrayList<>();
        this.checkouts.addAll(checkouts);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.checkout_adapter, null);
        TextView productName = (TextView) rowView.findViewById(R.id.ch_product_name);
        TextView productQuantity = (TextView) rowView.findViewById(R.id.ch_product_quantity);
        TextView productPrice = (TextView) rowView.findViewById(R.id.ch_product_price);
        TextView productTotal = (TextView) rowView.findViewById(R.id.ch_total);

        VTCheckoutModel model = checkouts.get(position);
        productName.setText(model.getItem().getName());
        productQuantity.setText(Integer.toString(model.getQuantity()));
        productPrice.setText(Integer.toString(model.getItem().getPrice()));
        productTotal.setText(Integer.toString(model.getItem().getPrice() * model.getQuantity()));

        return rowView;
    }

    @Override
    public int getCount() {
        return checkouts.size();
    }
}
