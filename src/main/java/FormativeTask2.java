import java.util.ArrayList;
import swiftbot.*;

public class FormativeTask2 {
    private static class inputResponse {
        private Boolean result;
        private Button button;
    }

    static SwiftBotAPI swiftbot;
    static char[] colours = { 'R', 'G', 'B', 'W' };
    static ArrayList<Character> sequence = new ArrayList<Character>();

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
        static inputResponse response = new inputResponse();

        private static boolean track(Button correct) {
            result = null;
            swiftbot.disableAllButtons();

            ArrayList<Button> buttons = new ArrayList<>();

            buttons.add(Button.A);
            buttons.add(Button.B);
            buttons.add(Button.X);
            buttons.add(Button.Y);

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

        private static inputResponse track(Button button1, Button button2) {
            swiftbot.disableAllButtons();
            response.result = null;
            response.button = null;

            ArrayList<Button> buttons = new ArrayList<>();

            buttons.add(button1);
            buttons.add(button2);

            for (Button button : buttons) {
                swiftbot.enableButton(button, () -> handle(button, button1, button2));
            }

            synchronized (lock) {
                while (response.result == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("input.track() interrupt");
                        System.out.println(e);
                    }
                }
            }

            return response;
        }

        private static inputResponse track(Button button1, Button button2, Button button3) {
            swiftbot.disableAllButtons();
            response.result = null;
            response.button = null;

            ArrayList<Button> buttons = new ArrayList<>();

            buttons.add(button1);
            buttons.add(button2);
            buttons.add(button3);

            for (Button button : buttons) {
                swiftbot.enableButton(button, () -> handle(button, button1, button2, button3));
            }

            synchronized (lock) {
                while (response.result == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.out.println("input.track() interrupt");
                        System.out.println(e);
                    }
                }
            }

            return response;
        }

        private static void handle(Button input, Button correct) {
            synchronized (lock) {
                result = (input == correct);
                lock.notify();
            }

            swiftbot.disableButton(input);
        }

        private static void handle(Button input, Button button1, Button button2) {
            synchronized (lock) {
                if (input == button1) {
                    response.result = true;
                    response.button = button1;
                } else if (input == button2) {
                    response.result = true;
                    response.button = button2;
                } else
                    response.result = null;
                lock.notify();
            }

            swiftbot.disableButton(input);
        }

        private static void handle(Button input, Button button1, Button button2, Button button3) {
            synchronized (lock) {
                if (input == button1) {
                    response.result = true;
                    response.button = button1;
                } else if (input == button2) {
                    response.result = true;
                    response.button = button2;
                } else if (input == button3) {
                    response.result = true;
                    response.button = button3;
                } else
                    response.result = null;
                lock.notify();
            }

            swiftbot.disableButton(input);
        }

    }

    private static void init() {
        swiftbot.disableAllButtons();

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

            while (true) {
                inputResponse response = input.track(Button.A, Button.B);

                if (response.result && response.button == Button.A) {
                    swiftbot.disableButtonLights();
                    return "end";
                }

                if (response.result && response.button == Button.B) {
                    swiftbot.disableButtonLights();
                    return "restart";
                }
            }
        } else if (state == 5) {
            System.out.println("Congratulations! Press A to end game, B to restart or X to continue.");
            swiftbot.setButtonLight(Button.A, true);
            swiftbot.setButtonLight(Button.B, true);
            swiftbot.setButtonLight(Button.X, true);

            while (true) {
                inputResponse response = input.track(Button.A, Button.B, Button.X);

                if (response.result && response.button == Button.A) {
                    swiftbot.disableButtonLights();
                    return "end";
                }

                if (response.result && response.button == Button.B) {
                    swiftbot.disableButtonLights();
                    return "restart";
                }

                if (response.result && response.button == Button.X) {
                    swiftbot.disableButtonLights();
                    return "continue";
                }
            }

        }
        return "unknown";
    }

    private static ArrayList<ArrayList<Character>> play(boolean add) {
        if (sequence.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                sequence.add(colours[(int) (Math.random() * colours.length)]);
            }
        } else if (add) {
            for (int i = 0; i < 5; i++) {
                sequence.add(colours[(int) (Math.random() * colours.length)]);
            }
        }

        ArrayList<ArrayList<Character>> array2D = new ArrayList<>();

        for (int i = 0; i < sequence.size(); i++) {
            ArrayList<Character> row = new ArrayList<>();

            for (int j = 0; j <= i; j++) {
                row.add(sequence.get(j));
            }

            array2D.add(row);
        }

        return array2D;
    }

    public static void main(String[] args) {
        swiftbot = SwiftBotAPI.INSTANCE;

        int score = 0;
        int rowCount = 0;

        boolean run = true;

        boolean correctInput = false;
        boolean correctRow = true;

        init();

        ArrayList<ArrayList<Character>> array = play(false);

        game: while (run) {
            if (score % 5 == 0 && score > 0) {
                String state = eog(5);

                swiftbot.disableAllButtons();

                switch (state) {
                    case "end":
                        System.out.println("end selected");
                        run = false;
                        break game;
                    case "restart":
                        sequence.clear();
                        array = play(false);
                        score = 0;
                        rowCount = 0;
                        correctInput = false;
                        correctRow = true;
                        continue game;
                    case "continue":
                        array = play(true);
                        break;
                    case "unknown":
                        System.out.println("What did you do");
                        System.exit(0);
                    default:
                        System.out.println("How did you get here");
                        System.exit(0);
                }
            }

            for (ArrayList<Character> row : array) {
                // Display
                for (char c : array.get(rowCount)) {
                    try {
                        Thread.sleep(100);
                        light(c);
                        Thread.sleep(300);
                        swiftbot.disableUnderlights();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupt exception in game loop");
                        System.out.println(e);
                    }
                }

                // Input
                for (char c : array.get(rowCount)) {
                    switch (c) {
                        case 'R':
                            correctInput = input.track(Button.A);
                            break;
                        case 'G':
                            correctInput = input.track(Button.B);
                            break;
                        case 'B':
                            correctInput = input.track(Button.X);
                            break;
                        case 'W':
                            correctInput = input.track(Button.Y);
                            break;
                    }

                    if (!correctInput) {
                        correctRow = false;
                        swiftbot.disableAllButtons();
                        String state = eog(0);

                        switch (state) {
                            case "end":
                                System.out.println("end selected");
                                run = false;
                                break game;
                            case "restart":
                                sequence.clear();
                                array = play(false);
                                score = 0;
                                rowCount = 0;
                                correctInput = false;
                                correctRow = true;
                                continue game;
                            case "unknown":
                                System.out.println("What did you do");
                                System.exit(0);
                            default:
                                System.out.println("How did you get here");
                                System.exit(0);
                        }

                        break;
                    }
                }

                if (correctRow) {
                    score += 1;
                    rowCount += 1;
                    System.out.println("Score: " + score);
                }
            }
        }
        swiftbot.disableUnderlights();
        System.exit(0);
    }
}
