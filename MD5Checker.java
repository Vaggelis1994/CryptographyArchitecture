import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class MD5Checker {

    /**
     *
     * @param pathName
     * @return
     */
    public static Set<String> readMDSFiles(String pathName) {

        HashSet<String> filesMD5 = new HashSet<>();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            File[] files = new File(pathName).listFiles();

            for (File file : files) {

                if (file.isFile()) {

                    String filename = file.getName();
                    InputStream is = Files.newInputStream(Paths.get(pathName + "\\" + filename));

                    byte[] digest = new byte[1024];
                    int numRead;

                    do {
                        numRead = is.read(digest);
                        if (numRead > 0) {
                            md.update(digest, 0, numRead);
                        }
                    } while (numRead != -1);
                    is.close();

                    digest = md.digest();
                    String result = "";
                    for (int i = 0; i < digest.length; i++) {
                        result += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
                    }
                    filesMD5.add(result.toUpperCase());
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return filesMD5;
    }

    /**
     *
     * @param checksumFilename
     * @return
     */
    public static Set<String> checksumSet(String checksumFilename) {

        BufferedReader bufferedReader;
        HashSet<String> checksumSet = new HashSet<>();

        try{
            bufferedReader = new BufferedReader(new FileReader(checksumFilename));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                checksumSet.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return checksumSet;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        String checksums = args[1];
        String filesPath = args[2];

        System.out.println(checksumSet(checksums).equals(readMDSFiles(filesPath)));
    }
}
