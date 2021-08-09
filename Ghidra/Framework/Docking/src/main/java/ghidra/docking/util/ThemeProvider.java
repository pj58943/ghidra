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

import ghidra.framework.preferences.Preferences;
import ghidra.util.Msg;
import ghidra.util.Swing;
import ghidra.util.classfinder.ExtensionPoint;

import javax.swing.*;

/**
 * Extension point for registering third-party Swing Look-and-Feel
 * implementations so they will appear in the options GUI; if they implement
 * {@link Theme} then they will additionally be made available to the
 * {@link Theming} API.
 * <p>
 * File names must end with 'ThemeProvider' in order to be found.
 */
public interface ThemeProvider extends ExtensionPoint {
	/**
	 * Invoked when populating the options GUI.  Implementations should invoke
	 * {@link UIManager#installLookAndFeel} to register any look-and-feel types
	 * they provide.
	 * <p>
	 * @implNote whichever look-and-feel is selected, this method will usually
	 * be invoked at some time <b>after</b> that has been activated, so it
	 * cannot be relied upon to perform any other initialization.
	 */
	void installLafInfos();


	/**
	 * Invoked early in application startup; ensures any desired theme is in
	 * place before any UI elements are created.
	 */
	static void initialize() {
		// Try to load the selected LaF class as a theme class.
		var themeId = Preferences.getProperty(DockingWindowsLookAndFeelUtils.LAST_LOOK_AND_FEEL_KEY);
		var themeClass = loadThemeClass(themeId);
		if (themeClass != null) {
			// If this succeeded, try to activate it.
			Theming.theme = Swing.runNow(() -> {
				try {
					var theme = themeClass.getConstructor().newInstance();
					UIManager.setLookAndFeel(theme);
					return theme;
				}
				catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
					Msg.error(ThemeProvider.class, "error initializing theme " + themeId, ex);
					return null;
				}
			});
		}

		// Proceed to the traditional Ghidra LaF initialization.
		DockingWindowsLookAndFeelUtils.loadFromPreferences();
	}

	/**
	 * Load the named class, if it exists; return the class object if it is a
	 * subtype of both {@link Theme} and {@link LookAndFeel}.  If the name does
	 * not identify a class or the class does not meet this criterion, return
	 * null silently.
	 *
	 * @param name  Binary name of theme class; may be null.
	 * @param <T>  Generic type used to represent the intersection of LookAndFeel
	 * and Theme.
	 * @return Class object; may be null.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends LookAndFeel & Theme> Class<T> loadThemeClass(String name) {
		if (name == null) return null;
		try {
			var cls = Class.forName(name);
			if (LookAndFeel.class.isAssignableFrom(cls) && Theme.class.isAssignableFrom(cls)) {
				return (Class<T>) cls;
			}
		}
		catch (ClassNotFoundException ex) {
			// ignore
		}
		return null;
	}
}
