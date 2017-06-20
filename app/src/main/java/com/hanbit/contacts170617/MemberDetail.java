package com.hanbit.contacts170617;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.hanbit.contacts170617.Main.MEM_ADDR;
import static com.hanbit.contacts170617.Main.MEM_EMAIL;
import static com.hanbit.contacts170617.Main.MEM_NAME;
import static com.hanbit.contacts170617.Main.MEM_PHONE;
import static com.hanbit.contacts170617.Main.MEM_PHOTO;
import static com.hanbit.contacts170617.Main.MEM_PWD;
import static com.hanbit.contacts170617.Main.MEM_SEQ;
import static com.hanbit.contacts170617.Main.TABLE_MEMBER;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context context=MemberDetail.this;
        ImageView profileImg= (ImageView) findViewById(R.id.profileImg);
        TextView name= (TextView) findViewById(R.id.name);
        TextView email= (TextView) findViewById(R.id.email);
        TextView phone= (TextView) findViewById(R.id.phone);
        TextView addr= (TextView) findViewById(R.id.addr);
        Intent intent=this.getIntent();
        final String seq=intent.getExtras().getString("seq");
        final ItemDetail query=new ItemDetail(context);
        Main.Member member=new Main.DetailService() {
            @Override
            public Main.Member perform() {
                return query.execute(seq);
            }
        }.perform();
        int profile=getResources()
                .getIdentifier(this.getPackageName()+":drawable/"+member.photo,null,null);
        profileImg.setImageDrawable(getResources().getDrawable(profile,context.getTheme()));
        name.setText(member.name);
        phone.setText(member.phone);
        email.setText(member.email);
        addr.setText(member.addr);
        final String spec=member.seq+","+member.name+","+member.email
                +","+member.addr+","+member.phone+","+member.photo;
        findViewById(R.id.dialBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.callBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.smsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.emailBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.albumBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.movieBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.mapBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.musicBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MemberUpdate.class);
                intent.putExtra("spec",spec);
                startActivity(intent);
            }
        });
        findViewById(R.id.listBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MemberList.class));
            }
        });
    }
    private class DetailQuery extends Main.QueryFactory{
        Main.SQLiteHelper helper;
        public DetailQuery(Context context) {
            super(context);
            helper=new Main.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemDetail extends  DetailQuery{
        public ItemDetail(Context context) {
            super(context);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }
        public Main.Member execute(String seq){
            Cursor cursor=this.getDatabase()
                    .rawQuery(String.format("SELECT * FROM %s WHERE %s = '%s'",TABLE_MEMBER,MEM_SEQ,seq),null);
            Main.Member m=new Main.Member();
            if (cursor.moveToNext()){
                m.seq=Integer.parseInt(cursor.getString(cursor.getColumnIndex(MEM_SEQ)));
                m.password=cursor.getString(cursor.getColumnIndex(MEM_PWD));
                m.addr=cursor.getString(cursor.getColumnIndex(MEM_ADDR));
                m.email=cursor.getString(cursor.getColumnIndex(MEM_EMAIL));
                m.name=cursor.getString(cursor.getColumnIndex(MEM_NAME));
                m.phone=cursor.getString(cursor.getColumnIndex(MEM_PHONE));
                m.photo=cursor.getString(cursor.getColumnIndex(MEM_PHOTO));
            }
            return m;
        }
    }
}
