package b19060209.iot.njupt.edu.cn.fighting;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SQLiteHelper extends SQLiteOpenHelper {
    private SQLiteDatabase sqLiteDatabase;
    //创建数据库
    public SQLiteHelper(Context context){
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERION);
        sqLiteDatabase = this.getWritableDatabase();
    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DBUtils.DATABASE_TABLE+"("+DBUtils.NOTEPAD_ID+
                " integer primary key autoincrement,"+ DBUtils.NOTEPAD_CONTENT +
                " text," + DBUtils.NOTEPAD_TIME+ " text,"+DBUtils.NOTEPAD_DEADLINE+" text,"+DBUtils.NOTEPAD_FLAG+" text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    //添加数据
    public boolean insertData(String userContent,String userTime,String flag){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_CONTENT,userContent);
        contentValues.put(DBUtils.NOTEPAD_TIME,userTime);
        contentValues.put(DBUtils.NOTEPAD_DEADLINE,"0");//如果不是倒计时页面的保存，就把deadline设置为0
        contentValues.put(DBUtils.NOTEPAD_FLAG,flag);
        return
                sqLiteDatabase.insert(DBUtils.DATABASE_TABLE,null,contentValues)>0;
    }
    public boolean insertData_deadline(String userContent,String userTime,String deadline,String flag){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_CONTENT,userContent);
        contentValues.put(DBUtils.NOTEPAD_TIME,userTime);
        contentValues.put(DBUtils.NOTEPAD_DEADLINE,deadline);
        contentValues.put(DBUtils.NOTEPAD_FLAG,flag);
        Log.i("测试", "insertData_deadline "+deadline);
        return
                sqLiteDatabase.insert(DBUtils.DATABASE_TABLE,null,contentValues)>0;
    }
    //删除数据
    public boolean deleteData(String id){
        String sql=DBUtils.NOTEPAD_ID+"=?";
        String[] contentValuesArray=new String[]{String.valueOf(id)};
        return
                sqLiteDatabase.delete(DBUtils.DATABASE_TABLE,sql,contentValuesArray)>0;
    }
    //修改数据
    public boolean updateData(String id,String content,String userYear,String flag){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_CONTENT,content);
        contentValues.put(DBUtils.NOTEPAD_TIME,userYear);
        contentValues.put(DBUtils.NOTEPAD_FLAG,flag);
        String sql=DBUtils.NOTEPAD_ID+"=?";
        String[] strings=new String[]{id};
        return
                sqLiteDatabase.update(DBUtils.DATABASE_TABLE,contentValues,sql,strings)>0;
    }
    public boolean updateData_deadline(String id,String content,String userYear,String deadline,String flag){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_CONTENT,content);
        contentValues.put(DBUtils.NOTEPAD_TIME,userYear);
        contentValues.put(DBUtils.NOTEPAD_FLAG,flag);
        contentValues.put(DBUtils.NOTEPAD_DEADLINE,deadline);
        String sql=DBUtils.NOTEPAD_ID+"=?";
        String[] strings=new String[]{id};
        return
                sqLiteDatabase.update(DBUtils.DATABASE_TABLE,contentValues,sql,strings)>0;
    }
    //查询数据
    public List<NotepadBean> query(String flag){

        Log.i("测试", "进入数据库查询函数 ");
        int num=0;//计数器，用于记录当前有几条有效数据
        List<NotepadBean> list=new ArrayList<NotepadBean>();
        List<NotepadBean> list_final=new ArrayList<NotepadBean>();
        Cursor cursor=sqLiteDatabase.query(DBUtils.DATABASE_TABLE,null,null,null,
                null,null,DBUtils.NOTEPAD_ID+" desc");
        Log.i("测试", "cursor "+cursor);
        if (cursor!=null){
            while (cursor.moveToNext()){

                NotepadBean noteInfo=new NotepadBean();
                String id = String.valueOf(cursor.getInt
                        (cursor.getColumnIndex(DBUtils.NOTEPAD_ID)));
                Log.i("测试", "读取数据，显示当前读取到的数据id "+id);
                String content = cursor.getString(cursor.getColumnIndex
                        (DBUtils.NOTEPAD_CONTENT));
                Log.i("测试", "读取数据，显示当前读取到的数据content "+content);
                String time = cursor.getString(cursor.getColumnIndex
                        (DBUtils.NOTEPAD_TIME));
                Log.i("测试", "读取数据，显示当前读取到的数据time "+time);
                String flag_ = cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_FLAG));//稍微有些不懂，到时候再看看
                Log.i("测试", "读取数据，显示当前读取到的数据flag "+flag_);
                String deadline_ = cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_DEADLINE));
                Log.i("测试", "读取数据，显示当前读取到的数据deadline " + deadline_);
                Log.i("测试", "deadline读取异常");

                if(flag_.equals(flag))//只有当符合标签的时候才显示
                {
                    noteInfo.setId(id);
                    noteInfo.setNotepadContent(content);
                    noteInfo.setNotepadTime(time);
                    noteInfo.setFlag(flag_);
                    noteInfo.setDeadline(deadline_);
                    list.add(noteInfo);
                    num=num+1;
                }

            }
            cursor.close();
            //初始化
            int[] list_index = new int[num];
            for (int i=0;i<num;i++){
                list_index[i]=i;
            }
            //使用选择排序排序,交换下标即可
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String today = simpleDateFormat.format(date);
            for (int i=0;i<num-1;i++){
                int mid;//中间用于交换的变量
                int min=i;
                for (int j=i+1;j<num;j++){
                    if (datetime_number(list.get(list_index[j]).getDeadline(),today)<datetime_number(list.get(list_index[min]).getDeadline(),today)){
                        min=j;
                    };
                }
                mid = list_index[i];
                list_index[i]= list_index[min];
                list_index[min] = mid;
            }
            for (int i=0;i<num;i++){
                list_final.add(list.get(list_index[i]));
                Log.i("测试", "测试排序是否正常：list_final:"+list_final.get(i).getDeadline());
            }

        }
        return list_final;
    }
    //用于计算天数
    public int datetime_number(String date,String today){

        int year=0,month=0,day=0;
        int today_y=0,today_m=0,today_d=0;
        year=Integer.parseInt(date.substring(0,date.indexOf("年")));
        month=Integer.parseInt(date.substring(date.indexOf("年")+1,date.indexOf("月")));
        day=Integer.parseInt(date.substring(date.indexOf("月")+1,date.indexOf("日")));
        today_d=1;
        today_m=1;
        today_y=2000;
        int diff= (year-today_y)*365+(month-today_m)*30+(day-today_d);
        Log.i("测试", "检测日期是否截取正常 year:"+year+"month:"+month+"day:"+day+" diff:"+diff);

        return diff;
    }

}
