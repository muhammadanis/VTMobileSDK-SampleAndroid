package id.co.veritrans.vtmobilesdk_samplestore.model;

/**
 * Created by muhammadanis on 2/4/15.
 */
public class VTCheckoutModel {
    private VTItem item;
    private int quantity;


    public VTCheckoutModel(VTItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public VTItem getItem() {
        return item;
    }

    public void setItem(VTItem item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
