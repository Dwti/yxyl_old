package com.yanxiu.gphone.studentold.bean;

/**
 * 柱状图
 */
public class DataElement {  
    
    public DataElement(float value, int color) {  
        this.value = value;  
        this.color = color;  
    }  
    public float getValue() {  
        return value;  
    }  
    public void setValue(float value) {  
        this.value = value;  
    }  
      
    public void setColor(int color) {  
        this.color = color;  
    }  
      
    public int getColor() {  
        return this.color;  
    }  
      
    private int color;  
    private float value;  
}  
