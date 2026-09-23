package it.polimi.ingsw;

import it.polimi.ingsw.Client.Controller.ClientController;
import it.polimi.ingsw.Client.Controller.MessagesToServer.HandShakeMTS;

public class mainTest {

    public static void main(String[] args) {
        //should corrupt stream
        ClientController clientController = new ClientController("Socket", "");
        new Thread(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("thread get messagetoClient");
            clientController.getClientConnectionHandler().SendMessageToSever(new HandShakeMTS("ugo"));

            clientController.getClientConnectionHandler().GetMessageToClient();

        }).start();
        System.out.println("main get MTC");
        clientController.getClientConnectionHandler().GetMessageToClient();
    }
//        public static void main(String[] args) {
//            int width = 30;
//            int height = 12;
//            double A = 0;
//            double B = 0;
//            double i, j;
//            double[] z = new double[width * height];
//            char[] b = new char[width * height];
//            System.out.print("\u001b[2J"); // Clear the screen
//
//            while (true) {
//                Arrays.fill(b, 0, width * height, ' ');
//                Arrays.fill(z, 0, width * height, 0);
//                for (j = 0; j < 6.28; j += 0.07) {
//                    for (i = 0; i < 6.28; i += 0.02) {
//                        double c = Math.sin(i);
//                        double d = Math.cos(j);
//                        double e = Math.sin(A);
//                        double f = Math.sin(j);
//                        double g = Math.cos(A);
//                        double h = d + 2;
//                        double D = 1 / (c * h * e + f * g + 5);
//                        double l = Math.cos(i);
//                        double m = Math.cos(B);
//                        double n = Math.sin(B);
//                        double t = c * h * g - f * e;
//                        int x = (int) (width / 2 + width * D * (l * h * m - t * n));
//                        int y = (int) (height / 2 + height * D * (l * h * n + t * m));
//                        int o = x + width * y;
//                        int N = (int) (8 * ((f * e - c * d * g) * m - c * d * e - f * g - l * d * n));
//                        if (height > y && y > 0 && width > x && x > 0 && D > z[o]) {
//                            z[o] = D;
//                            int charIndex = Math.max(0, Math.min(11, N));
//                            b[o] = ".,-~:;=!*#$@".charAt(charIndex);
//                        }
//                    }
//                }
//                System.out.print("\u001b[H"); // Move cursor to top left
//                for (int k = 0; k < width * height; k++) {
//                    System.out.print(k % width > 0 ? b[k] : 10);
//                }
//                A += 0.04;
//                B += 0.02;
//            }
//        }

}

