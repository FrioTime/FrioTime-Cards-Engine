// When the module is compiled, the output will be found in the games folder.
// To compile the test game use build/artifacts.

// Added dependency of the core project.
// Any tools to be used by a game should be found in com.ft.ce.tools in the core project.
// If there is an update to one of the files in the core project it will update here as well.
import com.ft.ce.tools.IGame;

public class TestIGame implements IGame {

    @Override
    public void init() {
        gameState.testPrint();
    }

    @Override
    public void run() {
        System.out.println("Hello run");
    }
}