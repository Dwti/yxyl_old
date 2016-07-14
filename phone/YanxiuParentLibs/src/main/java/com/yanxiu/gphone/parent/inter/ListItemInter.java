package com.yanxiu.gphone.parent.inter;


import java.util.List;

/**
 * Created by lee on 16-3-30.
 */
public interface ListItemInter<T>{
    void setList(List<T> list);
    List<T> getList();
     T getItem(int position);
}
