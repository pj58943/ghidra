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

import ghidra.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Lightweight theming API designed to enable runtime visual adjustments
 * with minimal changes to existing code.
 */
public final class Theming {
	private Theming() {}

	static Theme theme;

	/**
	 * Get the active theme.
	 * @return theme, or null if the default theme is in use.
	 */
	public static Theme getTheme() {
		return theme;
	}

	/**
	 * Check whether theming is in use, so code specific to the default LaF can
	 * be skipped.
	 * @return true if a non-default theme is in use.
	 */
	public static boolean isThemed() {
		return theme != null;
	}

	/**
	 * Return the color to use where the default theme specifies the given color.
	 * @param defaultValue  The color used in the default theme.
	 * @return Color to use, which will be the default if no theme is in use.
	 */
	public static Color themed(Color defaultValue) {
		return themed(defaultValue, null);
	}

	/**
	 * Return the name of the color to use where the default theme specifies the
	 * given color.  This method is for use where colors are defined in terms of
	 * {@link WebColors}, as with e.g. graphs.
	 * @param colorName  The color used in the default theme.
	 * @return Color to use, which will be the default if no theme is in use.
	 */
	public static String themed(String colorName) {
		return themed(colorName, null);
	}

	/**
	 * Return the color to use where the default theme specifies the given color,
	 * with an optional context hint.
	 * @param defaultValue  The color used in the default theme.
	 * @param contextHint  An optional contextual hint that themes can use to
	 * distinguish between different uses of the same color.  May be null if no
	 * contextual information is available.
	 * @return Color to use, which will be the default if no theme is in use.
	 */
	public static Color themed(Color defaultValue, String contextHint) {
		return theme != null ? theme.getColor(defaultValue, contextHint) : defaultValue;
	}

	/**
	 * Return the name of the color to use where the default theme specifies the
	 * given color.  This method is for use where colors are defined in terms of
	 * {@link WebColors}, as with e.g. graphs.
	 * @param colorName  The color used in the default theme.
	 * @param contextHint  An optional contextual hint that themes can use to
	 * distinguish between different uses of the same color.  May be null if no
	 * contextual information is available.
	 * @return Color to use, which will be the default if no theme is in use.
	 */
	public static String themed(String colorName, String contextHint) {
		if (theme != null) {
			var color = WebColors.getColor(colorName);
			if (color != null) {
				colorName = WebColors.toString(themed(color, contextHint));
			}
		}
		return colorName;
	}

	/**
	 * Return a thin line border that has the given color in the default theme.
	 * @param color  The color used in the default theme.
	 * @return Border to use.
	 * @apiNote the color should <em>not</em> have been passed to {@link #themed(Color)}.
	 */
	public static Border themedBorder(Color color) {
		return themedBorder(color, 1);
	}

	/**
	 * Return a line border that has the given color and thickness in the
	 * default theme.
	 * @param color  The color used in the default theme.
	 * @param thickness  The thickness used in the default theme.
	 * @return Border to use.
	 * @apiNote the color should <em>not</em> have been passed to {@link #themed(Color)}.
	 */
	public static Border themedBorder(Color color, int thickness) {
		return theme != null
				? theme.getBorder(color, thickness, thickness, thickness, thickness)
				: BorderFactory.createLineBorder(color, thickness);
	}

	/**
	 * Return a matte border that has the given color and thicknesses in the
	 * default theme.
	 * @param color  The color used in the default theme.
	 * @param top  The thickness of the top line in the default theme.
	 * @param left  The thickness of the left line in the default theme.
	 * @param bottom  The thickness of the bottom line in the default theme.
	 * @param right  The thickness of the right line in the default theme.
	 * @return Border to use.
	 * @apiNote the color should <em>not</em> have been passed to {@link #themed(Color)}.
	 */
	public static Border themedBorder(Color color, int top, int left, int bottom, int right) {
		return theme != null
				? theme.getBorder(color, top, left, bottom, right)
				: BorderFactory.createMatteBorder(top, left, bottom, right, color);
	}

