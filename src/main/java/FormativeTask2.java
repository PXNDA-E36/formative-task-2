import java.util.ArrayList;

import swiftbot.*;

public class FormativeTask2 {
    static SwiftBotAPI swiftbot;

    private static class sequence {
        static ArrayList<Character> sequence = new ArrayList<Character>();
        static char[] colours = { 'R', 'G', 'B', 'W' };

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
        int[] red = new int[] { 255, 0, 0 };
        int[] blue = new int[] { 0, 0, 255 };
        int[] green = new int[] { 0, 255, 0 };
        int[] white = new int[] { 255, 255, 255 };

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

    private static class input {
        static final Object lock = new Object();
        static Boolean result = null;

        static Button buttons[] = { Button.A, Button.B, Button.X, Button.Y };

        private static boolean track(Button correct) {
            // swiftbot.enableButton(Button.A, () -> handlePress(Button.A, button));
            //
            // swiftbot.enableButton(Button.B, () -> handlePress(Button.B, button));
            //
            // swiftbot.enableButton(Button.X, () -> handlePress(Button.X, button));
            //
            // swiftbot.enableButton(Button.Y, () -> handlePress(Button.Y, button));

            for (Button button : buttons) {
                swiftbot.enableButton(button, () -> handlePress(button, correct));
            }

            synchronized (lock) {
                while (result == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("input.track() interrupt");
                        System.out.println(e);
                    }
                }
            }

            return result;
        }

        private static void handlePress(Button input, Button correct) {
            synchronized (lock) {
                result = (input == correct);
                lock.notify();
            }

            swiftbot.disableButton(input);
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

        System.out.println("starting...");

        input.track(Button.A);
    }
}
