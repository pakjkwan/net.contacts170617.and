package com.hanbit.contacts170617;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static com.hanbit.contacts170617.Main.MEM_ADDR;
import static com.hanbit.contacts170617.Main.MEM_EMAIL;
import static com.hanbit.contacts170617.Main.MEM_PHONE;
import static com.hanbit.contacts170617.Main.MEM_SEQ;
import static com.hanbit.contacts170617.Main.TABLE_MEMBER;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context context=MemberUpdate.this;
        ImageView profileImg= (ImageView) findViewById(R.id.profileImg);
        TextView textName= (TextView) findViewById(R.id.textName);
        final EditText changeEmail= (EditText) findViewById(R.id.changeEmail);
        final EditText changePhone= (EditText) findViewById(R.id.changePhone);
        final EditText changeAddress= (EditText) findViewById(R.id.changeAddress);
        Intent intent=this.getIntent();
        final String spec=intent.getExtras().getString("spec");
        Log.d("intent 가 전달한 spec:",spec);
        final String[] specArr=spec.split(",");
        final String seq=specArr[0];
        final String name=specArr[1];
        final String email0=specArr[2];
        final String addr0=specArr[3];
        final String phone0=specArr[4];
        final String photo=specArr[5];
        textName.setText(name);
        changeEmail.setHint(email0);
        changeAddress.setHint(addr0);
        changePhone.setHint(phone0);
        final Main.Member m=new Main.Member();
        m.email=(changeEmail.getText().toString().equals("")) ? email0 : changeEmail.getText().toString();;
        m.addr=(changeAddress.getText().toString().equals("")) ? addr0 : changeAddress.getText().toString();;
        m.phone=(changePhone.getText().toString().equals("")) ? phone0 : changePhone.getText().toString();;

        int profile=getResources().getIdentifier(
                this.getPackageName()+":drawable/"+photo,null,null);
        profileImg.setImageDrawable(getResources().getDrawable(profile,context.getTheme()));
        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ItemUpdate query=new ItemUpdate(context);
                String email="";
                final String addr="";
                final String phone="";
                new Main.UpdateService() {
                    @Override
                    public void perform() {
                        query.execute(m.email,m.phone,m.addr,seq);
                    }
                }.perform();
            }
        });
        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MemberDetail.class);
                intent.putExtra("seq",String.valueOf(seq));
                startActivity(intent);
            }
        });

    }
    private class UpdateQuery extends Main.QueryFactory{
        Main.SQLiteHelper helper;
        public UpdateQuery(Context context) {
            super(context);
            helper=new Main.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() { return helper.getWritableDatabase(); }
    }
    private class ItemUpdate extends  UpdateQuery{
        public ItemUpdate(Context context) {
            super(context);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }
        public void execute(String email,String phone, String addr, String seq){

            this.getDatabase().execSQL(
                    String.format(
                            "UPDATE %s SET %s = '%s', %s = '%s', %s = '%s' WHERE %s = '%s';",
                            TABLE_MEMBER,MEM_EMAIL,email,MEM_PHONE,phone,MEM_ADDR,addr,MEM_SEQ,seq));


        }
    }
}
