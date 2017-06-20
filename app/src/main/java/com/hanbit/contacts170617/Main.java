package com.hanbit.contacts170617;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Context context=Main.this;
        final ItemExist query=new ItemExist(context);
        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputID= (EditText) findViewById(R.id.inputID);
                EditText inputPW= (EditText) findViewById(R.id.inputPW);
                final String sID=inputID.getText().toString();
                final String sPW=inputPW.getText().toString();
                SQLiteHelper helper=new SQLiteHelper(context);
                Log.d("넘어갈 ID",sID);
                Log.d("넘어갈 PASS",sPW);
                new LoginService() {
                    @Override
                    public void perform() {
                        if(query.execute(sID,sPW)){
                            Toast.makeText(context,"LOGIN SUCCESS",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(context,MemberList.class));
                        }else{
                            Toast.makeText(context,"LOGIN FAIL",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(context,Main.class));
                        }
                    }
                }.perform();

            }
        });
        findViewById(R.id.joinBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    static String DB_NAME="hanbit.db";
    static String TABLE_MEMBER="Member";
    static String MEM_SEQ="seq";
    static String MEM_NAME="name";
    static String MEM_PWD="password";
    static String MEM_EMAIL="email";
    static String MEM_PHONE="phone";
    static String MEM_ADDR="addr";
    static String MEM_PHOTO="photo";
    static class Member{int seq;String name,password,email,phone,addr,photo;}
    static interface LoginService{public void perform();}
    static interface ListService{public ArrayList<?> perform();}
    static interface DetailService{public Member perform();}
    static interface UpdateService{public void perform();}
    static interface DeleteService{public void perform();}
    static abstract class QueryFactory{
        Context context;
        public QueryFactory(Context context) {this.context = context; }
        public abstract SQLiteDatabase getDatabase();
    }
    static class SQLiteHelper extends SQLiteOpenHelper{
        public SQLiteHelper(Context context) {
            super(context, DB_NAME, null, 1);
            this.getWritableDatabase();
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s" +
                            "(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s TEXT," +
                            "%s TEXT,%s TEXT,%s TEXT,%s TEXT);",
                    TABLE_MEMBER,MEM_SEQ,MEM_NAME,MEM_PWD,MEM_EMAIL,MEM_PHONE,MEM_ADDR,MEM_PHOTO));

            /*for(int i=1;i<10;i++){
                db.execSQL(
                        String.format(
                                "INSERT INTO %s(%s,%s,%s,%s,%s,%s)" +
                                        "VALUES('%s','%s','%s','%s','%s','%s');",
                                TABLE_MEMBER,MEM_NAME,MEM_PWD,MEM_EMAIL,MEM_PHONE,MEM_ADDR,MEM_PHOTO,
                                "홍길동"+i,"1","hong"+i+"@test.com","010-1234-567"+i,"서울"+i,"profileimg"));
            }*/
            Log.d("########## 실행한 지점","onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_MEMBER);
            onCreate(db);
        }
    }
    private class LoginQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public LoginQuery(Context context) {
            super(context);
            helper=new Main.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemExist extends LoginQuery{

        public ItemExist(Context context) {
            super(context);
        }
        public boolean execute(String seq,String password){
            return super
                    .getDatabase()
                    .rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'",
                            TABLE_MEMBER,MEM_SEQ,seq, MEM_PWD, password),null)
                    .moveToNext();
        }
    }

}
