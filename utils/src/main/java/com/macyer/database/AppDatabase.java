package com.macyer.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.macyer.database.dao.UserDao;

/**
 * Created by Lenovo on 2017/11/26.
 */

@Database(version = 8 ,entities = {User.class},exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /*
    UserDao userDao = MyApplication.database.userDao();
    * */
    
    public abstract UserDao userDao();

}
