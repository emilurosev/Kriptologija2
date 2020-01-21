package ac.rs.singidunum.kriptologija2;

import java.util.Hashtable;

public class Configuration {

    private Hashtable<String, String> hashtable = new Hashtable<>();
    private String allSupportedKEX = "diffie-hellman-group-exchange-sha1,diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha256,ecdh-sha2-nistp256,ecdh-sha2-nistp384,ecdh-sha2-nistp521";
    private String allSupportedCiphers = "blowfish-cbc,3des-cbc,aes128-cbc,aes192-cbc,aes256-cbc,aes128-ctr,aes192-ctr,aes256-ctr,3des-ctr,arcfour,arcfour128,arcfour256";
    private String allSupportedMAC = "hmac-md5, hmac-sha1, hmac-md5-96, hmac-sha1-96";
    private String allSupportedHostKeyTypes = "ssh-dss,ssh-rsa,ecdsa-sha2-nistp256,ecdsa-sha2-nistp384,ecdsa-sha2-nistp521"; //server digital signatures
    private String allSupportedCompressionTypes = "zlib,zlib@openssh.com";

    //hashtable.put("CheckCiphers", "aes256-ctr,aes192-ctr,aes128-ctr,aes256-cbc,aes192-cbc,aes128-cbc,3des-ctr,arcfour,arcfour128,arcfour256");
    public void setConfigExtreme() {
        hashtable.put("StrictHostKeyChecking", "yes");
        hashtable.put("HashKnownHosts", "yes");
        hashtable.put("kex", "ecdh-sha2-nistp521,diffie-hellman-group-exchange-sha256");
        hashtable.put("cipher.s2c", "aes256-ctr,aes256-cbc");
        hashtable.put("cipher.c2s", "aes256-ctr,aes256-cbc");
        hashtable.put("mac.s2c", "hmac-md5,hmac-sha1"); //message authentication code
        hashtable.put("mac.c2s", "hmac-md5,hmac-sha1");
        hashtable.put("compression.s2c", "zlib,zlib@openssh.com");
        hashtable.put("compression.c2s", "zlib,zlib@openssh.com");
        hashtable.put("compression_level", "9");
        hashtable.put("server_host_key", "ssh-rsa,ecdsa-sha2-nistp521");
        hashtable.put("CheckCiphers", "aes256-ctr,aes256-cbc"); //A list of Ciphers which should be first checked for availability. All ciphers in this list which are not working will be removed from the ciphers.c2s and ciphers.s2c before sending these lists to the server in a KEX_INIT message.
    }

    public void setConfigStrong() {
        hashtable.put("StrictHostKeyChecking", "ask");
        hashtable.put("HashKnownHosts", "yes");
        hashtable.put("kex", "ecdh-sha2-nistp521,diffie-hellman-group-exchange-sha256");
        hashtable.put("cipher.s2c", "aes256-ctr,aes256-cbc");
        hashtable.put("cipher.c2s", "aes256-ctr,aes256-cbc");
        hashtable.put("mac.s2c", "hmac-md5,hmac-sha1");
        hashtable.put("mac.c2s", "hmac-md5,hmac-sha1");
        hashtable.put("compression.s2c", "zlib,zlib@openssh.com");
        hashtable.put("compression.c2s", "zlib,zlib@openssh.com");
        hashtable.put("compression_level", "9");
        hashtable.put("server_host_key", "ssh-rsa,ecdsa-sha2-nistp521");
        hashtable.put("CheckCiphers", "aes256-ctr,aes256-cbc");
    }

    public void setConfigDefault() {
        hashtable.put("HashKnownHosts", "yes");
    }

    public void setConfigWeak() {
        hashtable.put("StrictHostKeyChecking", "no");
    }

    public Hashtable<String, String> getConfig() {
        return hashtable;
    }
}
