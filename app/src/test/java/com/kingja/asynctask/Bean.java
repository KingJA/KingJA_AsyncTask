package com.kingja.asynctask;

/**
 * Description:TODO
 * Create Time:2017/10/13 13:27
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class Bean {
    static {
        System.out.println("静态代码块");
    }

    {
        System.out.println("构造代码块");
    }

    public Bean() {
        System.out.println("构造函数");
    }
}
