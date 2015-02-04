package id.co.veritrans.vtmobilesdk_samplestore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.veritrans.vtmobilesdk_samplestore.MainActivity;
import id.co.veritrans.vtmobilesdk_samplestore.R;
import id.co.veritrans.vtmobilesdk_samplestore.model.VTItem;

/**
 * Created by muhammadanis on 2/4/15.
 */
public class ShoppingItemAdapter extends ArrayAdapter<VTItem> {

    private ArrayList<VTItem> shoppingItems;
    private Context context;


    public ShoppingItemAdapter(Context context, List<VTItem> shoppingItems){
        super(context, R.layout.shopping_cart_adapter ,shoppingItems);
        this.shoppingItems = new ArrayList<VTItem>();
        this.shoppingItems.addAll(shoppingItems);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = layoutInflater.inflate(R.layout.shopping_cart_adapter,null);

        ImageView productImage = (ImageView) rowView.findViewById(R.id.product_image);
        TextView productName = (TextView) rowView.findViewById(R.id.product_name);
        TextView productPrice = (TextView) rowView.findViewById(R.id.product_price);
        Button buyBtn = (Button) rowView.findViewById(R.id.buy_btn);


        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VTItem item = shoppingItems.get(position);
                HashMap<VTItem,Integer> cart =((MainActivity)context).getCart();
                if(cart.containsKey(item)){
                    int quantity = cart.get(item) + 1;
                    cart.put(item,quantity);
                }else{
                    cart.put(item,1);
                }
                ((MainActivity)context).updatePrice();
            }

        });

        VTItem item = shoppingItems.get(position);
        productImage.setImageResource(item.getImageId());
        productName.setText(item.getName());
        productPrice.setText(item.getPrice().toString());

        return rowView;

    }

    @Override
    public int getCount() {
        return this.shoppingItems.size();
    }

    @Override
    public void add(VTItem object) {
        shoppingItems.add(object);
        super.add(object);
    }

    @Override
    public void remove(VTItem object) {
        shoppingItems.remove(object);
        super.remove(object);
    }
}