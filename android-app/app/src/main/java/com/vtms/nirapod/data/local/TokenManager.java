package com.vtms.nirapod.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class TokenManager {

    private static final String PREF_NAME  = "nirapod_secure";
    private static final String KEY_TOKEN  = "jwt_token";
    private static final String KEY_NAME   = "user_name";
    private static final String KEY_EMAIL  = "user_email";
    private static final String KEY_ROLE   = "user_role";
    private static final String KEY_TIN    = "tin_number";
    private static final String KEY_TP_ID  = "taxpayer_id";

    private final SharedPreferences prefs;

    public TokenManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            prefs = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("TokenManager init failed: " + e.getMessage());
        }
    }

    public void saveSession(String token, String name,
                            String email, String role,
                            String tin, long taxpayerId) {
        prefs.edit()
                .putString(KEY_TOKEN,  token)
                .putString(KEY_NAME,   name)
                .putString(KEY_EMAIL,  email)
                .putString(KEY_ROLE,   role)
                .putString(KEY_TIN,    tin)
                .putLong  (KEY_TP_ID,  taxpayerId)
                .apply();
    }

    public String  getToken()      { return prefs.getString(KEY_TOKEN, null); }
    public String  getUserName()   { return prefs.getString(KEY_NAME,  ""); }
    public String  getUserEmail()  { return prefs.getString(KEY_EMAIL, ""); }
    public String  getUserRole()   { return prefs.getString(KEY_ROLE,  ""); }
    public String  getTinNumber()  { return prefs.getString(KEY_TIN,   ""); }
    public long    getTaxpayerId() { return prefs.getLong  (KEY_TP_ID, -1); }
    public boolean isLoggedIn()    { return getToken() != null; }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}