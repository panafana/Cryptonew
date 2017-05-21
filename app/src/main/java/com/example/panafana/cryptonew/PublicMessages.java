package com.example.panafana.cryptonew;

import android.app.ListActivity;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.widget.ArrayAdapter;
//import org.bouncycastle.util.encoders.Base64;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.util.encoders.Base64;


public class PublicMessages  extends ListActivity {

    SharedPreferences SP;
    SharedPreferences SP2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_messages);

        PublicKey publicKey = null;

        SP = getSharedPreferences("messages", MODE_PRIVATE);
        SP2 = getSharedPreferences("KeyChain",MODE_PRIVATE);
        KeyGenerator keyzz= new KeyGenerator(this);


        Set<String> set = SP.getStringSet("messages", null);
        Set<String> set2 = SP.getStringSet("signatures", null);
        Map<String, ?> temp = SP2.getAll();

        ArrayList<String> keys = new ArrayList<String>();
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> mess = new ArrayList<String>(set);
        ArrayList<String> sign = new ArrayList<String>(set2);

        for (Map.Entry<String, ?> entry : temp.entrySet()) {
            keys.add(entry.getValue().toString());
            names.add(entry.getKey().toString());
        }

        ArrayList<String> decr = new ArrayList<String>();
        for (int i=0; i<mess.size(); i++){
            for (int j=0; j<keys.size() ; j++) {

                boolean flag =true;
                Signature signature = null;
                String pubKeyStr = keys.get(j);
                byte[] sigBytes = org.bouncycastle.util.encoders.Base64.decode(pubKeyStr);
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
                KeyFactory keyFact = null;
                try {
                    keyFact = KeyFactory.getInstance("RSA", "BC");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }
                try {
                    publicKey =  keyFact.generatePublic(x509KeySpec);
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }






                String tempmess = mess.get(i);
                byte[] encryptedBytes = new byte[0];
                byte[] encryptedBytestemp = tempmess.getBytes();
                encryptedBytes = org.bouncycastle.util.encoders.Base64.decode(encryptedBytestemp);
                byte[] decryptedBytes = new byte[0];
                Cipher cipher1 = null;

                try {
                    cipher1 = Cipher.getInstance("RSA/None/PKCS1Padding");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
                try {
                    cipher1.init(Cipher.DECRYPT_MODE, keyzz.getPrivateKey());
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }

                try {
                    decryptedBytes = cipher1.doFinal(encryptedBytes);
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                    flag = false;
                }
                String decrypted = new String(decryptedBytes);


                try {
                    signature = Signature.getInstance("SHA256withRSA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    signature.initVerify(publicKey);
                    signature.update(decryptedBytes);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }

                try {
                    if(signature.verify(sign.get(j).getBytes())){
                        System.out.println("Verified");
                    }else{
                        System.out.println("Something is wrong");
                    }
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
                if(flag){
                    decr.add(decrypted/* + " by "+names.get(j)*/);
                    System.out.println(decrypted+ "aaaaa");
                }





           }

        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                R.layout.rowlayout, R.id.listText, decr);

        // assign the list adapter
        setListAdapter(myAdapter);


    }



}
