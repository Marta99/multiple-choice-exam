package client;

public class Displayer implements DisplayerInt {

    @Override
    public void display(String msg) {
        System.out.println(msg);
    }
}