	/**
	 * Return an etched border that has the given colors in the default theme.
	 * @param highlight  The highlight color used in the default theme.
	 * @param shadow  The shadow color used in the default theme.
	 * @return Border to use.
	 * @apiNote colors should <em>not</em> have been passed to {@link #themed(Color)}.
	 */

	public static Border themedBorder(Color highlight, Color shadow) {
		return theme != null
				? theme.getBorder(highlight, shadow)
				: BorderFactory.createEtchedBorder(highlight, shadow);
	}

	/**
	 * Return a beveled border that has the given colors in the default theme.
	 * @param type  The type: {@link BevelBorder.RAISED} or {@link BevelBorder.LOWERED}
	 * @param highlight  The highlight color used in the default theme.
	 * @param shadow  The shadow color used in the default theme.
	 * @return Border to use.
	 * @apiNote colors should <em>not</em> have been passed to {@link #themed(Color)}.
	 */
	public static Border themedBevelBorder(int type, Color highlight, Color shadow) {
		return theme != null
				? theme.getBorder(type, null, highlight, shadow, null)
				: BorderFactory.createBevelBorder(type, highlight, shadow);
	}

	/**
	 * Return a beveled border that has the given colors in the default theme.
	 * @param type  The type: {@link BevelBorder.RAISED} or {@link BevelBorder.LOWERED}
	 * @param highlightOuter  The outer highlight color used in the default theme.
	 * @param highlightInner  The inner highlight color used in the default theme.
	 * @param shadowOuter  The outer shadow color used in the default theme.
	 * @param shadowInner  The inner shadow color used in the default theme.
	 * @return Border to use.
	 * @apiNote colors should <em>not</em> have been passed to {@link #themed(Color)}.
	 */
	public static Border themedBevelBorder(int type,
			Color highlightOuter, Color highlightInner,
			Color shadowOuter, Color shadowInner) {
		return theme != null
				? theme.getBorder(type, highlightOuter, highlightInner, shadowOuter, shadowInner)
				: BorderFactory.createBevelBorder(type, highlightOuter, highlightInner, shadowOuter, shadowInner);
	}

	/**
	 * Return the name of a font family that has the given name in the default theme.
	 * @param defaultValue  The name of a family, such as {@code Monospaced} or {@code Helvetica}
	 * @return Family name to use.
	 */
	public static String themedFont(String defaultValue) {
		return theme != null ? theme.getFontFamily(defaultValue) : defaultValue;
	}

	/**
	 * Return a font that has the given attributes in the default theme.
	 * @param family  The name of a family, such as {@code Monospaced} or {@code Helvetica}
	 * @param style   The style, such as {@link Font.PLAIN} or {@link Font.BOLD}
	 * @param size    The size, in points
	 * @return Font to use.
	 * @apiNote the family should <em>not</em> have been passed to {@link #themedFont(String)}.
	 */
	public static Font themedFont(String family, int style, int size) {
		return themedFont(new Font(family, style, size), null);
	}

	/**
	 * Return a font that has the given value in the default theme.
	 * @param defaultValue  The font used in the default theme.
	 * @return Font to use.
	 */
	public static Font themedFont(Font defaultValue) {
		return themedFont(defaultValue, null);
	}

	/**
	 * Return a font that has the given value in the default theme.
	 * @param defaultValue  The font used in the default theme.
	 * @param contextHint  An optional contextual hint that themes can use to
	 * distinguish between different uses of the same font.  May be null if no
	 * contextual information is available.
	 * @return Font to use.
	 */
	public static Font themedFont(Font defaultValue, String contextHint) {
		var font = theme != null ? theme.getFont(defaultValue, contextHint) : defaultValue;
		return SystemUtilities.adjustForFontSizeOverride(font);
	}
}
