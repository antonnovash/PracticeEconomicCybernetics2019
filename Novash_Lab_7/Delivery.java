
import javafx.beans.property.SimpleStringProperty;

public class Delivery {
    private final SimpleStringProperty delivery_id;
    private final SimpleStringProperty delivery_date;
    private final SimpleStringProperty delivery_provider;

    public Delivery(SimpleStringProperty delivery_id, SimpleStringProperty delivery_date,
                    SimpleStringProperty delivery_provider) {
        this.delivery_id = delivery_id;
        this.delivery_date = delivery_date;
        this.delivery_provider = delivery_provider;
    }

    public Delivery(String delivery_id, String delivery_date,
                    String delivery_provider) {
        this.delivery_id = new SimpleStringProperty(delivery_id);
        this.delivery_date = new SimpleStringProperty(delivery_date);
        this.delivery_provider = new SimpleStringProperty(delivery_provider);
    }

    public String getId() {
        return delivery_id.get();
    }

    public SimpleStringProperty idProperty() {
        return delivery_id;
    }

    public void setId(String delivery_id) {
        this.delivery_id.set(delivery_id);
    }

    public String getDate() {
        return delivery_date.get();
    }

    public SimpleStringProperty dateProperty() {
        return delivery_date;
    }

    public void setDate(String delivery_date) {
        this.delivery_date.set(delivery_date);
    }

    public String getProvider() {
        return delivery_provider.get();
    }

    public SimpleStringProperty providerProperty() {
        return delivery_provider;
    }

    public void setProvider(String delivery_provider) {
        this.delivery_provider.set(delivery_provider);
    }
}