package ir.mhdr.bmi.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PrivateSetting {
    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String uuid;
    @Index(unique = true)
    private String key;
    private String value;
    @Generated(hash = 840762953)
    public PrivateSetting(Long id, String uuid, String key, String value) {
        this.id = id;
        this.uuid = uuid;
        this.key = key;
        this.value = value;
    }
    @Generated(hash = 1847318442)
    public PrivateSetting() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }


}
