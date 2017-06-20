package com.hanbit.contacts170617;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.hanbit.contacts170617.Main.MEM_ADDR;
import static com.hanbit.contacts170617.Main.MEM_EMAIL;
import static com.hanbit.contacts170617.Main.MEM_NAME;
import static com.hanbit.contacts170617.Main.MEM_PHONE;
import static com.hanbit.contacts170617.Main.MEM_PHOTO;
import static com.hanbit.contacts170617.Main.MEM_PWD;
import static com.hanbit.contacts170617.Main.MEM_SEQ;
import static com.hanbit.contacts170617.Main.TABLE_MEMBER;

public class MemberList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context context=MemberList.this;
        final ItemLIst query=new ItemLIst(context);
        final ListView listView= (ListView) findViewById(R.id.listView);
        listView.setAdapter(new MemberAdapter(context,
                (ArrayList<Main.Member>)new Main.ListService() {
                    @Override
                    public ArrayList<?> perform() {
                        return query.execute();
                    }
                }.perform()
        ));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View v, int i, long l) {
                Intent intent=new Intent(context,MemberDetail.class);
                Main.Member m= (Main.Member) listView.getItemAtPosition(i);
                intent.putExtra("seq",String.valueOf(m.seq));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> p, View v, int i, long l) {
                return false;
            }
        });

    }
    private class ListQuery extends Main.QueryFactory{
        Main.SQLiteHelper helper;
        public ListQuery(Context context) {
            super(context);
            helper=new Main.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemLIst extends  ListQuery{
        public ItemLIst(Context context) {
            super(context);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }
        public ArrayList<Main.Member> execute(){
            ArrayList<Main.Member> list = new ArrayList<>();
            Cursor cursor=this.getDatabase().rawQuery(String.format("SELECT * FROM %s ;",TABLE_MEMBER),null);
            Main.Member m=null;
            if(cursor!=null){
                if (cursor.moveToFirst()){
                    do{
                        m=new Main.Member();
                        m.seq=Integer.parseInt(cursor.getString(cursor.getColumnIndex(MEM_SEQ)));
                        m.password=cursor.getString(cursor.getColumnIndex(MEM_PWD));
                        m.addr=cursor.getString(cursor.getColumnIndex(MEM_ADDR));
                        m.email=cursor.getString(cursor.getColumnIndex(MEM_EMAIL));
                        m.name=cursor.getString(cursor.getColumnIndex(MEM_NAME));
                        m.phone=cursor.getString(cursor.getColumnIndex(MEM_PHONE));
                        m.photo=cursor.getString(cursor.getColumnIndex(MEM_PHOTO));
                        list.add(m);
                    }while(cursor.moveToNext());
                }
            }else{
                Log.d("등록된 회원이 ","없습니다.");
            }
            return list;
        }
    }
    private class MemberAdapter extends BaseAdapter {
        ArrayList<Main.Member>list;
        LayoutInflater inflater;
        public MemberAdapter(Context context, ArrayList<Main.Member> list) {
            this.list=list;
            this.inflater=LayoutInflater.from(context);
        }
        private int[] photos={
                R.drawable.cupcake,
                R.drawable.donut,
                R.drawable.eclair,
                R.drawable.froyo,
                R.drawable.gingerbread,
                R.drawable.honeycomb,
                R.drawable.icecream,
                R.drawable.jellybean,
                R.drawable.kitkat,
                R.drawable.lollipop
        };

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v==null){
                v=inflater.inflate(R.layout.member_item,null);
                holder=new ViewHolder();
                holder.profileimg= (ImageView) v.findViewById(R.id.profileimg);
                holder.name= (TextView) v.findViewById(R.id.name);
                holder.phone= (TextView) v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder= (ViewHolder) v.getTag();
            }
            holder.profileimg.setImageResource(photos[i]);
            holder.name.setText(list.get(i).name);
            holder.phone.setText(list.get(i).phone);
            return v;
        }
    }
    static class ViewHolder{
        ImageView profileimg;
        TextView name;
        TextView phone;
    }
}
