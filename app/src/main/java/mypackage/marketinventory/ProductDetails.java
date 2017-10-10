package mypackage.marketinventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ProductDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MARKET_LOADER = 0;
    private Uri currentUri;
    private int quantity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        Intent intent = getIntent();
        currentUri = intent.getData();

        String p[] = {MarketContract.ProductEntry._ID,
                MarketContract.ProductEntry.COLUMN_NAME,
                MarketContract.ProductEntry.COLUMN_PRICE,
                MarketContract.ProductEntry.COLUMN_QUANTITY,
                MarketContract.ProductEntry.COLUMN_PIC
        };

        final Toast ta = Toast.makeText(this, "Cannot pass a negative value as an input", Toast.LENGTH_LONG);
        final Toast tq = Toast.makeText(this, "Quantity cannot be negative", Toast.LENGTH_LONG);


        Button add = (Button) findViewById(R.id.plusquantity);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView addt = (TextView) findViewById(R.id.plusquantitytext);
                TextView q = (TextView) findViewById(R.id.detail_quantity);
                if (Parcable(addt.getText().toString()) > -1) {

                    quantity += Parcable(addt.getText().toString());
                    q.setText("Quantity: " + String.valueOf(quantity));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MarketContract.ProductEntry.COLUMN_QUANTITY, quantity);
                    getContentResolver().update(currentUri, contentValues, null, null);

                }
                else if (Parcable(addt.getText().toString()) <= -1) ta.show();

            }
        });
        Button minus = (Button) findViewById(R.id.minusquantity);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView minust = (TextView) findViewById(R.id.minusquantitytext);
                TextView q = (TextView) findViewById(R.id.detail_quantity);

                String str = q.getText().toString();
                String numberOnly = str.replaceAll("[^0-9]", "");

                int total = Integer.valueOf(numberOnly) - Parcable(minust.getText().toString());
                if (total < 0) {
                    tq.show();
                } else if (total >= 0) {
                    q.setText("Quantity:" + String.valueOf(total));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MarketContract.ProductEntry.COLUMN_QUANTITY, total);
                    getContentResolver().update(currentUri, contentValues, null, null);
                }
            }
        });

        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteProduct();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
        Button order = (Button) findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView detailName = (TextView) findViewById(R.id.detail_name);
                String dname = detailName.getText().toString();
                String name = dname.replaceAll("Name:", "");
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "An Order of: " + name);
                startActivity(Intent.createChooser(emailIntent, "Send email"));
            }
        });
        getLoaderManager().initLoader(MARKET_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String p[] = {
                MarketContract.ProductEntry._ID,
                MarketContract.ProductEntry.COLUMN_NAME,
                MarketContract.ProductEntry.COLUMN_PRICE,
                MarketContract.ProductEntry.COLUMN_QUANTITY,
                MarketContract.ProductEntry.COLUMN_PIC
        };
        return new CursorLoader(this,
                currentUri,
                p,
                null,
                null,                
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) return;

        TextView nameView = (TextView) findViewById(R.id.detail_name);
        ImageView picView = (ImageView) findViewById(R.id.detail_pic);
        TextView priceView = (TextView) findViewById(R.id.detail_price);
        TextView quantityView = (TextView) findViewById(R.id.detail_quantity);


        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(MarketContract.ProductEntry.COLUMN_NAME);
            int priceColumnIndex = cursor.getColumnIndex(MarketContract.ProductEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(MarketContract.ProductEntry.COLUMN_QUANTITY);
            int picColumnIndex = cursor.getColumnIndex(MarketContract.ProductEntry.COLUMN_PIC);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            quantity = cursor.getInt(quantityColumnIndex);
            byte[] b = cursor.getBlob(picColumnIndex);
            nameView.setText("Name: " + name);

            if (b != null && b.length != 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                if (cursor.getBlob(picColumnIndex) != null)
                    picView.setImageBitmap(bitmap);
            }

            priceView.setText("Price: " + String.valueOf(price));
            quantityView.setText("Quantity: " + String.valueOf(quantity));
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        TextView nameView = (TextView) findViewById(R.id.detail_name);
        ImageView picView = (ImageView) findViewById(R.id.detail_pic);
        TextView priceView = (TextView) findViewById(R.id.detail_price);
        TextView quantityView = (TextView) findViewById(R.id.detail_quantity);

        nameView.setText("Name: ");
        picView.setImageResource(android.R.color.transparent);
        priceView.setText("Price: ");
        quantityView.setText("Quantity: ");


    }

    public int Parcable(String s) {
        try {
            int x = Integer.parseInt(s);
            return x;
        } catch (Exception e) {
            return 0;
        }
    }


    public void deleteProduct() {

        if (currentUri == null) return;

        int rowsDeleted = getContentResolver().delete(currentUri, null, null);

        if (rowsDeleted == 0) {
            Toast d = Toast.makeText(this, "Deletion failed", Toast.LENGTH_LONG);
            d.show();

        } else {
            Toast ds = Toast.makeText(this, "Deletion succesful", Toast.LENGTH_LONG);
            ds.show();
        }
        finish();
    }
}
