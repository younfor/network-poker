package com.hj.tool;

import java.util.ArrayList;
import java.util.List;

/*
 * QQ:361106306
 * by:小柒
 * 转载此程序须保留版权,未经作者允许不能用作商业用途!
 * */
public class Model {
	int count;//手数
	int value;//权值
	//一组牌
	List<String> a1=new ArrayList<String>(); //单张
	List<String> a2=new ArrayList<String>(); //对子
	List<String> a3=new ArrayList<String>(); //3带
	List<String> a123=new ArrayList<String>(); //连子
	List<String> a112233=new ArrayList<String>(); //连牌
	List<String> a111222=new ArrayList<String>(); //飞机
	List<String> a4=new ArrayList<String>(); //炸弹
}
