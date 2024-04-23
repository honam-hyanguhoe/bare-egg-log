package org.egglog.utility.utils;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomStringUtils {
    /**
     * 자릿수(digit) 만큼 랜덤한 숫자를 반환
     *
     * @param length 자릿수
     * @return 랜덤한 숫자
     */
    public static int generateRandomInt(int length){
        SecureRandom secureRandom = new SecureRandom();
        int upper = (int) Math.pow(10, length);
        return secureRandom.nextInt();
    }

    /**
     * 시작 범위(start)와 종료 범위(end) 값을 받아서 랜덤한 숫자를 반환
     *
     * @param start 시작 범위
     * @param end   종료 범위
     * @return 랜덤한 숫자
     */
    public static int generateRangeRandomNum(int start, int end) {
        SecureRandom secureRandom = new SecureRandom();
        return start + secureRandom.nextInt(end + 1);
    }

    /**
     * 자릿수(length) 만큼 랜덤한 알파벳 조합의 랜덤한 문자열을 출력
     *
     * @param length 문자의 범위
     * @return String 대소문자 조합 문자열
     */
    public static String generateRandomMixChar(int length){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        String alphabets = IntStream.concat(
                        IntStream.rangeClosed(33, 47),
                        IntStream.rangeClosed(58, 126))
                .mapToObj(ch -> String.valueOf((char) ch))
                .collect(Collectors.joining());
        for(int i=0 ; i<length ; i++){
            builder.append(
                    alphabets.charAt(
                            secureRandom.nextInt(alphabets.length())
                    )
            );
        }
        return builder.toString();
    }
    /**
     * 자릿수(length) 만큼 랜덤한 문자와 특수 문자 조합의 랜덤한 문자열을 출력
     *
     * @param length 문자의 범위
     * @return String 문자 + 특수 문자 조합 문자열
     */
    public static String generateRandomMixCharNSpecialChar(int length){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        String alphabets = IntStream.concat(
                IntStream.rangeClosed(65,90),
                IntStream.rangeClosed(97,122))
                .mapToObj(ch -> String.valueOf((char) ch))
                .collect(Collectors.joining());
        for(int i=0 ; i<length ; i++){
            builder.append(
                    alphabets.charAt(
                            secureRandom.nextInt(alphabets.length())
                    )
            );
        }
        return builder.toString();
    }
}
