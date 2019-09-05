package com.supadata.utils.mqtt;

/**
 * @Author: pxx
 * @Date: 2019/4/14 20:12
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
        int m = 4;
        prinCupples(m);
    }
    public static void  prinCupples(int m){
        String arr[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        int count = m+1;
        int number = 2 * m - 2;
        for(int i=0;i<count;i++){
            for(int j=0;j<i;j++){
                System.out.print(" ");
            }
            for(int j=0;j<2*(count-i)-1;j++){
                if (j == 0 && i != count-1) {
                    System.out.print(arr[i]);
                }else if (j ==(2*(count-i)-2) && i != count-1) {
                    System.out.print(arr[number-i]);
                } else {
                    if(i == count-2){
                        System.out.print(arr[number-i]);
                    }else{
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }
}
