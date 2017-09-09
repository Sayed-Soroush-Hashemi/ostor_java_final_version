package os.screendriver;

import hardware.screen.Screen;

public class ScreenDriver {
	protected Screen screen;

	public ScreenDriver(Screen screen) {
		this.screen = screen;
	}

	public void show(String x) {
		screen.show(x);
	}
}