import java.util.ArrayList;

import swiftbot.*;
// import com.pi4j.io.g

public class FormativeTask2 {
    static SwiftBotAPI swiftbot;
    static char[] colours = { 'R', 'G', 'B', 'W' };

    private static class sequence {
        static ArrayList<Character> sequence = new ArrayList<Character>();

        private static char[] generate() {
            // char[] colours = { 'R', 'G', 'B', 'W' };

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

    private static void light(char c) {
        int[] red = new int[] { 255, 0, 0 };
        int[] blue = new int[] { 0, 0, 255 };
        int[] green = new int[] { 0, 255, 0 };
        int[] white = new int[] { 255, 255, 255 };

        switch (c) {
            case 'R':
                swiftbot.fillUnderlights(red);
                break;
            case 'G':
                swiftbot.fillUnderlights(green);
                break;
            case 'B':
                swiftbot.fillUnderlights(blue);
                break;
            case 'W':
                swiftbot.fillUnderlights(white);
                break;
            default:
                swiftbot.disableUnderlights();
                break;
        }
    }

    private static class input {
        static final Object lock = new Object();
        static Boolean result = null;

        static Button buttons[] = { Button.A, Button.B, Button.X, Button.Y };

        private static boolean track(Button correct) {
            result = null;
            swiftbot.disableAllButtons();

            for (Button button : buttons) {
                swiftbot.enableButton(button, () -> handle(button, correct));
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

        private static void handle(Button input, Button correct) {
            synchronized (lock) {
                result = (input == correct);
                lock.notify();
            }

            swiftbot.disableButton(input);
        }
    }

    private static void init() {
        for (Character c : colours) {
            boolean correct;
            switch (c) {
                case 'R':
                    System.out.println("Red is assigned to button A");
                    light('R');
                    System.out.println("Press A to ack");
                    swiftbot.setButtonLight(Button.A, true);
                    correct = input.track(Button.A);
                    if (correct) {
                        swiftbot.disableButtonLights();
                        break;
                    }
                case 'G':
                    System.out.println("Green is assigned to button B");
                    light('G');
                    System.out.println("Press B to ack");
                    swiftbot.setButtonLight(Button.B, true);
                    correct = input.track(Button.B);
                    if (correct) {
                        swiftbot.disableButtonLights();
                        break;
                    }
                case 'B':
                    System.out.println("Blue is assigned to button X");
                    light('B');
                    System.out.println("Press X to ack");
                    swiftbot.setButtonLight(Button.X, true);
                    correct = input.track(Button.X);
                    if (correct) {
                        swiftbot.disableButtonLights();
                        break;
                    }
                case 'W':
                    System.out.println("White is assigned to button Y");
                    light('W');
                    System.out.println("Press Y to ack");
                    swiftbot.setButtonLight(Button.Y, true);
                    correct = input.track(Button.Y);
                    if (correct) {
                        swiftbot.disableButtonLights();
                        break;
                    }
                default:
                    break;
            }
            swiftbot.disableUnderlights();
        }
    }

    private static String eog(int state) {
        if (state == 0) {
            System.out.println("Incorrect input. Press A to end game or B to restart.");
            swiftbot.setButtonLight(Button.A, true);
            swiftbot.setButtonLight(Button.B, true);
            if (input.track(Button.A)) {
                swiftbot.disableButtonLights();
                return "end";
            } else if (input.track(Button.B)) {
                swiftbot.disableButtonLights();
                return "restart";
            }
        } else if (state == 5) {
            System.out.println("Congratulations! Press A to end game, B to restart or X to continue.");
            swiftbot.setButtonLight(Button.A, true);
            swiftbot.setButtonLight(Button.B, true);
            swiftbot.setButtonLight(Button.X, true);
            if (input.track(Button.A)) {
                swiftbot.disableButtonLights();
                return "end";
            } else if (input.track(Button.B)) {
                swiftbot.disableButtonLights();
                return "restart";
            } else if (input.track(Button.X)) {
                swiftbot.disableButtonLights();
                return "continue";
            }
        }

        return "unknown";
    }

    public static void main(String[] args) {
        swiftbot = SwiftBotAPI.INSTANCE;

        int score = 0;
        int round = 1;

        char[] array = sequence.generate();

        boolean run = true;

        boolean correct = false;

        game: while (run) {
            System.out.println(array);
            for (int i = 0; i < array.length + 1; i++) {
                for (int j = 0; j <= i; j++) {
                    char c = array[j];

                    light(c);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }

                    swiftbot.disableUnderlights();

                    switch (c) {
                        case 'R':
                            correct = input.track(Button.A);
                            break;
                        case 'G':
                            correct = input.track(Button.B);
                            break;
                        case 'B':
                            correct = input.track(Button.X);
                            break;
                        case 'W':
                            correct = input.track(Button.Y);
                            break;
                    }

                    if (correct) {
                        score += 1;
                        round += 1;
                        System.out.println("Score: " + score);
                    } else if (!correct) {
                        String state = eog(0);

                        switch (state) {
                            case "end":
                                System.out.println("end selected");
                                run = false;
                                break game;
                            case "restart":
                                array = null;
                                System.out.println(array);
                                array = sequence.generate();
                                System.out.println(array);
                                continue;
                        }
                    }
                }
            }
        }
        swiftbot.disableUnderlights();
        System.exit(0);
    }
}
