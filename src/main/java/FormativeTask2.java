import java.util.ArrayList;

import swiftbot.*;

public class FormativeTask2 {
    static SwiftBotAPI swiftbot;

    static char[] colours = { 'R', 'G', 'B', 'W' };

    static int[] red = new int[] { 255, 0, 0 };
    static int[] blue = new int[] { 0, 0, 255 };
    static int[] green = new int[] { 0, 255, 0 };
    static int[] white = new int[] { 255, 255, 255 };

    private static class sequence {
        static ArrayList<Character> sequence = new ArrayList<Character>();

        private static char[] generate() {
            for (int i = 0; i < 5; i++) {
                sequence.add(colours[(int) (Math.random() * colours.length)]);
            }

            char[] array = new char[sequence.size()];

            for (int i = 0; i < sequence.size(); i++) {
                array[i] = sequence.get(i);
            }

            return array;
        }

    }

    private static void lightButtons(char c) {
        switch (c) {
            case 'R':
                swiftbot.fillUnderlights(red);
            case 'G':
                swiftbot.fillUnderlights(green);
            case 'B':
                swiftbot.fillUnderlights(blue);
            case 'W':
                swiftbot.fillUnderlights(white);
            default:
                swiftbot.disableUnderlights();
        }
    }

    public static void main(String[] args) {
        int score = 0;
        int round = 1;

        char[] array = sequence.generate();

        boolean run = true;

        // while (run) {
        // for (int i = 0; i < array.length; i++) {
        //
        // }
        // }

        swiftbot = SwiftBotAPI.INSTANCE;
    }
}
