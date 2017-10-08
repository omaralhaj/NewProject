package mypackage.marketinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.ContentResolver;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Blob;

import static android.R.attr.id;
import static android.R.attr.x;
import static java.net.URI.create;
import static mypackage.marketinventory.R.id.sell;

/**
 * Created by Omar on 10/3/17.
 */

public class MarketCursorAdapter extends CursorAdapter {

    public MarketCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

            return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);


    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameView = (TextView) view.findViewById(R.id.list_name);
        ImageView picView = (ImageView) view.findViewById(R.id.list_pic);
        TextView priceView = (TextView) view.findViewById(R.id.list_price);
        final TextView quantityView = (TextView) view.findViewById(R.id.list_quantity);



            int nameColumnIndex = cursor.getColumnIndex(MarketContract.ProductEntry.COLUMN_NAME);
            int picColumnIndex = cursor.getColumnIndex(MarketContract.ProductEntry.COLUMN_PIC);
            int priceColumnIndex = cursor.getColumnIndex(MarketContract.ProductEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(MarketContract.ProductEntry.COLUMN_QUANTITY);



            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
        final int[] quantity = {cursor.getInt(quantityColumnIndex)};
            byte[] b = cursor.getBlob(picColumnIndex);

        final Uri currentUri = ContentUris.withAppendedId(MarketContract.ProductEntry.CONTENT_URI, quantityColumnIndex);



        nameView.setText("Name: " +name);

            if (b != null && b.length != 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                if (cursor.getBlob(picColumnIndex) != null)
                    picView.setImageBitmap(bitmap);
            }


            priceView.setText("Price: " + String.valueOf(price));
            quantityView.setText("Quantity: " +String.valueOf(quantity[0]));


            final ContentValues values = new ContentValues();

        Button sell = (Button) view.findViewById(R.id.sell);
        final Toast w = Toast.makeText(context, "Quantity cannot be negative" , Toast.LENGTH_LONG);
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity[0] > 0) {
                    quantity[0]--;
                    quantityView.setText(String.valueOf(quantity[0]));
                    values.put(MarketContract.ProductEntry.COLUMN_QUANTITY,quantity[0]);
                    Uri x = currentUri;
                    context.getContentResolver().update(x,values,null,null);

                }



                else if (quantity[0] <= 0) w.show();

            }
        });


        }


    }

