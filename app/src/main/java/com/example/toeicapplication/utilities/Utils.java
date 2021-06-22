package com.example.toeicapplication.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Utils {
    public static String getJsonFromAssets(InputStream is) {
        String jsonString;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public static String convertTime(long milliseconds) {
        return String.format(Locale.getDefault(),"%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    public static Map<Integer, Integer> listeningScore(){
        Map<Integer, Integer> score = new HashMap<>();
        score.put(0, 0);
        score.put(1, 5);
        score.put(2, 5);
        score.put(3, 5);
        score.put(4, 5);
        score.put(5, 5);
        score.put(6, 5);
        score.put(7, 5);
        score.put(8, 5);
        score.put(9, 5);
        score.put(10, 5);
        score.put(11, 5);
        score.put(12, 5);
        score.put(13, 5);
        score.put(14, 5);
        score.put(15, 5);
        score.put(16, 5);
        score.put(17, 5);
        score.put(18, 10);
        score.put(19, 15);
        score.put(20, 20);
        score.put(21, 25);
        score.put(22, 30);
        score.put(23, 35);
        score.put(24, 40);
        score.put(25, 45);
        score.put(26, 50);
        score.put(27, 55);
        score.put(28, 60);
        score.put(29, 70);
        score.put(30, 80);
        score.put(31, 85);
        score.put(32, 90);
        score.put(33, 95);
        score.put(34, 100);
        score.put(35, 105);
        score.put(36, 115);
        score.put(37, 125);
        score.put(38, 135);
        score.put(39, 140);
        score.put(40, 150);
        score.put(41, 160);
        score.put(42, 170);
        score.put(43, 175);
        score.put(44, 180);
        score.put(45, 190);
        score.put(46, 200);
        score.put(47, 205);
        score.put(48, 215);
        score.put(49, 220);
        score.put(50, 225);
        score.put(51, 230);
        score.put(52, 235);
        score.put(53, 245);
        score.put(54, 255);
        score.put(55, 260);
        score.put(56, 265);
        score.put(57, 275);
        score.put(58, 285);
        score.put(59, 290);
        score.put(60, 295);
        score.put(61, 300);
        score.put(62, 310);
        score.put(63, 320);
        score.put(64, 325);
        score.put(65, 330);
        score.put(66, 335);
        score.put(67, 340);
        score.put(68, 345);
        score.put(69, 350);
        score.put(70, 355);
        score.put(71, 360);
        score.put(72, 365);
        score.put(73, 370);
        score.put(74, 375);
        score.put(75, 385);
        score.put(76, 395);
        score.put(77, 400);
        score.put(78, 405);
        score.put(79, 415);
        score.put(80, 420);
        score.put(81, 425);
        score.put(82, 430);
        score.put(83, 435);
        score.put(84, 440);
        score.put(85, 445);
        score.put(86, 455);
        score.put(87, 460);
        score.put(88, 465);
        score.put(89, 475);
        score.put(90, 480);
        score.put(91, 485);
        score.put(92, 490);
        score.put(93, 495);
        score.put(94, 495);
        score.put(95, 495);
        score.put(96, 495);
        score.put(97, 495);
        score.put(98, 495);
        score.put(99, 495);
        score.put(100, 495);
        return score;
    }

    public static Map<Integer, Integer> readingScore(){
        Map<Integer, Integer> score = new HashMap<>();
        score.put(0, 0);
        score.put(1, 5);
        score.put(2, 5);
        score.put(3, 5);
        score.put(4, 5);
        score.put(5, 5);
        score.put(6, 5);
        score.put(7, 5);
        score.put(8, 5);
        score.put(9, 5);
        score.put(10, 5);
        score.put(11, 5);
        score.put(12, 5);
        score.put(13, 5);
        score.put(14, 5);
        score.put(15, 5);
        score.put(16, 5);
        score.put(17, 5);
        score.put(18, 5);
        score.put(19, 5);
        score.put(20, 5);
        score.put(21, 5);
        score.put(22, 10);
        score.put(23, 15);
        score.put(24, 20);
        score.put(25, 25);
        score.put(26, 30);
        score.put(27, 35);
        score.put(28, 40);
        score.put(29, 45);
        score.put(30, 55);
        score.put(31, 60);
        score.put(32, 65);
        score.put(33, 70);
        score.put(34, 75);
        score.put(35, 80);
        score.put(36, 85);
        score.put(37, 90);
        score.put(38, 95);
        score.put(39, 105);
        score.put(40, 115);
        score.put(41, 120);
        score.put(42, 125);
        score.put(43, 130);
        score.put(44, 135);
        score.put(45, 140);
        score.put(46, 145);
        score.put(47, 155);
        score.put(48, 160);
        score.put(49, 170);
        score.put(50, 175);
        score.put(51, 185);
        score.put(52, 195);
        score.put(53, 205);
        score.put(54, 210);
        score.put(55, 215);
        score.put(56, 220);
        score.put(57, 230);
        score.put(58, 240);
        score.put(59, 245);
        score.put(60, 250);
        score.put(61, 255);
        score.put(62, 260);
        score.put(63, 270);
        score.put(64, 275);
        score.put(65, 280);
        score.put(66, 285);
        score.put(67, 290);
        score.put(68, 295);
        score.put(69, 295);
        score.put(70, 300);
        score.put(71, 310);
        score.put(72, 315);
        score.put(73, 320);
        score.put(74, 325);
        score.put(75, 330);
        score.put(76, 335);
        score.put(77, 340);
        score.put(78, 345);
        score.put(79, 355);
        score.put(80, 360);
        score.put(81, 370);
        score.put(82, 375);
        score.put(83, 385);
        score.put(84, 390);
        score.put(85, 395);
        score.put(86, 405);
        score.put(87, 415);
        score.put(88, 420);
        score.put(89, 425);
        score.put(90, 435);
        score.put(91, 440);
        score.put(92, 450);
        score.put(93, 455);
        score.put(94, 460);
        score.put(95, 470);
        score.put(96, 475);
        score.put(97, 485);
        score.put(98, 485);
        score.put(99, 490);
        score.put(100, 495);
        return score;
    }
}
