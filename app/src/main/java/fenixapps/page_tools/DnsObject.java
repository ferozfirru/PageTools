package fenixapps.page_tools;

/**
 * Created by fenix on 31/3/17.
 */

public class DnsObject {
    private String type;
    private String country;
    private String ttl;
    private String record;

    public DnsObject(String type, String country,String ttl,String record) {
        this.type = type;
        this.country = country;
        this.ttl = ttl;
        this.record = record;
    }

    public String getType() {
        return type;
    }

    public String getCountry() {
        return country;
    }
    public String getTtl() {
        return ttl;
    }
    public String getRecord() {
        return record;
    }
}
