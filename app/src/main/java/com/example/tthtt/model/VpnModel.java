package com.example.tthtt.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by book4 on 2018/4/28.
 */

@Entity
public class VpnModel implements Serializable {

    private static final long serialVersionUID = 5041636359275823866L;
    @Id(autoincrement = true)
    private Long id;

    private String key;           // -1
    private String name = "";           // 0
    private int type;       // 1
    private String server = "";         // 2
    private String username = "";       // 3
    private String password = "";       // 4
    private String dnsServers = "";     // 5
    private String searchDomains = "";  // 6
    private String routes = "";         // 7
    private boolean mppe;        // 8
    private String l2tpSecret = "";     // 9
    private String ipsecIdentifier = "";// 10
    private String ipsecSecret = "";    // 11
    private String ipsecUserCert = "";  // 12
    private String ipsecCaCert = "";    // 13
    private String ipsecServerCert = "";// 14
    private boolean saveLogin;

    private String codeStr = "";
    private boolean isGood;
    private String city;
    private double lat;
    private double lng;



    @Generated(hash = 209557127)
    public VpnModel() {
    }

    @Generated(hash = 1131307849)
    public VpnModel(Long id, String key, String name, int type, String server,
            String username, String password, String dnsServers,
            String searchDomains, String routes, boolean mppe, String l2tpSecret,
            String ipsecIdentifier, String ipsecSecret, String ipsecUserCert,
            String ipsecCaCert, String ipsecServerCert, boolean saveLogin,
            String codeStr, boolean isGood, String city, double lat, double lng) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
        this.server = server;
        this.username = username;
        this.password = password;
        this.dnsServers = dnsServers;
        this.searchDomains = searchDomains;
        this.routes = routes;
        this.mppe = mppe;
        this.l2tpSecret = l2tpSecret;
        this.ipsecIdentifier = ipsecIdentifier;
        this.ipsecSecret = ipsecSecret;
        this.ipsecUserCert = ipsecUserCert;
        this.ipsecCaCert = ipsecCaCert;
        this.ipsecServerCert = ipsecServerCert;
        this.saveLogin = saveLogin;
        this.codeStr = codeStr;
        this.isGood = isGood;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean isMppe() {
        return mppe;
    }

    public void setMppe(boolean mppe) {
        this.mppe = mppe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodeStr() {
        return codeStr;
    }

    public void setCodeStr(String codeStr) {
        this.codeStr = codeStr;
    }

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean getMppe() {
        return this.mppe;
    }

    public boolean getIsGood() {
        return this.isGood;
    }

    public void setIsGood(boolean isGood) {
        this.isGood = isGood;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDnsServers() {
        return dnsServers;
    }

    public void setDnsServers(String dnsServers) {
        this.dnsServers = dnsServers;
    }

    public String getSearchDomains() {
        return searchDomains;
    }

    public void setSearchDomains(String searchDomains) {
        this.searchDomains = searchDomains;
    }

    public String getRoutes() {
        return routes;
    }

    public void setRoutes(String routes) {
        this.routes = routes;
    }

    public String getL2tpSecret() {
        return l2tpSecret;
    }

    public void setL2tpSecret(String l2tpSecret) {
        this.l2tpSecret = l2tpSecret;
    }

    public String getIpsecIdentifier() {
        return ipsecIdentifier;
    }

    public void setIpsecIdentifier(String ipsecIdentifier) {
        this.ipsecIdentifier = ipsecIdentifier;
    }

    public String getIpsecSecret() {
        return ipsecSecret;
    }

    public void setIpsecSecret(String ipsecSecret) {
        this.ipsecSecret = ipsecSecret;
    }

    public String getIpsecUserCert() {
        return ipsecUserCert;
    }

    public void setIpsecUserCert(String ipsecUserCert) {
        this.ipsecUserCert = ipsecUserCert;
    }

    public String getIpsecCaCert() {
        return ipsecCaCert;
    }

    public void setIpsecCaCert(String ipsecCaCert) {
        this.ipsecCaCert = ipsecCaCert;
    }

    public String getIpsecServerCert() {
        return ipsecServerCert;
    }

    public void setIpsecServerCert(String ipsecServerCert) {
        this.ipsecServerCert = ipsecServerCert;
    }

    public boolean isSaveLogin() {
        return saveLogin;
    }

    public void setSaveLogin(boolean saveLogin) {
        this.saveLogin = saveLogin;
    }

    public boolean getSaveLogin() {
        return this.saveLogin;
    }
}
