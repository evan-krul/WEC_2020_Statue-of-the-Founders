import java.io.*;

// adapted from https://bytes.com/topic/python/insights/949995-three-ways-run-python-programs-java
// accessed 23 Jan 2020

class PyInterface {
    public String call(String mainScript, String passables) {
        try {
            ProcessBuilder pyPB = new ProcessBuilder("python3", mainScript, passables);
            Process pyProcess = pyPB.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(pyProcess.getInputStream()));
            return in.readLine();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}