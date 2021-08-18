/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.app.decompiler.component;

import docking.widgets.EmptyBorderButton;
import ghidra.app.nav.DecoratorPanel;
import resources.Icons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.util.Arrays;

public class PendingRefreshDecoratorPanel extends DecoratorPanel {

	// TODO make sure the refresh action has a keyboard shortcut!
	private static final String TEMPLATE = "Click to refresh decompiler (this will take %s)";

	private final JPanel glass;
	private final JLabel message;
	private final Runnable refreshCallback;

	public PendingRefreshDecoratorPanel(JComponent component, boolean isConnected, Runnable onRefresh) {
		this(new JLayer<>(component), isConnected, onRefresh);
	}

	private PendingRefreshDecoratorPanel(JLayer<?> layer, boolean isConnected, Runnable onRefresh) {
		super(layer, isConnected);
		refreshCallback = onRefresh;

		message = new JLabel();
		message.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
					e.consume();
					hideBanner();
					refreshCallback.run();
				}
			}
		});

		var dismiss = new EmptyBorderButton(Icons.get("images/close16.gif"));
		dismiss.setBackground(new Color(0xffff96));
		dismiss.setToolTipText("Dismiss this message without refreshing.");
		dismiss.addActionListener(event -> hideBanner());

		// Create glass pane with banner.  We make this transparent so that the
		// main UI will remain visible through it, and non-focusable so that
		// DockableComponent.initializeComponents() does not add a mouse
		// listener which would prevent mouse interaction with the main UI.
		glass = new JPanel(new BorderLayout());
		glass.setFocusable(false);
		glass.setOpaque(false);
		glass.setVisible(false);

		var banner = new JPanel(new BorderLayout());
		banner.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		banner.setBackground(new Color(0xffff96));
		banner.add(message, BorderLayout.CENTER);
		banner.add(dismiss, BorderLayout.LINE_END);
		glass.add(banner, BorderLayout.PAGE_START);
		layer.setGlassPane(glass);
	}

	public void hideBanner() {
		glass.setVisible(false);
	}

	public void displayBanner(Duration estimate) {
		message.setText(String.format(TEMPLATE, formatApproximateDuration(estimate.toMillis())));
		glass.setVisible(true);
	}

	/**
	 * Formats a millisecond duration as a string expressing the approximate
	 * duration in terms of an appropriately scaled unit chosen from 100ms,
	 * 1s, 10s, 1m, 10m, 1h.  This can be more useful than the precise value
	 * when displaying an estimate of time required.
	 *
	 * @param millis Absolute number of milliseconds
	 * @return Approximate string such as "~5m"
	 */
	private static String formatApproximateDuration(long millis) {
		int ms = millis <= Integer.MAX_VALUE ? (int) millis : Integer.MAX_VALUE;
		if (ms < 100) return "<100ms";
		int i = Arrays.binarySearch(thresholds, ms);
		if (i < 0) i = ~i;

		int divisor = thresholds[i - 1];
		int value = ms + divisor / 2;
		if (i < thresholds.length && value >= thresholds[i]) {
			return "~1" + units[i];
		}
		return "~" + (value / divisor) + units[i - 1];
	}

	private static final int[] thresholds = { 0,   100, 1000, 10_000, 60_000, 600_000, 3_600_000 };
	private static final String[] units = { "", "00ms",  "s",   "0s",    "m",    "0m",       "h" };
}
