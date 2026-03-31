package comm.smartmetro.util;

public class GenerateHash {

    public static void main(String[] args) {

        String hashed = PasswordUtil.hashPassword("admin123");
        System.out.println("Hashed Password:");
        System.out.println(hashed);
    }
}
