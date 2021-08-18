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

import resources.ResourceManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public interface Theme {
	String getName();

	default Color getColor(Color defaultValue, String name) {
		return defaultValue;
	}

	default Border getBorder(Color color, int top, int left, int bottom, int right) {
		color = getColor(color, "line");
		return top == left && left == bottom && bottom == right
				? BorderFactory.createLineBorder(color, top)
				: BorderFactory.createMatteBorder(top, left, bottom, right, color);
	}

	default Border getBorder(Color highlight, Color shadow) {
		highlight = getColor(highlight, "line");
		shadow = getColor(shadow, "line");
		return BorderFactory.createEtchedBorder(highlight, shadow);
	}

	default Border getBorder(int mode, Color highlightOuter, Color highlight,
			Color shadow, Color shadowInner) {
		highlight = getColor(highlight, "bevel");
		shadow = getColor(shadow, "bevel");
		highlightOuter = highlightOuter != null ? getColor(highlightOuter, "bevel") : highlight.brighter();
		shadowInner = shadowInner != null ? getColor(shadowInner, "bevel") : shadow.brighter();
		return BorderFactory.createBevelBorder(mode, highlightOuter, highlight, shadow, shadowInner);
	}

	default Font getFont(Font defaultValue, String name) {
		return defaultValue;
	}

	default String getFontFamily(String defaultValue) {
		return defaultValue;
	}

	default ImageIcon getIcon(String resourceName) {
		return ResourceManager.loadImage(resourceName);
	}
}
