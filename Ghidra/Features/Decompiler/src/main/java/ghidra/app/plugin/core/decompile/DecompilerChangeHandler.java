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
package ghidra.app.plugin.core.decompile;

import ghidra.app.decompiler.DecompileOptions;
import ghidra.framework.model.DomainObjectChangeRecord;
import ghidra.framework.model.DomainObjectChangedEvent;
import ghidra.framework.model.DomainObjectListener;
import ghidra.program.database.SpecExtension;

import java.util.BitSet;

import static ghidra.framework.model.DomainObject.*;
import static ghidra.program.util.ChangeManager.*;

class DecompilerChangeHandler implements DomainObjectListener {
	private final DecompilerProvider provider;
	private final DecompileOptions options;

	DecompilerChangeHandler(DecompilerProvider provider, DecompileOptions options) {
		this.provider = provider;
		this.options = options;
		optionsChanged();
	}

	boolean optionsChanged() {
		IRRELEVANT_EVENTS.set(DOCR_EOL_COMMENT_CHANGED, !options.isEOLCommentIncluded());
		IRRELEVANT_EVENTS.set(DOCR_PRE_COMMENT_CHANGED, !options.isPRECommentIncluded());
		IRRELEVANT_EVENTS.set(DOCR_POST_COMMENT_CHANGED, !options.isPOSTCommentIncluded());
		IRRELEVANT_EVENTS.set(DOCR_PLATE_COMMENT_CHANGED, !options.isPLATECommentIncluded());

		// TODO if the decompiler options have not changed, return false.  Also
		//  ideally don't refresh if the change is one that can easily be
		//  applied without redecompilation, e.g. color changes.
		return true;
	}

	@Override
	public void domainObjectChanged(DomainObjectChangedEvent ev) {
		// Check for events that signal that a decompiler process' data is stale
		// and if so force a new process to be spawned.
		// DO_OBJECT_RESTORED is particularly unfortunate, since it is currently
		// fired every time the user hits 'undo'.  (TODO: find a way to ignore it for trivial undos.)
		if (ev.containsEvent(DOCR_MEMORY_BLOCK_ADDED) ||
				ev.containsEvent(DOCR_MEMORY_BLOCK_REMOVED) ||
				ev.containsEvent(DO_OBJECT_RESTORED)) {
			provider.getController().resetDecompiler();
		}
		else if (ev.containsEvent(DO_PROPERTY_CHANGED)) {
			for (DomainObjectChangeRecord record : ev) {
				if (record.getEventType() == DO_PROPERTY_CHANGED) {
					if (record.getOldValue() instanceof String) {
						String value = (String) record.getOldValue();
						if (value.startsWith(SpecExtension.SPEC_EXTENSION)) {
							provider.getController().resetDecompiler();
							break;
						}
					}
				}
			}
		}

		// Don't do anything else if the window is inactive
		if (!provider.isVisible()) return;

		// Check for events that cannot affect decompilation, and if that's all
		// we've got, don't do anything
		var bits = ev.getEventBits();
		bits.andNot(IRRELEVANT_EVENTS);
		if (bits.isEmpty()) return;

		// Other things that shouldn't trigger a refresh:
		// - changes to datatypes/functions/variables that aren't referenced
		//   in this function (note: datatypes need to check for members too)
		// - arguably most renamings

		// DOCR_SYMBOL_RENAMED
		// DOCR_EQUATE_RENAMED
		// DOCR_DATA_TYPE_RENAMED

		// Trigger a redecompile on any other program change
		provider.requestRefresh();
	}


	private static BitSet setOf(int... bits) {
		var bitSet = new BitSet();
		for (var bit: bits) bitSet.set(bit);
		return bitSet;
	}

	private static final BitSet IRRELEVANT_EVENTS = setOf(
			// saving the database presumably doesn't change how anything is decompiled
			DO_OBJECT_SAVED,

			// datatype categories don't affect anything do they?
			DOCR_CATEGORY_ADDED,
			DOCR_CATEGORY_REMOVED,
			DOCR_CATEGORY_RENAMED,
			DOCR_CATEGORY_MOVED,

			// and nor should some datatype operations
			DOCR_DATA_TYPE_ADDED, // ?
			DOCR_DATA_TYPE_MOVED,

			// bookmarks don't affect decompilation do they?
			DOCR_BOOKMARK_TYPE_ADDED,
			DOCR_BOOKMARK_TYPE_REMOVED,
			DOCR_BOOKMARK_ADDED,
			DOCR_BOOKMARK_REMOVED,
			DOCR_BOOKMARK_CHANGED,

			// and the decompiler never displays repeatable comments
			// (see #optionsChanged for other comment types)
			DOCR_REPEATABLE_COMMENT_CREATED,
			DOCR_REPEATABLE_COMMENT_ADDED,
			DOCR_REPEATABLE_COMMENT_CHANGED,
			DOCR_REPEATABLE_COMMENT_REMOVED,
			DOCR_REPEATABLE_COMMENT_DELETED

			// TODO almost certainly there are more!
			// DOCR_GROUP_*, DOCR_MODULE_*, DOCR_TREE_*?
	);
}
