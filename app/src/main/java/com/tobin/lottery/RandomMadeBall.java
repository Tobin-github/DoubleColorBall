package com.tobin.lottery;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Tobin
 * @since 2016/7/29 11:10
 */

public class RandomMadeBall {
    /**
     * 生成随机验证码的的方法
     *
     * @return 装载随机验证码的的数组randomword
     */
    public static ArrayList<String> getRedBall() {
        ArrayList<Integer> randomRed = new ArrayList<Integer>();
        for (int i = 0; i < 6; i++) {
            int hong = (int) (Math.random() * 33);
            if (i == 0) {
                randomRed.add(hong);
            } else {
                if (randomRed.indexOf(hong) != -1) {
                    i--;
                } else {
                    randomRed.add(hong);
                }
            }
        }

        Collections.sort(randomRed);
        ArrayList<String> redBall = new ArrayList<String>();
        for (int k = 0; k < randomRed.size(); k++) {
            redBall.add(randomRed.get(k).toString());
        }
        return redBall;
    }

    /**
     * 生成随机验证码的的方法
     *
     * @return 装载随机验证码的的数组randomword
     */
    public static ArrayList<String> getBlueBall() {

        ArrayList<String> blueBall = new ArrayList<String>();
        for (int i = 0; i < 1; i++) {
            int blue = (int) (Math.random() * 16);
            if (i == 0) {
                blueBall.add(blue + "");
            } else {
                if (blueBall.get(i - 1) == blue + "") {
                    i--;
                } else {
                    blueBall.add(blue + "");
                }
            }
        }
        return blueBall;
    }
}
