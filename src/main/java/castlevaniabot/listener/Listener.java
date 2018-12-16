package castlevaniabot.listener;

public class Listener {

    public void stop() {
        System.out.println("API stopped");
    }

    public void apiEnabled() {
        System.out.println("API enabled");
    }

    public void statusChanged(final String message) {
        System.out.format("Status message: %s%n", message);
    }

}
