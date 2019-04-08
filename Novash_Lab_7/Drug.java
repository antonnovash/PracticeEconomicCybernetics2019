import javafx.beans.property.SimpleStringProperty;

public class Drug {

    private final SimpleStringProperty id;
    private final SimpleStringProperty fulltitle;
    private final SimpleStringProperty price;
    private final SimpleStringProperty weight;
    private final SimpleStringProperty officialname;
    private SimpleStringProperty trackDelivery;

    public Drug(SimpleStringProperty id, SimpleStringProperty fulltitle,
                SimpleStringProperty price, SimpleStringProperty weight,
                SimpleStringProperty officialname, SimpleStringProperty trackDelivery) {
        this.id = id;
        this.fulltitle = fulltitle;
        this.price = price;
        this.weight = weight;
        this.officialname = officialname;
        this.trackDelivery = trackDelivery;
    }

    public Drug(String id, String fulltitle,
                String price, String weight,
                String officialname, String trackDelivery) {
        this.id = new SimpleStringProperty(id);
        this.fulltitle = new SimpleStringProperty(fulltitle);
        this.price = new SimpleStringProperty(price);
        this.weight = new SimpleStringProperty(weight);
        this.officialname = new SimpleStringProperty(officialname);
        this.trackDelivery = new SimpleStringProperty(trackDelivery);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getFulltitle() {
        return fulltitle.get();
    }

    public SimpleStringProperty fulltitleProperty() {
        return fulltitle;
    }

    public void setFulltitle(String fulltitle) {
        this.fulltitle.set(fulltitle);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getWeight() {
        return weight.get();
    }

    public SimpleStringProperty weightProperty() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight.set(weight);
    }

    public String getOfficialname() {
        return officialname.get();
    }

    public SimpleStringProperty officialnameProperty() {
        return officialname;
    }

    public void setOfficialname(String officialname) {
        this.officialname.set(officialname);
    }

    public String getTrackDelivery() {
        return trackDelivery.get();
    }

    public SimpleStringProperty trackDeliveryProperty() {
        return trackDelivery;
    }

    public void setTrackDelivery(String trackDelivery) {
        this.trackDelivery.set(trackDelivery);
    }
}