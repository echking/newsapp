package com.example.ts.news.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ts.news.R;

public class SearchGo extends AppCompatActivity {
    private ImageView back;
    private EditText search_ed;
    private Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }
    private void initView() {
        back=(ImageView) findViewById(R.id.back);
        search_ed= findViewById(R.id.search_ed);
        go=(Button) findViewById(R.id.go);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchGo.this.finish();
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchGo.this, SearchActivity.class);
                String title=search_ed.getText().toString();
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
    }
}

