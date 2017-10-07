package mypackage.marketinventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import static android.os.Build.VERSION_CODES.O;
import static mypackage.marketinventory.R.id.price;
import static mypackage.marketinventory.R.id.quantity;


public class AddProduct extends AppCompatActivity  {

    private static final int EXISTING_PET_LOADER = 0;

    private Uri currentUri;

    boolean flag = false;

    private static int RESULT_IMAGE = 1;

    Uri imageUri = null;

    Bitmap bitmap = null;

    protected void onCreate(Bundle savedInstanceState) {

        TextView nameView = (TextView) findViewById(R.id.name);
        TextView priceView = (TextView) findViewById(R.id.price);
        TextView quantityView = (TextView) findViewById(R.id.quantity);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);


       final Toast t = Toast.makeText(this,"You have to upload a picture" , Toast.LENGTH_LONG);

        final Toast p = Toast.makeText(this,"Wrong value for Price, saved as 0" , Toast.LENGTH_LONG);

        final Toast q = Toast.makeText(this,"Wrong value for Quantity, saved as 0" , Toast.LENGTH_LONG);


        Intent i = getIntent();
         currentUri = i.getData();


        Button image = (Button) findViewById(R.id.uploadpic);


        MarketHelper mDbHelper = new MarketHelper(this);
       final SQLiteDatabase db = mDbHelper.getWritableDatabase();


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

                if (!flag) {

                    t.show();
                }

                else {

                    TextView name = (TextView) findViewById(R.id.name);

                    TextView price = (TextView) findViewById(R.id.price);

                    TextView quantity = (TextView) findViewById(R.id.quantity);


                    ContentValues values = new ContentValues();

                    values.put(MarketContract.ProductEntry.COLUMN_NAME, name.getText().toString());
                    if (Parcable(price.getText().toString()) <= 0) {
                        values.put(MarketContract.ProductEntry.COLUMN_PRICE, 0);
                        p.show();
                    }
                    else
                        values.put(MarketContract.ProductEntry.COLUMN_PRICE, price.getText().toString());

                    if (Parcable(quantity.getText().toString()) <= 0) {
                        values.put(MarketContract.ProductEntry.COLUMN_QUANTITY, 0);
                        q.show();
                    }
                    else
                        values.put(MarketContract.ProductEntry.COLUMN_QUANTITY, quantity.getText().toString());




                        flag = false;

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);

                        values.put(MarketContract.ProductEntry.COLUMN_PIC, stream.toByteArray());
                       Uri newUri = getContentResolver().insert(MarketContract.ProductEntry.CONTENT_URI, values);


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
                startActivityForResult(intent,RESULT_IMAGE);
                flag = true;



            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_IMAGE && resultCode == RESULT_OK && data != null )

            imageUri = data.getData();

        try{
            InputStream inputStream= getContentResolver().openInputStream(imageUri);


             bitmap = BitmapFactory.decodeStream(inputStream);






        }

        catch (Exception e){
e.printStackTrace();
        }
    }

    public int Parcable(String s){

        try{
           int x =  Integer.parseInt(s);
            return x;
        }

        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }



}
