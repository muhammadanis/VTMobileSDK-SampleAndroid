package id.co.veritrans.vtmobilesdk_samplestore.model;

/**
 * Created by muhammadanis on 2/3/15.
 */
public class VTItem {
    private String id;
    private int imageId;
    private String name;
    private Integer price;

    public VTItem() {
    }

    public VTItem(String id, int resId, String name, int price) {
        this.id = id;
        this.imageId = resId;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        VTItem that = (VTItem)o;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
