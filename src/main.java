/**
 * Created by benjamingarner on 10/07/2016.
 */
import java.io.*;

public class main {
    public static void main(String[] args) {
//        System.out.println("Hello World!"); // Display the string.

        authentication psnAuth = new authentication();
        manifestretrieval mani = new manifestretrieval();
//        try
//        {
//            psnAuth.sendGet();
//        }
//        catch(Exception ex)
//        {
//
//        }

        try
        {
            mani.updateManifest();
        }
        catch(Exception ex)
        {
            System.out.println("Failed");
            System.out.println(ex);
        }

    }
}
