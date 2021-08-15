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
package ghidra.docking.util;

import javax.swing.border.Border;
import java.awt.*;

import static ghidra.docking.util.Theming.*;

/**
 * Constants for some extremely common colors and borders, using the theming
 * API to provide appropriate values.
 */
public interface ThemeConstants {
	/**
	 * The default foreground color.
	 */
	Color FOREGROUND = themed(Color.BLACK);

	/**
	 * A lower-contrast foreground color.
	 */
	Color GRAY_FOREGROUND = themed(Color.GRAY);

	/**
	 * Foreground color for errors and alerts.
	 */
	Color ALERT_FOREGROUND = themed(Color.RED);

	/**
	 * The default background color.
	 */
	Color BACKGROUND = themed(Color.WHITE);

	/**
	 * The default background color for code listings.
	 */
	Color CODE_BACKGROUND = themed(Color.WHITE, "code");

	/**
	 * The foreground for selected text with background {@link #SELECTED_BACKGROUND}.
	 * <p>
	 * In the default Ghidra theme, this pair displays selected text with a
	 * pale background and the same foreground as normal text.
	 *
	 * @see #SELECTED_FOREGROUND_CONTRAST
	 */
	Color SELECTED_FOREGROUND = themed(Color.BLACK, "selected");

	/**
	 * The background for selected text with foreground
	 * {@link #SELECTED_FOREGROUND}.
	 */
	Color SELECTED_BACKGROUND = themed(new Color(204, 204, 255));

	/**
	 * The foreground for selected text with background
	 * {@link #SELECTED_BACKGROUND_CONTRAST}.
	 * <p>
	 * In the default Ghidra theme, this pair displays selected text with a
	 * darker background and different foreground than normal text.
	 *
	 * @see #SELECTED_FOREGROUND
	 */
	Color SELECTED_FOREGROUND_CONTRAST = themed(Color.WHITE, "selected");

	/**
	 * The background for selected text with foreground
	 * {@link #SELECTED_FOREGROUND_CONTRAST}.
	 */
	Color SELECTED_BACKGROUND_CONTRAST = themed(new Color(120, 140, 189));

	/**
	 * The primary background color for highlighting text.
	 */
	Color HIGHLIGHT_BACKGROUND = themed(Color.YELLOW, "bg");

	/**
	 * The color for the text cursor in active windows.
	 */
	Color CURSOR = themed(Color.RED, "cursor");

	/**
	 * The color for the text cursor in inactive windows.
	 */
	Color UNFOCUSED_CURSOR = themed(Color.PINK, "cursor");

	/**
	 * The default thin line border.
	 */
	Border NORMAL_BORDER = themedBorder(Color.GRAY);

	/**
	 * A higher-contrast thin line border.
	 */
	Border CONTRAST_BORDER = themedBorder(Color.BLACK);
}
