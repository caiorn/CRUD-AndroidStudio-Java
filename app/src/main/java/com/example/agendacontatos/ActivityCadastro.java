package com.example.agendacontatos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

public class ActivityCadastro extends AppCompatActivity {

    private EditText getEditNome(){ return (EditText) findViewById(R.id.editTextName);}
    private EditText getEditTele(){ return (EditText) findViewById(R.id.editTextPhone);}
    private Button getBtnUpdat(){ return (Button) findViewById(R.id.btnCadastrarAlterar);}
    private Button getBtnDelet() {return (Button) findViewById(R.id.btnExcluir);}

    private String  oldNameUpdatePK, oldTelUpdatePK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //verifica se a activity foi aberta através de algum item da lista
        boolean isModeUpdate = isModeUpdateAndConfigure();

        if(getEditNome().requestFocus()){
            //exibe teclado
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        getBtnUpdat().setOnClickListener(v -> {
            HelperDB helperDB = new HelperDB(ActivityCadastro.this);

            String newNome = getEditNome().getText().toString().trim();
            String newTele = getEditTele().getText().toString().trim();
            try {
                if(isModeUpdate) {
                    helperDB.Update(new String[]{oldNameUpdatePK, oldTelUpdatePK}, newNome, newTele);
                    Toast.makeText(ActivityCadastro.this, "Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }else{
                    long rowid = helperDB.Insert(newNome, newTele);
                    Toast.makeText(ActivityCadastro.this, "Cadastrado com Sucesso! nº:"+rowid, Toast.LENGTH_SHORT).show();
                    getEditNome().setText("");
                    getEditTele().setText("");
                    getEditNome().requestFocus();
                    setResult(RESULT_OK);
                }
            } catch (SQLException e) {
                new AlertDialog.Builder(this).setTitle("SQL Erro").setMessage(e.getMessage()).setNeutralButton("OK", null).show();
            }
        });

        getBtnDelet().setOnClickListener(v -> {
            confirmDeleteDialog();
        });

        getBtnUpdat().setOnLongClickListener(v -> {
            new HelperDB(this).InsertTest(Integer.valueOf(getEditTele().getText().toString()));
            Toast.makeText(ActivityCadastro.this, "Teste Inseridos!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            return true;
        });
    }

    private void confirmDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Deletar "+oldNameUpdatePK+" ?")
                .setMessage("Tem certeza de que deseja "+oldTelUpdatePK+"?");

        builder.setPositiveButton("Sim", (dialog, which) -> {
                try {
                    String[] PKClustered = new String[]{oldNameUpdatePK, oldTelUpdatePK};
                    HelperDB hdb = new HelperDB(ActivityCadastro.this);
                    hdb.Delete(PKClustered);
                    setResult(RESULT_OK);
                    Toast.makeText(ActivityCadastro.this, "Deletado com Sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }catch (SQLException e) {
                    new AlertDialog.Builder(ActivityCadastro.this).setTitle("SQL Erro").setMessage(e.getMessage()).setNeutralButton("OK", null).show();
                }
        });builder.setNegativeButton("Não", (dialog, which) -> {

        });
        builder.create().show();
    }

    private Boolean isModeUpdateAndConfigure(){
        if(getIntent().hasExtra("nome") && getIntent().hasExtra("tele")){
            //configura activity para modo update
            oldNameUpdatePK = getIntent().getStringExtra("nome");
            oldTelUpdatePK = getIntent().getStringExtra("tele");
            setTitle("Alterar");
            getEditNome().setHint(oldNameUpdatePK);
            getEditTele().setHint(oldTelUpdatePK);
            getBtnUpdat().setText("Atualizar");
            getBtnDelet().setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }
}

/*
*   Problemas e Soluções:
*   Ao abrir uma nova activity e voltar, a activity principal estava recriando(metodo Oncreate() ou recreate())
    Solução: https://stackoverflow.com/questions/12276027/how-can-i-return-to-a-parent-activity-correctly/15933890#15933890
             https://stackoverflow.com/questions/15559838/actionbar-up-navigation-recreates-parent-activity-instead-of-onresume
**/