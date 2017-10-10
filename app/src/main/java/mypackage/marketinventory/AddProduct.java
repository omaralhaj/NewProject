package mypackage.marketinventory;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class AddProduct extends AppCompatActivity {


    private static int RESULT_IMAGE = 1;
    boolean flag = false;
    Uri imageUri = null;

    Bitmap bitmap = null;


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        final Toast t = Toast.makeText(this, "You have to upload a picture", Toast.LENGTH_LONG);
        final Toast nn = Toast.makeText(this, "Invalid name", Toast.LENGTH_LONG);
        final Toast qt = Toast.makeText(this, "Please enter valid value for Quantity and Price", Toast.LENGTH_LONG);

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProduct.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView name = (TextView) findViewById(R.id.name);
                TextView price = (TextView) findViewById(R.id.price);
                TextView quantity = (TextView) findViewById(R.id.quantity);

                if (!flag) {

                    t.show();
                    if (Parcable(quantity.getText().toString()) <= 0 || Parcable(price.getText().toString()) <= 0)
                        qt.show();
                    if (name.getText().toString().equals("")) nn.show();
                } else if (Parcable(quantity.getText().toString()) <= 0 || Parcable(price.getText().toString()) <= 0) {
                    qt.show();
                    if (name.getText().toString().equals("")) nn.show();
                } else if (name.getText().toString().equals("")) nn.show();


                else {

                    ContentValues values = new ContentValues();
                    values.put(MarketContract.ProductEntry.COLUMN_NAME, name.getText().toString());
                    values.put(MarketContract.ProductEntry.COLUMN_PRICE, price.getText().toString());
                    values.put(MarketContract.ProductEntry.COLUMN_QUANTITY, quantity.getText().toString());

                    flag = false;

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
                    values.put(MarketContract.ProductEntry.COLUMN_PIC, stream.toByteArray());
                    getContentResolver().insert(MarketContract.ProductEntry.CONTENT_URI, values);
                    Intent i = new Intent(AddProduct.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });

        Button upload = (Button) findViewById(R.id.uploadpic);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_IMAGE);
                flag = true;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_IMAGE && resultCode == RESULT_OK && data != null)
            imageUri = data.getData();

        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int Parcable(String s) {
        try {
            int x = Integer.parseInt(s);
            return x;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


}
