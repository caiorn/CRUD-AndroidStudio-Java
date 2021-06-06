package com.example.agendacontatos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter customAdapter;
    private FloatingActionButton add_button;

    private ImageView getImgEmpty(){ return (ImageView) findViewById(R.id.imageViewNoData);}
    private TextView getTextEmpty(){ return (TextView) findViewById(R.id.textViewNoData);}

    ArrayList<String> rowid, nomes, telefones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializa os dados
        rowid = new ArrayList<>();
        nomes = new ArrayList<>();
        telefones = new ArrayList<>();
        //obtém dados do banco de dados
        getAllDataFromDB();
        recyclerView = findViewById(R.id.recyclerView1);
        //configura o recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityMain.this));
        //cria a lista personalizada passando os dados para o adapter
        customAdapter = new CustomAdapter(ActivityMain.this, rowid, nomes, telefones);
        //atribui a lista com recyclew view para exibir
        recyclerView.setAdapter(customAdapter);

        add_button = findViewById(R.id.floatingActionButton3);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityCadastro.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //1 = retorno de Cadastro, 2 = retorno de update
        if(requestCode == 1 || requestCode == 2){
            //se o cadastro ou atualização foi efetivado
            if(resultCode == RESULT_OK) {
                //buscar novamente
                getAllDataFromDB();
                //notifica alteraçoes na lista
                customAdapter.notifyDataSetChanged();
                Toast.makeText(ActivityMain.this, "Lista Atualizada!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_deletar, menu);
        return super.onCreateOptionsMenu(menu);
        /*
            Para adicionar um menu ir botao Direito Res > New Android Resource Directory > Recource Type = Menu
            Botao Direito na pasta Menu > New Menu Source File.
        */
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btnMenu_deletarTudo){
            confirmDeleteAllDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getAllDataFromDB(){
        //limpar os dados
        rowid.clear();
        nomes.clear();
        telefones.clear();

        Cursor cursor = HelperDB.select_all(this);
        if(cursor.getCount() == 0){
            getImgEmpty().setVisibility(View.VISIBLE);
            getTextEmpty().setVisibility(View.VISIBLE);
        }else{
            getImgEmpty().setVisibility(View.GONE);
            getTextEmpty().setVisibility(View.GONE);
            while(cursor.moveToNext()){
                rowid.add(cursor.getString(0));
                nomes.add(cursor.getString(1));
                telefones.add(cursor.getString(2));
            }
        }
    }


    private void confirmDeleteAllDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Deletar todos contatos ?")
                .setMessage("Tem certeza de que deseja TUDO?");

        builder.setPositiveButton("Sim", (dialog, which) -> {
            try {
                new HelperDB(this).DeleteAll();
                Toast.makeText(this, "Dados Deletado!", Toast.LENGTH_SHORT).show();
                getAllDataFromDB();
                customAdapter.notifyDataSetChanged();
            }catch (SQLException e) {
                new AlertDialog.Builder(this).setTitle("SQL Erro").setMessage(e.getMessage()).setNeutralButton("OK", null).show();
            }
        });builder.setNegativeButton("Não", (dialog, which) -> {

        });
        builder.create().show();
    }
}

/*
* Problemas e Solucoes:
* startActivityForResult foi deprecated
* solução: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
*
*
*
* */