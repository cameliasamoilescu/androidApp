package com.example.bazededate;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    // metodele care nu nevoie de query

    @Insert
    void insertAll(User... users);
    // insertAll(user1, user2);
    // insertAll(User[] users);

    @Delete
    void delete(User user);

    // metodele care au nevoie de query ( nu pot fi facute pe main thread)
    @Query("SELECT * FROM user")
    public List<User> getAll();

    // fa query pe select first_name from user


    @Query("SELECT * from user where age > :minAge")
    public List<User> getUserOlderThan(int minAge);

    @Query("SELECT * FROM user where id in (:ids)")
    public List<User> getAllByIds(int[] ids);

    @Query("SELECT * from user where firstName in (:first_name)")
    public  List<User> getAllByFirstName(String first_name);

    @Query("SELECT password from user where firstName = :first_name")
    public String getPassword(String first_name);

    @Query("delete from user where id = :ids")
    public  void deleteAllId(int ids);
}
