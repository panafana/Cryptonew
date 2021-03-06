package com.example.panafana.cryptonew;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import com.example.panafana.cryptonew.KeyGenerator;
import android.content.SharedPreferences;
import org.bouncycastle.util.encoders.Base64;
import static android.R.attr.id;
import static android.R.attr.name;


public class MainActivity extends AppCompatActivity {


    Context context = this;
    public String input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button generate = (Button) findViewById(R.id.generate);
        Button next = (Button) findViewById(R.id.next);
        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button store = (Button) findViewById(R.id.store);
        Button display = (Button) findViewById(R.id.display);


        final EditText editText = (EditText) findViewById(R.id.editText);
        final EditText editText2 = (EditText) findViewById(R.id.editText2);
        final EditText editText3 = (EditText) findViewById(R.id.editText3);
        final TextView showKey = (TextView) findViewById(R.id.showKey);
        final KeyGenerator keys = new KeyGenerator(context);

        //Key Generator
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                keys.generateKeys();

            }});

        //Message Encryption and Send
        generate.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view) {

                String msg1 = editText3.getText().toString();

                PublicKey publicKey = null;
                PrivateKey privateKey;
                byte[] encryptedBytes = new byte[0], decryptedBytes = new byte[0];
                Cipher cipher = null, cipher1 = null;
                String encrypted = null, decrypted;
                //publicKey = keys.getPublicKey();



                String pubKeyStr = getIntent().getStringExtra("publicK");
                byte[] sigBytes = Base64.decode(pubKeyStr);
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
                    publicKey = keyFact.generatePublic(x509KeySpec);
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }


                try {
                    cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
                try {
                    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }

                byte[] encryptedBytes3 = new byte[0];

                try {
                    encryptedBytes3 = cipher.doFinal(msg1.getBytes());
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }

/*
                try {
                    cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
                try {
                    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }


                encryptedBytes3 = cipher.update(encryptedBytes);
                try {
                    encryptedBytes3 = cipher.doFinal();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
*/
                //Encode to Base64
                byte[] encryptedBytes2 = Base64.encode(encryptedBytes3);
                encrypted = new String(encryptedBytes2);



                /*
                try {
                    cipher1 = Cipher.getInstance("RSA/None/PKCS1Padding");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
                try {
                    cipher1.init(Cipher.DECRYPT_MODE, publicKey);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                try {
                    decryptedBytes = cipher1.doFinal(encryptedBytes);
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
                */
                decrypted = new String(decryptedBytes);

                //String decryptedStr = new String(decrypted);




                //SIGN
/*
                Signature privateSignature = null;
                try {
                    privateSignature = Signature.getInstance("SHA256withRSA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    privateSignature.initSign(privateKey);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                try {
                    privateSignature.update(msg1.getBytes());
                } catch (SignatureException e) {
                    e.printStackTrace();
                }

                byte[] signature = new byte[0];
                try {
                    signature = privateSignature.sign();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
                System.out.println(signature.length);
*/

                String method = "register";
                BackgroundTask backgroundTask = new BackgroundTask(context);
                backgroundTask.execute(method, encrypted,"0");


            }

        });


        //Show Public Key
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                showKey.setTextIsSelectable(true);
                //KeyGenerator keys = new KeyGenerator(context);
                String publicKey = keys.getPublicKeyAsString();
                showKey.setText(publicKey);
            }

        });

        //Store new Contact
        store.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){

                SharedPreferences SP;
                SharedPreferences.Editor SPE;
                String keytostore = editText2.getText().toString();
                SP = context.getSharedPreferences("KeyChain", MODE_PRIVATE);
                SPE = SP.edit();
                SPE.putString(editText.getText().toString(), keytostore);
                SPE.commit();
                editText.setText("");
                editText2.setText("");


            }

        });



        //Go to Display Class
        display.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                Intent i = new Intent(getApplicationContext(),Display.class);
                startActivity(i);
            }

        });

        //Go to your Messages Class
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick (View view){
                Intent i = new Intent(getApplicationContext(),PublicMessages.class);
                startActivity(i);
            }

        });


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), KeyList.class);
            startActivity(i);
        }else if(id== R.id.action_clear){
            alertMessage();
        }

        return super.onOptionsItemSelected(item);
    }

    public void alertMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        SharedPreferences SP;
                        SharedPreferences.Editor SPE;
                        SP = context.getSharedPreferences("KeyChain", MODE_PRIVATE);
                        SPE = SP.edit();
                        SPE.clear();
                        SPE.commit();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete all Public Keys?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
