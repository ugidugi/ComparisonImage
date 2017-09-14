
import java.io.IOException;


public class Main{
    public static void main(String[]args) throws IOException{
	String im_1 = "D:\\image\\image1.png";
        String im_2 = "D:\\image\\image2.png";
        Comparison com = new Comparison(im_1,im_2);        
        com.searchDifferent(); 
    
        
    }
}
