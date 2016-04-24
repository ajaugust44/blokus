package blokus;

class BlokusUtils {
    static int[] buttonColors() {
        return new int[] {180, 200, 100};
    }

    static void println() {
        System.out.println();
    }

    static void println(String s) {
        System.out.println(s);
    }

    static void err(){
        System.err.println();
    }

    static void err(String s) {
        System.err.println(s);
    }
}