import java.io.*;
import java.util.Scanner;

// adapted from https://bytes.com/topic/python/insights/949995-three-ways-run-python-programs-java and lots of stack overflow
// accessed 23 Jan 2020

class PyInterface {

    public String call(String mainScript, String passables) {
        try {
            ProcessBuilder pyPB = new ProcessBuilder("python3", mainScript, passables);
            Process pyProcess = pyPB.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(pyProcess.getInputStream()));
            Scanner scan = new Scanner(in).useDelimiter("\\A");
            return scan.hasNext() ? scan.next() : "";
        } catch(Exception e) {
            System.out.println(e);
        }
        return "fail";
    }

    public static void main(String[] args) {
        PyInterface cd = new PyInterface();
        String res = cd.call("/Users/spencercomin/eclipse-javaworkspace/WEC_2020_Statue-of-the-Founders/Python/venv/test.py", "");
        System.out.println(res);
        return;
    }
}