package cn.com.twoke.game.spider_solitaire.utils;

import java.util.Random;

import cn.com.twoke.game.spider_solitaire.entity.Poker;

public final class PokerUtils {

    private static Random random = new Random();
	public static void shuffle(Poker[] arr) {
        for (int i = arr.length; i > 0; i--) {
            int rand = random.nextInt(i);
            swap(arr, rand, i - 1);
        }
    }


	public static void swap(Poker[] arr, int j, int i) {
		Poker temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
