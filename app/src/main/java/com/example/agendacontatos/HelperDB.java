package com.example.agendacontatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.FontRequest;

public class HelperDB extends SQLiteOpenHelper {

    private static String DATABASE = "db_agenda";
    private static int VERSION = 1;

    //chave primária composta :)
    String[] tables = {
        "CREATE TABLE agenda(" +
            "nome TEXT," +
            "telefone TEXT," +
            "PRIMARY KEY (nome, telefone)" +
            ")"
    };

    public HelperDB(Context context){
        super(context, DATABASE,null, VERSION);
    }

    //metodo executa se o banco nao existir
    @Override
    public void onCreate(SQLiteDatabase db) {
        //é possivel somente criar uma tabela por execução
        for (String table : tables) {
            db.execSQL(table);
        }
    }

    //metodo executa se o banco ja existir e a versão mudar
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //code backup

        db.execSQL("DROP TABLE IF EXISTS agenda");
        onCreate(db);
        db.close();
    }

    public long Insert(String nome, String telefone) {
        SQLiteDatabase  db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", nome);
        cv.put("telefone", telefone);
        long id = db.insertOrThrow("agenda", null, cv);
        db.close();
        return id;
    }

    public long Update(String[] PK_NomeTel, String novoNome, String novoTelefone){
        SQLiteDatabase  db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", novoNome);
        cv.put("telefone", novoTelefone);
        long rows_affected = db.update("agenda", cv, "nome = ? and telefone = ?", PK_NomeTel);
        db.close();
        return rows_affected;

    }

    public void Delete(String[] PK_NomeTel){
        SQLiteDatabase  db = getWritableDatabase();
        db.delete("agenda", "nome = ? and telefone = ?", PK_NomeTel);
    }

    public void InsertTest(int Quantidade){
        SQLiteDatabase  db = getWritableDatabase();
        for (int i = 1; i <= Quantidade; i++) {

            String queryInsert = "INSERT INTO agenda VALUES ( 'Contato"+i+"',"+i+");";
            db.execSQL(queryInsert);
        }
    }

    public void DeleteAll(){
        SQLiteDatabase  db = getWritableDatabase();
        String queryInsert = "DELETE FROM agenda";
        db.execSQL(queryInsert);
    }

    public static Cursor select_all(Context context){
        SQLiteDatabase  db = new HelperDB(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("select rowid, * from agenda order by rowid desc", null);//order by rowid desc
        return cursor;
    }

    /*
    public static Cursor select_by_primaryKey(String[] PK_NomeTel, Context context){
        SQLiteDatabase  db = new HelperDB(context).getReadableDatabase();
        String[] columns =  {"nome","telefone"};
        Cursor cursor = null;
        if(db != null) {
             cursor = db.query("agenda", columns, "nome = ? and telefone = ?", PK_NomeTel, null, null, null);
        }return cursor;
    }
    */
}
