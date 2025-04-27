import java.io.File;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String filePath = "/home/iamhda/ETC/Room-Renting/backend/src/main/resources/static/MessageMedia/Screenshot from 2025-04-24 20-37-13.png";
        File file = new File(filePath);
        if(file.exists()){
            if(file.delete()){
                System.out.println("file deleted");
            }else{
                System.out.println("file not deleted");
            }
            System.out.println("file exists");
        }else{
            System.out.println("file does not exist");
        }
    }
}