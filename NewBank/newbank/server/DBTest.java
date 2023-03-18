package newbank.server;

public class DBTest {
    public static void main(){
        Database db = new Database();
        Customer c = new Customer("abc","abc","abc","abc","abc","abc");
        db.addCustomer(c);
        return;
    }
}
