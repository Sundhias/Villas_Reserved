package com.azhar.villasreserved.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.azhar.villasreserved.model.ModelWisata;
import com.azhar.villasreserved.R;
import com.azhar.villasreserved.api.Api;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailWisataActivity extends AppCompatActivity {


    Toolbar tbDetailWisata;
    Button btnMove;
    Button btnHome;
    Button btnWeb;
    Button btnShare;
    TextView tvNamaWisata, tvDescWisata;
    ImageView imgWisata;
    String idWisata, NamaWisata, Desc;
    ModelWisata modelWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);
        btnMove = findViewById(R.id.btnMove);
        btnHome = findViewById(R.id.btnHome);
        btnWeb = findViewById(R.id.btnWeb);
        btnShare = findViewById(R.id.btnShare);
        tbDetailWisata = findViewById(R.id.tbDetailWisata);
        tbDetailWisata.setTitle("Detail Wisata");
        setSupportActionBar(tbDetailWisata);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modelWisata = (ModelWisata) getIntent().getSerializableExtra("detailWisata");
        if (modelWisata != null) {
            idWisata = modelWisata.getIdWisata();
            NamaWisata = modelWisata.getTxtNamaWisata();

            //set Id
            imgWisata = findViewById(R.id.imgWisata);
            tvNamaWisata = findViewById(R.id.tvNamaWisata);
            tvDescWisata = findViewById(R.id.tvDescWisata);

            //get Image
            Glide.with(this)
                    .load(modelWisata.getGambarWisata())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgWisata);

            getDetailWisata();
        }
        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailWisataActivity.this, HotelActivity.class);
                startActivity(i);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailWisataActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/maps/"));
                startActivity(i);

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Terima Kasih telah menggunakan aplikasi kami !, Silahkan share kepada teman dan orang terdekat anda mengenai pengalaman anda menggunakan aplikasi kami :)");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });
    }

    private void getDetailWisata() {
        AndroidNetworking.get(Api.DetailWisata)
                .addPathParameter("id", idWisata)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                NamaWisata = response.getString("nama");
                                Desc = response.getString("deskripsi");

                                //set Text
                                tvNamaWisata.setText(NamaWisata);
                                tvDescWisata.setText(Desc);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(DetailWisataActivity.this,
                                        "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(DetailWisataActivity.this,
                                "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
